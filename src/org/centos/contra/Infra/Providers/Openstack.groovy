package org.centos.contra.Infra.Providers

class Openstack extends Host implements Serializable{
    String flavor
    String image
    String uuid
    String region
    String keyPair
    String fipPool
    String network
    String userdata
    String volumeSize
    String bootVolume
    Boolean terminateVolume
    Boolean bootFromVolume
    Boolean autoIP
    ArrayList<String> nics = new ArrayList<String>()
    ArrayList<String> securityGroups = new ArrayList<String>()
    ArrayList<String> volumes = new ArrayList<String>()

    Openstack(String name, String flavor, String image) {
        super(name, 'openstack')
        this.flavor = flavor
        this.image = image
        this.uuid = UUID.randomUUID().toString().split('-')[0]
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

    String getVolumeSize() {
        return volumeSize
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
}
