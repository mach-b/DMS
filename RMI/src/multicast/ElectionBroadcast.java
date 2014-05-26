package multicast;

import message.MessageType;
import message.Message;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Iterator;
import javax.swing.Timer;
import peerNode.Leader;

/**
 * Handles the election process for electing a new leader
 * 
 * @author Mark Burton, Kerry Powell
 * @version 1.1
 */
public class ElectionBroadcast extends Thread{
    
    private HashSet<Message> messages;
    private final Leader leader;
    private Timer timer = null;
    private static final int WAIT_TIME = 4000;
    
    public ElectionBroadcast(Leader leader) {
        this.leader = leader;
        messages = new HashSet<Message>();
    }
    
    public synchronized void voteSelf() throws UnknownHostException {
        
        if (!isAlive()) {
            Message message = new Message(MessageType.ELECT, "");
            Broadcast.sendBroadcast(message);
            start();
        }
    }
    
    public void addElection(Message message) throws UnknownHostException {

        messages.add(message);
    }
    
    private void chooseLeader() throws UnknownHostException {

        //find the leader in the messages
        String leaderIP = "";
        Iterator<Message> itr = messages.iterator();
        while(itr.hasNext()) {
            if(itr.next().getSenderIPAddress().compareTo(leaderIP)<1) {
                leaderIP = itr.next().getSenderIPAddress();
            }
        }
        //broadcast messages
        Message m = new Message(MessageType.DECLARE_LEADER, leaderIP);
        Broadcast.sendBroadcast(m);
        messages = new HashSet<Message>();
    }

    @Override
    public void run() {
        // A sleeping process that is waiting for leader responces
        try {
            sleep(WAIT_TIME);
            if (!leader.hasLeader()) {
                // Choose a leader if one has not been declared
                chooseLeader();
            }
        } catch (UnknownHostException | InterruptedException ex) {
            System.out.println(ex);
        } 
    }
}