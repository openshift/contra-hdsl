deployInfra
===========
The ``deployInfra()`` primitive handles deploying infrastructure defined in the configuration file.

Requirements
------------

Usage of this primitive assumes that a configuration file has been parsed with ``parseConfig`` or equivalent JSON has been
assigned to the environmental variable ``configJSON`` prior to its usage.

Usage of this primitive also requires the configuration of credentials and SSH keys on the Jenkins master as applicable
for each resource type.

Examples
--------

Basic usage
~~~~~~~~~~~
This usage assumes the following for each provider type defined in the configuration file

* A secret file credential is configured in Jenkins
    * This file contains the necessary data to authenticate against the provider service
    * The secret file for authentication is named in the format <provider>.creds
* A private SSH key credential is configured in Jenkins
    * This key has the ability to login to the provisioned resources.
    * The private SSH key credential ID should be named in the format <provider>.ssh

::

   deployInfra()

Enabling verbose output
~~~~~~~~~~~~~~~~~~~~~~~
Verbose output, piped from linchpin, can be displayed by adding a verbose parameter. By default, we opt for non-
verbose output. ::

  deployInfra verbose: true

Overriding the container where ansible will execute in pod
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
It is possible to override the container name in the executing pod where linchpin is run from. ::

   deployInfra linchpinContainerName: "my-linchpin-executor"

Provide arbitrary values for credential and SSH key IDs
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
It is possible to override the default expectation that the credentials and ssh key used for a given provider match the
provider name by specifying values for the ``<provider>_credentials_id`` and the ``<provider>_ssh_id``. ::

   deployInfra <provider>_credentials_id: "credentials file ID",
               <provider>_ssh_id: "SSH private key ID"

**Note**: There should be one <provider>_credentials_id key per provider. The same private SSH ID value can be specified
for multiple providers, but must be assigned to the ``<provider>_ssh_id key``.
