import org.centos.contra.Infra.Utils

def call(Boolean verbose) {

    def infraUtils = new Utils()

    def configData = readJSON text: env.configJSON

    // If we have a repo defined to checkout the infra configuration playbooks from, let's handle that
    if (configData.tests.repo) {
        String url = configData.tests.repo.url
        String branch = configData.tests.repo.branch
        String folder = configData.tests.repo.destiation_folder ?: null
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
            String playbook_path = playbook.location
            String paramString = "-e ${playbook.vars.iterator().join(', -e ')}"
            infraUtils.executeInAnsible(playbook_path, paramString, verbose)
        }
    }

}
