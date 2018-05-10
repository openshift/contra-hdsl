import org.centos.contra.Infra.Utils

def call(Map<String, ?> config=[:]){
    def infraUtils = new Utils()

    def configData = readJSON text: env.configJSON

    infraUtils.executeInLinchpin("destroy", "--creds-path /keys/linchpin", config.verbose as Boolean)
}
