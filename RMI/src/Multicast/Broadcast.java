package Multicast;

import Message.Message;
import com.google.gson.Gson;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
* Class for sending a broadcast message on a new thread
* 
* @author Kerry Powell, Mark Burton
* @version 1.0
*/
public class Broadcast extends Thread {

    private final Message message;
    private final int PORT = 8888;
    private final String BROADCAST_HOST = "224.2.2.3";

    /**
     * Build a broadcast
     * 
     * @param message to be sent
     */
    private Broadcast(Message message) {
        this.message = message;
    }

    /**
     * Send a broadcast with the given message
     * 
     * @param message 
     */
    public static void sendBroadcast(Message message) {

        Broadcast broadcast = new Broadcast(message);
        broadcast.start();
    }

    @Override
    public void run() {
        try {
            // Send to multicast IP address and port
            InetAddress address = InetAddress.getByName(BROADCAST_HOST);
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

}
