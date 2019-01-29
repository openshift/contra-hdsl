infra
=====
The top level ``infra`` key contains sub-keys which pertain to the deployment and configuration of infrastructure resources.

.. table:: Infra Sub-keys

   ================================= ============================================================== ============
   Sub-key                            Purpose                                                        Sub-Keys
   ================================= ============================================================== ============
   :doc:`provision <keys_provision>` contains sub-keys pertaining to the deployment of resources    * cloud
                                                                                                    * baremetal
   :doc:`configure <keys_configure>` contains sub-keys pertaining to the configuration of resources * playbooks
   ================================= ============================================================== ============

.. toctree::
   :maxdepth: 1

   keys_provision
   keys_configure