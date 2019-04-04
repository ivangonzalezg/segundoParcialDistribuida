/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmiclient;

import com.shared.objects.RMIServerInterface;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 *
 * @author digonzalez
 */
public class RMIClient {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost",1099);
            RMIServerInterface stub = (RMIServerInterface)registry.lookup("RemoteObject");
            
            byte[] keyValue = "hello".getBytes();
            System.out.println("Real message: " + new String(keyValue));
            String Key = "81357";
            System.out.println("Real key: " + Key);            
            String checksum = stub.GetChecksum(keyValue);
            System.out.println("Checksum: " + checksum);
            byte[] response =  stub.EncryptFile( keyValue , Key);
            System.out.println("Encrypted message: " + new String (response));
            byte[] responsed =  stub.DecryptFile( response , Key);
            System.out.println("Decrypted message: " + new String (responsed));
            long inicio = 0;
            long fin = 199999;
            System.out.println("Cracking key...");
            String key = stub.CrackFileAndFindKey(inicio, fin, response, checksum);
            System.out.println("Cracked key: " + key);
        } catch(Exception error) {
            System.err.println("Connection error!!");
        }
    }
    
}
