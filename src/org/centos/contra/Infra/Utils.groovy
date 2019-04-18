package org.centos.contra.Infra

import groovy.text.StreamingTemplateEngine
import org.centos.contra.Infra.Providers.Aws
import org.centos.contra.Infra.Providers.Beaker
import org.centos.contra.Infra.Providers.Openstack


/**
 * Method to create a collection of instances of the AWS cloud provider type based on supplied data
 * @param aws_data
 * @return
 */
def createAwsInstances(HashMap<String,String>aws_data){

    ArrayList<Aws> aws_instances = []

    aws_data.instances.each { LinkedHashMap<String,String> instance ->

        // Set our required params to either this instances value, or the default value
        instance.ami = instance.ami ?: aws_data.ami
        instance.region = instance.region ?: aws_data.region
        instance.instance_type = instance.instance_type ?: aws_data.instance_type

        // Combine our 'global' aws tags with the tags from this instance
        def tags = []
        if (aws_data.instance_tags) { aws_data.instance_tags.each{ tags << it } }
        if (instance.instance_tags){ instance.instance_tags.each{ tags << it } }
        instance.instance_tags = tags.toSet() as ArrayList<String>

        // Combine our 'global' aws security_groups with the security_groups from this instance
        def security_groups = []
        if (aws_data.security_groups) { aws_data.security_groups.each{ security_groups << it } }
        if (instance.security_groups){ instance.security_groups.each{ security_groups << it } }
        instance.security_groups = security_groups.toSet() as ArrayList<String>

        // Set vpc_subnet_id. Since we can only have one value of this per instance, we look for a global first,
        // but if there's an instance, we overwrite it for this instance
        def vpc_subnet_id = null
        if (aws_data.vpc_subnet_id){ vpc_subnet_id = aws_data.vpc_subnet_id }
        if (instance.vpc_subnet_id){ vpc_subnet_id = instance.vpc_subnet_id }
        instance.vpc_subnet_id = vpc_subnet_id

        // Set ssh key_pair. Since we can only have one value of this per instance, we look for a global value first,
        // if there's an instance value, we overwrite it for this instance
        def key_pair = null
        if (aws_data.key_pair){ key_pair = aws_data.key_pair }
        if (instance.key_pair){ key_pair = instance.key_pair }
        instance.key_pair = key_pair


        // Set ssh user. Since we can only have one value of this per instance, we look for a global value first,
        // if there's an instance value, we overwrite it for this instance
        def user = null
        if (aws_data.user){ user = aws_data.user }
        if (instance.user){ user = instance.user }
        instance.user = user

        // Set assign_public_ip. Since we can only have one value of this per instance, we look for a global value first.
        // If there's an instance value, we overwrite the global with this for this instance.
        def assign_public_ip = false
        if (aws_data.assign_public_ip){ assign_public_ip = aws_data.assign_public_ip }
        if (instance.assign_public_ip){ assign_public_ip = instance.assign_public_ip }
        instance.assign_public_ip = assign_public_ip

        // If we have a count of instances, we will construct their name to be <name>_<count> and set security groups,
        // instance tags, etc on them. If we don't have an instance count, we perform the same actions on a single
        // instance.
        if (instance.count){
            String base_name = instance.name
            for (i in 1..instance.count){
                instance.name = "${base_name}_${i}".toString()
                Aws aws_instance = new Aws(instance.ami, instance.region, instance.name, instance.instance_type)
                if (instance.security_groups){
                    aws_instance.setSecurity_groups(instance.security_groups as ArrayList)
                }
                if (instance.instance_tags){aws_instance.instance_tags = (instance.instance_tags as ArrayList)}
                if (instance.vpc_subnet_id){aws_instance.setVpcSubnetID(instance.vpc_subnet_id.toString())}
                if (instance.key_pair){aws_instance.setKeyPair(instance.key_pair.toString())}
                if (instance.user){aws_instance.setUser(instance.user.toString())}
                if (instance.assign_public_ip){aws_instance.setAssignPublicIP(new Boolean(instance.assign_public_ip))}

                aws_instances << aws_instance
            }
        } else {
            def aws_instance = new Aws(instance.ami, instance.region, instance.name, instance.instance_type)
            if (instance.security_groups){aws_instance.setSecurity_groups(instance.security_groups as ArrayList)}
            if (instance.instance_tags){aws_instance.instance_tags = (instance.instance_tags as ArrayList)}
            if (instance.vpc_subnet_id){aws_instance.setVpcSubnetID(instance.vpc_subnet_id.toString())}
            if (instance.key_pair){aws_instance.setKeyPair(instance.key_pair.toString())}
            if (instance.user){aws_instance.setUser(instance.user.toString())}
            if (instance.assign_public_ip){aws_instance.setAssignPublicIP(new Boolean(instance.assign_public_ip))}
            aws_instances << aws_instance
        }
    }
    return aws_instances
}

/**
 * Method to create a collection of instances of the Beaker baremetal provider type based on supplied data
 * @param beaker_data
 * @return
 */
def createBeakerInstances(HashMap<String,String>beaker_data){

    ArrayList<Beaker> beaker_instances = []

    beaker_data.instances.each { LinkedHashMap<String,String> instance ->

        // Set our required params to either this instance value or the default value
        instance.arch = instance.arch ?: beaker_data.arch
        instance.distro = instance.distro ?: beaker_data.distro
        instance.variant = instance.variant ?: beaker_data.variant
        instance.job_group = instance.job_group ?: beaker_data.job_group

        // combine our 'global' beaker hostrequires with the hostrequires from this instance
        ArrayList host_requires = []
        if (beaker_data.hostrequires) { beaker_data.hostrequires.each{ entry -> host_requires << entry} }
        if (instance.hostrequires) {instance.hostrequires.each{ entry -> host_requires << entry} }
        instance.hostrequires = host_requires.toSet() as ArrayList<String>

        // combine our 'global' beaker keyvalue with the keyvalue from this instance
        ArrayList keyvalue = []
        if (beaker_data.keyvalue) { beaker_data.keyvalue.each{ entry -> keyvalue << entry} }
        if (instance.keyvalue) {instance.keyvalue.each{ entry -> keyvalue << entry} }
        instance.keyvalue = keyvalue.toSet() as ArrayList<String>

        if (instance.count) {
            String name = instance.name ?: UUID.randomUUID().toString()
            for (i in 1..instance.count) {
                // For some reason, trying to create this string using string interpolation results
                // in a JSON object instead of a String. I have no explaination for this, so I have
                // altered the String create to use the '+' operator because this works as expected.
                instance.name = name + "_${i}"
                def beaker_instance = new Beaker(instance.name, instance.distro, instance.arch, instance.variant)
                beaker_instance.hostrequires = instance.hostrequires ?: beaker_instance.hostrequires
                beaker_instance.keyvalue = instance.keyvalue ?: beaker_instance.keyvalue
                beaker_instance.job_group = instance.job_group ?: beaker_instance.job_group
                beaker_instance.bkr_data = instance.bkr_data ?: beaker_instance.bkr_data
                beaker_instance.whiteboard = instance.whiteboard ?: beaker_instance.whiteboard

                beaker_instances << beaker_instance
            }
        } else {
            def beaker_instance = new Beaker(instance.name, instance.distro, instance.arch, instance.variant)
            beaker_instance.hostrequires = instance.hostrequires ?: beaker_instance.hostrequires
            beaker_instance.keyvalue = instance.keyvalue ?: beaker_instance.keyvalue
            beaker_instance.job_group = instance.job_group ?: beaker_instance.job_group
            beaker_instance.bkr_data = instance.bkr_data ?: beaker_instance.bkr_data
            beaker_instance.whiteboard = instance.whiteboard ?: beaker_instance.whiteboard

            beaker_instances << beaker_instance
        }
    }
    return beaker_instances
}

/**
 * Method to create a collection of instances of the Openstack cloud provider type based on supplied data
 * @param openstackData
 * @return
 */
def createOpenstackInstances(HashMap<String, String>openstackData){
    ArrayList<Openstack> openstackInstances = []

    openstackData.instances.each { LinkedHashMap<String, String> instance ->
        // Set our required params to either this instances value, or the default value
        instance.name = instance.name ?: openstackData.name
        instance.flavor = instance.flavor ?: openstackData.flavor
        instance.image = instance.image ?: openstackData.image

        // Set our region. Since we can only have one region for this instance, we look for a global first,
        // but if there's an instance value, we overwrite with that
        def region = null
        if (openstackData.region){ region = openstackData.region }
        if (instance.region){ region = instance.region }
        instance.region = region

        // Set our keypair. Since we can only have one region for this instance, we look for a global first,
        // but if there's an instance value, we overwrite with that
        def keyPair = null
        if (openstackData.key_pair){ keyPair = openstackData.key_pair }
        if (instance.key_pair){ keyPair = instance.key_pair }
        instance.key_pair = keyPair

        // Set our floating IP pool. Since we can only have one region for this instance, we look for a global first,
        // but if there's an instance value, we overwrite with that
        def fipPool = null
        if (openstackData.floating_ip_pool){ fipPool = openstackData.floating_ip_pool }
        if (instance.floating_ip_pool){ fipPool = instance.floating_ip_pool }
        instance.floating_ip_pool = fipPool

        // Set our network. Since we can only have one network for this instance, we look for a global first,
        // but if there's an instance value, we overwrite with that
        def network = null
        if (openstackData.network){ network = openstackData.network }
        if (instance.network){ network = instance.network }
        instance.network = network

        // Set our user data. We combine our global user_data with the instance user_data
        def userData = []
        if (openstackData.user_data){ userData.addAll(openstackData.user_data) }
        if (instance.user_data){ userData.addAll(instance.user_data) }
        instance.user_data = userData.toSet() as ArrayList<String>

        // Set our volume size, if provided. Since we can only have one volume size for this instance, we look for a
        // global first, but if there's an instance value, we overwrite with that.
        def volumeSize = null
        if (openstackData.volume_size){ volumeSize = openstackData.volume_size }
        if (instance.volume_size){ volumeSize = instance.volume_size }
        instance.volume_size = volumeSize

        // Set our boot volume, if provided. Since we can only have one boot volume for this instance, we look for a
        // global first, but if there's an instance value, we overwrite with that.
        def bootVolume = null
        if (openstackData.boot_volume){ bootVolume = openstackData.boot_volume }
        if (instance.boot_volume){ bootVolume = instance.boot_volume }
        instance.boot_volume = bootVolume

        // Set the value for terminate volume, if provided. Since we can only have one value for this instance,
        // we look for a global first, but if there's an instance value, we overwrite with that.
        def terminateVolume = null
        if (openstackData.terminate_volume){ terminateVolume = openstackData.terminate_volume }
        if (instance.boot_volume){ terminateVolume = instance.terminate_volume }
        instance.terminate_volume = terminateVolume

        // Set the value for boot from volume, if provided. Since we can only have one value for this instance,
        // we look for a global first, but if there's an instance value, we overwrite with that.
        def bootFromVolume = null
        if (openstackData.boot_from_volume){ bootFromVolume = openstackData.boot_from_volume }
        if (instance.boot_from_volume){ bootFromVolume = instance.boot_from_volume }
        instance.boot_from_volume = bootFromVolume

        // Set the value for auto IP, if provided. Since we can only have one value for this instance,
        // we look for a global first, but if there's an instance value, we overwrite with that.
        def autoIP = null
        if (openstackData.auto_ip != null){ autoIP = openstackData.auto_ip }
        if (instance.auto_ip != null){ autoIP = instance.auto_ip }
        instance.auto_ip = autoIP

        // Combine our global security groups with the security_groups from this instance
        def securityGroups = []
        if (openstackData.security_groups) { securityGroups.addAll(openstackData.security_groups) }
        if (instance.security_groups){ securityGroups.addAll(instance.security_groups) }
        instance.security_groups = securityGroups.toSet() as ArrayList<String>

        // Combine our global volumes with the volumes from this instance
        def volumes = []
        if (openstackData.volumes) { volumes.addAll(openstackData.volumes) }
        if (instance.volumes){ volumes.addAll(instance.volumes) }
        instance.volumes = volumes.toSet() as ArrayList<String>

        // Set ssh user. Since we can only have one value of this per instance, we look for a global value first,
        // if there's an instance value, we overwrite it for this instance
        def user = null
        if (openstackData.user){ user = openstackData.user }
        if (instance.user){ user = instance.user }
        instance.user = user


        // If we have a count of instances, we will construct their name to be <name>_<count> and set security groups,
        // instance tags, etc on them. If we don't have an instance count, we perform the same actions on a single
        // instance.

        if (instance.count){
            String base_name = instance.name
            for (i in 1..instance.count){
                instance.name = "${base_name}_${i}".toString()

                Openstack openstack_instance = new Openstack(instance.name, instance.flavor, instance.image)
                if (instance.region){openstack_instance.setRegion(instance.region)}
                if (instance.key_pair){openstack_instance.setKeyPair(instance.key_pair)}
                if (instance.floating_ip_pool){openstack_instance.setFipPool(instance.floating_ip_pool)}
                if (instance.network){openstack_instance.setNetwork(instance.network)}
                if (instance.user_data){openstack_instance.setUserdata(instance.user_data)}
                if (instance.volume_size){openstack_instance.setVolumeSize(instance.volume_size)}
                if (instance.boot_volume){openstack_instance.setBootVolume(instance.boot_volume)}
                if (instance.user){openstack_instance.setUser(instance.user.toString())}
                if (instance.terminate_volume != null){openstack_instance.setTerminateVolume(instance.terminate_volume as Boolean)}
                if (instance.boot_from_volume != null){openstack_instance.setBootFromVolume(instance.boot_from_volume as Boolean)}
                if (instance.auto_ip != null){openstack_instance.setAutoIP(instance.auto_ip as Boolean)}
                if (instance.security_groups){openstack_instance.setSecurityGroups(instance.security_groups as ArrayList) }
                if (instance.volumes){openstack_instance.setVolumes(instance.volumes as ArrayList) }

                openstackInstances.add(openstack_instance)
            }
        } else {
            Openstack openstack_instance = new Openstack(instance.name, instance.flavor, instance.image)
            if (instance.region){openstack_instance.setRegion(instance.region)}
            if (instance.key_pair){openstack_instance.setKeyPair(instance.key_pair)}
            if (instance.floating_ip_pool){openstack_instance.setFipPool(instance.floating_ip_pool)}
            if (instance.network){openstack_instance.setNetwork(instance.network)}
            if (instance.user_data){openstack_instance.setUserdata(instance.user_data)}
            if (instance.volume_size){openstack_instance.setVolumeSize(instance.volume_size)}
            if (instance.boot_volume){openstack_instance.setBootVolume(instance.boot_volume)}
            if (instance.user){openstack_instance.setUser(instance.user.toString())}
            if (instance.terminate_volume != null){openstack_instance.setTerminateVolume(instance.terminate_volume as Boolean)}
            if (instance.boot_from_volume != null){openstack_instance.setBootFromVolume(instance.boot_from_volume as Boolean)}
            if (instance.auto_ip != null){openstack_instance.setAutoIP(instance.auto_ip as Boolean)}
            if (instance.security_groups){openstack_instance.setSecurityGroups(instance.security_groups as ArrayList) }
            if (instance.volumes){openstack_instance.setVolumes(instance.volumes as ArrayList) }

            openstackInstances.add(openstack_instance)
        }
    }
    return openstackInstances
}

/**
 * Method to execute linchpin commands inside of the HDSL linchpin container
 * @param command - The linchpin command to execute. Typically this should be "up" or "destroy"
 * @param options - Options to be passed to the linchpin command
 * @param verbose - whether to include -vvv to the execution of the linchpin command
 * @param containerName - name of linchpin container in executing pod. default is 'linchpin-executor'.
 * @param args    - Arguments for the linchpin command. For linchpin "up" or "destroy" this could be target.
 * @return
 */
def executeInLinchpin(String command, String options, Boolean verbose, String containerName, String args = '') {
    containerName = containerName ?: 'linchpin-executor'
    String workspace = "${WORKSPACE}/linchpin"
    if (verbose) {
        options = "--verbose ${options}"
    }

    executeInContainer(containerName, workspace, 'linchpin', options, command, args) {
        // Create our linchpin.conf file and configure it to be able to create distilled output
        writeFile(file: "${WORKSPACE}/linchpin/linchpin.conf", text: """[lp]\ndistill_data = True\n\n[evars]\ngenerate_resources = False\n""")
        writeFile(file: "${WORKSPACE}/localhost", text: "localhost ansible_connection=local")
    }

    if (command == 'up') {
        echo("Waiting 30 seconds for systems to be available")
        sleep(new Long(30))
    }
}

/**
 * Execute playbooks in the ansible container
 * @param playbookPath
 * @param paramString
 * @param verbose - whether to include -vvv to the execution of the ansible-playbook command
 * @param containerName - name of ansible container in executing pod. default is 'ansible-executor'.
 * @return
 */
def executeInAnsible(String playbookPath, String paramString, Boolean verbose, String containerName) {
    containerName = containerName ?: 'ansible-executor'
    String options = "-i \"${WORKSPACE}/linchpin/inventory\""
    if (verbose) {
        options = "-vvv ${options}"
    }

    executeInContainer(containerName, WORKSPACE, 'ansible-playbook', options, "\"${WORKSPACE}/${playbookPath}\"", paramString)
}

/**
 * Method to execute arbitary commands inside of on of the the HDSL containers
 * @param containerName - Name of container in executing pod.
 * @param workspace     - The directory in which the program will run.
 * @param executable    - The executable program to run. Should be available on the path.
 * @param options       - Options to be passed to the executable.
 * @param command       - The main command passed to the executable.
 * @param args          - Any additional arguments passed after the command.
 * @param preExecute    - A closure to run in the container context prior to running the executable.
 * @return
 */
def executeInContainer(String containerName, String workspace, String executable, String options, String command, String args, Closure preExecute = { -> }) {

    // Kubernetes plugin does not let containers inherit env vars from host. We force them in.
    def containerEnv = env.getEnvironment().collect { key, value -> return "${key}=${value}" }

    try {
        withEnv(containerEnv) {
            container(containerName) {
                dir (workspace ?: '.') {
                    if (preExecute) {
                        preExecute()
                    }
                    sh """${executable} ${options} ${command} ${args}"""
                }
            }
        }
    } catch (err) {
        throw err
    } finally {
        // Do something
    }
}

/**
 * Passthrough utility method for calling executeInContainer with named parameters (see @function executeInContainer above)
 * @param args - a Map of arguments
 * @return
 */
def executeInContainer(Map args) {
    executeInContainer(args.containerName as String,
                       args.workspace as String,
                       args.executable as String,
                       args.options as String,
                       args.command as String,
                       args.args as String,
                       args.preExecute as Closure)
}

/**
 * A method to create topologies, based on the providerInstance type.
 *
 * @param providerInstance
 * @param index
 * @param pinfile_directory
 * @return
 */
def generateTopology(providerInstance, int index, String pinfile_directory){
    dir(pinfile_directory) {

        String pinfile = "PinFile"
        String topology = null

        if (providerInstance instanceof Aws) {
            topology = createTopology(providerInstance, index)
        }

        if (providerInstance instanceof Beaker) {
            topology = createTopology(providerInstance, index)
        }

        if (providerInstance instanceof Openstack) {
            topology = createTopology(providerInstance, index)
        }

        if ( fileExists (pinfile) ){
            content = readFile file: pinfile
            writeFile file: pinfile, text: "${content}\n${topology}"
        } else {
            writeFile file: pinfile, text: topology
        }
    }
}

/**
 * A method to create a configuration file based on the instance provider type.
 * This method assumes there is a secret file configured in the Jenkins master's credentials store
 * whose name corresponds to the form specified in @param credentialFileId and will be installed
 * on @param containerName with permissions specified by @param permissions.
 * @param credentialFileId
 * @param targetPath
 * @param containerName
 * @param permissions
 * @return
 */
def createFile(String credentialFileId, String targetPath, String containerName, String permissions = '0600') {
    String targetDir = targetPath.contains('/') ? targetPath.substring(0, targetPath.lastIndexOf('/')) : null
    String ensureTargetDirExists = targetDir ? "mkdir -p \"${targetDir}\"" : ''
    container(containerName) {
        withCredentials([file(credentialsId: credentialFileId, variable: 'CREDENTIAL_FILE_NAME')]) {
            sh """
            #!/bin/bash -x
            ${ensureTargetDirExists}
            cp "${CREDENTIAL_FILE_NAME}" "${targetPath}"
            chmod ${permissions} "${targetPath}"
            """
        }
    }
}

/**
 * A method to create an SSH key for a given providerType within a specified container
 * By default, keys are stored at /ansible/keys
 * @param providerType
 * @param containerName
 * @param sshId
 * @return SSH_USERNAME
 */
String createSSHKeyFile(String providerType, String containerName, String sshFileId=null){
    String key_store_path="${WORKSPACE}/ansible/keys"
    String sshFile = sshFileId ?: "${providerType}.ssh"
    String sshUser = env.SSH_USERNAME ?: null
    String sshPassphrase = env.SSH_PASSPHRASE ?: ''
    container(containerName){
        withCredentials([sshUserPrivateKey(
                credentialsId: sshFile,
                keyFileVariable: 'SSH_FILE_NAME',
                passphraseVariable: 'SSH_PASSPHRASE',
                usernameVariable: 'SSH_USERNAME')]) {
            sh """
            #!/bin/bash -x
            mkdir -p "${key_store_path}"
            cp "${env.SSH_FILE_NAME}" "${key_store_path}/${providerType}.ssh"
            """

            sshUser = sshUser ?: env.SSH_USERNAME
            sshPassphrase = sshPassphrase ?: env.SSH_PASSPHRASE
        }
        if (sshPassphrase) {
            sh "ssh-keygen -p -f '${key_store_path}/${providerType}.ssh' -N ${sshPassphrase}"
        }
        sh "chmod 0600 '${key_store_path}/${providerType}.ssh'"
    }

    return sshUser
}

/**
 * A method to parse the distilled context file generated by Linchpin.
 * @param context_filename
 * @return
 */
def parseDistilledContext(String context_filename="linchpin.distilled") {
    try {
        return readJSON(file: "${WORKSPACE}/linchpin/resources/${context_filename}")
    } catch (e) {
        println(e)
        return null
    }
}

/**
 * A method to return the instance context.
 * @param instance
 * @param context
 * @return
 */
def getInstanceFromContext(instance, context) {
    def instanceContext = [:]
    context.each { k,v ->
        String name = instance instanceof Openstack ? instance.getNameWithUUID() : instance.name
        Map mergedContext = v.collectEntries()
        if (mergedContext.name.startsWith(name)) {
            instanceContext << mergedContext
            instanceContext.name = instance.name
            return instanceContext
        }
    }

    return instanceContext
}

/**
 * A method to generate an inventory file based on the results of linchpin deployment.
 * @param instanceList
 * @param context
 * @param inventoryFilename
 * @param keyStorePath
 * @return
 */
def generateInventory(instanceList, context, inventoryFilename="inventory", keyStorePath="ansible/keys") {
    def DOMAIN_KEYS = [
            'aws'       : 'public_ip',
            'beaker'    : 'system',
            'openstack' : 'public_v4',
    ]
    def types = [:]
    def tags = [:]

    String inventoryFile = "${WORKSPACE}/linchpin/${inventoryFilename}"
    String sshStorePath = "${WORKSPACE}/${keyStorePath}"
    String inventoryFileContent = ""

    if (fileExists(inventoryFile)){
        inventoryFileContent += readfile file: inventoryFile
    }
    inventoryFileContent+="\n"

    // for each instance in the context, list it in the inventory by its name
    instanceList.each { instance ->
        def instanceContext = getInstanceFromContext(instance, context)
        if (instanceContext) {
            inventoryFileContent+="${instance.name} " +
                    "ansible_host=${instanceContext[DOMAIN_KEYS[instance.providerType]]}"

            if (instance.keyPair) {
                inventoryFileContent+=" ansible_ssh_private_key_file=\"${sshStorePath}/${instance.providerType}.ssh\""
            }

            if (instance.user) {
                inventoryFileContent+=" ansible_user=${instance.user}"
            }

            inventoryFileContent+="\n"

            // track types or tags
            if (types.containsKey(instance.providerType)) {
                types[instance.providerType].add(instance.name)
            } else {
                types[instance.providerType] = [instance.name]
            }

            instance.instance_tags.each {
                if (tags.containsKey(it)) {
                    tags[it].add(instance.name)
                } else {
                    tags[it] = [instance.name]
                }
            }
        }
    }

    inventoryFileContent+="\n"

    // list categories by instance type
    types.each { k, v ->
        inventoryFileContent+="[${k}]\n"
        v.each {
            inventoryFileContent+="${it}\n"
        }
        inventoryFileContent+="\n"
    }

    // list categories by tag
    tags.each { k, v ->
        inventoryFileContent+="[${k}]\n"
        v.each {
            inventoryFileContent+="${it}\n"
        }
        inventoryFileContent+="\n"
    }
    writeFile file: inventoryFile, text: inventoryFileContent
}

/**
 * A method to get the template text from a resource file based on providerInstance type
 * @param providerInstance
 * @return
 */
def getTemplateText(providerInstance){
    String resourcePath = "org/centos/contra/Infra/topologyTemplates"
    if ( providerInstance instanceof Aws) {
        String templateText = libraryResource "${resourcePath}/aws.template"
        return templateText
    }

    if (providerInstance instanceof Beaker){
        String templateText = libraryResource "${resourcePath}/beaker.template"
        return templateText
    }

    if (providerInstance instanceof Openstack){
        String templateText = libraryResource "${resourcePath}/openstack.template"
        return templateText
    }

}

/**
 * A method to return the binding data for a template for the AWS provider type
 * @param providerInstance
 * @param topologyIndex
 * @return
 */
static
def getBinding(Aws providerInstance, int topologyIndex){
    return [
            index               : topologyIndex,
            providerType        : providerInstance.getProviderType(),
            name                : providerInstance.getName(),
            instanceType        : providerInstance.getInstance_type(),
            region              : providerInstance.getRegion(),
            ami                 : providerInstance.getAmi(),
            securityGroups      : providerInstance.getSecurity_groups(),
            assignPublicIp      : providerInstance.getAssignPublicIP(),
            keyPair             : providerInstance.getKeyPair(),
            vpcSubnetId         : providerInstance.getVpcSubnetID()

    ] as LinkedHashMap
}

/**
 * A method to return the binding data for a template for the Beaker provider type
 * @param providerInstance
 * @param topologyIndex
 * @return
 */
static
def getBinding(Beaker providerInstance, int topologyIndex){
    return [
        index           : topologyIndex,
        providerType    : providerInstance.getProviderType(),
        jobGroup        : providerInstance.getJob_group(),
        whiteboard      : providerInstance.getWhiteboard(),
        distro          : providerInstance.getDistro(),
        arch            : providerInstance.getArch(),
        variant         : providerInstance.getVariant(),
        bkrData         : providerInstance.getBkr_data(),
        hostrequires    : providerInstance.getHostrequires(),
        keyvalue        : providerInstance.getKeyvalue(),
        name            : providerInstance.getName()
    ] as LinkedHashMap
}

/**
 * A method to return the binding data for a template for the Openstack provider type
 * @param providerInstance
 * @param topologyIndex
 * @return
 */
static
def getBinding(Openstack providerInstance, int topologyIndex){
    return [
            index           : topologyIndex,
            providerType    : providerInstance.getProviderType(),
            name            : providerInstance.getNameWithUUID(),
            instanceType    : providerInstance.getFlavor(),
            image           : providerInstance.getImage(),
            keyPair         : providerInstance.getKeyPair(),
            fipPool         : providerInstance.getFipPool(),
            network         : providerInstance.getNetwork(),
            userData        : providerInstance.getUserdata(),
            volumeSize      : providerInstance.getVolumeSize(),
            bootVolume      : providerInstance.getBootVolume(),
            terminateVolume : providerInstance.getTerminateVolume(),
            bootFromVolume  : providerInstance.getBootFromVolume(),
            autoIP          : providerInstance.getAutoIP(),
            nics            : providerInstance.getNics(),
            security_groups : providerInstance.getSecurityGroups(),
            volumes         : providerInstance.getVolumes()
    ] as LinkedHashMap
}

/**
 * A method which returns the topology string for the AWS provider type
 * @param providerInstance
 * @param topologyIndex
 * @return
 */
def createTopology(Aws providerInstance, int topologyIndex){
    def template = new StreamingTemplateEngine().createTemplate(getTemplateText(providerInstance))
    return template.make(getBinding(providerInstance, topologyIndex))
}

/**
 * A method which returns the topology string for the Beaker provider type
 * @param providerInstance
 * @param topologyIndex
 * @return
 */
def createTopology(Beaker providerInstance, int topologyIndex){
    def template = new StreamingTemplateEngine().createTemplate(getTemplateText(providerInstance))
    return template.make(getBinding(providerInstance, topologyIndex))
}

/**
 * A method which returns the topology string for the Openstack provider type
 * @param providerInstance
 * @param topologyIndex
 * @return
 */
def createTopology(Openstack providerInstance, int topologyIndex){
    def template = new StreamingTemplateEngine().createTemplate(getTemplateText(providerInstance))
    return template.make(getBinding(providerInstance, topologyIndex))
}

/**
 * Figures out current OpenShift namespace (project name). That is the project
 * the Jenkins is running in.
 *
 * The method assumes that the Jenkins instance has kubernetes plugin installed
 * and properly configured.
 */
def getOpenshiftNamespace() {
    return openshift.withCluster() {
        def openshiftNamespace = openshift.project()
        env.openshiftNamespace = openshiftNamespace
        openshiftNamespace
    }
}

/**
 * Figures out the Docker registry URL which is supposed to host all the images
 * for current OpenShift project.
 *
 * The method assumes that all images in the current project are stored in the
 * internal Docker registry. This is not 100% bullet proof, but should be good
 * enough as starting point.
 */
def getOpenshiftDockerRegistryURL() {
    if (env.openshiftDockerRegistryUrl){
        return env.openshiftDockerRegistryUrl
    }

    return openshift.withCluster() {
        def someImageUrl = openshift.raw("get imagestream -o=jsonpath='{.items[0].status.dockerImageRepository}'").out.toString()
        String[] urlParts = someImageUrl.split('/')

        // there should be three parts in the image url:
        // <docker-registry-url>/<namespace>/<image-name:tag>
        if (urlParts.length != 3) {
            throw new IllegalStateException(
                    "Can not determine Docker registry URL!" +
                            " Unexpected image URL: $someImageUrl" +
                            " - expecting the URL in the following format:" +
                            " '<docker-registry-url>/<namespace>/<image-name:tag>'.")
        }
        def registryUrl = urlParts[0]
        // store this as an env var as well.
        env.openshfitDockerRegistryUrl = registryUrl
        registryUrl
    }
}

/**
 * A method to return the URL to use to pull an image from Docker Hub
 *
 * There are essentially two types of images on Docker Hub:
 *  "official" - these are pulled from index.docker.io and when browsing
 *               Docker Hub, their URLs look like the following example:
 *               https://hub.docker.com/_/nginx/
 *  "personal" - these are pulled from registry.hub.docker.com and when
 *               browsing Docker Hub, their URLs look like the following
 *               example:
 *               https://hub.docker.com/r/contrainfra/ansible-executor/
 *
 * @param imageName - Image name.
 * @param imageTag - Image tag.
 * @return URL for image pull.
 */
static String getDockerHubImageURL(String imageName, String imageTag){
    //Define the URL base for official and personal images.
    Map<String, String> imageURL = [
            officialImage: 'index.docker.io',
            personalImage: 'registry.hub.docker.com'
    ]

    String namespace = imageName.contains("/") ? imageName.split("/")[0] : null

    String containerName = imageName.contains("/") ? imageName.split("/")[-1] : imageName

    if ( namespace ){
        String registryURL = imageURL.personalImage
        return "${registryURL}/${namespace}/${containerName}:${imageTag}"
    } else {
        String registryURL = imageURL.officialImage
        return "${registryURL}/${containerName}:${imageTag}"

    }
}

/**
 * A method to return the URL to use to pull an image from OpenShift
 * @param imageName - Image name.
 * @param imageTag - Image tag.
 * @return - URL for image pull.
 */
def getOpenShiftImageUrl(String openshiftNamespace = null, String imageName, String imageTag){

    // Check to see if these values are available as env vars,
    // which would be the case if either method has been called previously
    String openshiftDockerRegistryURL = env.openshiftDockerRegistryUrl ?: getOpenshiftDockerRegistryURL()
    openshiftNamespace = openshiftNamespace ?: (env.openshiftNamespace ?: getOpenshiftNamespace())

    return "${openshiftDockerRegistryURL}/${openshiftNamespace}/${imageName}:${imageTag}"

}

// Leave this so that we can use pipeline dsl steps in methods here
return this
