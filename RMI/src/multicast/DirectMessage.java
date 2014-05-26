/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package multicast;

import message.Message;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Sends a message to the recipient specified in the Message object.
 * @author markburton
 */
public class DirectMessage extends Thread{
    
    
    private final Message message;
    private final int PORT = 8888;
    private final String recipientAddress;

    /**
     * Build a broadcast
     * 
     * @param message to be sent
     */
    private DirectMessage(Message message, String recipientAddress) {
        this.message = message;
        this.recipientAddress = recipientAddress;
    }

    /**
     * Send a broadcast with the given message
     * 
     * @param message 
     * @param recipientAddress 
     */
    public static void sendDirectMessage(Message message, String recipientAddress) {

        DirectMessage directMessage = new DirectMessage(message, recipientAddress);
        directMessage.start();
    }

    @Override
    public void run() {
        try {
            // Send to multicast IP address and port
            InetAddress address = InetAddress.getByName(recipientAddress);
            // Setup message to be sent
            DatagramSocket socket = new DatagramSocket();
            byte[] outBuf = new Gson().toJson(message).getBytes();
            // Create packet with message data
            DatagramPacket outPacket = new DatagramPacket(outBuf, outBuf.length, address, PORT);
            // Send packet
            socket.send(outPacket);
        } catch (IOException ioe) {
            System.out.println(ioe);
        }
    }
    
//    private Message message;
//    private ObjectOutputStream out;
//    private Gson gson = new Gson();
//    
//    public DirectMessage(Message message) {
//        this.message = message;
//    }
//
//    @Override
//    public void run() {
//        System.out.println("Leader Address propsed.");
//        DatagramSocket socket = null;
//        DatagramPacket outPacket = null;
//        byte[] outBuf;
//        final int PORT = 8888;
//
//        try {
//            socket = new DatagramSocket();
//            long counter = 0;
//            String msg;
//
//            while (counter < 10) {
//                msg = gson.toJson(message);
//                System.out.println("JSON: "+msg);
//                counter++;
//                outBuf = msg.getBytes();
//                //Send to multicast IP address and port
//                InetAddress address = InetAddress.getByName(message.getRecipientIPAddress());
//                outPacket = new DatagramPacket(outBuf, outBuf.length, address, PORT);
//                socket.send(outPacket);
//                System.out.println("Server sends : " + msg + " counter=" + counter);
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException ie) {
//                }
//            }
//        } catch (IOException ioe) {
//            System.out.println(ioe);
//        }
//    }
//    
////    @Override
////    public void run() {
////        System.out.println(message.getMessageType().name());
////        DatagramSocket socket = null;
////        DatagramPacket outPacket = null;
////        byte[] outBuf;
////        final int PORT = 8888;
////
////        try {
////            socket = new DatagramSocket();
////            long counter = 0;
////            String msg;
////            
////            while (counter < 100) {
////                //msg = "Candidate: " + InetAddress.getLocalHost();
////                counter++;
////                ByteArrayOutputStream baos = new ByteArrayOutputStream();
////                out = new ObjectOutputStream(baos);
////                out.writeObject(message);
////                outBuf = baos.toByteArray();
////                //Send to multicast IP address and port
////                InetAddress address = InetAddress.getByName(message.getRecipientIPAddress()); //"224.2.2.3"
////                outPacket = new DatagramPacket(outBuf, outBuf.length, address, PORT);
////                socket.send(outPacket);
////                System.out.println("Server sends : " + message.getMessageType().toString() + " counter=" + counter);
////                try {
////                    Thread.sleep(500);
////                } catch (InterruptedException ie) {
////                }
////            }
////        } catch (IOException ioe) {
////            System.out.println(ioe);
////        }
////    }
    
}
