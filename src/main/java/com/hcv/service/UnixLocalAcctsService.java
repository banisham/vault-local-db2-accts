package com.hcv.service;

import com.hcv.dto.SSHPublicKeyRequest;
import com.hcv.dto.UnixAssetOnboardingRequest;
import com.hcv.utils.UnixLocalAcctsUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

@Service
public class UnixLocalAcctsService {

    private static final Logger logger = LoggerFactory.getLogger(UnixLocalAcctsService.class);
/*

    public String rotateUnixAcctPassword(String targetServer, String targetAccount, String newPasswd) {
        String outputMessage;
        try {
            String scriptPath = UnixLocalAcctsUtils.getAbsolutePath("scripts/passwd-change.sh");
            boolean fileExist = UnixLocalAcctsUtils.checkFileExists(scriptPath);

            */
/**
             * This below needs to be replaced with reading from HCV KV path - the private key of hcvpwdmanid
             * {kv-path}/unix/{host-name}/hcvpwdmanid/private-key
              *//*

            String privateSSHKey = UnixLocalAcctsUtils.readPrivateKeyAsString("/Users/bans/Documents/Learning/Labs/AWS/ec2-ssh-keys/id_rsa_version-1");

            String tempPrivateKeyPath = UnixLocalAcctsUtils.writePrivateKeyToTempFile(privateSSHKey);

        //  Construct command to execute the expect script
            String[] command = {"/usr/bin/expect", scriptPath, targetServer, tempPrivateKeyPath, targetAccount, newPasswd};

            logger.debug("Starting execution of command : {}", command.toString());
            long startTime = System.currentTimeMillis();
            // Execute the command
            Process process = Runtime.getRuntime().exec(command);

            long endTime1 = System.currentTimeMillis();
            logger.info("Time taken for executing process: {} seconds", (endTime1-startTime)/1000);

            */
/**
             * Read the outut of the process. Only use for debugging. Comment out by default
             *//*


            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            long endTime2 = System.currentTimeMillis();
            logger.info("Time taken for reading process output: {} seconds", (endTime2-startTime)/1000);

            // Wait for the process to complete and get exit code
            int exitCode = process.waitFor();

            // Check if the process exited successfully
            if (exitCode == 0) {
              outputMessage = "Password change successful.\n" + output.toString();
*/
/*
              outputMessage =  "Password change successful with exit code: " + exitCode;
*//*

                logger.debug(outputMessage);
            } else {
              outputMessage =  "Password change failed with exit code: " + exitCode + "\n" + output.toString();
*/
/*
                outputMessage =  "Password change failed with exit code: " + exitCode;
*//*

                logger.debug(outputMessage);
            }
            boolean tempFileDeletion = UnixLocalAcctsUtils.deleteFile(tempPrivateKeyPath);
            long endTime = System.currentTimeMillis();
            long totalTime = (endTime-startTime) / 1000;
            logger.info("Total time taken for updating the password: {} seconds", totalTime);
        } catch (IOException | InterruptedException e) {
            logger.error("Error executing password change script: {}" + e.getMessage());
            return "Error executing password change script: " + e.getMessage();
        }
        return outputMessage;
    }
*/


    public String rotateUnixAcctPassword(String targetServer, String targetAccount, String newPasswd) {
        String outputMessage;
        try {
            // Construct command to execute the shell script
             String scriptPath = UnixLocalAcctsUtils.readScriptFromJar("scripts/passwd-change.sh");
     //        String scriptPath = UnixLocalAcctsUtils.getAbsolutePath("scripts/passwd-change.sh");

            String privateSSHKey = UnixLocalAcctsUtils.readPrivateKeyAsString("/Users/bans/Documents/Learning/Labs/AWS/ec2-ssh-keys/id_rsa_version-1");
            String tempPrivateKeyPath = UnixLocalAcctsUtils.writePrivateKeyToTempFile(privateSSHKey, "/tmp/id_rsa");
            logger.debug("Starting ProcessBuilder: {}, {}, {}, {}, {}", scriptPath, targetServer, tempPrivateKeyPath, targetAccount, newPasswd);

            ProcessBuilder processBuilder = new ProcessBuilder("/bin/bash", scriptPath, targetServer, tempPrivateKeyPath, targetAccount, newPasswd);

            // Start the process
            Process process = processBuilder.start();

            // Read the output of the process
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));

            StringBuilder output = new StringBuilder();
            StringBuilder errorOutput = new StringBuilder();

            String line;

            // Read standard output
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            // Read standard error
            while ((line = errorReader.readLine()) != null) {
                errorOutput.append(line).append("\n");
            }

            // Wait for the process to complete and get exit code
            int exitCode = process.waitFor();

            logger.info("Process output: {}", exitCode);
            logger.debug("Process outputMessage: {}", output.toString());
            logger.debug("Error outputMessage: {}", errorOutput.toString());


            // Check if the process exited successfully
            if (exitCode == 0) {
                outputMessage = "Password change successful with exit code: " + exitCode;
            } else {
                outputMessage = "Password change failed with exit code: " + exitCode ;
            }

            // Delete temporary private key file
            boolean tempFileDeletion = UnixLocalAcctsUtils.deleteFile(tempPrivateKeyPath);
            // Delete temporary script file extracted from jar
            boolean tempScriptFileDeletion = UnixLocalAcctsUtils.deleteFile(scriptPath);

        } catch (IOException | InterruptedException e) {
            outputMessage = "Error executing password change script: " + e.getMessage();
            e.printStackTrace();
        }
        return outputMessage;
    }


    public String configureUnixFunctionalAcct(UnixAssetOnboardingRequest onboardingRequest) {
        String publicKeypath = "/Users/bans/Documents/Learning/Labs/AWS/ec2-ssh-keys/id_rsa.pub";
        String privateKeyPath = "/Users/bans/Documents/Learning/Labs/AWS/ec2-ssh-keys/id_rsa";
        String message = null;
        Map<String, String> keyPairMap;
        String functionalAccount = onboardingRequest.getFunctionalAccount();;
        String targetHost = onboardingRequest.getTargetHost();

        try {
            /**
             * Write the logic to check whether this hostname already with private / public key exists in KV path
             * {kv-path}/unix/{host-name}/hcvpwdmanid/public-key
             * {kv-path}/unix/{host-name}/hcvpwdmanid/private-key
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
            keyPairMap = UnixLocalAcctsUtils.generateSSHKeyPair(functionalAccount, targetHost, "version-1");
            if(keyPairMap != null && keyPairMap.size() > 0){
                logger.debug("privateKey: \n" + keyPairMap.get("privateKey"));
                logger.debug("publicKey: \n" + keyPairMap.get("publicKey"));
            }
            message = "SSH Keypair generated for "+ functionalAccount + " @ " + targetHost +
                    " and onboarded to HCV successfully ";

        } catch(Exception e){
            logger.error("SSH Keypair failed for "+ functionalAccount + " @ " + targetHost + e.getMessage());
            message = "SSH Keypair failed for "+ functionalAccount + " @ " + targetHost + e.getMessage();

        }

        return message ;
    }


    public String rotateUnixFunctionalAcct(UnixAssetOnboardingRequest onboardingRequest) {
        String publicKeypath = "/Users/bans/Documents/Learning/Labs/AWS/ec2-ssh-keys/id_rsa1.pub";
        String privateKeyPath = "/Users/bans/Documents/Learning/Labs/AWS/ec2-ssh-keys/id_rsa1";
        String message = null;
        String functionalAccount = onboardingRequest.getFunctionalAccount();
        String targetHost = onboardingRequest.getTargetHost();
        Map<String, String> keyPairMap;

        try {
            /**
             * Write the logic to check whether this hostname already with private / public key exists in KV path
             * {kv-path}/unix/{host-name}/hcvpwdmanid/public-key
             * {kv-path}/unix/{host-name}/hcvpwdmanid/private-key
             * if KV path exists, proceed to next step
             * if NOT, return error
             */


            /**
             * Generate the SSH key-pair. If successful, then write the keys to existing KV paths accordingly.
             * {kv-path}/unix/{host-name}/hcvpwdmanid/public-key  - no authentication, public path.
             * {kv-path}/unix/{host-name}/hcvpwdmanid/private-key  - accessible only to onboarding service, no one else.
             * return the KV path of public key and also other attributes required.
             */
            keyPairMap = UnixLocalAcctsUtils.generateSSHKeyPair(functionalAccount, targetHost, "version-2");
            if(keyPairMap != null && keyPairMap.size() > 0){
                logger.debug("privateKey: \n" + keyPairMap.get("privateKey"));
                logger.debug("publicKey: \n" + keyPairMap.get("publicKey"));
            }

            message = "SSH Keypair rotated for "+ functionalAccount + " @ " + targetHost +
                    " and updated in HCV successfully ";


        } catch(Exception e){
            logger.error("SSH Keypair failed for "+ functionalAccount + " @ " + targetHost + e.getMessage());
            message = "SSH Keypair failed for "+ functionalAccount + " @ " + targetHost + e.getMessage();
        }

        return message;
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
            String publicSSHKey = UnixLocalAcctsUtils.readPublicKeyFromFile("/Users/bans/Documents/Learning/Labs/AWS/ec2-ssh-keys/" + publicKeyName);
            sshPublicKeyRequest.setPublicKey(publicSSHKey);
        } catch(Exception e){
            logger.error("Error executing readSSHPublicKey: {}", e.getMessage());
        }
        return sshPublicKeyRequest;
    }


    private String executeCommand(String command) {
        StringBuilder output = new StringBuilder();
        Process process;
        try {
            process = Runtime.getRuntime().exec(command);
            process.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
        } catch (IOException | InterruptedException e) {
            logger.error("Error executing command: {}", e.getMessage());
            return "Error executing command: " + e.getMessage();
        }
        return output.toString();
    }
}
