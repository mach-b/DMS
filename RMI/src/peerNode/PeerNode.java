package peerNode;

import Message.Message;
import Message.MessageType;
import Multicast.BroadcastListener;
import Multicast.DirectMessage;
import Multicast.ElectionBroadcast;
import java.net.InetAddress;
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
    public final String ipAddress;

    public PeerNode(Leader leader, SharedFiles sharedFiles) throws UnknownHostException, RemoteException {
        this.leader = new Leader();
        this.sharedFiles = sharedFiles;
        electionBroadcast = new ElectionBroadcast(new Leader());
        timeStampHM = new HashMap();
        ipAddress = getPeerIPString();
    }

    @Override
    public synchronized void broadcastRecieved(Message message) {
        System.out.println("Message Object Properties: "
                + message.getMessageType().toString() + " "
                + message.getSenderIPAddress());
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
                case REQUEST_SNAPSHOT:  // Request snapshot to include requester's IP in message content.
                    //DirectMessage sender
                    Message m = new Message(MessageType.SUPPLY_TIMESTAMP,
                            message.getSenderIPAddress(), "Supplying Timestamp");
                    DirectMessage.sendDirectMessage(m, m.getRecipientIPAddress());
                    break;
                case SUPPLY_TIMESTAMP:
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
     *
     * @param args
     * @throws UnknownHostException
     * @throws java.rmi.RemoteException
     */
    public static void main(String[] args) throws UnknownHostException, RemoteException {

        System.out.println("Address = " + InetAddress.getLocalHost());
    }

    private void updateTimeStamps(String senderIPAddress, long timeStamp) {
        if (timeStamp > -1) {
            timeStampHM.put(senderIPAddress, timeStamp);
        }
    }

    public static String getPeerIPString() {
        try {
            String s = InetAddress.getLocalHost().toString();
            String[] segments = s.split("/");
            return segments[segments.length - 1];
        } catch (UnknownHostException ex) {
            return "";
        }
    }

}
