package com.hcv.utils;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.KeyPair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.PosixFilePermission;
import java.util.*;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

public class UnixAcctsUtils {
    private static final Logger logger = LoggerFactory.getLogger(UnixAcctsUtils.class);

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
        ClassLoader classLoader = UnixAcctsUtils.class.getClassLoader();
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

    public static String writePrivateKeyToTempFile(String privateKey, String filePath) {
        FileWriter fileWriter = null;
        boolean result = false;
        try {
            logger.info("File path: {}", filePath);
            logger.info("privateSSHKey: \n" + privateKey);

            // Convert the private key string to bytes
            byte[] privateKeyBytes = privateKey.getBytes();

            // Create a Path object from the file path
            Path path = Paths.get(filePath);

            // Write the bytes to the file, overwriting existing content
            Files.write(path, privateKeyBytes);

            // Set file permissions to 600
            Set<PosixFilePermission> perms = new HashSet<>();
            perms.add(PosixFilePermission.OWNER_READ);
            perms.add(PosixFilePermission.OWNER_WRITE);
            Files.setPosixFilePermissions(path, perms);
            logger.info("Private key successfully written to file: {}", path.toAbsolutePath().toString());

            // Return the absolute path of the file as a string
            return path.toAbsolutePath().toString();


        } catch (IOException e) {
            logger.error("Error writing private key to file: {}. {}", filePath, e.getMessage());

            e.printStackTrace();
            return null;
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
        boolean fileDeleted = false;
        if (tempFile.exists()) {
            fileDeleted = tempFile.delete();
            if(fileDeleted){
                logger.info("File deleted succesfully : {}", tempFilePath);
            }
        } else {
            logger.error("File to delete does not exist : {}", tempFilePath);
        }
        return fileDeleted;
    }


    public static String readScriptFromJar(String filePath) {
        String scriptPath = null;
        try{
            String jarFilePath = UnixAcctsUtils.class.getProtectionDomain().getCodeSource().getLocation().getPath();
            JarFile jarFile = new JarFile(jarFilePath);
            ZipEntry entry = jarFile.getEntry(filePath);
            if (entry != null) {
                // Load the script file from the JAR as an InputStream
                InputStream scriptStream = jarFile.getInputStream(entry);
                // Create a temporary file
                File tempFile = File.createTempFile("passwd-change", ".sh");
                // Write the contents of the InputStream to the temporary file
                Files.copy(scriptStream, tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                // Set file permissions to make the temporary file executable
                tempFile.setExecutable(true);
                // Get the absolute path of the temporary file
                scriptPath = tempFile.getAbsolutePath();
            }else{
                logger.error("File not found inside Jar file : {}", filePath);
            }
           // Print the script path for verification
            logger.info("Script path: {} ", scriptPath);

        }catch (IOException e) {
            logger.error("Error executing script: {}", e.getMessage());
        }
        return scriptPath;

    }

}
