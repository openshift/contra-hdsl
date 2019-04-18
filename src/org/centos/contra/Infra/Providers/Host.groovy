package org.centos.contra.Infra.Providers

class Host implements Serializable {
    final String name
    final String providerType
    String user
    String keyPair
    List<String> instance_tags = []

    Host(String name, String providerType) {
        this.name = name ?: UUID.randomUUID().toString()
        this.providerType = providerType
    }
}
