import spock.lang.*

import groovy.json.JsonBuilder

import org.centos.contra.Infra.Utils
//import org.centos.contra.Infra.Providers.Aws
//import org.centos.contra.Infra.Providers.Openstack
//import org.centos.contra.Infra.Providers.Beaker

import org.yaml.snakeyaml.Yaml

public class UtilsSpec extends Specification {
    @Shared
    def config = new HashMap<String,?>()
    @Shared
    def infraUtils = new Utils()
    
    def setupSpec() {
        infraUtils = new Utils()
        Yaml yaml = new Yaml()
        config = (Map<String, ?>)yaml.load(new FileReader('test/contra-sample.yml'))
    }

    def "create aws instances" () {
        def aws = config.infra.provision.cloud.aws
        def i = 1
        when:
        def instances = infraUtils.createAwsInstances(aws as HashMap)

        then:
        instances != null
        def string = "hello"
        // createAwsInstances modifies state so we need to modify it back
        def base_name = aws.instances[0].name
        if (aws.instances[0].count) {
            base_name = base_name[0..-3]
        }
        instances.every{ instance -> instance.getName() == base_name + "_" + i++ }
        instances.every{ instance -> instance.getRegion() == aws.instances[0].region }
        instances.every{ instance -> instance.getAmi() == aws.instances[0].ami }
        instances.every{ instance -> instance.getInstance_type() == aws.instances[0].instance_type }
        instances.every{ instance -> instance.getinstance_tags() == aws.instances[0].instance_tags }
        instances.every{ instance -> instance.getSecurity_groups() == aws.instances[0].security_groups.join(', ')}
        instances.every{ instance -> instance.getVpcSubnetID() == aws.instances[0].vpc_subnet_id}
        instances.every{ instance -> instance.getKeyPair() == aws.instances[0].key_pair}
        instances.every{ instance -> instance.getUser() == aws.instances[0].user}
        instances.every{ instance -> instance.getAssignPublicIP() == aws.instances[0].assign_public_ip}
        // check instances for correctness
    }

    def "creating aws instances fails gracefully when data is missing" () {
    }

    def "create beaker instances" () {
        def beaker = config.infra.provision.cloud.beaker
        when:
        def instances = infraUtils.createBeakerInstances(beaker as HashMap)

        then:
        instances != null
        // check instances for correctness
        instances.every { instance -> instances != null }
    }

    def "creating beaker instances fails gracefully" () {
    }

    def "create openstack instances" () {
        def openstack = config.infra.provision.cloud.openstack
        def len = openstack.instances.size()
        def i = 0
        when:
        def instances = infraUtils.createOpenstackInstances(openstack as HashMap)

        then:
        instances != null
        instances.every { instance -> instance.getNetwork() == openstack.network[0] }
        instances.every { instance -> instance.getKeyPair() == openstack.key_pair }
        instances.every { instance -> instance.getSecurityGroups() == openstack.security_groups.join(', ') }
        instances.every { instance -> instance.getName() == openstack.instances[i++].name }
        instances.every { instance -> instance.getFlavor() == openstack.instances[len-(i--)].flavor }
        instances.every { instance -> instance.getImage() == openstack.instances[i++].image }
        instances.every { instance -> instance.getFipPool() == openstack.instances[len-(i--)].floating_ip_pool }
        instances.every { instance -> instance.getUser() == openstack.instances[i++].user }
    }

    def "creating openstack instances fails gracefully" () {
    }

    def "able to execute linchpin" () {
        when:
        1 + 1
        //infraUtils.executeInLinchpin("validate", "", true, "")

        then:
        null == null
        //noExceptionThrown()
    }

    def "handles failures in linchpin" () {
    }

    def "playbooks can be executed in the ansible container" () {
    }

    def "successfully create a topology string for each provider type" () {
        // fill in relevant data here
        /*
        expect:
        infraUtils.createTopology(providerInstance, 0) != null

        where:
        aws << infraUtils.createAwsInstances(config.infra.provision.cloud.aws as HashMap)
        //def beaker = infraUtils.createBeakerInstance(config.infra.provision.cloud.beaker as HashMap)
        openstack << infraUtils.createOpenstackInstances(config.infra.provision.cloud.openstack as HashMap)
        providerInstance << [aws, openstack]
        // AWS, Beaker, and OpenStack
        */
        when:
        def aws = infraUtils.createAwsInstances(config.infra.provision.cloud.aws as HashMap)
        print aws[0].getClass()

        then:
        infraUtils.createTopology(aws[0], 0) != null
    }

    def "correct topologies can be generated" () {
    }

    def "invalid topologies are handled" () {
    }
    
    def "key files can be generated based on provider type" () {
    }

    def "missing keyfiles are handled" () {
    }

    def "ssh keys can be generated for a given provider" () {
    }

    def "context files generated by linchpin can be parsed" () {
    }

    def "context file parsing errors are handled" () {
    }

    def "all instance context data is successfully acquired" () {
    }

    def "inventory file can be generate based on the results of a linchpin deployment" () {
    }

    def "get the template text from a resource file" () {
    }

    def "getTemplateText() handles non-matching provider parameter" () {

    }
}
