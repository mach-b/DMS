/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package peerNode;

import Multicast.BroadcastListener;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import rmi.LeaderRMI;

/**
 * A class that handles finding the networks leader and electing the leader, is 
 * the broadcast listener for dealing with elections
 * 
 * @author Kerry Powell
 * @version 1.0
 */
public class Leader extends BroadcastListener implements LeaderRMI{
    
    private String leaderIp = null;
    private boolean electingLeader = false;
    private ArrayList<FileNameEntry> fileRegestry = new ArrayList<>();
    
    public Leader() {
        
        registerLeaderRMI(); //Register this RMI
        
         //See who is leader
        
         //If no leader is found, start election
        
    }
    
    /**
     * Begin electing a new leader
     */
    public synchronized void startElection() {
        
        electingLeader = true;
        leaderIp = null;
        
    }
    
    /**
     * Set who the current leader is
     * 
     * @param ip of the leader or null if self is leader
     */
    public synchronized void setLeader(String ip) {
        
        leaderIp = ip;
        electingLeader = false;
    }
    
    /**
     * Get the current leader so that files can be registered for sharing, if 
     * there is an election then this returns null
     * 
     * @return The current leader or null if currently electing
     * @throws RemoteException when no connection can be made to the remote process
     * @throws NotBoundException when the remote process has not been bound
     */
    public LeaderRMI getLeader() throws RemoteException, NotBoundException {
        
        if (electingLeader) {
            return null;
        }
        
        if (leaderIp == null) {
            
            return this;
        } else {
            
            Registry reg =  LocateRegistry.getRegistry(leaderIp, 1099);
            LeaderRMI remoteFiles = (LeaderRMI)reg.lookup("Leader");
            return remoteFiles;
        }
    }
    
    /**
     * Register the object as an RMI
     */
    private void registerLeaderRMI() {
        
        Registry reg;
        try {
            reg = LocateRegistry.createRegistry(1099);
            reg.bind("Leader", this);
        } catch (Exception ex) {
            System.err.println(ex);
        } 
    }

    @Override
    public synchronized ArrayList<String[][]> getAllFileNames(String ip) throws RemoteException {
        ArrayList<String[][]> fileNames = new ArrayList<>();
        for (FileNameEntry entry: fileRegestry) {
            if (!entry.ip.equals(ip)) {
                fileNames.add(entry.array);
            }
        }
        return fileNames;
    }

    @Override
    public synchronized void updateFileNames(String ip, String[] fileNames) throws RemoteException {
        fileRegestry.add(new FileNameEntry(ip, fileNames));
    }

    @Override
    public synchronized void broadcastRecieved(String message) {
        System.out.println("Broadcast Recieved:" + message);
        if (message.equals("Election Proposed")) {
            startElection();
        }
    }
    
    /**
     * Hold the file names and ip addresses in a convenient class to be passed 
     * to other processes
     * 
     * @author Kerry Powell
     * @version 1.0
     */
    private class FileNameEntry {
        
        private String ip;
        private String[][] array;
        
        /**
         * Create an entry, this will populate the array ready for retrieval
         * 
         * @param ip of the remote process
         * @param fileNames list of file names available
         */
        private FileNameEntry(String ip, String[] fileNames) {
            this.ip = ip;
            array = new String[fileNames.length][2];
            for (int i = 0; i < fileNames.length; i++) {
                array[i][0] = ip;
                array[i][1] = fileNames[i];
            }
        }
    }
    
}
