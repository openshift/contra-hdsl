import groovy.json.JsonBuilder

/**
 * A method to read a yaml config file and parse it to JSON so that it can be
 * serialized/deserialized easily and passed around within the pipeline.
 *
 * @param filename
 * @return
 */


def call(String filename=".contra.yml"){

    def yaml = readYaml file: "${WORKSPACE}/${filename}" as String

    env.configJSON = new JsonBuilder(yaml)
}