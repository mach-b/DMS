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
 * Represents a peer node that reacts to the broadcasts sent throughout the 
 * network
 *
 * @author Mark Burton, Kerry Powell
 * @version 1.0
 */
public class PeerNode extends BroadcastListener {
    
    private final Leader leader;

    private final ElectionBroadcast electionBroadcast;

    public PeerNode(Leader leader) throws UnknownHostException, RemoteException {
        this.leader = leader;
        electionBroadcast = new ElectionBroadcast(new Leader());
        leader.findLeader();
    }

    @Override
    public synchronized void broadcastRecieved(Message message) {
        System.out.println("Message Object Properties: " +
                message.getMessageType().toString()+ " " + 
                message.getSenderIPAddress());
        try {

            switch (message.getMessageType()) {
                case ELECTION:
                    startElection();
                    break;
                case ELECT:
                    electionBroadcast.addElection(message);
                    break;
                case DECLARE_LEADER:
                    electionBroadcast.leaderChosen(message);
                    break;
                case PEER_LOST:
                    //Tell leader to remove ip address from files
                    break;
                case FIND_LEADER:
                    //Broadcast request for who is the leader
                    break;
            }
            
        } catch (UnknownHostException ex) {
            Logger.getLogger(PeerNode.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Begin an election
     * 
     * @throws UnknownHostException 
     */
    private synchronized void startElection() throws UnknownHostException {
        
        electionBroadcast.voteSelf();
        leader.electionStarted();
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
     * @throws java.rmi.RemoteException
     */
    public static void main(String[] args) throws UnknownHostException, RemoteException {
        
        PeerNode node = new PeerNode(new Leader());
        //node.broadcastListener.start();
        System.out.println("BroadcastListener run in main.");
        //node.callElection();
        Message m = new Message(MessageType.DECLARE_LEADER, "192.168.1.2", "192.168.1.2", 1, "");
        SendMessage sendMessage = new SendMessage(m);
        sendMessage.start();
    }
}