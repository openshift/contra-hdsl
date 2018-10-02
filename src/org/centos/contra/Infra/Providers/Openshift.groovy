package org.centos.contra.Infra.Providers

class Openshift implements Serializable{
    String name
    String apiEndpoint
    String apiToken
    String apiVersion
    String kind
    String metadataName   //check if these are better left as name
    String metadataNamespace    // ""
    String replicas	//check if to be passed as int
    String selectorName
    String containerImage
    String containerName
    String jenkinsMasterUrl
    String jenkinsValue
    String jenkinsSlaveName
    String jenkinsNodeValue
    String restartPolicy
    String runAsUser
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

    Openshift(String name, apiEndpoint, String apiToken) {
      this.name = name;
      //this.apiEndpoint = apiEndpoint;   //check if api* values go out
      //this.apiToken = apiToken;
    }

    String getName(){
      return name;
    }

//check if below are needed
/*
    String getApiEndpoint(){
      return apiEndpoint;
    }

    void setApiEndpoint(String apiEndpoint){
      this.apiEndpoint = apiEndpoint;
    }

    String getApiToken(){
      return apiToken;
    }

    void setApiToken(String apiToken){
      this.apiToken = apiToken;
    }
*/

    String getApiVersion(){
      return apiToken;
    }

    void setApiVersion(String apiVersion){
      this.apiVersion = apiVersion;
    }

    String getKind(){
      return kind;
    }

    void setKind(String kind){
      this.kind = kind;
    }

    String getMetadataName(){
      return metadataName;
    }

    void setMetadataName(String setMetadataName){
      this.metadataName = metadataName;
    }

    String getMetadataNamespace(){
      return metadataNamespace;
    }

    void setMetadataNamespace(String setMetadataNamespace){
      this.metadataName = metadataName;
    }

    String getReplicas(){
      return replicas;
    }

    void setReplicas(String replicas){
      this.replicas = replicas;
    }

    String getSelectorName(){
      return selectorName;
    }

    void setSelectorname(String selectorName){
      this.selectorName = selectorName;
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

    void setJenkinsSlavename(){
      this.getJenkinsSlavename = getJenkinsSlavename;
    }

    String getJenkinsNodeValue(){
      return jenkinsNodeValue;
    }

    void setJenkinsNodeValue(String getJenkinsNodeValue){
      this.jenkinsNodeValue = jenkinsNodeValue;
    }

    String getRestartPolicy(){
      return restartPolicy;
    }

    void setRestartPolicy(String restartPolicy){
      this.restartPolicy = restartPolicy;
    }

    String getRunAsUser(){
      return runAsUser;
    }

    void setRunAsUser(String runAsUser){
      this.runAsUser = runAsUser;
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

    void setLabelNames(ArrayList<String> labels){
      this.labelNames = labelNames;
    }

}
