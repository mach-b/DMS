/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Multicast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 *
 * @author markburton
 */
public class ElectionBroadcast {

    
    public ElectionBroadcast() {
    }
    
    public void proposeLeader() {
        DatagramSocket socket = null;
        DatagramPacket outPacket = null;
        byte[] outBuf;
        final int PORT = 8888;
        try {
            socket = new DatagramSocket();
            long counter = 0;
            String msg;

            while (true) {
                msg = "This is multicast! " + counter;
                counter++;
                outBuf = msg.getBytes();

                //Send to multicast IP address and port
                InetAddress address = InetAddress.getByName("224.2.2.3");
                outPacket = new DatagramPacket(outBuf, outBuf.length, address, PORT);

                socket.send(outPacket);

                System.out.println("Server sends : " + msg);
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
