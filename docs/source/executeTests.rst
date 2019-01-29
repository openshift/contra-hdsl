executeTests
============
The ``executeTests()`` primitive handles executing tests on the infrastructure deployed by the Contra HDSL by executing
Ansible playbooks defined in the configuration file.

Requirements
------------
Basic usage of this primitive assumes that a configuration file has been parsed with ``parseConfig`` or equivalent JSON
has been assigned to the environmental variable ``configJSON`` prior to its usage.

Usage of this variable assumes that infrastructure has been deployed via the use of the deployInfra variable, or that an
Ansible inventory file exists in the root of the ``WORKSPACE`` for the Jenkins job and is named inventory.

Examples
--------

Basic usage
~~~~~~~~~~~
In the basic usage, the primitive draws all configuration from the configuration file. ::

   executeTests()

Enabling verbose output
~~~~~~~~~~~~~~~~~~~~~~~
Verbose output, piped from ansible-playbook, can be displayed by adding a verbose parameter. By default, we opt for non-
verbose output. ::

   executeTests verbose: true

.. _execute_tests_specify_playbook_base_dir:

Specifying base directory
~~~~~~~~~~~~~~~~~~~~~~~~~

It is possible to specify a base directory, relative to the workspace root, which will be prefixed to the playbook path.
This can be useful in situations where a repository containing configuration playbooks is checked out dynamically from
the Jenkinsfile. ::

   executeTests baseDir: "baseDir"

Overriding the container where ansible will execute in pod
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
It is possible to override the container name in the executing pod where ansible is run from. ::

   executeTests ansibleContainerName: "my-ansible-executor"

Overriding or Adding Variables:
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
It is possible to override, or add additional variables to, the existing vars from the configuration file, when calling
this method. ::

   executeTests([var1: value, var2: value2, var3: value3])
