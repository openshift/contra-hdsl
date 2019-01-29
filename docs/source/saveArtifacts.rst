saveArtifacts
=============
The ``saveArtifacts()`` primitive handles storing the specified set of files from the ``WORKSPACE`` of the Jenkins job
within the Jenkins master.

Requirements
------------
For basic usage, this primitive assumes the presence of a configuration file in the root of ``WORKSPACE``, by default
named ``contra.yaml`` for the Jenkins job. If `passing-arbitrary-configuration-parameters`_, this requirement is not
applicable for this primitive.

Examples
--------

Basic usage
~~~~~~~~~~~
In the basic usage, the primitive draws all configuration from the configuration file. ::

   saveArtifacts()

.. _passing-arbitrary-configuration-parameters:

Passing arbitrary configuration parameters
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
It is possible to overwrite the default values from the configuration file, or to provide them if no configuration file
exists. This usage allows for the passing of a specific configuration parameters. The optional parameters below can be
used independently of one another. ::

   saveArtifacts filesToSave: 'foo.txt, foo/**/bar.txt',
                 filesToExclude: ['bar.txt', 'baz.txt'],
                 allowEmptyArchive: false,
                 fingerprint: true,
                 onlyIfSuccessful: true,
                 defaultExcludes: false

