import org.centos.contra.Infra.Utils

/**
 *
 * @param config
 * @param body
 * @return
 */
def call(Map config=[:], Closure body){

    infraUtils = new Utils()

    env.userPodName = config.podName ?: "pod-${UUID.randomUUID()}"
    String openshiftServiceAccount = config.openshift_service_account ?: 'jenkins'
    String openshiftNamespace = infraUtils.getOpenshiftNamespace()
    String dockerRegistryURL = infraUtils.getOpenshiftDockerRegistryURL()

    containers = new ArrayList()

    for(container in config.containers) {
        if ('containerName' in container && 'image' in container) {
            containers.add(
                containerTemplate(
                    name: container.containerName,
                    image: "${dockerRegistryURL}/${openshiftNamespace}/${container.image}:${container.tag ?: 'stable'}",
                    ttyEnabled: container.ttyEnabled in ['true', true],
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

