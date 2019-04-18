import org.centos.contra.Infra.Providers.Aws
import org.centos.contra.Infra.Providers.Beaker
import org.centos.contra.Infra.Providers.Openstack
import org.centos.contra.Infra.Utils

import static org.centos.contra.Infra.Defaults.*

/**
 *
 * @param config: An optional map that holds configuration parameters.
 * @param config.verbose: A key with a Boolean value which enables verbose output from the `linchpin up` execution.
 * @param config.linchpinContainerName: a key with a String value which indicates the container name where linchpin
 *                                      will be executed from.
 * @param config.ansibleContainerName: a key with a String value which indicates the container name where ansible
 *                                      will be executed from.
 * @return
 */
def call(Map<String, ?> config=[:]) {

    final String LINCHPIN_CREDS_DIR = "${WORKSPACE}/linchpin/creds"

    def infraUtils = new Utils()

    config = (env.configJSON ? readJSON(text: env.configJSON) : [:]) << config

    String linchpinContainerName = config.linchpinContainerName ?: 'linchpin-executor'
    String ansibleContainerName = config.ansibleContainerName ?: 'ansible-executor'

    String awsCredentialsId = config.aws_credentials_id ?: AWS_CREDS_ID
    String openstackCredentialsId = config.openstack_credentials_id ?: OPENSTACK_CREDS_ID
    String beakerCredentialsId = config.beaker_credentials_id ?: BEAKER_CREDS_ID
    List<Map> beakerConfigurationFiles = config.beaker_configuration_files ?: []
    List<Map> beakerConfigurationCommands = config.beaker_configuration_commands ?: []

    ArrayList infraInstances = []

    if (config.infra.provision.cloud) {
        if (config.infra.provision.cloud.aws) {
            def aws_instances = infraUtils.createAwsInstances(config.infra.provision.cloud.aws as HashMap)
            if (aws_instances) {
                infraInstances.addAll(aws_instances)
                // Create our aws credential auth file, and our aws ssh key
                infraUtils.createFile(awsCredentialsId, "${LINCHPIN_CREDS_DIR}/${AWS_CREDS_ID}", linchpinContainerName)
                infraUtils.createSSHKeyFile('aws', ansibleContainerName, config.aws_ssh_id as String)
            }
        }
        if (config.infra.provision.cloud.openstack) {
            def openstack_instances = infraUtils.createOpenstackInstances(config.infra.provision.cloud.openstack as HashMap)
            if (openstack_instances) {
                infraInstances.addAll(openstack_instances)
                // Create our openstack credential auth file, and our openstack ssh key
                infraUtils.createFile(openstackCredentialsId, "${LINCHPIN_CREDS_DIR}/${OPENSTACK_CREDS_ID}", linchpinContainerName)
                infraUtils.createSSHKeyFile('openstack', ansibleContainerName, config.openstack_ssh_id as String)
            }
        }
    }

    if (config.infra.provision.baremetal) {
        if (config.infra.provision.baremetal.beaker) {
            ArrayList<Beaker> beaker_instances = infraUtils.createBeakerInstances(config.infra.provision.baremetal.beaker as HashMap)
            if (beaker_instances) {
                infraInstances.addAll(beaker_instances)

                // Create and save beaker credentials
                infraUtils.createFile(beakerCredentialsId, "${LINCHPIN_CREDS_DIR}/${BEAKER_CREDS_ID}", linchpinContainerName)
                String sshUser = infraUtils.createSSHKeyFile('beaker', ansibleContainerName, config.beaker_ssh_id as String)
                for (instance in beaker_instances) {
                    instance.user = sshUser
                    instance.keyPair = "$BEAKER_CREDS_ID"
                }

                // Create beaker configuration files
                for (file in beakerConfigurationFiles) {
                    infraUtils.createFile(file.id, file.path, linchpinContainerName, file.permissions)
                }

                // Perform additional beaker configuration steps
                for (Map command in beakerConfigurationCommands) {
                    infraUtils.executeInContainer(command)
                }
            }
        }
    }

    infraInstances.sort{it.providerType}
    infraInstances.eachWithIndex { Object instance, int index ->
        infraUtils.generateTopology(instance, index, "${WORKSPACE}/linchpin")
    }

    infraUtils.executeInLinchpin('up', "--workspace \"${WORKSPACE}/linchpin\" --creds-path \"${WORKSPACE}/linchpin/creds\"", config.verbose as Boolean, linchpinContainerName)

    def instance_information = infraUtils.parseDistilledContext()
    if (instance_information) {
        try {
            infraUtils.generateInventory(infraInstances, instance_information)
        } catch (Exception e) {
            println(e)
        }
    } else {
        println("No context was parsed")
    }
}
