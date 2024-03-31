package com.hcv.controller;

import com.hcv.dto.SSHPublicKeyRequest;
import com.hcv.dto.UnixAcctPasswdRequest;
import com.hcv.dto.UnixAssetOnboardingRequest;
import com.hcv.service.UnixLocalAcctsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UnixLocalAcctsController {

    @Autowired
    private UnixLocalAcctsService unixLocalAcctsService;

    private static final Logger logger = LoggerFactory.getLogger(UnixLocalAcctsController.class);

    @PostMapping("/unix-functional-acct/onboard")
    public String onboardUnixServer(@RequestBody UnixAssetOnboardingRequest request) {
        String targetHost = request.getTargetHost();
        String targetAccount = request.getFunctionalAccount();

        logger.debug("Received onboard unix server request: {}", request);
        return unixLocalAcctsService.configureUnixFunctionalAcct(request);
    }

    @PostMapping("/unix-functional-acct/rotate")
    public String rotateUnixFunctionalAccount(@RequestBody UnixAssetOnboardingRequest request) {
        String targetHost = request.getTargetHost();
        String targetAccount = request.getFunctionalAccount();

        logger.debug("Received onboard unix server request: {}", request);
        return unixLocalAcctsService.rotateUnixFunctionalAcct(request);
    }

    @PostMapping("/unix-local-acct/rotate")
    public String rotateUnixLocalAccount(@RequestBody UnixAcctPasswdRequest request) {
        String targetHost = request.getTargetHost();
        String privSSHKey = request.getPrivSSHKey();
        String targetAccount = request.getTargetAccount();
        String newPasswd = request.getNewPasswd();

        logger.debug("Received change password request: {}", request);
        return unixLocalAcctsService.rotateUnixAcctPassword(targetHost, targetAccount, newPasswd);
    }

    @PostMapping("/ssh-publickey/read")
    public SSHPublicKeyRequest readSSHPublicKey(@RequestBody SSHPublicKeyRequest request) {

        logger.debug("Received change password request: {}", request);
        return unixLocalAcctsService.readSSHPublicKey(request);
    }
}
