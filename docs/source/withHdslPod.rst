withHdslPod
===========
The ``withHdslPod()`` primitive handles creating an OpenShift pod, with the specified necessary containers inside of it,
as required by the Contra HDSL.

Requirements
------------
The ``withHdslPod()`` primitive handles creating the necessary containers which are utilized by the Contra HDSL.
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
In the basic usage, no additional information is required.

**Note:** The OpenShift service account is often ``jenkins``. ::

    withHdslPod{
        // Jenkinsfile content goes here
    }

Overriding the ansible and/or linchpin container name(s)
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
It is possible to specify an alternate image by providing a name and / or tag of a different container for the
linchpin-executor, ansible-executor, or jnlp slave. ::

    withHdslPod ansible_container_name: 'my-ansible-container',
                ansible_executor_tag: 'latest'
                linchpin_container_name: 'my-linchpin-container',
    {
        // Jenksinfile content goes here
    }
