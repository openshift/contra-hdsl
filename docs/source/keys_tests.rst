tests
=====
The top level ``tests`` key can contain a sub-key per type of test which can be utilized.

Currently a single sub-key, ``playbooks``, is available.

.. note:: A playbook can initiate any type of testing, and is merely the wrapper around starting testing code.

.. table:: Available tests sub-keys
   :widths: 10,15,75

   ============ ============================================================== ================
   Sub-Key      Purpose                                                        Available Values
   ============ ============================================================== ================
   `playbooks`_ Contains a list of entries of playbook data.                   * `location`_
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
   String    true     A path, relative to the workspace root, for a playbook for testing.
   ========= ======== =======
.. note:: The provided path should be relative to the workspace, unless you wish to :ref:`specify the base directory <execute_tests_specify_playbook_base_dir>`

vars
~~~~
.. table::
   :widths: 10,15,75

   ========= ======== =======
   Type      Required Purpose
   ========= ======== =======
   List      false    A list of ``key : value`` pairs to be passed to the Ansible playbook as variables.
   ========= ======== =======
