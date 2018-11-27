destroyInfra
============
The destroyInfra variable handles tearing down infrastructure deployed by the Contra HDSL.

Requirements
------------

Usage of this variable assumes that infrastructure has been deployed via the use of the deployInfra variable.

Usage of this variable also requires the configuration of credentials on the Jenkins master as applicable for each
resource type.

Examples
--------

Basic usage
~~~~~~~~~~~
The basic usage of ``destroyInfra()`` tears down infrastructure with only a minimal amount of detail provided. ::

   destroyInfra()


This usage assumes the following for each provider type defined in the configuration file

* A secret file credential is configured in Jenkins which contains the necessary data to authenticate against the provider
  service

* The secret file for authentication is named in the format <provider>.creds

Enabling verbose output
~~~~~~~~~~~~~~~~~~~~~~~
Verbose output, piped from linchpin, can be displayed by adding a verbose parameter. By default, we opt for non-
verbose output. ::

   destroyInfra verbose: true


Overriding the container where ansible will execute in pod
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
It is possible to override the container name in the executing pod where linchpin is run from. ::

   destroyInfra linchpinContainerName: "my-linchpin-executor"


Provide arbitrary values for credential and SSH key IDs
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
It is possible to override the default expectation that the credentials and ssh key used for a given provider match the
provider name by specifying values for the ``<provider>_credentials_id`` and the ``<provider>_ssh_id``. ::

   destroyInfra <provider>_credentials_id: "credentials file ID",
                <provider>_ssh_id: "SSH private key ID"

**Note**: There should be one <provider>_credentials_id key per provider. The same private SSH ID value can be specified
for multiple providers, but must be assigned to the ``<provider>_ssh_id key``.
