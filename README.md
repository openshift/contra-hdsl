# contra-hdsl

## Summary

This repo contains a Jenkins shared library, referred to as the contraDSL, which provides global variables representing a set of CI/CD primatives. These global variables are intended to simplify the creation of Jenkinsfiles. Additionally, the underlying methods in the shared library can be imported and used on an ad hoc basis in the creation of more complex Jenkinsfiles or in the creation of other libraries or global variables.

## Requirements

* Usage of shared libraries requires Jenkins 2.x. The contraDSL was written primarily against Jenkins 2.89.4
* Openshift 3.x. The contraDSL was written primarily against Openshift 3.6
* Some contraDSL global variables are executed inside of specific containers, inside of an Openshift pod. 
  * The s2i templates for these containers are in the ```config/s2i/ansible``` and ```config/s2i/linchpin``` directories and need to be built on your Openshift instance prior to implementing the contraDSL.

## Configuration

### OpenShift Configuration
#### Container Templates
The contraDSL makes use of two containers, ```linchpin-executor``` and ```ansible-executor```. These containers must exist within OpenShift and can be added by performing the follwoing steps:
* Checkout the contra-hdsl repo, if you haven't already: ```$ git clone https://github.com/openshift/contra-hdsl.git```
* Login to your OpenShift instance: ```$ oc login```
* Select your project namespace: ```$ oc project <your project namespace>```
* Change to the root of the contra-hdsl repo: ```$ cd /path/to/contra-hdsl```
* Add the ```linchpin-executor``` buildconfig template: ```$ oc create -f config/s2i/linchpin/linchpin-buildconfig-template.yaml```
* Add the ```ansible-executor``` buildconfig template: ```$ oc create -f config/s2i/ansible/ansible-buildconfig-template.yaml```

#### Service Account Permissions
In order to execute some tasks within the ```linchpin-executor``` and ```ansible-executor``` containers, it may be necessary to add your service account to the ```anyuid``` scc.

```$ oc adm policy add-scc-to-user anyuid -z jenkins```

See [this link](https://docs.openshift.org/latest/admin_guide/manage_scc.html#enable-dockerhub-images-that-require-root) for more details.


### Global Library Configuration
The contraDSL needs to be configured on the Jenkins master. The necessary steps are below.
* Click on ```Jenkins -> Manage Jenkins -> Configure System```
* Scroll to ```Global Pipeline Libraries``` and click ```Add```
* Provide a ```Name``` for the library (we use contraDSL, but the name is arbitrary)
* For ```Default Version```, enter ```master```
* Ensure that ```Load Implicitly``` is checked.
* Under ```Retrieval Method``` select ```Modern SCM```
* Under ```Source Code Management``` select ```GitHub```
* In the ```Credentials``` dropdown, select or add your ssh key that has access to the repository on GitHub
* Enter ```openshift``` in the ```Owner``` field
* In the ```Repository``` dropdown, select ```contra-hdsl```
* Click ```Save``` at the bottom of the page.

### Credential configuration
Certain methods, such as ```deployInfra```, ```configureInfra```, and ```executeTests``` expect credentials and SSH keys to be configured on the Jenkins master to allow for communication with provider services and provisioned resources. 

For each provider, (aws, openstack, beaker, etc.) the default behavior is that there should exist two credentials:
* \<provider>.ssh
  * A private key SSH credential. A username is not required to be configured, as the user to use when connecting is specified in the yaml configuration file.
  * Example file name: openstack.ssh
* \<provider>.creds
  * A secret file credential. This file should contain the necessary data to authenticate against the provider service.
  * Example file name: aws.creds

## Usage

### Usage in your project
The contraDSL makes use of a yaml format configuration file. This file should reside at the root of your workspace in Jenkins and can be stored in your project repository, or another repository, provided that it ultimately arrives at the root of your workspace.

The default name for this file is ```contra.yml```, but any file name can be used. See the docs for further details on using non-default file names.

## Links
* ContraDSL GitHub repository:
  * This repository contains the contraDSL shared library
  * https://github.com/openshift/contra-hdsl
* ContraDSL sample repository:
  * This repo contains sample yaml configuration files, along with Jenkinsfile examples
  * https://github.com/robnester-rh/hdsl_sample