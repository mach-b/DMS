package rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author markburton
 */
public interface RMI extends Remote{
    
    public String getString(String string) throws RemoteException;
}
