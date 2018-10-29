package org.centos.contra.Infra.Providers

class Openshift implements Serializable{
    String name
    String kind
    String metadataNamespace
    String replicas
    /**
    make image params hardcoded in reource files for now
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

    Openshift(String name, metadataNamespace, String kind, String replicas) {
      this.name = name;
      this.metadataNamespace = metadataNamespace;
      this.kind = kind;
      this.replicas = replicas;
    }

    String getName(){
      return name;
    }

    String getMetadataNamespace(){
      return metadataNamespace;
    }

    String getKind(){
      return kind;
    }

    String getReplicas(){
      return replicas;
    }

/**
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
*/
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
