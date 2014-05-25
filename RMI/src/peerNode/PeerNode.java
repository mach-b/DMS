package peerNode;

import Message.Message;
import Multicast.BroadcastListener;
import Multicast.ElectionBroadcast;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import share.SharedFiles;

/**
 * Represents a peer node that reacts to the broadcasts sent throughout the 
 * network
 *
 * @author Mark Burton, Kerry Powell
 * @version 1.0
 */
public class PeerNode extends BroadcastListener {
    
    private final Leader leader;
    private final SharedFiles sharedFiles;
    private final ElectionBroadcast electionBroadcast;
    private HashMap timeStampHM;
    private long timeStamp;

    public PeerNode(Leader leader, SharedFiles sharedFiles) throws UnknownHostException, RemoteException {
        this.leader = new Leader();
        this.sharedFiles = sharedFiles;
        electionBroadcast = new ElectionBroadcast(new Leader());
        timeStampHM = new HashMap();
        timeStamp = 0;
    }

    @Override
    public synchronized void broadcastRecieved(Message message) {
        System.out.println("Message Object Properties: " +
                message.getMessageType().toString()+ " " + 
                message.getSenderIPAddress());
        if(message.getTimeStamp()>timeStamp){
            updateTimeStamps(message.getRecipientIPAddress(), timeStamp);
        }
        updateTimeStamps(message.getSenderIPAddress(), message.getTimeStamp());
        try {
            switch (message.getMessageType()) {
                case ELECTION:
                    startElection();
                    break;
                case ELECT:
                    electionBroadcast.addElection(message);
                    break;
                case DECLARE_LEADER:
                    leader.setLeader(message);
                    break;
                case PEER_LOST:
                    //Tell leader to remove ip address from files
                    sharedFiles.peerLost(message);
                    break;
                case FIND_LEADER:
                    //Broadcast request for who is the leader
                    leader.broadcastLeader();
                    break;
                case GET_SNAPSHOT:
                    //Message leader timeStamp
                    
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
     *
     * @param args
     * @throws UnknownHostException
     * @throws java.rmi.RemoteException
     */
    public static void main(String[] args) throws UnknownHostException, RemoteException {
        
    }

    private void updateTimeStamps(String senderIPAddress, long timeStamp) {
        if(timeStamp>-1) {
            timeStampHM.put(senderIPAddress, timeStamp);
        }
    }
    
    
}