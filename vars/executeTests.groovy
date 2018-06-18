import org.centos.contra.Infra.Utils

/**
 * A method that executes tests by calling executeInAnsible for each test playbook defined in the configuration
 * yaml file.
 * @param config: An optional map that holds configuration parameters. The following are used keys in config:
 *                  * verbose: A key with a Boolean value which enables verbose output from the `ansible-playbook` execution.
 *                  * baseDir: A key with a String value which indicates a baseDir relative to the workspace which should be
 *                             prepended to the playbook location
 *                  * vars: A key with a HashMap of vars which will be appended to, or overwrite vars from the configuraiton
 *                          file.
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
            HashMap playbookParams = playbook.vars ?: [:]
            if (config.vars){
                playbookParams  << (config.vars as HashMap)
            }
            String paramString = playbookParams ? "-e ${playbookParams.entrySet().iterator().join(' -e ')}" : ""
            String playbook_path = config.baseDir ? "${config.baseDir}/${playbook.location}" : playbook.location
            infraUtils.executeInAnsible(playbook_path, paramString, config.verbose as Boolean)
        }
    }

}
