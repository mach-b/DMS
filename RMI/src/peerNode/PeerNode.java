/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package peerNode;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import rmi.RMIClient;
import rmi.RMIServer;

/**
 * Represents a peer node
 * @author markburton
 */
public class PeerNode {
    
    private boolean isLeader;
    private PeerNode leader;
    
    private InetAddress ipAddress;
    
    private List<InetAddress> peerAddressList;  
    
    private RMIClient rmiClient;
    private RMIServer rmiServer;
    
    
    
    public PeerNode() throws UnknownHostException {
        isLeader = true; // On startup is only node so is leader.
        setAsLeader(); 
        ipAddress = getIPAddress();
        peerAddressList = new ArrayList<>();
        peerAddressList.add(ipAddress);
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
    private boolean mergeLists(List listOne, List listTwo) {
        return listOne.addAll(listTwo);
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
    public static void main(String args[]) throws UnknownHostException {
        PeerNode node = new PeerNode();
        System.out.println("IP address = " + node.ipAddress.toString());
    }
}
