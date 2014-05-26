package peerNode;

import message.Message;
import message.MessageType;
import multicast.Broadcast;
import multicast.LeaderBroadcast;

/**
 * A class that handles finding the networks leader and electing the leader, is 
 * the broadcast listener for dealing with elections
 * 
 * @author Kerry Powell
 * @version 1.0
 */
public class Leader {
    
    private String leaderIp = null;
    private String leaderId = null;
    private boolean isLeader = false;
    private boolean electingLeader = false;
    
    public Leader() {
        // See who is leader
        LeaderBroadcast.findLeader(this);
    }
    
    /**
     * Check to see if this instance of the leader class is the current leader
     * 
     * @return true if this class is the current leader
     */
    public boolean isLeader() {
        return isLeader;
    }
    
    public boolean electingLeader() {
        return electingLeader;
    }
    
    public String getLeaderId() {
        return leaderId;
    }
    
    /**
     * Set who the current leader is
     * 
     * @param message of the leader or null if self is leader
     */
    public synchronized void setLeader(Message message) {
        
        leaderIp = message.getMessageContent().split(":")[0];
        leaderId = message.getID();
        isLeader = leaderId.equals(Message.getMasterID());
        electingLeader = false;
    }
    
    /**
     * Set the leader to be in leader election state
     */
    public synchronized void electionStarted() {
        
        leaderIp = null;
        leaderId = null;
        isLeader = false;
        electingLeader = true;
    }

    /**
     * Is there a leader that has been chosen?
     * 
     * @return true if there is a leader
     */
    public synchronized boolean hasLeader() {
        
        return leaderIp != null;
    }
    
    /**
     * Send out a broadcast message saying you are the leader
     */
    public void broadcastLeader() {
        
        if (isLeader) {

            Message message = new Message(MessageType.DECLARE_LEADER, Message.getIPString());
            Broadcast.sendBroadcast(message);
        }
    }
    
    /**
     * Get the current leader so that files can be registered for sharing, if 
     * there is an election then this returns null
     * 
     * @return The current leader or null if currently electing
     */
    public String getLeaderIp() {
        
        return leaderIp;
    }
}
