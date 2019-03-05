Getting Started
===============

Requirements
------------
* Usage of shared libraries requires Jenkins 2.x. The Contra HDSL was written primarily against Jenkins 2.89.4
* Openshift 3.x. The Contra HDSL was written primarily against Openshift 3.6
* Some Contra HDSL global variables are executed inside of specific containers, inside of an Openshift pod.

  * The s2i templates for these containers are in the config/s2i/ansible and config/s2i/linchpin directories and need to
    be built on your Openshift instance prior to implementing the Contra HDSL.

Configuration
-------------
In order to utilize the Contra HDSL shared library, there are some simple preliminary steps that must occur on both
OpenShift and Jenkins

OpenShift Configuration
~~~~~~~~~~~~~~~~~~~~~~~

.. _add-container-templates:

~~~~~~~~~~~~~~~~~~~~~~~~~~~
**Add Container Templates**
~~~~~~~~~~~~~~~~~~~~~~~~~~~
The Contra HDSL makes use of three containers, linchpin-executor, ansible-executor, and jenkins-contra-slave. These
containers must exist within OpenShift and can be added by performing the following steps:

* Checkout the Contra HDSL repo, if you haven't already:
  ``$ git clone https://github.com/openshift/Contra HDSL.git``
* Login to your OpenShift instance:
  ``$ oc login``
* Select your project namespace:
  ``$ oc project <your project namespace>``
* Change to the root of the Contra HDSL repo:
  ``$ cd /path/to/Contra HDSL``
* Add the linchpin-executor buildconfig template:
  ``$ oc create -f config/s2i/linchpin/linchpin-buildconfig-template.yaml``
* Add the ansible-executor buildconfig template: $
  ``oc create -f config/s2i/ansible/ansible-buildconfig-template.yaml``
* Add the jenkins-contra-slave buildconfig template: $
  ``oc create -f config/s2i/jslave/jslave-buildconfig-template.yaml``

~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
**Create Container Imagestreams**
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
The Contra HDSL requires that the necessary container imagestreams exist.

* Deploy the linchpin-executor imagestream:
  ``$ oc new-app linchpin-executor``
* Create the ansible-executor imagestream:
  ``$ oc new-app ansible-executor``
* Create the jenkins-contra-slave imagestream:
  ``$ oc new-app jenkins-contra-slave-builder``

~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
**Tag Container Imagestreams**
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
The Contra HDSL expects that the necessary container imagestreams should be tagged as "stable" by default, although this
 can be overridden.

* Tag the linchpin-executor imagestream:
  ``$ oc tag <project namespace>/ansible-executor:latest <project namespace>/ansible-executor:stable``
* Tag the ansible-executor imagestream:
  ``$ oc tag <project namespace>/linchpin-executor:latest <project namespace>/linchpin-executor:stable``
* Tag the jenkins-contra-slave imagestream:
  ``$ oc tag <project namespace>/jenkins-contra-slave:latest <project namespace>/jenkins-contra-slave:stable``

Jenkins Configuration
~~~~~~~~~~~~~~~~~~~~~

~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
**Global Library Configuration**
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

The Contra HDSL needs to be configured on the Jenkins master. The necessary steps are below.

* Click on ``Jenkins -> Manage Jenkins -> Configure System``
* Scroll to ``Global Pipeline Libraries`` and click ``Add``
* Provide a ``Name`` for the library (we use Contra HDSL, but the name is arbitrary)
* For ``Default Version``, enter ``master`` or a **tagged release number**
* Ensure that ``Load Implicitly`` **is** ``checked``
* Under ``Retrieval Method`` select ``Modern SCM``
* Under ``Source Code Management`` select ``Git``
* In the ``Project Repository`` field, enter ``https://github.com/openshift/contra-hdsl.git``
* Click ``Save`` at the bottom of the page.

~~~~~~~~~~~~~~~~~~~~~~~~~~~~
**Credential configuration**
~~~~~~~~~~~~~~~~~~~~~~~~~~~~

Certain methods, such as ``deployInfra()``, ``configureInfra()``, and ``executeTests()`` expect credentials and SSH keys
to be configured on the Jenkins master to allow for communication with provider services and provisioned resources.

For each provider, (aws, openstack, beaker, etc.) the default behavior is that there should exist two credentials:

* ``<provider>.ssh``

  * A private key SSH credential. A username is not required to be configured, as the user to use when connecting is
    specified in the configuration file.
  * Example file name: ``openstack.ssh``

* ``<provider>.creds``

  * A secret file credential. This file should contain the necessary data to authenticate against the provider service.
  * Example file name: ``aws.creds``


Usage
-----

The Contra HDSL makes use of a YAML format configuration file. This file should reside at the root of your workspace in
Jenkins and can be stored in your project repository, or another repository, provided that it ultimately arrives at the
root of your workspace.

The default name for this file is ``contra.yml``, but any file name can be used. See the documentation
:ref:`here <using-a-custom-configuration-file-name>` for further details on using non-default file names.