parseConfig
===========
The ``parseConfig()`` primitive handles parsing the YAML configuration file used in the Contra HDSL. This YAML file is
converted to JSON and serialized to a string and assigned to the environmental variable ``configJSON``

Requirements
------------
This primitive requires that either a configuration file exist. By default, the name of this configuration file is
expected to be ``contra.yaml``, although you can use any arbitrary name. See the
:ref:`Using a custom configuration file name <using-a-custom-configuration-file-name>` section below for details.

Examples
--------

Basic usage
~~~~~~~~~~~
In the basic usage, the primitive uses the default configuration file name - ``contra.yaml``. ::

   parseConfig()

.. _using-a-custom-configuration-file-name:

Using a custom configuration file name
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
It is possible to pass a custom configuration file to the ``parseConfig()`` method. If the file doesn't exist in the
root of the ``WORKSPACE``, the path to the configuration file should be relative to the root of ``WORKSPACE`` for the
Jenkins job. ::

    parseConfig('path/to/filename.yml')
