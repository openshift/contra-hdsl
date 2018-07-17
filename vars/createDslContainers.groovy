/**
 * A method which executes the podTemplate step to create the required ansible-executor and linchpin-executor containers defined.
 * @param config: A map that holds configuration parameters.
 * @param config.podName: String # The name for the pod
 * @param config.openshiftServiceAccount: String  # The name service account on Openshift which will deploy the pod
 * @param config.dockerRepoURL: String # The URL to the openshift URL containing the docker images
 * @param config.ansibleExecutorTag: String # The tag to use for the ansible container
 * @param config.linchinExecutorTag: String # The tag to use for the linchin container
 * @param body - The remainder of the Jenkinsfile which will be executed.
 * @return
 */
def call(Map<String, ?> config=[:], Closure body){

    String podName = config.podName ?: "contraDSL-${UUID.randomUUID()}"
    String openshiftServiceAccount = config.openshiftServiceAccount ?: 'jenkins'
    String openshiftNamespace = config.openshiftNamespace ?: 'contra-dsl'
    String dockerRepoURL = config.dockerRepoURL ?: '172.30.1.1:5000'
    String ansibleExecutorTag = config.ansibleExecutorTag ?: 'stable'
    String linchpinExecutorTag = config.linchpinExecutorTag ?: 'stable'

    podTemplate(name: podName,
            label: podName,
            cloud: 'openshift',
            serviceAccount: openshiftServiceAccount,
            idleMinutes: 0,
            namespace: openshiftNamespace,
            containers:[
                    // This adds the custom slave container to the pod. Must be first with name 'jnlp'
                    containerTemplate(name: 'jnlp',
                            image: "${dockerRepoURL}/${openshiftNamespace}/jenkins-contra-slave:stable",
                            ttyEnabled: false,
                            args: '${computer.jnlpmac} ${computer.name}',
                            command: '',
                            workingDir: '/workDir'),
                    // This adds the ansible-executor container to the
                    containerTemplate(name: 'ansible-executor',
                            image: "${dockerRepoURL}/${openshiftNamespace}/ansible-executor:${ansibleExecutorTag}",
                            ttyEnabled: true,
                            command: '',
                            workingDir: '/workDir'),
                    // This adds the rpmbuild test container to the pod.
                    containerTemplate(name: 'linchpin-executor',
                            alwaysPullImage: true,
                            image: "${dockerRepoURL}/${openshiftNamespace}/linchpin-executor:${linchpinExecutorTag}",
                            ttyEnabled: true,
                            command: '',
                            workingDir: '/workDir'
                            ),
                    ]
    ) {
        body()
    }
}




