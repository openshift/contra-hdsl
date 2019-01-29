withPod
=======
The ``withPod()`` primitive handles creating an OpenShift pod, optionally with a user-provided name, containing the
user-specified containers within it.

This step writes an environmental variable ``userPodName`` which refers to the name of the created pod. This
can be used in a ``node()`` call as seen in the `Examples`_ section.

Parameters
----------

.. table:: ``withPod`` Arguments
   :widths: 20,10,10,60

   =============== ======= ======== ==============================================
   Argument        Type    Required Purpose
   =============== ======= ======== ==============================================
   containers      List    true     | A list of entries of containers to deploy in the pod.
                                    | See the table below for container entry parameters
   pod_name        String  false    | A user provided name for the pod to use.
                                    | **Default** ``'pod-{UUID}'``
   service_account String  false    | OpenShift service account name
                                    | **Default** ``'jenkins'``
   namespace       String  false    | OpenShift project namespace
                                    | **Default** Automatically detected
   =============== ======= ======== ==============================================


.. table:: Container Entry parameters

   ============= ======= ======== ==============================================================
   Parameter     Type    Required Purpose
   ============= ======= ======== ==============================================================
   name          String  true     A user-provided name for a container to place in the pod
   image         String  true     A user-provided name for the image to use for this container
   source        String  false    | Where the image should be pulled from.
                                  | Possible values are:
                                  | * ``'openshift'``
                                  | * ``'dockerhub'``
                                  | **Default**: ``'openshift'``
   tag           String  false    Tag to use for the provided image
   command       String  false    | Command to pass to the container
                                  | **Default**: ``''``
   args          String  false    | Args to pass to the container
                                  | **Default**: ``''``
   workingDir    String  false    | The directory to mount the ``$WORKSPACE`` in, within the
                                  | container.
                                  | The ``$WORKSPACE`` variable can be used within the container
                                  | **Default**: ``'/workdir'``
   ============= ======= ======== ==============================================================



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
