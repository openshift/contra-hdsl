import org.centos.contra.Infra.Utils

/**
 *
 * @param config
 * @param body
 * @return
 */
def call(Map config=[:], Closure body){

    infraUtils = new Utils()

    env.podName = config.podName ?: "rhprod-${UUID.randomUUID()}"
    String openshiftServiceAccount = config.openshift_service_account ?: 'jenkins'
    String openshiftNamespace = infraUtils.getOpenshiftNamespace()
    String dockerRegistryURL = infraUtils.getOpenshiftDockerRegistryURL()
    String ansibleExecutorTag = config.linchpin_executor_tag ?: 'stable'
    String linchpinExecutorTag = config.linchpin_executor_tag ?: 'stable'
    String jenkinsContraSlaveTag = config.jenkins_slave_tag ?: 'stable'
    String ansibleExecutorContainerName = config.productization_container_name ?: 'ansible-executor'
    String linchpinExecutorContainerName = config.productization_container_name ?: 'linchpin-executor'
    String jslaveContainerName = config.jenkins_slave_container_name ?: 'jenkins-contra-slave'

    podTemplate(name: env.podName,
            label: env.podName,
            cloud: 'openshift',
            serviceAccount: openshiftServiceAccount,
            idleMinutes: 0,
            namespace: openshiftNamespace,
            containers:[
                    // This adds the custom slave container to the pod. Must be first with name 'jnlp'
                    containerTemplate(name: 'jnlp',
                            image: "${dockerRegistryURL}/${openshiftNamespace}/${jslaveContainerName}:${jenkinsContraSlaveTag}",
                            ttyEnabled: false,
                            args: '${computer.jnlpmac} ${computer.name}',
                            command: '',
                            workingDir: '/workDir'),
                    // This adds the ansible-executor container to the pod.
                    containerTemplate(name: ansibleContainerName,
                            image: "${dockerRegistryURL}/${openshiftNamespace}/${ansibleExecutorContainerName}:${ansibleExecutorTag}",
                            ttyEnabled: true,
                            command: '',
                            workingDir: '/workDir'),
                    // This adds the rpmbuild test container to the pod.
                    containerTemplate(name: linchpinContainerName,
                            alwaysPullImage: true,
                            image: "${dockerRegistryURL}/${openshiftNamespace}/${linchpinExecutorContainerName}:${linchpinExecutorTag}",
                            ttyEnabled: true,
                            command: '',
                            workingDir: '/workDir'
                    ),
            ]
    ) {
       body()
    }
}

