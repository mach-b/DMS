package peerNode;

import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import message.Message;
import message.MessageType;
import multicast.Broadcast;
import multicast.BroadcastListener;
import multicast.DirectMessage;
import multicast.ElectionBroadcast;
import share.SharedFiles;

/**
 * Represents a peer node that reacts to the broadcasts sent throughout the
 * network
 *
 * @author Mark Burton, Kerry Powell
 * @version 1.0
 */
public class PeerNode extends BroadcastListener {

    private final Leader LEADER;
    private final SharedFiles SHARED_FILES;
    private final HashMap TIME_STAMPS;

    public PeerNode(SharedFiles sharedFiles) throws UnknownHostException, RemoteException {
        this.LEADER = new Leader();
        this.SHARED_FILES = sharedFiles;
        TIME_STAMPS = new HashMap();
    }

    /**
     * Get the leader object created by the peer node
     * 
     * @return the leader object for the application
     */
    public Leader getLeader() {
        return LEADER;
    }
    
    /**
     * Get the time stamps to make a snapshot of the system
     * 
     * @return a map with the network snapshot
     */
    public HashMap getTimeStamps() {
        
        Message message = new Message(MessageType.REQUEST_SNAPSHOT, "");
        Broadcast.sendBroadcast(message);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException ex) {}
        return TIME_STAMPS;
    }

    @Override
    public synchronized void broadcastRecieved(Message message) {
        
        if (updateTimeStamps(message)) {
            System.out.println("Message Recieved: " + message.toString());
            try {
                switch (message.getMessageType()) {
                    case ELECTION:
                        startElection();
                        break;
                    case ELECT:
                        ElectionBroadcast.addElection(message);
                        break;
                    case DECLARE_LEADER:
                        LEADER.setLeader(message);
                        break;
                    case PEER_LOST:
                        //Tell leader to remove ip address from files
                        SHARED_FILES.peerLost(message);
                        break;
                    case FIND_LEADER:
                        //Broadcast request for who is the leader
                        LEADER.broadcastLeader();
                        break;
                    case REQUEST_SNAPSHOT:  // Request snapshot to include requester's IP in message content.
                        //DirectMessage sender
                        Message reply = new Message(MessageType.SUPPLY_TIMESTAMP,
                                message.getSenderIPAddress(), "Supplying Timestamp");
                        DirectMessage.sendDirectMessage(reply);
                        break;
                    case SUPPLY_TIMESTAMP:
                        break;
                } 
            } catch (UnknownHostException ex) {
                Logger.getLogger(PeerNode.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            System.out.println("Message Ignored: " + message.toString()); 
        }
    }

    /**
     * Begin an election
     *
     * @throws UnknownHostException
     */
    private synchronized void startElection() throws UnknownHostException {

        ElectionBroadcast.voteSelf(LEADER);
    }

    /**
     * Update the timestamp for a given IP address
     * 
     * @param senderIPAddress the IP address of the sender
     * @param timeStamp the timestamp from the sender
     */
    private boolean updateTimeStamps(Message message) {
        
        Object value = TIME_STAMPS.get(message.getID());
        long returnValue = value == null ? 0 : (long)value;
        if (returnValue < message.getTimeStamp()) {
            //Add and process timestamp if it is current
            TIME_STAMPS.put(message.getID(), message.getTimeStamp());
            return true;
        }
        System.out.println("Time:" + returnValue + " ID:" +message.getID());
        return false;
    }
}
