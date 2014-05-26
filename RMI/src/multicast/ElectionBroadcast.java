/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package multicast;

import message.MessageType;
import message.Message;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Timer;
import peerNode.Leader;

/**
 *
 * @author Mark Burton and Kerry Powell
 */
public class ElectionBroadcast implements ActionListener{
    
    private HashSet<Message> messages;
    private final Leader leader;
    private Timer timer = null;
    
    public ElectionBroadcast(Leader leader) {
        this.leader = leader;
        messages = new HashSet<Message>();
    }
    
    private synchronized void startTimer() {
        if (timer == null) {
            timer = new Timer(10000, this);
            timer.start();
        }
    }
    
    private synchronized void stopTimer() {
        if (timer != null) {
            if (timer.isRunning()) 
                timer.stop();
            timer = null;
        }
    }
    
    public void beginElection() throws UnknownHostException {
        Message message = new Message(MessageType.ELECTION, "");
        Broadcast.sendBroadcast(message); //send message
    }
    
    public void voteSelf() throws UnknownHostException {
        Message message = new Message(MessageType.ELECT, "");
        Broadcast.sendBroadcast(message);
        startTimer();
    }
    
    public void addElection(Message message) throws UnknownHostException {
        startTimer();
        messages.add(message);
    }
    
    private void chooseLeader() throws UnknownHostException {
        stopTimer();
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
    public void actionPerformed(ActionEvent e) {
        try {
            chooseLeader();
        } catch (UnknownHostException ex) {
            Logger.getLogger(ElectionBroadcast.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
