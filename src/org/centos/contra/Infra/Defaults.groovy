package org.centos.contra.Infra

/**
 * A class to hold static default values.
 * To use them, import the class and address them with
 * Defaults.<the value you want>
 */
class Defaults {

/**********************
    HDSL Pod Defaults
***********************/

    // Set our default source for images in the HDSL pod, valid options are 'openshift' and 'dockerhub'
    public static final String defaultImageSource = 'openshift'

    // Set our default OpenShift service account
    public static final String openshiftServiceAccount = 'jenkins'
    // The default docker hub namespace
    public static final String dockerHubNameSpace = 'contrainfra'

    // Set the default tag for images from Docker Hub in the HDSL pod.
    public static final String ansibleDockerhubTag = 'v1.1.2'
    public static final String jnlpDockerhubTag = 'v1.1.2'
    public static final String linchpinDockerhubTag = 'v1.1.0'

    // Set the default tag for imagestreams from OpenShift in the HDSL pod.
    public static final String ansibleOpenshiftTag = 'stable'
    public static final String jnlpOpenshiftTag = 'stable'
    public static final String linchpinOpenshiftTag = 'stable'

    // The default image names for containers
    public static final String ansibleImageName = 'ansible-executor'
    public static final String jnlpImageName = 'jenkins-contra-slave'
    public static final String linchpinImageName = 'linchpin-executor'

    // The default names for containers
    public static final String ansibleContainerName = 'ansible-executor'
    // The jnlp container must be named 'jnlp', per the Kubernetes plugin podTemplate step
    public static final String jnlpContainerName = 'jnlp'
    public static final String linchpinContainerName = 'linchpin-executor'


}
