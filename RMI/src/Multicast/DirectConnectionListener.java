/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Multicast;

import Message.Message;
import com.google.gson.Gson;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

/**
 *
 * @author markburton
 */
public abstract class DirectConnectionListener extends Thread {
    
    
    private Gson gson = new Gson();
    
    protected DirectConnectionListener(){
        
        this.start();
    }

    @Override
    public void run() {
        MulticastSocket socket = null;
        DatagramPacket inPacket = null;
        byte[] inBuf = new byte[1024];
        while (true) {
            try {
                //Prepare to join multicast group  ** CAN FUNCTION AS MULTICAST **
                socket = new MulticastSocket(8889);
                InetAddress address = InetAddress.getByName("224.2.2.3");
                socket.joinGroup(address);

                while (true) {
                    inPacket = new DatagramPacket(inBuf, inBuf.length);
                    socket.receive(inPacket);
                    String msg = new String(inBuf, 0, inPacket.getLength());
                    //System.out.println("From " + inPacket.getAddress() + " Msg : " + msg);
                    broadcastRecieved(msg);
                    Message m = gson.fromJson(msg, Message.class);
                    System.out.println("Message Object Properties: " +m.getMessageType().toString()+ " " + m.getSenderIPAddress());
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
    
    abstract public void broadcastRecieved(String message);
    
}
