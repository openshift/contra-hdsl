report
======

The ``report()`` primitive handles reporting test results and other data.

Requirements
------------
For basic usage, this primitive assumes the presence of a configuration file in the root of ``WORKSPACE``, by default
named ``contra.yaml`` for the Jenkins job. If `passing arbitrary configuration parameters`_, this requirement is not
applicable for this primitive.

Examples
--------

Basic usage
~~~~~~~~~~~
In the basic usage, the primitive draws all configuration from the configuration file. ::

   report()

.. _passing-arbitrary-email-addresses:

Passing arbitrary configuration parameters
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
It is possible to overwrite the default values from the configuration file, or to provide them if no configuration file
exists. This usage allows for the passing of a specific configuration parameters. The optional parameters below can be
used independently of one another. ::

   report recipientEmail: ['carol@example.com', 'susan@example.com'],
                 ccEmail: ['bob@example.com', 'kim@example.com'],
                 bccEmail: 'carl@example.org',
                 replyTo: 'bot@example.com'
