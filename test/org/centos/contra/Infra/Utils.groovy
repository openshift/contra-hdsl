import spock.lang.*
import com.homeaway.devtools.jenkins.testing.JenkinsPipelineSpecification

import org.yaml.snakeyaml.Yaml

public class UtilsSpec extends JenkinsPipelineSpecification {
    @Shared
    def config = new HashMap<String, ?>()

    @Shared
    def infraUtils

    def setup() {
        infraUtils = loadPipelineScriptForTest("../../src/org/centos/contra/Infra/Utils.groovy")
        infraUtils.getBinding().setVariable("WORKSPACE", "workspace")
        infraUtils.getBinding().setVariable("CREDENTIAL_FILE_NAME", "credsfile")
        infraUtils.getBinding().setVariable("SSH_FILE_NAME", "sshfile")
        explicitlyMockPipelineStep('container')

    }

    def setupSpec() {
        Yaml yaml = new Yaml()
        config = (Map<String, ?>) yaml.load(new FileReader('test/contra-sample.yml'))
    }

    def "create aws instances"() {
        setup:
            def aws = config.infra.provision.cloud.aws
            def i = 1
        when:
            def instances = infraUtils.createAwsInstances(aws as HashMap)
            def base_name = aws.instances[0].name[0..-3]

        then:
            instances != null
            instances.every { instance -> instance.getName() == base_name + "_" + i++ }
            instances.every { instance -> instance.getRegion() == aws.instances[0].region }
            instances.every { instance -> instance.getAmi() == aws.instances[0].ami }
            instances.every { instance -> instance.getInstance_type() == aws.instances[0].instance_type }
            instances.every { instance -> instance.getinstance_tags() == aws.instances[0].instance_tags }
            instances.every { instance -> instance.getSecurity_groups() == aws.instances[0].security_groups.join(', ') }
            instances.every { instance -> instance.getVpcSubnetID() == aws.instances[0].vpc_subnet_id }
            instances.every { instance -> instance.getKeyPair() == aws.instances[0].key_pair }
            instances.every { instance -> instance.getUser() == aws.instances[0].user }
            instances.every { instance -> instance.getAssignPublicIP() == aws.instances[0].assign_public_ip }
    }

    def "creating aws instances drops to defaults when data is missing"() {
        setup:
            def aws = [instances: [[:]]]
        when:
            def instances = infraUtils.createAwsInstances(aws as HashMap)

        then:
            instances != null
    }

    def "create beaker instances"() {
        setup:
            def beaker = config.infra.provision.cloud.beaker
        when:
            def instances = infraUtils.createBeakerInstances(beaker as HashMap)

        then:
            instances != null
            instances.every { instance -> instances != null }
    }

    def "creating beaker instances drops to defaults when data is missing"() {
        setup:
            def beaker = [instances: [[:]]]
        when:
            def instances = infraUtils.createBeakerInstances(beaker as HashMap)

        then:
            instances != null
    }

    def "create openstack instances"() {
        setup:
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
            instances.every { instance -> instance.getFlavor() == openstack.instances[len - (i--)].flavor }
            instances.every { instance -> instance.getImage() == openstack.instances[i++].image }
            instances.every { instance -> instance.getFipPool() == openstack.instances[len - (i--)].floating_ip_pool }
            instances.every { instance -> instance.getUser() == openstack.instances[i++].user }
    }

    def "creating openstack instances drops to defaults when data is missing"() {
        setup:
            def openstack = [instances: [[:]]]
        when:
            def instances = infraUtils.createOpenstackInstances(openstack as HashMap)

        then:
            instances != null
    }

    def "able to execute linchpin"() {
        setup:
            getPipelineMock("env.getEnvironment")() >> []

        when:
            infraUtils.executeInLinchpin("validate", "", true, "abc")

        then:
            noExceptionThrown()
    }

    def "ansible containers can be called without errors"() {
        setup:
            getPipelineMock("env.getEnvironment")() >> []

        when:
            infraUtils.executeInAnsible("anything.yml", "", true, "abc")

        then:
            noExceptionThrown()
    }

    def "executeInAnsible sends correct command to the container for playbook execution"() {
        setup:
            infraUtils.getBinding().setVariable("WORKSPACE", "workspace")
        when:
            def status = infraUtils.executeInAnsible("anything.yml", "", true, "abc")
        then:
            1 * getPipelineMock("sh")("""ansible-playbook -vvv -i "workspace/inventory" "workspace/anything.yml" """)
    }

    def "aws topology is created and is correct"() {
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
            profile: default
'''
            }
            def instance = infraUtils.createAwsInstances(config.infra.provision.cloud.aws as HashMap)[0]

        when:
            infraUtils.generateTopology(instance, 1, "somedir")

        then:
            noExceptionThrown()
            1 * getPipelineMock("writeFile")([file: "PinFile", text: '''1_aws:
  topology:
    topology_name: "ec2-new"
    resource_groups:
      - resource_group_name: "aws"
        resource_group_type: "aws"
        resource_definitions:
          - name: "aws-ec2-1_2_1"
            flavor: "ec2"
            role: "aws_ec2"
            region: "us-east-1"
            image: "ami-984189e2"
            security_group: "group1"
            count: 1
            vpc_subnet_id: abc
            keypair: aws_creds
            assign_public_ip: true
        credentials:
            filename: "aws.creds"
            profile: default
'''])
    }

    def "beaker topology is created and is correct"() {
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
                count: 1<% if (bkrData) out.print "\\n                bkr_data: ${bkrData}" %><% if (hostrequires) {
                  out.print '\\n                hostrequires:\'
              hostrequires.eachWithIndex { entry, index ->
                   entry.each { k, v ->
                       if (index == 0) out.print "\\n                  - ${k}: \\"${v}\\"" else out.print "\\n                    ${k}: \\"${v}\\""
                   }
              }
          }%>
'''
            }
            def instance = infraUtils.createBeakerInstances(config.infra.provision.cloud.beaker as HashMap)[0]

        when:
            infraUtils.generateTopology(instance, 1, "somedir")

        then:
            noExceptionThrown()
            1 * getPipelineMock("writeFile")([file: "PinFile", text: '''1_beaker:
  topology:
    topology_name: "beaker-slave"
    resource_groups:
      - resource_group_name: "beaker-slaves"
        resource_group_type: "beaker"
        resource_definitions:
          - role: "bkr_server"
            whiteboard: "Dynamically provisioned"
            recipesets:
              - name: "hello"
                distro: "RHEL-6.5"
                arch: "x86_64"
                variant: "a"
                count: 1
                hostrequires:
                  - b: "c"
                    a: "b"
'''])

    }

    def "openstack topology is created and is correct"() {
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
            def instance = infraUtils.createOpenstackInstances(config.infra.provision.cloud.openstack as HashMap)[0]
            def instance_name = instance.getNameWithUUID()

        when:
            infraUtils.generateTopology(instance, 1, "somedir")

        then:
            noExceptionThrown()
            1 * getPipelineMock("writeFile")([file: "PinFile", text: """1_openstack:
  topology:
    topology_name: "os-single-new"
    resource_groups:
      - resource_group_name: "os-server-new"
        resource_group_type: "openstack"
        resource_definitions:
          - name: "$instance_name"
            role: "os_server"
            flavor: "m1.small"
            image: "CentOS-7-x86_64-GenericCloud-1612"
            count: 1
            keypair: ci-factory
            fip_pool: 10.8.240.0
            networks:
              - atomic-e2e-jenkins-test
            
        credentials:
          filename: "openstack.creds"
          profile: ci-rhos
"""])
    }

    def "key files can be generated for aws"() {
        when:
            infraUtils.createKeyFile('aws')
        then:
            1 * getPipelineMock("file.call").call(['credentialsId': "aws.creds", 'variable': 'CREDENTIAL_FILE_NAME'])
    }

    def "key files can be generated for beaker"() {
        when:
            infraUtils.createKeyFile('beaker')
        then:
            1 * getPipelineMock("file.call").call(['credentialsId': "beaker.creds", 'variable': 'CREDENTIAL_FILE_NAME'])
    }

    def "key files can be generated for openstack"() {
        when:
            infraUtils.createKeyFile('openstack')
        then:
            1 * getPipelineMock("file.call").call(['credentialsId': "openstack.creds", 'variable': 'CREDENTIAL_FILE_NAME'])
    }

    def "ssh keys can be generated for aws"() {
        when:
            infraUtils.createSSHKeyFile('aws', 'someContainer')
        then:
            1 * getPipelineMock("sshUserPrivateKey.call").call(['credentialsId': 'aws.ssh', 'keyFileVariable': 'SSH_FILE_NAME', 'passphraseVariable': 'SSH_PASSPHRASE', 'usernameVariable': 'SSH_USERNAME'])
    }

    def "ssh keys can be generated for beaker"() {
        when:
            infraUtils.createSSHKeyFile('beaker', 'someContainer')
        then:
            1 * getPipelineMock("sshUserPrivateKey.call").call(['credentialsId': 'beaker.ssh', 'keyFileVariable': 'SSH_FILE_NAME', 'passphraseVariable': 'SSH_PASSPHRASE', 'usernameVariable': 'SSH_USERNAME'])
    }

    def "ssh keys can be generated for openstack"() {
        when:
            infraUtils.createSSHKeyFile('openstack', 'someContainer')
        then:
            1 * getPipelineMock("sshUserPrivateKey.call").call(['credentialsId': 'openstack.ssh', 'keyFileVariable': 'SSH_FILE_NAME', 'passphraseVariable': 'SSH_PASSPHRASE', 'usernameVariable': 'SSH_USERNAME'])
    }

    def "context files generated by linchpin can be parsed"() {
        setup:
            getPipelineMock("readJSON")(_) >> {
                return ['validJSON': true]
            }
        when:
            def result = infraUtils.parseDistilledContext('someFile')
        then:
            result == ['validJSON': true]
    }

    def "inventory file can be generated"() {
        setup:
            def instance = infraUtils.createAwsInstances(config.infra.provision.cloud.aws as HashMap)[0]
        when:
            infraUtils.generateInventory(instance, [:])
        then:
            1 * getPipelineMock("writeFile").call(['file': 'workspace/inventory', 'text': '\n\n'])

    }
}
