package rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author markburton
 */
public interface SharedFilesRMI extends Remote{
    
    public String[] getFileNames() throws RemoteException;
}
