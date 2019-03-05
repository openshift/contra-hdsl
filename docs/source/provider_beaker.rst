Beaker Provider
===============
The following tables and example YAML configuration file snippets contain details needed to provision resources via the
Contra HDSL.

Additional reference can be made to the `Linchpin Beaker provider documentation <https://linchpin.readthedocs.io/en/latest/beaker.html>`_
as well as the `bkr_server.py module <https://raw.githubusercontent.com/CentOS-PaaS-SIG/linchpin/develop/linchpin/provision/library/bkr_server.py>`_
and the `bkr_info.py module <https://raw.githubusercontent.com/CentOS-PaaS-SIG/linchpin/develop/linchpin/provision/library/bkr_info.py>`_ on which the
Linchpin Beaker provider is based.

Linchpin supports the Kerberos and OAuth2 authentication methods see the `documentation at <https://linchpin.readthedocs.io/en/latest/beaker.html#credentials-management>`_
for more details.

.. |name| replace:: Names should be unique per resource type.
.. |keypair| replace:: The SSH keypair name defaults to :samp:`{provider}.ssh` unless specifically provided.


Beaker configuration keys
-------------------------

distro
~~~~~~
.. table::
   :widths: 10,15,75

   ====== ======== =======
   Type   Required Purpose
   ====== ======== =======
   String true     A user defined name to refer to this resource.
   ====== ======== =======

arch
~~~~
.. table::
   :widths: 10,15,75

   ====== ======== =======
   Type   Required Purpose
   ====== ======== =======
   String true     Processor architecture for the requested system.
   ====== ======== =======

variant
~~~~~~~
.. table::
   :widths: 10,15,75

   ====== ======== =======
   Type   Required Purpose
   ====== ======== =======
   String true     The operating system for the requested system.
   ====== ======== =======

job_group
~~~~~~~~~
.. table::
   :widths: 10,15,75

   ====== ======== =======
   Type   Required Purpose
   ====== ======== =======
   String false    The job group that a given machine belongs to.
   ====== ======== =======

bkr_data
~~~~~~~~
.. table::
   :widths: 10,15,75

   ====== ======== =======
   Type   Required Purpose
   ====== ======== =======
   String false    Arbitrary data passed to beaker.
   ====== ======== =======

count
~~~~~
.. table::
   :widths: 10,15,75

   ======= ======== =======
   Type    Required Purpose
   ======= ======== =======
   Integer false    | The quantity desired fo the requested resource.
                    | If the count is greater than one, resources will be named ``name_1``, etc.
                    |
                    | Defaults to 1
   ======= ======== =======

whiteboard
~~~~~~~~~~
.. table::
   :widths: 10,15,75

   ====== ======== =======
   Type   Required Purpose
   ====== ======== =======
   String false    A message to be displayed on beaker.
   ====== ======== =======

hostrequires
~~~~~~~~~~~~
.. table::
   :widths: 10,15,100,100,100

   +------+----------+-----------+--------------+----------+
   | Type | Required | Sub-field Layout Options            |
   +      +          +-----------+--------------+----------+
   |      |          | **param** | **required** | **type** |
   +======+==========+===========+==============+==========+
   | List | False    | tag       | true         | String   |
   +      +          +-----------+--------------+----------+
   |      |          | op        | false        | String   |
   +      +          +-----------+--------------+----------+
   |      |          | value     | false        | String   |
   +      +          +-----------+--------------+----------+
   |      |          | type      | false        | String   |
   +------+----------+-----------+--------------+----------+
   | Dict | False    | force     | false        | String   |
   +------+----------+-----------+--------------+----------+
   | Dict | False    | rawxml    | false        | String   |
   +------+----------+-----------+--------------+----------+

keyvalue
~~~~~~~~
.. table::
   :widths: 10,15,75

   ====== ======== =======
   Type   Required Purpose
   ====== ======== =======
   List   false
   ====== ======== =======
