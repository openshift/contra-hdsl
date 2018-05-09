# contra-hdsl

## Summary
This repo contains a Jenkins shared library, referred to as the contraDSL, which provides global variables representing a set of CI/CD primatives. These global variables are intended to simplify the creation of Jenkinsfiles. Additionally, the underlying methods in the shared library can be imported and used on an ad hoc basis in the creation of more complex Jenkinsfiles or in the creation of other libraries or global variables.

## Requirements and Usage
### Requirements
* Usage of shared libraries requires Jenkins 2.x. The contraDSL was written primarily against Jenkins 2.89.4
* Openshift 3.x. The contraDSL was written primarily against Openshift 3.6
* Some contraDSL global variables are executed inside of specific containers, inside of an Openshift pod. 
  * The s2i templates for these containers are in the ```config/s2i/ansible``` and ```config/s2i/linchpin``` directories and need to be build on your Openshift instance prior to implementing the contraDSL.
### Usage
The contraDSL needs to be configured at Jenkins master level. Configuration steps are below.
* Click on ```Jenkins``` -> ```Manage Jenkins``` -> ```Configure System```
* Scroll to ```Global Pipeline Libraries``` and click ```add```
* 


## Links
