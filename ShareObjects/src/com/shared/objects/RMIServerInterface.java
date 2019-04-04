/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shared.objects;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author digonzalez
 */
public interface RMIServerInterface extends Remote {

    public byte[] EncryptFile(byte[] fileBytes, String key) throws RemoteException;
    public byte[] DecryptFile(byte[] fileBytes, String key) throws RemoteException;
    public String GetChecksum(byte[] fileBytes) throws RemoteException;
    public String CrackFileAndFindKey(long fromIndex, long toIndex, byte[] inputFile, String checkSum) throws RemoteException;
    public String GetStudentId() throws RemoteException;   
    public String GetStudentFullName() throws RemoteException;
    
}
