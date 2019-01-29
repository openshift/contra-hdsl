provision
=========
The ``provision`` key can contain a sub-key per type of resource which can be provisioned.

.. table::
   :widths: 15,70,15

   ========= ============================================================== ================
   Sub-key   Purpose                                                        Available Values
   ========= ============================================================== ================
   baremetal Contains sub-keys for baremetal resource providers             * :doc:`beaker <provider_beaker>`
   cloud     Contains sub-keys for cloud resource providers                 * :doc:`aws <provider_aws>`
                                                                            * :doc:`openshift <provider_openshift>`
                                                                            * :doc:`openstack <provider_openstack>`
   ========= ============================================================== ================

baremetal
---------
Baremetal resources are, as the name suggest, physical hardware.

The following baremetal providers are currently supported:

* `Beaker <https://beaker-project.org/>`_

cloud
-----
Cloud resources are provisioned as VMs or images on a cloud provider.

The following cloud providers are currently supported:

* `AWS <https://aws.amazon.com>`_
* `OpenStack <https://www.openstack.org>`_
* `OpenShift <https://www.openshift.com>`_