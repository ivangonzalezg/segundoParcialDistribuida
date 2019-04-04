package rmiserverversion2;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.security.DigestInputStream;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
/**
 *
 * @author user
 */
public  class DPUCrypter {
    
    public static byte[] CryptByteUsingAES(boolean encrypt, String key, byte[] inputBytes,String checkSum) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IOException {
        
        if(key.length()<16){                
            key=String.format("%016d", Integer.parseInt(key));
        }
        Key secretKey = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        if (encrypt) {
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        } else {
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
        }     
        byte[] outputBytes = null;       
        if(checkSum!=null){
            try {
                outputBytes = cipher.doFinal(inputBytes);                
            } catch (Exception error) {
                return ("".getBytes());
            }
        }
        outputBytes = cipher.doFinal(inputBytes);
        
        return outputBytes;
    }
    
    public static String CrackFile(long fromIndex, long toIndex, byte[] inputBytes, String checkSum) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IOException, IOException {

        for (long currentIndex = fromIndex; currentIndex <= toIndex; currentIndex++) {   
            Date startDate=new Date();
            String numberAsString = Long.toString(currentIndex);
            System.out.println("Current key: "+numberAsString+" / Pending tries: "+(toIndex- currentIndex)+ " / cracking started at "+startDate.toString());
            byte[] internalReturn = DPUCrypter.CryptByteUsingAES(false, numberAsString, inputBytes, checkSum);
            MessageDigest md = MessageDigest.getInstance("MD5");
            if (checkSum.equals(DPUCrypter.checksum(internalReturn, md))) {
                System.out.println("Key found: " + numberAsString);
                return numberAsString;
            }
        }
        
        return "Sorry, key not found!";
    }
    
    public static String checksum(byte[] inputBytes, MessageDigest md) throws IOException {

        File filepath = File.createTempFile("tempfile", ".tmp"); 
        BufferedWriter bw = new BufferedWriter(new FileWriter(filepath));
        bw.write(new String(inputBytes));
        bw.close();
        try (DigestInputStream dis = new DigestInputStream(new FileInputStream(filepath), md)) {
            while (dis.read() != -1) ;
            md = dis.getMessageDigest();
        }
        StringBuilder result = new StringBuilder();
        for (byte b : md.digest()) {
            result.append(String.format("%02x", b));
        }
        filepath.delete();
        
        return result.toString();
    }    
}
