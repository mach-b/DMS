/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Multicast;

import Message.*;
import com.google.gson.Gson;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashSet;
import javax.swing.Timer;
import peerNode.Leader;

/**
 *
 * @author markburton
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
        new Broadcast(message); //send message
    }
    
    public void voteSelf() throws UnknownHostException {
        Message message = new Message(MessageType.ELECT, "");
        new Broadcast(message);
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
    
    private class Broadcast extends Thread {
        
        private final Message message;
        
        private Broadcast(Message message) {
            this.message = message;
            this.start();
        }
        
        @Override
        public void run() {
            System.out.println("Leader Address propsed.");
            DatagramSocket socket = null;
            DatagramPacket outPacket = null;
            byte[] outBuf;
            final int PORT = 8888;

            try {
                socket = new DatagramSocket();
                outBuf = new Gson().toJson(message).getBytes();

                //Send to multicast IP address and port
                InetAddress address = InetAddress.getByName("224.2.2.3");
                outPacket = new DatagramPacket(outBuf, outBuf.length, address, PORT);
                socket.send(outPacket);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ie) {}
                
            } catch (IOException ioe) {
                System.out.println(ioe);
            }
        }
        
    }

    
}
