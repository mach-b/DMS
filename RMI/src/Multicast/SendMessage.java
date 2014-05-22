/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Multicast;

import Message.Message;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 *
 * @author markburton
 */
public class SendMessage extends Thread{
    
    private Message message;
    private ObjectOutputStream out;
    
    public SendMessage(Message message) {
        this.message = message;
    }

    
    @Override
    public void run() {
        System.out.println(message.getMessageType().name());
        DatagramSocket socket = null;
        DatagramPacket outPacket = null;
        byte[] outBuf;
        final int PORT = 8888;

        try {
            socket = new DatagramSocket();
            long counter = 0;
            String msg;
            
            while (true) {
                //msg = "Candidate: " + InetAddress.getLocalHost();
                counter++;
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                out = new ObjectOutputStream(baos);
                out.writeObject(message);
                outBuf = baos.toByteArray();
                //Send to multicast IP address and port
                InetAddress address = InetAddress.getByName(message.getRecipientIPAddress()); //"224.2.2.3"
                outPacket = new DatagramPacket(outBuf, outBuf.length, address, PORT);
                socket.send(outPacket);
                System.out.println("Server sends : " + message.getMessageType().toString() + " counter=" + counter);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ie) {
                }
            }
        } catch (IOException ioe) {
            System.out.println(ioe);
        }
    }
    
}
