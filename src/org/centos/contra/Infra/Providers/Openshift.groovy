package org.centos.contra.Infra.Providers

class Openshift implements Serializable {
    String name
    String metadataNamespace
    String password
    String username
    String kind
    String replicas
    /**
    make image params hardcoded in resource files for now
    String containerImage
    String containerName
    */
    String jenkinsMasterUrl
    String jenkinsValue
    String jenkinsSlaveName
    String jenkinsNodeValue
    String providerType = 'openshift'
    String runPolicy
    String type
    String secret
    String uri
    String script
    ArrayList<String> labelNames = new ArrayList<String>()

/**
 *OKD build-config values not seen in Linchpin
 * runPolicy, type, secret, uri, script
 */

    Openshift(String name, metadataNamespace, String password, String username) {
      this.name = name;
      this.metadataNamespace = metadataNamespace;
      this.password = password;
      this.username = username;
    }

    String getName() {
      return name;
    }

    String getmetadataNamespace() {
      return metadataNamespace;
    }

    String getPassword() {
      return password;
    }

    String getUsername() {
      return username;
    }

/**
    String getKind(){
      return kind;
    }

    void setKind(String kind){
      this.kind = kind;
    }

    String getReplicas(){
      return replicas;
    }

    void setReplicas(String replicas){
      this.replicas = replicas;
    }


    String getContainerImage(){
      return containerImage;
    }

    void setContainerImage(String containerImage){
      this.containerImage = containerImage;
    }

    String getContainerName(){
      return containerName;
    }

    void setContainerName(String containerName){
      this.containerName = containerName;
    }

    String getJenkinsMasterUrl(){
      return jenkinsMasterUrl;
    }

    void setJenkinsMasterUrl(String jenkinsMasterUrl){
      this.jenkinsMasterUrl = jenkinsMasterUrl;
    }

    String getJenkinsValue(){
      return jenkinsValue;
    }

    void setJenkinsValue(String jenkinsValue){
      this.jenkinsValue = jenkinsValue;
    }

    String getJenkinsSlavename(){
      return jenkinsSlavename;
    }

    void setJenkinsSlavename(String jenkinsSlavename){
      this.jenkinsSlavename = jenkinsSlavename;
    }

    String getJenkinsNodeValue(){
      return jenkinsNodeValue;
    }

    void setJenkinsNodeValue(String jenkinsNodeValue){
      this.jenkinsNodeValue = jenkinsNodeValue;
    }

    String getProviderType(){
      return providerType;
    }

    void setProviderType(String providerType){
      this.providerType = providerType;
    }

    String getRunPolicy(){
      return runPolicy;
    }

    void setRunPolicy(String runPolicy){
      this.runPolicy = runPolicy;
    }

    String getType(){
      return type;
    }

    void setType(String type){
      this.type = type;
    }

    String getSecret(){
      return secret;
    }

    void setSecret(String secret){
      this.secret = secret;
    }

    String getUri(){
      return uri;
    }

    void setUri(String uri){
      this.uri = uri;
    }

    String getScript(){
      return script;
    }

    void setScript(String script){
      this.script = script;
    }

    ArrayList<String> getLabelNames(){
      return labelNames;
      }
*/
    void setLabelNames(ArrayList<String> labels){
      this.labelNames = labelNames;
    }

}
