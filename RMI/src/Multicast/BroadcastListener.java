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
    
//    @Override
//    public void run() {
//        MulticastSocket socket = null;
//        DatagramPacket inPacket = null;
//        byte[] inBuf = new byte[1024];
//        while (true) {
//            try {
//                //Prepare to join multicast group
//                socket = new MulticastSocket(8888);
//                InetAddress address = InetAddress.getByName("224.2.2.3");
//                socket.joinGroup(address);
//
//                while (true) {
//                    inPacket = new DatagramPacket(inBuf, inBuf.length);
//                    socket.receive(inPacket);
//                    //String senderIP = (String) inPacket.getAddress().toString();
//                    //System.out.println("Sender IP: "+senderIP);
////                    String msg = new String(inBuf, 0, inPacket.getLength());
////                    System.out.println("From " + inPacket.getAddress() + " Msg : ");
////                    broadcastRecieved(msg);
//                    ByteArrayInputStream bais = new ByteArrayInputStream(inBuf);
//                    ObjectInputStream ois = new ObjectInputStream(bais);
//                    Message m = (Message) ois.readObject();
//                    System.out.println("Message :" + m.getMessageType().name()+ " from :"+m.getSenderIPAddress());
//                }
//            } catch (IOException ioe) {
//                System.out.println(ioe);
//            } catch (ClassNotFoundException ex) {
//                Logger.getLogger(BroadcastListener.class.getName()).log(Level.SEVERE, null, ex);
//            }
//            try {
//                Thread.sleep(500);
//            } catch (InterruptedException ie) {
//            }
//        }
//    }
    
    abstract public void broadcastRecieved(String message);

}
