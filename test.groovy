import groovy.json.JsonSlurperClassic
@Grab('org.yaml:snakeyaml')

import org.yaml.snakeyaml.Yaml
import groovy.json.JsonBuilder

env=[:]
WORKSPACE = "/home/rnester/git/testing"

def parseConfigYaml(String filename=".contra"){
    Yaml yamlParser = new Yaml()
    try{
        env.configJSON = new JsonBuilder(yamlParser.load(("${WORKSPACE}/${filename}.yaml" as File).text))
        println "Found ${filename}.yaml"
    } catch ( FileNotFoundException ){
        try{
            env.configJSON = new JsonBuilder(yamlParser.load(("${WORKSPACE}/${filename}.yml" as File).text))
            println "Found ${filename}.yml"
        } catch ( FileNotFoundException e ){
            println(e)
            System.exit(1)
        }
    }
}

def deployInfra(){
    def parser = new JsonSlurperClassic()
    def configData = parser.parseText(env.configJSON.toString())
    println(configData.infra.keySet())
}
parseConfigYaml()
deployInfra()