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

.. note:: In a forthcoming release, we intend to transition to using images stored in Docker Hub as the image source
          for the containers in the Contra HDSL. Currently, the existing s2i templates are pulling the Docker Hub image
          for the linchpin-executor and ansible-executor containers.
          This change should ensure that version of these containers are synced to the version of the Contra HDSL library.

Parameters
----------
.. table::
   :widths: 20, 10, 10, 60

   ========================= ====== ======== =======
   Parameter                 Type   Required Purpose
   ========================= ====== ======== =======
   openshift_service_account String False    | OpenShift service account to use.
                                             | **Default**: ``'jenkins'``
   linchpin_container_name   String False    | Name of the linchpin container
                                             | **Default**: ``'linchpin-executor'``
   linchpin_image_name       String False    | Image to use
                                             | **Default**: ``'linchpin-executor'``
   linchpin_tag              String False    Tag to use
   ansible_container_name    String False    | Name of the ansible-exeuctor container
                                             | **Default**: ``'ansible-executor'``
   ansible_container_name    String False    | Image to use
                                             | **Default**: ``'ansible-executor'``
   ansible_tag               String False    Tag to use
   jnlp_image_name           String False    | Image to use for the jnlp node
                                             | **Default**: ``'jenkins-contra-slave'``
   jnlp_tag                  String False    Tag to use
   ========================= ====== ======== =======

Examples
--------

Default usage
~~~~~~~~~~~~~
In the basic usage, no additional information is required. ::

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
