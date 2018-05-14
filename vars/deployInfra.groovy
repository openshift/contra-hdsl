import org.centos.contra.Infra.Providers.Aws
import org.centos.contra.Infra.Providers.Beaker
import org.centos.contra.Infra.Providers.Openstack
import org.centos.contra.Infra.Utils

/**
 *
 * @param config: An optional map that holds configuration parameters.
 * @param config.verbose: A key with a Boolean value which enables verbose output from the `linchpin up` execution.
 * @return
 */
def call(Map<String, ?> config=[:]){

    env.HOME = "/root"

    def infraUtils = new Utils()

    def configData = readJSON text: env.configJSON

    ArrayList infraInstances = new ArrayList()

    if (configData.infra.provision.cloud) {
        if (configData.infra.provision.cloud.aws) {
            def aws_instances = infraUtils.createAwsInstances(configData.infra.provision.cloud.aws as HashMap)
            if (aws_instances) {
                infraInstances.addAll(aws_instances)
                aws_instances.eachWithIndex { Aws instance, index ->
                    infraUtils.generateTopology(instance, index, "${WORKSPACE}/linchpin")
                }
                // Create our aws credential auth file, and our aws ssh key
                infraUtils.createKeyFile('aws', config.aws_credentials_id as String)
                infraUtils.createSSHKeyFile('aws', 'ansible-executor', config.aws_ssh_id as String)
            }
        }

        if (configData.infra.provision.cloud.openstack){
            def openstack_instances = infraUtils.createOpenstackInstances(configData.infra.provision.cloud.openstack as HashMap)
            if (openstack_instances){
                infraInstances.addAll(openstack_instances)
                openstack_instances.eachWithIndex { Openstack instance, index ->
                    infraUtils.generateTopology(instance, index, "${WORKSPACE}/linchpin")
                }

                // Create our openstack credential auth file, and our openstack ssh key
                infraUtils.createKeyFile('openstack', config.openstack_credentials_id as String)
                infraUtils.createSSHKeyFile('openstack', 'ansible-executor', config.openstack_ssh_id as String)
            }
        }
    }

    if (configData.infra.provision.baremetal){
        if (configData.infra.provision.baremetal.beaker){
            ArrayList<Beaker> beaker_instances = infraUtils.createBeakerInstances(configData.infra.provision.baremetal.beaker as HashMap)
            if (beaker_instances) {
                infraInstances.addAll(beaker_instances)
                beaker_instances.eachWithIndex { Beaker instance, int index ->
                    infraUtils.generateTopology(instance, index, "${WORKSPACE}/linchpin")
                }

                infraUtils.createKeyFile('beaker', config.beaker_credentials_id as String)
                infraUtils.createSSHKeyFile('beaker', 'ansible-executor', config.beaker_ssh_id as String)
            }
        }
    }


    infraUtils.executeInLinchpin("up", "--creds-path /keys/linchpin", config.verbose as Boolean)

    def instance_information = infraUtils.parseDistilledContext()

    if (instance_information){
        infraUtils.generateInventory(infraInstances, instance_information)
    } else {
        println("No context was parsed")
    }

}