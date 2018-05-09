package org.centos.contra.Infra.Providers

class Aws implements Serializable{
    String ami
    String region
    String name
    String instance_type
    String keyPair
    String user
    String vpcSubnetID
    Boolean AssignPublicIP
    String providerType = 'aws'
    ArrayList<String> security_groups = []
    ArrayList<String> instance_tags = []

    Aws(String ami, String region, String name, String instance_type){
        this.ami = ami
        this.region = region
        this.name = name
        this.instance_type = instance_type
    }

    String getAmi() {
        return ami
    }

    String getRegion() {
        return region
    }

    String getName() {
        return name
    }

    String getSecurity_groups() {
        return security_groups.join(', ')
    }

    void setSecurity_groups(ArrayList<String> security_groups) {
        this.security_groups = security_groups
    }

    String getInstance_type() {
        return instance_type
    }

    void setInstance_type(String size) {
        this.instance_type = size
    }

    ArrayList<String> getinstance_tags() {
        return instance_tags
    }

    void setinstance_tags(ArrayList<String> instance_tags) {
        this.instance_tags = instance_tags
    }

    String getProviderType() {
        return providerType
    }

    void setProviderType(String type) {
        this.providerType = type
    }

    String getKeyPair() {
        return keyPair
    }

    void setKeyPair(String keyPair) {
        this.keyPair = keyPair
    }

    String getVpcSubnetID() {
        return vpcSubnetID
    }

    void setVpcSubnetID(String vpcSubnetID) {
        this.vpcSubnetID = vpcSubnetID
    }

    Boolean getAssignPublicIP() {
        return AssignPublicIP
    }

    void setAssignPublicIP(Boolean assignPublicIP) {
        AssignPublicIP = assignPublicIP
    }

    String getUser() {
        return user
    }

    void setUser(String user) {
        this.user = user
    }
}