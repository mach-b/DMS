/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package peerNode;

import Message.Message;
import Message.MessageType;
import Multicast.BroadcastListener;
import Multicast.ElectionBroadcast;
import Multicast.SendMessage;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a peer node
 *
 * @author markburton
 */
public class PeerNode {

    private boolean isLeader;
    private boolean electionRunning;
    private InetAddress ipAddress, leaderAddress;
    private Set<InetAddress> peerAddressList;  // if Leader, populate, else null??
    private int clock;
    private BroadcastListener broadcastListener;
    private ElectionBroadcast electionBroadcast;
    private DirectoryManager dirManager;

    public PeerNode() throws UnknownHostException, RemoteException {
        isLeader = true; // On startup is only node so is leader.
        //setAsLeader(); 
        ipAddress = getIPAddress();
        leaderAddress = getIPAddress();
        peerAddressList = new HashSet<>();
        peerAddressList.add(ipAddress);
        dirManager = new DirectoryManager();
        dirManager.createDefaultDirectory();
        clock = 0;
    }

    private String getIPAddressAsString() {
        String s = "";
        try {
            
        } catch (Exception e) {
        }
        return s;
    }

    private void callElection() {
        electionBroadcast = new ElectionBroadcast();
        electionBroadcast.start();
    }
    
    

    /**
     * Merge two InetAddress Sets
     *
     * @param listOne
     * @param listTwo
     * @return boolean success
     */
    private boolean mergeSets(Set hsOne, Set hsTwo) {
        return hsOne.addAll(hsTwo);
    }

    /**
     * Gets the IP address of this PeerNode
     *
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

        node.broadcastListener = new BroadcastListener() {

            @Override
            public void broadcastRecieved(String message) {
                System.out.println("Message Recieved:" + message);
            }
        };
        //node.broadcastListener.start();
        System.out.println("BroadcastListener run in main.");

        node.callElection();
        Message m = new Message(MessageType.SELF_DISCOVERY, "192.168.1.2", "192.168.1.2", 1);
        SendMessage sendMessage = new SendMessage(m);
        sendMessage.start();
    }
}
