AWS Provider
============
The following tables and example YAML configuration file snippets contain details needed to provision resources via the
Contra HDSL.

Additional reference can be made to the `Linchpin AWS provider documentation <https://linchpin.readthedocs.io/en/latest/aws.html>`_
as well as the `Ansible ec2 module <https://docs.ansible.com/ansible/devel/modules/ec2_module.html>`_ , on which the
Linchpin AWS provider is based.

Requirements for the content of the credentials file for this provider can be seen in the Linchpin
`documentation <https://linchpin.readthedocs.io/en/latest/aws.html#credentials-file>`_

.. |name| replace:: Names should be unique per resource type.
.. |keypair| replace:: The SSH keypair name defaults to :samp:`{provider}.ssh` unless specifically provided.


AWS configuration keys
----------------------

ami
~~~
.. table::
   :widths: 10,15,75

   ====== ======== =======
   Type   Required Purpose
   ====== ======== =======
   String true     Defines the AMI of the resource to deploy.
   ====== ======== =======

region
~~~~~~
.. table::
   :widths: 10,15,75

   ====== ======== =======
   Type   Required Purpose
   ====== ======== =======
   String false    Defines the region to deploy the resource.
   ====== ======== =======

name
~~~~
.. table::
   :widths: 10,15,75

   ====== ======== =======
   Type   Required Purpose
   ====== ======== =======
   String true     A user defined name to refer to this resource.
   ====== ======== =======

.. note:: |name|

instance_type
~~~~~~~~~~~~~
.. table::
   :widths: 10,15,75

   ====== ======== =======
   Type   Required Purpose
   ====== ======== =======
   String true     The instance type for the resource.
   ====== ======== =======

key_pair
~~~~~~~~
.. table::
   :widths: 10,15,75

   ====== ======== =======
   Type   Required Purpose
   ====== ======== =======
   String false    The name of the keypair to use when provisioning resources.
   ====== ======== =======

.. note:: |keypair|

vpc_subnet_id
~~~~~~~~~~~~~
.. table::
   :widths: 10,15,75

   ====== ======== =======
   Type   Required Purpose
   ====== ======== =======
   String false    The subnet ID in which to launch the instance (VPC).
   ====== ======== =======

user
~~~~
.. table::
   :widths: 10,15,75

   ====== ======== =======
   Type   Required Purpose
   ====== ======== =======
   String true     The username to use when connecting to this instance via SSH.
   ====== ======== =======

assign_public_ip
~~~~~~~~~~~~~~~~
.. table::
   :widths: 10,15,75

   ======= ======== =======
   Type    Required Purpose
   ======= ======== =======
   Boolean false    When provisioning within vpc, assign a public IP address.
   ======= ======== =======

count
~~~~~
.. table::
   :widths: 10,15,75

   ======= ======== =======
   Type    Required Purpose
   ======= ======== =======
   Integer false    The number of this specific instance to launch.
   ======= ======== =======

instance_tags
~~~~~~~~~~~~~
.. table::
   :widths: 10,15,75

   ======= ======== =======
   Type    Required Purpose
   ======= ======== =======
   List    false    A list of hash / dictionaries of ``key: value`` tag names and values.
   ======= ======== =======

security_groups
~~~~~~~~~~~~~~~
.. table::
   :widths: 10,15,75

   ======= ======== =======
   Type    Required Purpose
   ======= ======== =======
   List    false    Security group(s) to use with the instance.
   ======= ======== =======
