package org.centos.pipeline.Infra.Providers

class Beaker {
    String name
    String distro
    String arch
    String variant
    HashMap<String, String> hostrequires = [:]
    ArrayList<HashMap<String,String>> keyvalue = []
    String bkr_data = ""
    String whiteboard = ""
    String provider_type = 'beaker'


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

    HashMap<String, String> getHostrequires() {
        return hostrequires
    }

    void setHostrequires(HashMap<String, String> hostrequires) {
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
}
