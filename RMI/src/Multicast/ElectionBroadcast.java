/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Multicast;

import Message.*;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 *
 * @author markburton
 */
public class ElectionBroadcast extends Thread {
    
    private Message message = null;

    public void beginElection() throws UnknownHostException {
        message = new Message(MessageType.ELECTION, "");
    }
    
    @Override
    public void start() {
        
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
            long counter = 0;
            String msg;

            while (counter < 1) {
                msg = "Candidate: " + InetAddress.getLocalHost();
                counter++;
                outBuf = msg.getBytes();

                //Send to multicast IP address and port
                InetAddress address = InetAddress.getByName("224.2.2.3");
                outPacket = new DatagramPacket(outBuf, outBuf.length, address, PORT);
                socket.send(outPacket);
                System.out.println("Server sends : " + msg + " counter=" + counter);
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
