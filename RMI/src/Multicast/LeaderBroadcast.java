package Multicast;

import Message.Message;
import Message.MessageType;
import java.net.UnknownHostException;
import peerNode.Leader;

/**
 * Send a broadcast requesting the current leader from the network
 * 
 * @author Kerry Powell, Mark Burton
 * @version 1.0
 */
public class LeaderBroadcast extends Thread {
    
    private Leader leader;
    private static final int WAIT_TIME = 15000;
    
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
        try {
            // Send a broadcast to request who is the leader
            message = new Message(MessageType.FIND_LEADER, "Find leader from LeaderBroadcast");
            Broadcast.sendBroadcast(message);
            // Wait for a response
            sleep(WAIT_TIME);
            // If a leader has not been set, declear election
            if (!leader.hasLeader()) {
                message = new Message(MessageType.ELECTION, "Begin election from LeaderBroadcast");
                Broadcast.sendBroadcast(message);
            }
            
        } catch (UnknownHostException | InterruptedException ex) {
            
            System.out.println(ex);
        }
        
    }
}
