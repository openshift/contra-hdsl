package org.centos.pipeline.Infra.Providers

class Aws {
    String ami
    String region
    String name
    String instance_type
    String provider_type = 'aws'
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

    void setAmi(String ami) {
        this.ami = ami
    }

    String getRegion() {
        return region
    }

    void setRegion(String region) {
        this.region = region
    }

    String getName() {
        return name
    }

    void setName(String name) {
        this.name = name
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

    String getProvider_type() {
        return provider_type
    }

    void setProvider_type(String type) {
        this.provider_type = type
    }
}