package com.hcv.dto;

public class UnixAssetOnboardingRequest {
    private String targetHost;
    private String privateSSHKey;
    private String publicSSHKey;
    private String functionalAccount;
    private String lob;
    private String requestedBy;

    public String getTargetHost() {
        return targetHost;
    }

    public void setTargetHost(String targetHost) {
        this.targetHost = targetHost;
    }

    public String getPrivateSSHKey() {
        return privateSSHKey;
    }

    public void setPrivateSSHKey(String privateSSHKey) {
        this.privateSSHKey = privateSSHKey;
    }

    public String getPublicSSHKey() {
        return publicSSHKey;
    }

    public void setPublicSSHKey(String publicSSHKey) {
        this.publicSSHKey = publicSSHKey;
    }

    public String getFunctionalAccount() {
        return functionalAccount;
    }

    public void setFunctionalAccount(String functionalAccount) {
        this.functionalAccount = functionalAccount;
    }

    public String getLob() {
        return lob;
    }

    public void setLob(String lob) {
        this.lob = lob;
    }

    public String getRequestedBy() {
        return requestedBy;
    }

    public void setRequestedBy(String requestedBy) {
        this.requestedBy = requestedBy;
    }

    @Override
    public String toString() {
        return "UnixAssetOnboardingRequest{" +
                "targetHost='" + targetHost + '\'' +
                ", privateSSHKey='" + privateSSHKey + '\'' +
                ", publicSSHKey='" + publicSSHKey + '\'' +
                ", functionalAccount='" + functionalAccount + '\'' +
                ", lob='" + lob + '\'' +
                ", requestedBy='" + requestedBy + '\'' +
                '}';
    }
}
