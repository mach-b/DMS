/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rmiserver;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import rmi.RMI;

/**
 *
 * @author markburton
 */
public class RMIServer extends UnicastRemoteObject implements RMI {

    public RMIServer() throws RemoteException {
        super();
    }
    
    @Override
    public String getString(String string) throws RemoteException {
        string = "Hi "+string;
        return string;
    }

    /**
     *
     * @param args
     */
    public static void main(String args[]) {
        try {
            Registry reg = LocateRegistry.createRegistry(1099);
            reg.bind("server", new RMIServer());
            System.out.println("Server has started.");
        }catch (Exception e){
            System.out.println(e);
        }
    }

    
    
}
