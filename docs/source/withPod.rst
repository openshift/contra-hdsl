withPod
=======
The ``withPod()`` primitive handles creating an OpenShift pod, optionally with a user-provided name, containing the
user-specified containers within it.

This step writes an environmental variable ``userPodName`` which refers to the name of the created pod. This
can be used in a ``node()`` call as seen in the `Examples`_ section.

Parameters
----------

.. table:: ``withPod`` Arguments
   :widths: 15,50,10,10,15

   =============== ============================================== ======= ========
   Argument        Purpose                                        Type    Required
   =============== ============================================== ======= ========
   containers      | A list of entries of containers to deploy in List    true
                   | the pod. See the table below for containe
                   | entry parameters
   pod_name        | A user provided name for the pod to use.     String  false
                   | **Default** ``'pod-{UUID}'``
   service_account | OpenShift service account name               String  false
                   | **Default** ``'jenkins'``
   namespace       | OpenShift project namespace                  String  false
                   | **Default** Automatically detected
   =============== ============================================== ======= ========


.. table:: Container entry parameters

   ============= ============================================================== ======= ========
   Parameter     Purpose                                                        Type    Required
   ============= ============================================================== ======= ========
   name          | A user-provided name for a container to place in the pod     String  true
                 |
   image         | A user-provided name for the image to use for this container String  true
                 |
   source        | Where the image should be pulled from.                       String  false
                 | Possible values are:
                 | * ``'openshift'``
                 | * ``'dockerhub'``
                 | **Default**: ``'openshift'``

   tag           Tag to use for the provided image                              String  false
   command       | Commands to pass to the container                            String  false
                 | **Default**: ``''``
   args          | Args to pass to the container                                String  false
                 | **Default**: ``''``
   workingDir    | The directory to mount the ``$WORKSPACE`` in, within the     String  false
                 | container.
                 | The ``$WORKSPACE`` variable can be used within the container
                 | **Default**: ``'/workdir'``
   ============= ============================================================== ======= ========



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
