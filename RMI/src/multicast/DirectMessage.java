package multicast;

import message.Message;
import com.google.gson.Gson;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Sends a message to the recipient specified in the Message object.
 * 
 * @author Mark Burton
 * @version 1.0
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
    private DirectMessage(Message message) {
        this.message = message;
        this.recipientAddress =  message.getRecipientIPAddress();
    }

    /**
     * Send a broadcast with the given message
     * 
     * @param message the message to be sent 
     */
    public static void sendDirectMessage(Message message) {

        DirectMessage directMessage = new DirectMessage(message);
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
    
}
