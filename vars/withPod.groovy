import org.centos.contra.Infra.Utils
import static org.centos.contra.Infra.Defaults.*

/**
 *
 * @param config
 * @param body
 * @return
 */
def call(Map config=[:], Closure body) {
    def infraUtils = new Utils()

    env.userPodName = config.podName ?: "pod-${UUID.randomUUID()}"

    String openshiftServiceAccount = config.openshift_service_account ?: openshiftServiceAccount
    openshiftNamespace = config.openshift_namespace ?: infraUtils.getOpenshiftNamespace()

    config.jnlp_image_name = config.jnlp_image_name ?: jnlpImageName
    config.jnlp_tag = config.jnlp_tag ?:
            defaultImageSource == 'openshift' ?
                    jnlpOpenshiftTag :
                    jnlpDockerhubTag

    String jnlpImageURL = defaultImageSource == 'openshift' ?
            infraUtils.getOpenShiftImageUrl(openshiftNamespace, config.jnlp_image_name as String, config.jnlp_tag as String) :
            infraUtils.getDockerHubImageURL(config.jnlp_iamge_name as String, config.jnlp_tag as String)

    // Create the containers array, and prepopulate it with our JNLP slave
    ArrayList containers = new ArrayList()

    containers.add([name: jnlpContainerName,
                    image: jnlpImageURL,
                    ttyEnabled: false,
                    args: '${computer.jnlpmac} ${computer.name}',
                    command: '',
                    workingDir: '/workDir']
    )

    for(Map<String,String> container in config.containers) {
        if ('containerName' in container && 'image' in container) {
            /*
              Default to the source of the container image being an OpenShift image stream.
              If desired, this can be set to 'dockerhub' and we will pull the container image from Docker Hub directly.
            */
            container.source = container.source ?: 'openshift'

            // Read the value of the 'tag' key from the container if present. Otherwise default to 'latest'.
            container.tag = container.tag ?: 'latest'

            containers.add(
                    containerTemplate(
                            name: container.containerName,
                            image: container.source == 'openshift' ?
                                    infraUtils.getOpenShiftImageUrl(openshiftNamespace, container.image, container.tag) :
                                    infraUtils.getDockerHubImageURL(container.image, container.tag),
                            ttyEnabled: container.ttyEnabled in ['true', true],
                            alwasyPullImage: true,
                            args: container.args ?: '',
                            command: container.command ?: '',
                            workingDir: container.workingDir ?: '/workDir'
                    )
            )
        } else {
            throw new Exception('Container must have containerName and image provided')
        }
    }

    podTemplate(
        name: env.userPodName,
        label: env.userPodName,
        cloud: 'openshift',
        serviceAccount: openshiftServiceAccount,
        idleMinutes: 0,
        namespace: openshiftNamespace,
        containers: containers
    ) {
        body()
    }
}
