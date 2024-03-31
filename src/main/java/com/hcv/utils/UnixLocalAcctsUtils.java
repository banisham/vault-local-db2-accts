package com.hcv.utils;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.KeyPair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class UnixLocalAcctsUtils {
    private static final Logger logger = LoggerFactory.getLogger(UnixLocalAcctsUtils.class);

    public static Map<String, String> generateSSHKeyPair(String accountName, String hostName, String version) throws Exception{

        Map<String, String> keyPairMap = new HashMap<>();
        try {
            // Generate RSA key pair
            JSch jsch = new JSch();
            com.jcraft.jsch.KeyPair keyPair = com.jcraft.jsch.KeyPair.genKeyPair(jsch, KeyPair.RSA);

            ByteArrayOutputStream publicKeyOut = new ByteArrayOutputStream();
            ByteArrayOutputStream privateKeyOut = new ByteArrayOutputStream();

            String comment = version + "@" + accountName + "@" + hostName ;

            keyPair.writePublicKey(publicKeyOut, comment);
            keyPair.writePrivateKey(privateKeyOut);

            keyPair.dispose();

            String publicKey = Base64.getEncoder().encodeToString(publicKeyOut.toByteArray());
            byte[] decodedBytes = Base64.getDecoder().decode(publicKey.getBytes());
            publicKey = new String(decodedBytes);

            String privateKey = privateKeyOut.toString("UTF-8");

            keyPairMap.put("publicKey", publicKey);
            keyPairMap.put("privateKey", privateKey);

           logger.info("SSH key pair generated successfully.");

            saveKeysToFile(privateKey, publicKey, version);

        } catch (JSchException e) {
            logger.error("Error generating SSH key pair: JSchException : " + e.getMessage());
            throw e;
        }

        return keyPairMap;

    }


    public static void saveKeysToFile(String privateKey, String publicKey, String version) {
        String privateKeyFilePath = "/Users/bans/Documents/Learning/Labs/AWS/ec2-ssh-keys/id_rsa_" + version;
        String publicKeyFilePath = "/Users/bans/Documents/Learning/Labs/AWS/ec2-ssh-keys/id_rsa_" + version+ ".pub";
        try {
            FileWriter privateKeyWriter = new FileWriter(privateKeyFilePath);
            privateKeyWriter.write(privateKey);
            privateKeyWriter.close();

            FileWriter publicKeyWriter = new FileWriter(publicKeyFilePath);
            publicKeyWriter.write(publicKey);
            publicKeyWriter.close();

            logger.info("Private key saved to: " + privateKeyFilePath);
            logger.info("Public key saved to: " + publicKeyFilePath);

        } catch (IOException e) {
            logger.error("Error saving keys to file: {}", e.getMessage());        }
    }

    public static boolean checkFileExists(String fileName){
        // Create a File object using the relative path
        File scriptFile = new File(fileName);
        boolean flag = false;
        // Check if the file exists and is readable
        if (scriptFile.exists())  {
            logger.info("File exists  {}", fileName);
            if (scriptFile.canRead()){
                logger.info("File exists and is readable {}", fileName);
                flag = true;
            }
        } else {
            logger.error("File does not exist or is not readable {}", fileName);
        }
        return flag;
    }

    public static String getAbsolutePath(String fileName){
        String scriptAbsolutePath;
        ClassLoader classLoader = UnixLocalAcctsUtils.class.getClassLoader();
        URL scriptUrl = classLoader.getResource(fileName);
        if (scriptUrl != null) {
            File scriptFile = new File(scriptUrl.getFile());
             scriptAbsolutePath = scriptFile.getAbsolutePath();
            // Use scriptAbsolutePath as needed
        } else {
            scriptAbsolutePath = null;
            logger.error("File not found {}", fileName);
        }
        return  scriptAbsolutePath;
    }

    public static String readPrivateKeyAsString(String filePath) throws IOException {
        // Using FileReader and BufferedReader for efficiency
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            StringBuilder privateKeyBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                privateKeyBuilder.append(line).append("\n");
            }
            return privateKeyBuilder.toString().trim();
        }
    }

    public static String writePrivateKeyToTempFile(String privateKey) {
        FileWriter fileWriter = null;
        try {
            // Create a temporary file in the /tmp directory
            File tempFile = File.createTempFile("private_key", null, new File("/tmp"));

            // Set the file permissions to 600
            tempFile.setReadable(false, false);
            tempFile.setReadable(true, true);
            tempFile.setWritable(false, false);
            tempFile.setWritable(true, true);
            tempFile.setExecutable(false, false);
            tempFile.setExecutable(false, true);

            // Write the private key string to the temporary file
            fileWriter = new FileWriter(tempFile);
            fileWriter.write(privateKey);

            return tempFile.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            // Close the file writer
            if (fileWriter != null) {
                try {
                    fileWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public static String readPublicKeyFromFile(String filePath) {
        try {
            // Read the contents of the .pub file
            byte[] publicKeyBytes = Files.readAllBytes(Paths.get(filePath));
            String publicKeyContent = new String(publicKeyBytes);
            return publicKeyContent.trim();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }



    public static boolean deleteFile(String tempFilePath) {
        File tempFile = new File(tempFilePath);
        if (tempFile.exists()) {
            logger.info("File deleted succesfully : {}", tempFilePath);
            return tempFile.delete();
        } else {
            logger.error("File to delete does not exist : {}", tempFilePath);
            return false;
        }
    }

}
