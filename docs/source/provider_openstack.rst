OpenStack Provider
==================
The following tables and example YAML configuration file snippets contain details needed to provision resources via the
Contra HDSL.

Additional reference can be made to the `Linchpin OpenStack provider documentation <https://linchpin.readthedocs.io/en/latest/openstack.html>`_
as well as the `Ansible OpenStack module <http://docs.ansible.com/ansible/latest/os_server_module.html>`_ , on which the
Linchpin OpenStack provider is based.

Requirements for the content of the credentials file for this provider can be seen in the Linchpin
`documentation <https://linchpin.readthedocs.io/en/latest/openstack.html#credentials-management>`_

.. |name| replace:: Names should be unique per resource type.
.. |keypair| replace:: The SSH keypair name defaults to :samp:`{provider}.ssh` unless specifically provided.


OpenStack configuration keys
----------------------------

name
~~~~
.. table::
   :widths: 10,15,75

   ====== ======== =======
   Type   Required Purpose
   ====== ======== =======
   String true     A user defined name to refer to this resource.
   ====== ======== =======

flavor
~~~~~~
.. table::
   :widths: 10,15,75

   ====== ======== =======
   Type   Required Purpose
   ====== ======== =======
   String true     The name or id of the flavor in which the new instance has to be created.
   ====== ======== =======

image
~~~~~
.. table::
   :widths: 10,15,75

   ====== ======== =======
   Type   Required Purpose
   ====== ======== =======
   String true     The name or id of the base image to boot.
   ====== ======== =======

region
~~~~~~
.. table::
   :widths: 10,15,75

   ====== ======== =======
   Type   Required Purpose
   ====== ======== =======
   String false    Name of the region.
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
                    | Defaults to 1.
   ======= ======== =======

key_pair
~~~~~~~~
.. table::
   :widths: 10,15,75

   ====== ======== =======
   Type   Required Purpose
   ====== ======== =======
   String false    The key pair name to be used when creating a instance.
   ====== ======== =======

security_groups
~~~~~~~~~~~~~~~
.. table::
   :widths: 10,15,75

   ======= ======== =======
   Type    Required Purpose
   ======= ======== =======
   List    false    Names of the security groups to which the instance should be added.
   ======= ======== =======

floating_ip_pool
~~~~~~~~~~~~~~~~
.. table::
   :widths: 10,15,75

   ======= ======== =======
   Type    Required Purpose
   ======= ======== =======
   String  false    Name of floating IP pool from which to choose a floating IP.
   ======= ======== =======

network
~~~~~~~
.. table::
   :widths: 10,15,75

   ======= ======== =======
   Type    Required Purpose
   ======= ======== =======
   String  false    Name or ID of a network to attach this instance to.
   ======= ======== =======

user_data
~~~~~~~~~
.. table::
   :widths: 10,15,75

   ======= ======== =======
   Type    Required Purpose
   ======= ======== =======
   String  false    An opaque blob of data which is made available to the instance.
   ======= ======== =======

volume_size
~~~~~~~~~~~
.. table::
   :widths: 10,15,75

   ======= ======== =======
   Type    Required Purpose
   ======= ======== =======
   String  false    The size, in GB, if booting from volume based on an image.
   ======= ======== =======

boot_volume
~~~~~~~~~~~
.. table::
   :widths: 10,15,75

   ======= ======== =======
   Type    Required Purpose
   ======= ======== =======
   String  false    | Volume name or id to use as the volume to boot from.
                    | Implies ``boot_from_volume``.
   ======= ======== =======


boot_from_volume
~~~~~~~~~~~~~~~~
.. table::
   :widths: 10,15,75

   ======= ======== =======
   Type    Required Purpose
   ======= ======== =======
   String  false    Should the instance boot from a persistent volume.
   ======= ======== =======

terminate_volume
~~~~~~~~~~~~~~~~
.. table::
   :widths: 10,15,75

   ======= ======== =======
   Type    Required Purpose
   ======= ======== =======
   String  false    If ```yes```, delete volume when deleting instance (if booted from volume).
   ======= ======== =======

auto_ip
~~~~~~~
.. table::
   :widths: 10,15,75

   ======= ======== =======
   Type    Required Purpose
   ======= ======== =======
   String  false    Ensure instance has public ip however the cloud wants to do that.
   ======= ======== =======

volumes
~~~~~~~
.. table::
   :widths: 10,15,75

   ======= ======== =======
   Type    Required Purpose
   ======= ======== =======
   List    false    A list of preexisting volumes names or ids to attach to the instance.
   ======= ======== =======
