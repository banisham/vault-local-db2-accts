package com.hcv.dto;

public class SSHKeysRequest {
    private String targetHost;
    private String targetAccount;
    private String sourceIPs;
    private String privateSSHKey;
    private String publicSSHKey;
    private String entityId;
    private String entityInstanceId;
    private String lob;
    private String requestedBy;

    public String getTargetAccount() {
        return targetAccount;
    }

    public void setTargetAccount(String targetAccount) {
        this.targetAccount = targetAccount;
    }

    public String getSourceIPs() {
        return sourceIPs;
    }

    public void setSourceIPs(String sourceIPs) {
        this.sourceIPs = sourceIPs;
    }

    public String getEntityId() {
        return entityId;
    }

    public String getEntityInstanceId() {
        return entityInstanceId;
    }

    public void setEntityInstanceId(String entityInstanceId) {
        this.entityInstanceId = entityInstanceId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }


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
        return "SSHKeysRequest{" +
                "targetHost='" + targetHost + '\'' +
                ", targetAccount='" + targetAccount + '\'' +
                ", sourceIPs='" + sourceIPs + '\'' +
                ", privateSSHKey='" + privateSSHKey + '\'' +
                ", publicSSHKey='" + publicSSHKey + '\'' +
                ", entityId='" + entityId + '\'' +
                ", entityInstanceId='" + entityInstanceId + '\'' +
                ", lob='" + lob + '\'' +
                ", requestedBy='" + requestedBy + '\'' +
                '}';
    }

}
