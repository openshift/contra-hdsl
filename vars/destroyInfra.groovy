import org.centos.contra.Infra.Utils

/**
 * A method to tear down infrastructure instances.
 * @param config: An optional map that holds configuration parameters. Valid parameters are:
 * @param config.verbose: A key with a Boolean value which enables verbose output from the `linchpin destroy` execution.
 * @param config.linchpinContainerName: a key with a String value which indicates the container name where linchpin
 *                                      will be executed from.
 * @return
 */
def call(Map<String, ?> config=[:]){
    def infraUtils = new Utils()

    def configData = readJSON text: env.configJSON

    infraUtils.executeInLinchpin("destroy", "--creds-path ${WORKSPACE}/linchpin/creds", config.verbose as Boolean,
            config.linchpinContainerName)
}
