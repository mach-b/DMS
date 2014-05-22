/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package peerNode;

import Multicast.BroadcastListener;
import Multicast.ElectionBroadcast;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.util.HashSet;
import java.util.Set;


/**
 * Represents a peer node
 * @author markburton
 */
public class PeerNode {
    
    private boolean isLeader;
    private PeerNode leader;
    private boolean electionRunning;
    private InetAddress ipAddress, leaderAddress;
    private Set<InetAddress> peerAddressList;  // if Leader, populate, else null??
    private int clock;
    private BroadcastListener broadcastListener;
    private ElectionBroadcast electionBroadcast;
    private DirectoryManager dirManager;
    
    public PeerNode() throws UnknownHostException, RemoteException {
        isLeader = true; // On startup is only node so is leader.
        setAsLeader(); 
        ipAddress = getIPAddress();
        leaderAddress = getIPAddress();
        peerAddressList = new HashSet<>();
        peerAddressList.add(ipAddress);
        dirManager = new DirectoryManager();
        dirManager.createDefaultDirectory();
        clock = 0;
        broadcastListener = new BroadcastListener();
        electionBroadcast = new ElectionBroadcast();
    }
    
    /**
     * Sets this PeerNode as Leader
     */
    private void setAsLeader() {
        leader = this;
    }
    
    /**
     * Merge two InetAddressLists
     * @param listOne 
     * @param listTwo
     * @return boolean success
     */
    private boolean mergeSets(Set hsOne, Set hsTwo) {
        return hsOne.addAll(hsTwo);
    }
    
    /**
     * Sets the Leader
     * @param node - The Leader PeerNode
     */
    private void setLeader(PeerNode node) {
        leader = node;
    }
    
    /**
     * Gets the IP address of this PeerNode
     * @return InetAddress
     * @throws UnknownHostException 
     */
    private InetAddress getIPAddress() throws UnknownHostException {
       return InetAddress.getLocalHost();
    }
    
    /**
     *
     * @param args
     * @throws UnknownHostException
     */
    public static void main(String[] args) throws UnknownHostException, RemoteException {
        PeerNode node = new PeerNode();
        System.out.println("IP address = " + node.ipAddress.toString());
    }
}
