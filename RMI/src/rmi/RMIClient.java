/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmi;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 *
 * @author markburton
 */
public class RMIClient {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        RMIClient client = new RMIClient();
        client.connectServer();
    }

    private void connectServer() {
        try {
            Registry reg = LocateRegistry.getRegistry("localhost", 1099);
            //Registry reg =  LocateRegistry.getRegistry("127.0.0.1", 1099);
            RMI rmi = (RMI) reg.lookup("server");
            System.out.println("Connected to server.");
            String text = rmi.getString("Mark");
            System.out.println(text);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private void connectServer(String ipAddress) {
        try {
            Registry reg = LocateRegistry.getRegistry(ipAddress, 1099);
            //Registry reg =  LocateRegistry.getRegistry("127.0.0.1", 1099);
            RMI rmi = (RMI) reg.lookup("server");
            System.out.println("Connected to server.");
            String text = rmi.getString("Mark");
            System.out.println(text);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void connectServer(String ipAddress, int port) {
        try {
            Registry reg = LocateRegistry.getRegistry(ipAddress, port);
            //Registry reg =  LocateRegistry.getRegistry("127.0.0.1", 1099);
            RMI rmi = (RMI) reg.lookup("server");
            System.out.println("Connected to server.");
            String text = rmi.getString("Mark");
            System.out.println(text);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    
}
