import org.centos.contra.Infra.Utils

import static org.centos.contra.Infra.Defaults.*

/**
 * A method to create the required HDSL pod and constituent containers.
 *
 * By default we will attempt to pull the images from Docker hub
 * @param config
 * @param body
 * @return
 */
def call(Map<String,String> config=[:], Closure body){

    def infraUtils = new Utils()

    env.hdslPodName = config.podName ?: "hdsl-${UUID.randomUUID()}"

    config.source = config.source ?: defaultImageSource

    String openshiftServiceAccount = config.openshift_service_account ?: openshiftServiceAccount
    String openshiftNamespace = config.openshift_namespace ?: infraUtils.getOpenshiftNamespace()

    // Linchpin Container
    config.linchpinContainerName = config.linchpinContainerName ?: linchpinContainerName

    /*
        If there's a value for config.<container_image_name>, we use that.
        Otherwise,
            If the source is 'openshift' we just use the value of <containerImageName>.
            If the source is 'dockerhub', we set config.<container_image_name> to
                ${Default.dockerHubNamesSpace}/${<containerImageName>}

        Example:
            source = 'openshift'
            Image name = 'linchpin'

            source = 'dockerhub'
            Image name = 'contraInfra/linchpin'\

        A similar approach is taken for the image tag as well.

        This is repeated for each of the containers in the Contra HDSL
     */

    config.linchpin_image_name = config.linchpin_image_name ?:
            config.source == 'openshift' ?
                    linchpinImageName :
                    "${dockerHubNameSpace}/${linchpinImageName}"
    config.linchpin_tag = config.linchpin_tag ?:
            config.source == 'openshift' ?
                    linchpinOpenshiftTag :
                    linchpinDockerhubTag

    // Ansible Container
    config.ansibleContainerName = config.ansibleContainerName ?: ansibleContainerName
    config.ansible_image_name = config.ansible_image_name ?:
            config.source == 'openshift' ?
                    ansibleImageName :
                    "${dockerHubNameSpace}/${ansibleImageName}"
    config.ansible_tag = config.ansible_tag ?:
            config.source == 'openshift' ?
                    ansibleOpenshiftTag :
                    ansibleDockerhubTag

    //JNLP Container
    config.jnlp_container_name = jnlpContainerName
    config.jnlp_image_name = config.jnlp_image_name ?:
            config.source == 'openshift' ?
                    jnlpImageName :
                    "${dockerHubNameSpace}/${jnlpImageName}"
    config.jnlp_tag = config.jnlp_tag ?:
            config.source == 'openshift' ?
                    jnlpOpenshiftTag :
                    jnlpDockerhubTag

    podTemplate(name: env.hdslPodName,
            label: env.hdslPodName,
            cloud: 'openshift',
            serviceAccount: openshiftServiceAccount,
            idleMinutes: 0,
            namespace: openshiftNamespace,
            containers:[
                    // This adds the custom slave container to the pod. Must be first with name 'jnlp'
                    containerTemplate(
                            name: config.jnlp_container_name,
                            image: config.source == 'openshift' ?
                                    infraUtils.getOpenShiftImageUrl(openshiftNamespace, config.jnlp_image_name, config.jnlp_tag) :
                                    infraUtils.getDockerHubImageURL(config.jnlp_image_name, config.jnlp_tag),
                            ttyEnabled: false,
                            alwaysPullImage: true,
                            args: '${computer.jnlpmac} ${computer.name}',
                            command: '',
                            workingDir: '/workDir',
                    ),
                    // This adds the ansible-executor container to the pod.
                    containerTemplate(
                            name: config.ansibleContainerName,
                            image: config.source == 'openshift' ?
                                    infraUtils.getOpenShiftImageUrl(openshiftNamespace, config.ansible_image_name, config.ansible_tag) :
                                    infraUtils.getDockerHubImageURL(config.ansible_image_name, config.ansible_tag),
                            ttyEnabled: true,
                            alwaysPullImage: true,
                            command: '',
                            workingDir: '/workDir'
                    ),
                    // This adds the linchpin-executor container to the pod.
                    containerTemplate(
                            name: config.linchpinContainerName,
                            image: config.source == 'openshift' ?
                                    infraUtils.getOpenShiftImageUrl(openshiftNamespace, config.linchpin_image_name, config.linchpin_tag) :
                                    infraUtils.getDockerHubImageURL(config.linchpin_image_name, config.linchpin_tag),
                            ttyEnabled: true,
                            alwaysPullImage: true,
                            command: '',
                            workingDir: '/workDir'
                    ),
            ]
    ) {
       body()
    }
}
