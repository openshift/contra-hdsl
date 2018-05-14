import groovy.json.JsonBuilder

/**
 * A method to read a yaml config file and parse it to JSON so that it can be
 * serialized/deserialized easily and passed around within the pipeline.
 *
 * @param config: An optional map that holds configuration parameters.
 * @param config.filename: String file name of the yaml configuration file to parse.
 * @return
 */


def call(Map <String,?> config=[:]){

    configuration_file = config.filename ?: '.contra.yml'

    def yaml = readYaml file: "${WORKSPACE}/${configuration_file}" as String

    env.configJSON = new JsonBuilder(yaml)
}