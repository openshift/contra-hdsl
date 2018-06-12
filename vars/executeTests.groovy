import org.centos.contra.Infra.Utils

/**
 * A method that executes tests by calling executeInAnsible for each test playbook defined in the configuration
 * yaml file.
 * @param config: An optional map that holds configuration parameters.
 * @param config.verbose: A key with a Boolean value which enables verbose output from the ansible-playbook execution.
 * @param config.baseDir: A key with a String value which indicates a baseDir relative to the workspace which should be
 *                        prepended to the playbook location
 * @return
 */
def call(Map<String, String> config = [:]) {

    def infraUtils = new Utils()

    def configData = readJSON text: env.configJSON

    // If we have a repo defined to checkout the infra configuration playbooks from, let's handle that
    if (configData.tests.repo) {
        String url = configData.tests.repo.url
        String branch = configData.tests.repo.branch
        String folder = configData.tests.repo.destination_folder ?: null
        if ( folder ){
            dir(folder){
                git branch: branch, url: url
            }
        } else {
            git branch: branch, url: url
        }
    }

    // Let's execute our playbooks!
    if ( configData.tests.playbooks ) {
        configData.tests.playbooks.each { LinkedHashMap playbook ->
            String playbook_path = config.baseDir ? "${config.baseDir}/${playbook.location}" : playbook.location
            String paramString = "-e ${playbook.vars.iterator().join(', -e ')}"
            infraUtils.executeInAnsible(playbook_path, paramString, config.verbose as Boolean)
        }
    }

}
