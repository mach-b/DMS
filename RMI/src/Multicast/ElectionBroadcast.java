/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Multicast;

import Message.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.UnknownHostException;
import java.util.HashSet;
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
            timer = new Timer(300000, this);
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
    
    public void leaderChosen(Message message) {
        
    }
    
    private void chooseLeader() {
        stopTimer();
        //find the leader in the messages
        //broadcast messages
        messages = new HashSet<Message>();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        chooseLeader();
    } 
}
