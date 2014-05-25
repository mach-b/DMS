package peerNode;

import Message.Message;
import Message.MessageType;
import Multicast.BroadcastListener;
import Multicast.ElectionBroadcast;
import Multicast.SendMessage;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Represents a peer node
 *
 * @author markburton
 */
public class PeerNode extends BroadcastListener {

    private boolean isLeader;
    private boolean electionRunning;
    private InetAddress ipAddress, leaderAddress;
    private String inetAddressString;
    private int clock;
    private final ElectionBroadcast electionBroadcast;
    private boolean electingLeader;
    private Object leaderIp;

    public PeerNode() throws UnknownHostException, RemoteException {
        ipAddress = getIPAddress();
        setInetAddressString();
        clock = 0;
        System.out.println("IP address :: "+inetAddressString);
        electionBroadcast = new ElectionBroadcast(new Leader());
    }

    @Override
    public synchronized void broadcastRecieved(Message message) {
        System.out.println("Message Object Properties: " +
                message.getMessageType().toString()+ " " + 
                message.getSenderIPAddress());
        try {
        if (message.getMessageType() == MessageType.ELECTION) {
            startElection();
        }
        if (message.getMessageType() == MessageType.ELECT) {
            electionBroadcast.addElection(message);
        }
        if (message.getMessageType() == MessageType.DECLARE_LEADER) {
            electionBroadcast.leaderChosen(message);
        }
        if (message.getMessageType() == MessageType.PEER_LOST) {
            //Tell leader to remove ip address from files
        }
        if (message.getMessageType() == MessageType.FIND_LEADER) {
            //Broadcast request for who is the leader
        }
        } catch (UnknownHostException ex) {
            Logger.getLogger(PeerNode.class.getName()).log(Level.SEVERE, null, ex);
        }
        //ELECTION("Election message"), ELECT("Elect"), DECLARE_LEADER("Declare leader message"), PEER_LOST("Peer host has been dropped"), FIND_LEADER("Find the leader on the network");
    }
    
    public synchronized void startElection() throws UnknownHostException {
        
        electingLeader = true;
        leaderIp = null;
        electionBroadcast.voteSelf();
    }

    private void callElection() {
//        electionBroadcast = new ElectionBroadcast();
//        electionBroadcast.start();
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

    private void setInetAddressString() throws UnknownHostException {
        String temp = ""+InetAddress.getLocalHost();
        inetAddressString = "";
        for(int i = 0; i < temp.length(); i++) {
            if(temp.charAt(i) == '/') {
                inetAddressString = "/";
            }else {
                inetAddressString += temp.charAt(i);
            }
        }
    }
    
    /**
     *
     * @param args
     * @throws UnknownHostException
     */
    public static void main(String[] args) throws UnknownHostException, RemoteException {
        PeerNode node = new PeerNode();
        System.out.println("IP address = " + node.ipAddress.toString());
        
        //node.broadcastListener.start();
        System.out.println("BroadcastListener run in main.");

        //node.callElection();
        Message m = new Message(MessageType.DECLARE_LEADER, "192.168.1.2", "192.168.1.2", 1, "");
        SendMessage sendMessage = new SendMessage(m);
        sendMessage.start();
    }
}


