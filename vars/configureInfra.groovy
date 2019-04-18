import org.centos.contra.Infra.Utils

/**
 * A method that configures provisioned infrastructure instances by calling executeInAnsible for each configuration
 * playbook defined in the configuration yaml file.
 * @param config: An optional map that holds configuration parameters. The following are used keys in config:
 *                  * verbose: A key with a Boolean value which enables verbose output from the `ansible-playbook` execution.
 *                  * baseDir: A key with a String value which indicates a baseDir relative to the workspace which should be
 *                             prepended to the playbook location
 *                  * vars: A key with a HashMap of vars which will be appended to, or overwrite vars from the configuraiton
 *                          file.
 *                  * ansibleContainerName: a key with a String value which indicates the container name where ansible
 *                          will be executed from.
 * @return
 */
def call(Map<String, ?> config = [:]) {

    def infraUtils = new Utils()

    config = (env.configJSON ? readJSON(text: env.configJSON) : [:]) << config

    // No configuration required
    if (!config.infra.configure) {
        return
    }

    // If we have a repo defined to checkout the infra configuration playbooks from, let's handle that
    if (config.infra.configure.repo) {
        String url = config.infra.configure.repo.url
        String branch = config.infra.configure.repo.branch
        String folder = config.infra.configure.repo.destination_folder ?: null
        if ( folder ){
            dir(folder){
                git branch: branch, url: url
            }
        } else {
            git branch: branch, url: url
        }
    }

    // Let's execute our playbooks!
    if (config.infra.configure.playbooks) {
        config.infra.configure.playbooks.each { LinkedHashMap playbook ->
            HashMap playbookParams = playbook.vars ?: [:]
            if (config.vars){
                playbookParams  << (config.vars as HashMap)
            }
            String paramString = playbookParams ? "-e ${playbookParams.entrySet().iterator().join(' -e ')}" : ""
            String playbook_path = config.baseDir ? "${config.baseDir}/${playbook.location}" : playbook.location
            infraUtils.executeInAnsible(playbook_path, paramString, config.verbose as Boolean, config.ansibleContainerName)
        }
    }
}
