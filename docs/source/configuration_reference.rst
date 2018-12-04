Contra YAML Reference
=====================
For many of the primitives offered by the Contra HDSL configuration information is provided, by default, via a YAML
configuration file. By default, this file is named ``contra.yaml``, although it is possible to :ref:`use an alternate file
name <Using-a-custom-configuration-file-name>` when calling :doc:`parseConfig() <parseConfig>`.

Example YAML configuration file
-------------------------------
An example of what a ``contra.yaml`` file might look like can be seen below

.. note:: By convention, all keys are lower-cased.

.. code-block:: yaml
   :linenos:

    ---
    infra:
      provision:
        cloud:
          openstack:
            network:
            - atomic-e2e-jenkins-test
            key_pair: ci-factory
            security_group:
              - default
            instances:
            - name: rnester-database
              flavor: m1.small
              image: CentOS-7-x86_64-GenericCloud-1612
              floating_ip_pool: 10.8.240.0
              user: centos
      configure:
        playbooks:
          - location: playbooks/configuration/install_vim.yml
    tests:
      playbooks:
        - location: playbooks/tests/verify_vim.yml
    results:
      outputTypes:
        artifacts:
          filesToSave:
            - 'linchpin/*'
            - 'resources/*'
            - 'Jenkinsfile'
          filesToExclude: 'inventory'
      reportTypes:
        email:
          recipientEmail:
          - john@example.org
          cc_email:
          - paul@example.org
          - george@example.org
          bcc_email:
          - ringo@example.org

Top level YAML keys
-------------------
You can learn more about the top level keys from the links below:

.. toctree::
   :maxdepth: 4

   keys_infra
   keys_tests
   keys_results