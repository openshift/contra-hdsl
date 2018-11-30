configure
=========
The ``configure`` key can contain a sub-key per type of configuration which can occur.

Currently a single sub-key, ``playbooks``, is available.

.. table::
   :widths: 15,70,15

   ============ ============================================================== ================
   Sub-key      Purpose                                                        Available Values
   ============ ============================================================== ================
   `playbooks`_ Contains sub-keys for baremetal resource providers             * `location`_
                                                                               * `vars`_
   ============ ============================================================== ================

playbooks
---------
.. table::
   :widths: 10,15,75

   ========= ======== =======
   Type      Required Purpose
   ========= ======== =======
   List      True     Contains a list of entries of playbook data.
   ========= ======== =======

location
~~~~~~~~
.. table::
   :widths: 10,15,75

   ========= ======== =======
   Type      Required Purpose
   ========= ======== =======
   String    true     A path, relative to the workspace root, for a playbook for configuration.
   ========= ======== =======
.. note:: The provided path should be relative to the workspace, unless you wish to :ref:`specify the base directory <configure_infra_specify_playbook_base_dir>`

vars
~~~~
.. table::
   :widths: 10,15,75

   ========= ======== =======
   Type      Required Purpose
   ========= ======== =======
   List      false    A list of ``key : value`` pairs to be passed to the Ansible playbook as variables.
   ========= ======== =======
