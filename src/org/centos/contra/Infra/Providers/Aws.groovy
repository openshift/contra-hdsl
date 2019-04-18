package org.centos.contra.Infra.Providers

class Aws extends Host implements Serializable {
    String ami
    String region
    String instance_type
    String vpcSubnetID
    Boolean AssignPublicIP
    ArrayList<String> security_groups = []

    Aws(String ami, String region, String name, String instance_type){
        super(name, 'aws')
        this.ami = ami
        this.region = region
        this.instance_type = instance_type
    }

    String getAmi() {
        return ami
    }

    String getRegion() {
        return region
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
}
