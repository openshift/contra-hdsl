import org.centos.contra.Infra.Utils

def call(Map<String, String> config = [:]) {

    def infraUtils = new Utils()

    def configData = readJSON text: env.configJSON

    // If we have a repo defined to checkout the infra configuration playbooks from, let's handle that
    if (configData.infra.configure.repo) {
        String url = configData.infra.configure.repo.url
        String branch = configData.infra.configure.repo.branch
        String folder = configData.infra.configure.repo.destiation_folder ?: null
        if ( folder ){
            dir(folder){
                git branch: branch, url: url
            }
        } else {
            git branch: branch, url: url
        }

    }

    // Let's execute our playbooks!
    if ( configData.infra.configure.playbooks ) {
        configData.infra.configure.playbooks.each { LinkedHashMap playbook ->
            String playbook_path = playbook.location
            String paramString = "-e ${playbook.vars.iterator().join(', -e ')}"
            infraUtils.executeInAnsible(playbook_path, paramString, config.verbose as Boolean)
        }
    }

}
