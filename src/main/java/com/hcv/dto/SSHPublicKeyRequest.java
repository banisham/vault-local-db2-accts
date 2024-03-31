package com.hcv.dto;

public class SSHPublicKeyRequest {

    private String host;
    private String publicKey;
    private String account;
    private String version;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "SSHPublicKeyRequest{" +
                "host='" + host + '\'' +
                ", publicKey='" + publicKey + '\'' +
                ", account='" + account + '\'' +
                ", version='" + version + '\'' +
                '}';
    }
}
