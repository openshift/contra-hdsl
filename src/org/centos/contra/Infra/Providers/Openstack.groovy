package org.centos.contra.Infra.Providers

class Openstack implements Serializable{
    String name
    String flavor
    String image
    String uuid
    String region
    String keyPair
    String fipPool
    String network
    String user
    String userdata
    String volumeSize
    String bootVolume
    String providerType = 'openstack'
    Boolean terminateVolume
    Boolean bootFromVolume
    Boolean autoIP
    ArrayList<String> nics = new ArrayList<String>()
    ArrayList<String> securityGroups = new ArrayList<String>()
    ArrayList<String> volumes = new ArrayList<String>()
    ArrayList<String> instance_tags = new ArrayList<String>()



    Openstack(String name, String flavor, String image) {
        this.name = name
        this.flavor = flavor
        this.image = image
        this.uuid = UUID.randomUUID().toString().split('-')[0]
    }

    String getName() {
        return name
    }

    String getNameWithUUID() {
        return "${name}-${uuid}"
    }

    String getFlavor() {
        return flavor
    }

    String getImage() {
        return image
    }

    String getRegion() {
        return region
    }

    void setRegion(String region) {
        this.region = region
    }

    String getKeyPair() {
        return keyPair
    }

    void setKeyPair(String keyPair) {
        this.keyPair = keyPair
    }

    String getFipPool() {
        return fipPool
    }

    void setFipPool(String fipPool) {
        this.fipPool = fipPool
    }

    String getNetwork() {
        return network
    }

    void setNetwork(String network) {
        this.network = network
    }

    String getUserdata() {
        return userdata
    }

    void setUserdata(String userdata) {
        this.userdata = userdata
    }

    String getTerminateVolume() {
        return terminateVolume
    }

    void setVolumeSize(String volumeSize) {
        this.volumeSize = volumeSize
    }

    String getBootVolume() {
        return bootVolume
    }

    void setBootVolume(String bootVolume) {
        this.bootVolume = bootVolume
    }

    void setTerminateVolume(Boolean terminateVolume) {
        this.terminateVolume = terminateVolume
    }

    String getProviderType() {
        return providerType
    }

    void setProviderType(String providerType) {
        this.providerType = providerType
    }

    String getVolumeSize() {
        return volumeSize
    }

    String getUser() {
        return user
    }

    void setUser(String user) {
        this.user = user
    }

    Boolean getBootFromVolume() {
        return bootFromVolume
    }

    void setBootFromVolume(Boolean bootFromVolume) {
        this.bootFromVolume = bootFromVolume
    }

    String getAutoIP() {
        if ( autoIP != null ){
            if (autoIP) {
                return "yes"
            }

            if (!autoIP){
                return "no"
            }
        }

        return autoIP
    }

    void setAutoIP(Boolean autoIP) {
        this.autoIP = autoIP
    }

    ArrayList<String> getNics() {
        return nics
    }

    void setNics(ArrayList<String> nics) {
        this.nics = nics
    }

    String getSecurityGroups() {
        return securityGroups.join(', ')
    }

    void setSecurityGroups(ArrayList<String> securityGroups) {
        this.securityGroups = securityGroups
    }

    ArrayList<String> getVolumes() {
        return volumes
    }

    void setVolumes(ArrayList<String> volumes) {
        this.volumes = volumes
    }

    ArrayList<String> getInstance_tags() {
        return instance_tags
    }

    void setInstance_tags(ArrayList<String> instance_tags) {
        this.instance_tags = instance_tags
    }
}
