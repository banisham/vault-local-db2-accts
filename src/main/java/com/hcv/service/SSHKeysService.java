package com.hcv.service;

import com.hcv.dto.SSHKeysRequest;
import com.hcv.dto.SSHPublicKeyRequest;
import com.hcv.utils.UnixAcctsUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class SSHKeysService {

    private static final Logger logger = LoggerFactory.getLogger(SSHKeysService.class);


    public String generateSSHKeys(SSHKeysRequest sshKeysRequest) {
        String publicKeypath = "/Users/bans/Documents/Learning/Labs/AWS/ec2-ssh-keys/id_rsa.pub";
        String privateKeyPath = "/Users/bans/Documents/Learning/Labs/AWS/ec2-ssh-keys/id_rsa";
        String message = null;
        Map<String, String> keyPairMap;
        String targetHost = sshKeysRequest.getTargetHost();
        String targetAccount = sshKeysRequest.getTargetAccount();
        String sourceIPs = sshKeysRequest.getSourceIPs();
        String entityID = sshKeysRequest.getEntityId();

        try {
            /**
             * Write the logic to check whether this hostname already with private / public key exists in KV path
             * {kv-path}/unix/ssh/public-key/{targetHost}/{targetAccount}
             * {kv-path}/unix/ssh/private-key/{targetHost}/{targetAccount}
             * if KV path exists, return error and the end-point of public-key
             * if NOT, proceed to next step
             */


            /**
             * Generate the SSH key-pair. If successful, then write the keys to KV accordingly.
             * KV should be enabled for only 2 versions. Using -max-versions 2
             * {kv-path}/unix/{host-name}/hcvpwdmanid/public-key  - no authentication, public path.
             * {kv-path}/unix/{host-name}/hcvpwdmanid/private-key  - accessible only to onboarding service, no one else.
             * return the KV path of public key and also other attributes required.
             */
            keyPairMap = UnixAcctsUtils.generateSSHKeyPair(targetAccount, targetHost, "version-1");
            if(keyPairMap != null && keyPairMap.size() > 0){
                logger.debug("privateKey: \n" + keyPairMap.get("privateKey"));
                logger.debug("publicKey: \n" + keyPairMap.get("publicKey"));
            }
            message = "SSH Keypair generated for "+ targetAccount + " @ " + targetHost +
                    " and onboarded to HCV successfully ";

        } catch(Exception e){
            logger.error("SSH Keypair failed for "+ targetAccount + " @ " + targetHost + e.getMessage());
            message = "SSH Keypair failed for "+ targetAccount + " @ " + targetHost + e.getMessage();

        }

        return message ;
    }


    public String rotateSSHKeys(SSHKeysRequest sshKeysRequest) {

        String message = null;
        Map<String, String> keyPairMap;
        String targetHost = sshKeysRequest.getTargetHost();
        String targetAccount = sshKeysRequest.getTargetAccount();
        String sourceIPs = sshKeysRequest.getSourceIPs();
        String entityID = sshKeysRequest.getEntityId();

        try {
            /**
             * Write the logic to check whether this hostname already with private / public key exists in KV path
             * {kv-path}/unix/ssh/public-key/{targetHost}/{targetAccount}
             * {kv-path}/unix/ssh/private-key/{targetHost}/{targetAccount}
             * if KV path DO NOT exist, return error
             * if exists, proceed to next step
             */


            /**
             * Generate the SSH key-pair. If successful, then write the keys to KV accordingly.
             * KV should be enabled for only 2 versions. Using -max-versions 2
             * {kv-path}/unix/{host-name}/hcvpwdmanid/public-key  - no authentication, public path.
             * {kv-path}/unix/{host-name}/hcvpwdmanid/private-key  - accessible only to onboarding service, no one else.
             * return the KV path of public key and also other attributes required.
             */
            keyPairMap = UnixAcctsUtils.generateSSHKeyPair(targetAccount, targetHost, "version-2");
            if(keyPairMap != null && keyPairMap.size() > 0){
                logger.debug("privateKey: \n" + keyPairMap.get("privateKey"));
                logger.debug("publicKey: \n" + keyPairMap.get("publicKey"));
            }
            message = "SSH Keypair rotated for "+ targetAccount + " @ " + targetHost +
                    " and onboarded to HCV successfully ";

        } catch(Exception e){
            logger.error("SSH Keypair failed for "+ targetAccount + " @ " + targetHost + e.getMessage());
            message = "SSH Keypair failed for "+ targetAccount + " @ " + targetHost + e.getMessage();

        }

        return message ;
    }


    /**
     * This method is for only for testing and will not be used for actual runtime  testing as the public key
     * will be read from HCV KV path as string.
     * @param sshPublicKeyRequest
     * @return
     */
    public SSHPublicKeyRequest readSSHPublicKey(SSHPublicKeyRequest sshPublicKeyRequest) {
        try {
            String account = sshPublicKeyRequest.getAccount();
            String targetHost = sshPublicKeyRequest.getHost();
            String version = sshPublicKeyRequest.getVersion();

            String publicKeyName = "id_rsa_" + version + ".pub";
            String publicSSHKey = UnixAcctsUtils.readPublicKeyFromFile("/Users/bans/Documents/Learning/Labs/AWS/ec2-ssh-keys/" + publicKeyName);
            sshPublicKeyRequest.setPublicKey(publicSSHKey);
        } catch(Exception e){
            logger.error("Error executing readSSHPublicKey: {}", e.getMessage());
        }
        return sshPublicKeyRequest;
    }

}
