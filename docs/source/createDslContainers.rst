createDslContainers
===================

DEPRECATED
----------
This step is deprecated, please use the :doc:`withHdslPod <withHdslPod>` step instead.

Requirements
------------
The ``createDslContainers()`` primitive handles creating the necessary containers which are utilized by the Contra HDSL.
There are two containers which are added to the pod for a given job:

* linchpin-executor
    This container is utilized to perform all linchpin commands, including provisioning and tearing down infrastructure
    described in the configuration file.

* ansible-executor
    This container is utilized to perform all Ansible playbook execution. This includes infrastructure provisioning and
    test execution.

Please ensure that the appropriate images have been created in OpenShift before calling this method. See the section
:ref:`Add Container Templates <add-container-templates>` in :doc:`getting_started`

Examples
--------

Default usage
~~~~~~~~~~~~~
In the basic usage, the primitive just needs values for dockerRepoURL, the OpenShift project namespace and the OpenShift
service account. **Note:** The OpenShift service ac count is often ``jenkins``. ::

    createDslContainers podName: "pod name",
                        dockerRepoURL: "Openshift docker registry URL",
                        openshiftNamespace: "Openshift project namespace",
                        openshiftServiceAccount: "Openshift Service Account"

Overriding the ansible and/or linchpin container name(s)
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
It is possible to provide the name of a different container to be used for any linchpin or ansible tasks. ::

    createDslContainers podName: "pod name",
                        dockerRepoURL: "Openshift docker registry URL",
                        openshiftNamespace: "Openshift project namespace",
                        openshiftServiceAccount: "Openshift Service Account",
                        linchpinContainerName: "my-linchpin-executor",
                        ansibleContainerName: "my-ansible-executor"

