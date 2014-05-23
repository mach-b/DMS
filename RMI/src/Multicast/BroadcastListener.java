/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Multicast;

import Message.Message;
import com.google.gson.Gson;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * BroadcastListener listens for DatagramPackets on a MulticastSocket,
 * it listens on the group address 224.2.2.3 port 8888.
 * @author markburton
 */
public abstract class BroadcastListener extends Thread {
    
    private Gson gson = new Gson();
    
    protected BroadcastListener(){
        
        this.start();
    }

    @Override
    public void run() {
        MulticastSocket socket = null;
        DatagramPacket inPacket = null;
        byte[] inBuf = new byte[1024];
        while (true) {
            try {
                //Prepare to join multicast group
                socket = new MulticastSocket(8888);
                InetAddress address = InetAddress.getByName("224.2.2.3");
                socket.joinGroup(address);

                while (true) {
                    inPacket = new DatagramPacket(inBuf, inBuf.length);
                    socket.receive(inPacket);
                    String packetString = new String(inBuf, 0, inPacket.getLength());
                    Message message = gson.fromJson(packetString, Message.class);
                    broadcastRecieved(message);
                }
            } catch (IOException ioe) {
                System.out.println(ioe);
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException ie) {
            }
        }
    }
    
    abstract public void broadcastRecieved(Message message);

}
