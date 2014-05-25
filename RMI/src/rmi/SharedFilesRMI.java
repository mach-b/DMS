package rmi;

import java.io.File;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * The interface for sharing files between computers on a network
 * 
 * @author Mark Burton, Kerry Powell
 * @version 1.0
 */
public interface SharedFilesRMI extends Remote{
    
    /**
     * Get a file with a given file name
     * 
     * @param fileName the name of the file
     * @return the File 
     * @throws RemoteException if the remote object is not found
     */
    public File getFile(String fileName) throws RemoteException;
    
    /**
     * Get the file names of all the remotely available files and their ip address
     * 
     * @param ip address of the process requesting the file names
     * @return the ip addresses(col=0) and remote file names(col=1)
     * @throws RemoteException if the remote object cannot be accessed
     */
    public ArrayList<String[][]> getAllFileNames(String ip) throws RemoteException;
    
    /**
     * Add file names to the leaders registry of available files on the network
     * 
     * @param ip of the process adding the file names
     * @param fileNames the names of all the files to add
     * @throws RemoteException if the remote object cannot be accessed
     */
    public void updateFileNames(String ip, String[] fileNames) throws RemoteException;
}
