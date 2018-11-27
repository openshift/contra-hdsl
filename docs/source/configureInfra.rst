configureInfra
==============
The ``configureInfra()`` primitive handles configuring infrastructure deployed by the Contra HDSL by executing Ansible
playbooks whose locations are either specified in the configuration file, or provided when
`overriding or adding variables`_ to the call.

Requirements
------------

The ``configureInfra()`` primitive assumes that infrastructure has been deployed via the use of the ``deployInfra()``
primitive, or that an Ansible inventory file exists in the root of the WORKSPACE for the Jenkins job and is named
``inventory``.

Examples:
---------

Basic usage:
~~~~~~~~~~~~
In the basic usage, the primitive draws all configuration from the configuration file. ::

    configureInfra()

Enabling verbose output
~~~~~~~~~~~~~~~~~~~~~~~
Verbose output, piped from ansible-playbook, can be displayed by adding a verbose parameter. By default, we opt for non-
verbose output. ::

    configureInfra verbose: true

.. _configure_infra_specify_playbook_base_dir:

Specifying base directory
~~~~~~~~~~~~~~~~~~~~~~~~~
It is possible to specify a base directory, relative to the workspace root, which will be prefixed to the playbook path.
This can be useful in situations where a repository containing configuration playbooks is checked out dynamically from
the jenkinsfile. ::

    configureInfra baseDir: "baseDir"

Overriding the container where ansible will execute in pod
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
It is possible to override the container name in the executing pod where ansible is run from. ::

    configureInfra ansibleContainerName: "my-ansible-executor"


Overriding or Adding Variables
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
It is possible to override, or add additional variables to, the existing vars from the configuration file, when calling
this method. ::

   configureInfra([var1: value, var2: value2, var3: value3])
