import org.centos.pipeline.Infra.Providers.Aws
@Grab('org.yaml:snakeyaml')

import org.yaml.snakeyaml.Yaml

yaml="""
---
infra:
    provision:
      cloud:
        aws:
          count: 4
          name:
          - host1
          - db_server
          - rhel-host1
          - rhel-host2
          location: "us-east-2"
          size: "m1-small"
          security_group: "default"
          ami: "ami23413"





#        - name: host
#          location: "us-east-2"
#          size: "m1-small"
#          security_group: "default"
#          ami: "ami23413"
#          count: 3
#        - name: db_server1
#          location: "us-east-2"
#          size: "m2.small"
#          security_group: "default"
#          ami: "ami09234"
      baremetal:
        beaker:
        - name: host 3
          distro: "rhel"
          arch: "x86_64"
          variant: "server"
        - name: host 4
          distro: "rhel"
          arch: "x86_64"
          variant: "client"
      container:
          name: mysql
          type: docker
          count: 3
#
#  - configure:
#    - playbook:
#        location: /home/runtest.yml
#        params: 'version=3.7'
#
#tests:
#  - playbook:
#      location: /home/runtest.yml
#      params: 'version=3.7'
#
#results:
#  - format:
#    - xunit:
#      location: /path/to/output/
#      file_names:
#        - ouptut.xml
#        - errors.xml
#    - artifacts:
#      location: /path/for/artifact/storage/
#      file_names: "*"
#
#  - delivery:
#    - email:
#        addresses:
#          - you@example.com
#          - me@example.com
#    - messaging:
#        - irc:
#          server: irc.foo.com
#          channel: "#yipee"
#          message: "Run completed successfully"
#        - umb:
#          topic: "thisIsMyTopic"
#          message: "This is my message"
"""

//JsonBuilder(yamlParser.load(("${WORKSPACE}/${filename}.yaml" as File).text)
Yaml yamlParser = new Yaml()

yamlData = yamlParser.load(yaml)
aws_instances = []
println(yamlData)
println(yamlData.infra.provision.cloud.aws)
aws_data = yamlData.infra.provision.cloud.aws
if (aws_data.count){
    if (aws_data.count != aws_data.name.size() && aws_data.name.size() != 1){
        println("Number of instances is not equal to number of provided names")
    } else {
        for ( x in aws_data.name) {
            aws_instances.add(new Aws(aws_data.ami, aws_data.location, x, aws_data.security_group, aws_data.size))
        }
    }
}

println aws_instances
aws_instances.each{ x ->
    println x.name
    println x.ami

}
//println(yamlData.infra.keySet())
//for (i in yamlData.infra.provision.cloud.aws) {
//    println i.keySet()
//}
//
//for (i in yamlData.infra.provision.cloud.aws){
//    if ( i.count ){
//        for ( x in 1..i.count){
//            println "${i.name}_${x}"
//            println (i.location)
//            println (i.ami)
//            println ("${i.size}\n")
//        }
//    }
//}