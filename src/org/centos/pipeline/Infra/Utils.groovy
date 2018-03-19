package org.centos.pipeline.Providers.Infra

class Utils {

    /*
    Method to generate the topology string necessary to create
    either a topology file or append to a given pin file.
     */
    def generateTopology(providerInstance){
        baseDir = "${WORKSPACE}/linchpin"

    }

    /*
    Method to execute the varsious linchpin commands inside the linchpin container
     */
    def executeInLinchpin(String command ){
        //
        // Kubernetes plugin does not let containers inherit
        // env vars from host. We force them in.
        //
        def containerEnv = env.getEnvironment().collect { key, value -> return "${key}=${value}" }

        try {
            withEnv(containerEnv){
                container('linchpin'){
                    sh """${command}"""
                }
            }
        } catch (err) {
            throw err
        } finally {
            // Do something
        }
    }
}