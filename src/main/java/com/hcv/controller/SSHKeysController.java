package com.hcv.controller;

import com.hcv.dto.SSHKeysRequest;
import com.hcv.dto.SSHPublicKeyRequest;
import com.hcv.service.SSHKeysService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SSHKeysController {

    @Autowired
    private SSHKeysService sshKeysService;

    private static final Logger logger = LoggerFactory.getLogger(SSHKeysController.class);

    @PostMapping("/ssh/keys/onboard")
    public String onboardSSHKeys(@RequestBody SSHKeysRequest request) {
        logger.debug("Received onboard SSH keys request: {}", request);
        return sshKeysService.generateSSHKeys(request);
    }

    @PostMapping("/ssh/keys/rotate")
    public String rotateSSHKeys(@RequestBody SSHKeysRequest request) {
        logger.debug("Received rotate SSH keys request: {}", request);
        return sshKeysService.rotateSSHKeys(request);
    }


    @PostMapping("/ssh-publickey/read")
    public SSHPublicKeyRequest readSSHPublicKey(@RequestBody SSHPublicKeyRequest request) {

        logger.debug("Received change password request: {}", request);
        return sshKeysService.readSSHPublicKey(request);
    }
}
