package org.centos.contra.Infra.Providers

class Beaker implements Serializable{
    String name
    String distro
    String arch
    String variant
    ArrayList<HashMap<String,String>> hostrequires = []
    ArrayList<HashMap<String,String>> keyvalue = []
    String job_group = ""
    String bkr_data = ""
    String whiteboard = ""
    String providerType = 'beaker'


    Beaker(String name, String distro, String arch, String variant) {
        this.name = name
        this.distro = distro
        this.arch = arch
        this.variant = variant
    }

    String getName() {
        return name
    }

    String getDistro() {
        return distro
    }

    String getArch() {
        return arch
    }

    String getVariant() {
        return variant
    }

    ArrayList<HashMap<String,String>> getHostrequires() {
        return hostrequires
    }

    void setHostrequires(ArrayList<HashMap> hostrequires) {
        this.hostrequires = hostrequires
    }

    ArrayList<HashMap<String, String>> getKeyvalue() {
        return keyvalue
    }

    void setKeyvalue(ArrayList<HashMap<String, String>> keyvalue) {
        this.keyvalue = keyvalue
    }

    String getBkr_data() {
        return bkr_data
    }

    void setBkr_data(String bkr_data) {
        this.bkr_data = bkr_data
    }

    String getWhiteboard() {
        return whiteboard
    }

    void setWhiteboard(String whiteboard) {
        this.whiteboard = whiteboard
    }

    String getJob_group() {
        return job_group
    }

    void setJob_group(String job_group) {
        this.job_group = job_group
    }
}
