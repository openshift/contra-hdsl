import spock.lang.*
import com.homeaway.devtools.jenkins.testing.JenkinsPipelineSpecification

import org.yaml.snakeyaml.Yaml

public class UtilsSpec extends JenkinsPipelineSpecification {
    @Shared
    def config = new HashMap<String,?>()

    @Shared
    def infraUtils
    def setup() {
        infraUtils = loadPipelineScriptForTest( "../../src/org/centos/contra/Infra/Utils.groovy" )
        explicitlyMockPipelineStep('dir')
        explicitlyMockPipelineStep('writeFile')
        explicitlyMockPipelineStep('fileExists')
        explicitlyMockPipelineStep('withEnv')
        explicitlyMockPipelineStep('container')
        explicitlyMockPipelineVariable('WORKSPACE')
    }
    
    def setupSpec() {
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
    }

    def "creating aws instances drops to defaults when data is missing" () {
        def aws = [instances: [[:]]]
        when:
        def instances = infraUtils.createAwsInstances(aws as HashMap)

        then:
        instances != null
    }

    def "create beaker instances" () {
        def beaker = config.infra.provision.cloud.beaker
        when:
        def instances = infraUtils.createBeakerInstances(beaker as HashMap)

        then:
        instances != null
        // TODO: check instances for correctness
        instances.every { instance -> instances != null }
    }

    def "creating beaker instances drops to defaults when data is missing" () {
        def beaker = [instances: [[:]]]
        when:
        def instances = infraUtils.createBeakerInstances(beaker as HashMap)

        then:
        instances != null
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

    def "creating openstack instances drops to defaults when data is missing" () {
        def openstack = [instances: [[:]]]
        when:
        def instances = infraUtils.createOpenstackInstances(openstack as HashMap)

        then:
        instances != null
    }

    def "able to execute linchpin" () {
        setup:
        getPipelineMock("env.getEnvironment")() >> []

        when:
        infraUtils.executeInLinchpin("validate", "", true, "abc")

        then:
        noExceptionThrown()
    }

    def "handles failures in linchpin" () {
    }

    def "ansible containers run without errors" () {
        setup:
        getPipelineMock("env.getEnvironment")() >> []

        when:
        infraUtils.executeInAnsible("anything.yml", "", true, "abc")

        then:
        noExceptionThrown()
    }

    def "playbooks can be executed in the ansible container" () {
    }

    def "successfully create a topology string - aws" () {
        setup:
        getPipelineMock("libraryResource")(_) >> {
            return '''${index}_${providerType}:
  topology:
    topology_name: "ec2-new"
    resource_groups:
      - resource_group_name: "aws"
        resource_group_type: "aws"
        resource_definitions:
          - name: "${name}"
            flavor: "${instanceType}"
            role: "aws_ec2"
            region: "${region}"
            image: "${ami}"
            security_group: "${securityGroups ? securityGroups : 'default' }"
            count: 1
            <% if (vpcSubnetId) out.print "vpc_subnet_id: ${vpcSubnetId}" %>
            <% if (keyPair) out.print "keypair: ${keyPair}" %>
            <% if (assignPublicIp) out.print "assign_public_ip: ${assignPublicIp}" %>
        credentials:
            filename: "aws.creds"
            profile: default'''
        }

        when:
        def instance = infraUtils.createAwsInstances(config.infra.provision.cloud.aws as HashMap)[0]
        infraUtils.generateTopology(instance, 1, "somedir")

        then:
        noExceptionThrown()
    }

    def "successfully create a topology string - beaker" () {
        setup:
        getPipelineMock("libraryResource")(_) >> {
            return '''${index}_${providerType}:
  topology:
    topology_name: "beaker-slave"
    resource_groups:
    - resource_group_name: "beaker-slaves"
      resource_group_type: "beaker"
      resource_definitions:
      - role: "bkr_server"<% if ( jobGroup ) "\\n        job_group: \\"${jobGroup}\\"" %>
        whiteboard: "${whiteboard ? whiteboard : 'Dynamically provisioned'}"
        recipesets:
        - name: "${name}"
          distro: "${distro}"
          arch: "${arch}"
          variant: "${variant}"
          count: 1<% if (bkrData) out.print "\\n          bkr_data: ${bkrData}" %><% if (hostrequires) {
              out.print '\\n          hostrequires:\'
              hostrequires.each { entry ->
                   entry.eachWithIndex { k, v, index->
                       if (index == 0) out.print "\\n          - ${k}: \\"${v}\\"" else out.print "\\n            ${k}: \\"${v}\\""
                   }
              }
          }%>
'''
        }

        when:
        def instance = infraUtils.createBeakerInstances(config.infra.provision.cloud.beaker as HashMap)[0]
        infraUtils.generateTopology(instance, 1, "somedir")

        then:
        noExceptionThrown()

    }

    def "successfully create a topology string - openstack" () {
        setup:
        getPipelineMock("libraryResource")(_) >> {
            return '''${index}_${providerType}:
  topology:
    topology_name: "os-single-new"
    resource_groups:
      - resource_group_name: "os-server-new"
        resource_group_type: "openstack"
        resource_definitions:
          - name: "${name}"
            role: "os_server"
            flavor: "${instanceType}"
            image: "${image}"
            count: 1
            <% if (keyPair) out.print "keypair: ${keyPair}" %>
            <% if (fipPool) out.print "fip_pool: ${fipPool}" %>
            <% if (network) out.print "networks:\\n              - ${network}" %>
            <% if (autoIP) out.print "auto_ip: ${autoIP}"%>
        credentials:
          filename: "openstack.creds"
          profile: ci-rhos
'''
        }

        when:
        def instance = infraUtils.createOpenstackInstances(config.infra.provision.cloud.openstack as HashMap)[0]
        infraUtils.generateTopology(instance, 1, "somedir")

        then:
        noExceptionThrown()
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
