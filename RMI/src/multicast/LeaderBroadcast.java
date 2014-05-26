package multicast;

import message.Message;
import message.MessageType;
import peerNode.Leader;

/**
 * Send a broadcast requesting the current leader from the network
 * 
 * @author Kerry Powell, Mark Burton
 * @version 1.0
 */
public class LeaderBroadcast extends Thread {
    
    private Leader leader;
    private static final int WAIT_TIME = 1500;
    
    public LeaderBroadcast(Leader leader) {
        this.leader = leader;
    }
    
    public static void findLeader(Leader leader) {
        
        LeaderBroadcast findLeader = new LeaderBroadcast(leader);
        findLeader.start();
    }
    
    @Override
    public void run() {
        
        Message message;
        // Send a broadcast to request who is the leader
        message = new Message(MessageType.FIND_LEADER, "Find leader from LeaderBroadcast");
        Broadcast.sendBroadcast(message);
        try {
            // Wait for a response
            sleep(WAIT_TIME);
        } catch (InterruptedException ex) { }
        // If a leader has not been set, declear election
        if (!leader.hasLeader()) {
            message = new Message(MessageType.ELECTION, "Begin election from LeaderBroadcast");
            Broadcast.sendBroadcast(message);
        }
    }
}
