import org.centos.contra.Infra.Providers.Aws
import org.centos.contra.Infra.Providers.Beaker
import org.centos.contra.Infra.Providers.Openstack
import org.centos.contra.Infra.Utils

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
def call(Map<String, ?> config=[:]){

    def infraUtils = new Utils()

    def configData = readJSON text: env.configJSON

    String ansibleContainerName = config.ansibleContainerName ?: 'ansible-executor'

    ArrayList infraInstances = new ArrayList()

    if (configData.infra.provision.cloud) {
        if (configData.infra.provision.cloud.aws) {
            def aws_instances = infraUtils.createAwsInstances(configData.infra.provision.cloud.aws as HashMap)
            if (aws_instances) {
                infraInstances.addAll(aws_instances)
                // Create our aws credential auth file, and our aws ssh key
                infraUtils.createKeyFile('aws', config.aws_credentials_id as String)
                infraUtils.createSSHKeyFile('aws', ansibleContainerName, config.aws_ssh_id as String)
            }
        }
        if (configData.infra.provision.cloud.openstack){
            def openstack_instances = infraUtils.createOpenstackInstances(configData.infra.provision.cloud.openstack as HashMap)
            if (openstack_instances){
                infraInstances.addAll(openstack_instances)
                // Create our openstack credential auth file, and our openstack ssh key
                infraUtils.createKeyFile('openstack', config.openstack_credentials_id as String)
                infraUtils.createSSHKeyFile('openstack', ansibleContainerName, config.openstack_ssh_id as String)
            }
        }
    }

    if (configData.infra.provision.baremetal){
        if (configData.infra.provision.baremetal.beaker){
            ArrayList<Beaker> beaker_instances = infraUtils.createBeakerInstances(configData.infra.provision.baremetal.beaker as HashMap)
            if (beaker_instances) {
                infraInstances.addAll(beaker_instances)
                infraUtils.createKeyFile('beaker', config.beaker_credentials_id as String)
                infraUtils.createSSHKeyFile('beaker', ansibleContainerName, config.beaker_ssh_id as String)
            }
        }
    }

    infraInstances.sort{it.providerType}

    infraInstances.eachWithIndex { Object instance, int index ->
        infraUtils.generateTopology(instance, index, "${WORKSPACE}/linchpin")
    }

    infraUtils.executeInLinchpin("up", "--creds-path ${WORKSPACE}/linchpin/creds", config.verbose as Boolean, config.linchpinContainerName)

    def instance_information = infraUtils.parseDistilledContext()

    if (instance_information){
        infraUtils.generateInventory(infraInstances, instance_information)
    } else {
        println("No context was parsed")
    }

}