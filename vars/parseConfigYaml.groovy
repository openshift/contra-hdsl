@Grab('org.yaml:snakeyaml')

import org.yaml.snakeyaml.Yaml
import groovy.json.*

/**
 * This method reads a yaml file and parses it to JSON so that it can be
 * serialized/deserialized easily and passed around within the pipeline.
 *
 * @param filename
 * @return
 */

env = System.getenv()
print"ENV\n\n\n${env}\n"
def call(String filename=".contra"){
    Yaml parser = new Yaml()

    String configYaml = null
    try{
        env.configJSON = new JsonBuilder(parser.load(("${WORKSPACE}/${filename}.yaml" as File).text))
        echo "Found ${filename}.yaml"


    } catch ( FileNotFoundException ){
        try{
            env.configJSON = new JsonBuilder(parser.load(("${WORKSPACE}/${filename}.yml" as File).text))
            echo "Found ${filename}.yml"


        } catch ( FileNotFoundException e ){
            println(e)
            System.exit(1)

        }
    }
}