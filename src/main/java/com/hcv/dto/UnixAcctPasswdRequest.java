package com.hcv.dto;

public class UnixAcctPasswdRequest {

    private String targetHost;
    private String privSSHKey;
    private String targetAccount;
    private String newPasswd;

    public String getTargetHost() {
        return targetHost;
    }

    public void setTargetHost(String targetHost) {
        this.targetHost = targetHost;
    }

    public String getPrivSSHKey() {
        return privSSHKey;
    }

    public void setPrivSSHKey(String privSSHKey) {
        this.privSSHKey = privSSHKey;
    }

    public String getTargetAccount() {
        return targetAccount;
    }

    public void setTargetAccount(String targetAccount) {
        this.targetAccount = targetAccount;
    }

    public String getNewPasswd() {
        return newPasswd;
    }

    public void setNewPasswd(String newPasswd) {
        this.newPasswd = newPasswd;
    }

    @Override
    public String toString() {
        return "UnixAcctPasswdRequest{" +
                "targetHost='" + targetHost + '\'' +
                ", privSSHKey='" + privSSHKey + '\'' +
                ", targetAccount='" + targetAccount + '\'' +
                ", newPasswd='" + newPasswd + '\'' +
                '}';
    }
}
