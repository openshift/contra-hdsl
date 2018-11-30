withPod
=======
The ``withPod()`` primitive handles creating an OpenShift pod, optionally with a user-provided name, containing the
user-specified containers within it.

This step writes an environmental variable ``userPodName`` which refers to the name of the created pod. This
can be used in a ``node()`` call as seen in the `Examples`_ section.

Parameters
----------

.. table:: ``withPod`` Arguments

   ========================= ============================================== ======= ======== =====================
   Arguments                 Purpose                                        Type    Required Default
   ========================= ============================================== ======= ======== =====================
   podName                   A user provided name for the pod to use.       String  false    ``pod-{UUID}``
   containers                | A list of entries of containers to deploy in List    true
                             | the pod. See the table below for container
                             | entry parameters.
   openshift_service_account OpenShift service account name                 String  false    ``jenkins``
   ========================= ============================================== ======= ======== =====================



.. table:: Container entry parameters

   ============= ============================================ ======= ======== ==============
   Parameters    Purpose                                      Type    Required Default
   ============= ============================================ ======= ======== ==============
   containerName | A user-provided name for a container to    String  true
                 | place in the pod
   image         | A user-provided name for the image to      String  true
                 | use for this container
   tag           Tag to use for the provided image            String  false    ``stable``
   command       Commands to pass to the container            String  false    ``''``
   args          Args to pass to the container                String  false    ``''``
   workingDir    | The directory to mount the ``$WORKSPACE``  String  false    ``'/workdir'``
                 | in, with the container. The ``$WORKSPACE``
                 | variable can be used within the container
   ============= ============================================ ======= ======== ==============

Requirements
------------
Please ensure that the appropriate images have been created in OpenShift before calling this method with a container name.

Examples
--------

Default usage
~~~~~~~~~~~~~
In the basic usage, no podName is required. The pod will be created with a name in the form of ``pod-{random UUID}``.
The podName can be referenced with ``env.userPodName`` ::


    withPod containers: [
        [containerName: 'bottlenose', image: 'tursiops_v1.2'],
        [containerName: 'spotted', image: 'stenella_v7.4'],
    ],
    {
        node(env.userPodName){
            stage('Stage One'){
              echo "Hello World"
            }
        }
    }


Advanced usage
~~~~~~~~~~~~~~
With advanced usage, multiple optional arguments can be provided. ::

    withPod podName: 'cetacea',
        containers: [
            [containerName: 'bottlenose_1', image: 'tursiops_v1.2'],
            [containerName: 'bottlenose_2', image: 'tursiops_v1.2', tag: 'latest'],
            [containerName: 'spotted_1', image: 'stenella_v7.4', tag: 'latest', args: 'someArgs'],
        ],
        {
        node(env.userPodName){
            stage('Stage One'){
              echo "Hello World"
            }
        }
    }
