/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmiserverversion2;

import com.shared.objects.RMIServerInterface;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 *
 * @author digonzalez
 */
public class RMIServerVersion2 implements RMIServerInterface{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            RMIServerVersion2 remoteObject = new RMIServerVersion2();
            RMIServerInterface skeleton =  (RMIServerInterface)
                    UnicastRemoteObject.exportObject(remoteObject, 0);
            LocateRegistry.createRegistry(1099);
            Registry registry = LocateRegistry.getRegistry();
            registry.bind("RemoteObject", skeleton);
            System.out.println("Server running...");
        } catch (Exception error) {
        } 
    }

    @Override
    public byte[] EncryptFile(byte[] fileBytes, String key) throws RemoteException {
        
        byte[] keyValue = null;
        try {
            keyValue = DPUCrypter.CryptByteUsingAES(true, key, fileBytes, null);
        } catch (IllegalBlockSizeException | BadPaddingException | InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | IOException ex) {
            Logger.getLogger(RMIServerVersion2.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return keyValue;
    }

    @Override
    public byte[] DecryptFile(byte[] fileBytes, String key) throws RemoteException {
        
        byte[] keyValue = null;
        try {
            keyValue = DPUCrypter.CryptByteUsingAES(false, key, fileBytes, null);
        } catch (IllegalBlockSizeException | BadPaddingException | InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | IOException ex) {
            Logger.getLogger(RMIServerVersion2.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return keyValue;
    }

    @Override
    public String CrackFileAndFindKey(long fromIndex, long toIndex, byte[] inputFile, String checkSum) throws RemoteException {
        
        String key = null;
        try {
            key =  DPUCrypter.CrackFile(fromIndex, toIndex, inputFile, checkSum);
        } catch (IllegalBlockSizeException | BadPaddingException | InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | IOException ex) {
            Logger.getLogger(RMIServerVersion2.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return key;
    }

    @Override
    public String GetChecksum(byte[] fileBytes) throws RemoteException {
        
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(RMIServerVersion2.class.getName()).log(Level.SEVERE, null, ex);
        }
        String checkSumOfDecryptedFile = null;
        try {
            checkSumOfDecryptedFile = DPUCrypter.checksum(fileBytes, md);
        } catch (IOException ex) {
            Logger.getLogger(RMIServerVersion2.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return (checkSumOfDecryptedFile);
    }
}
