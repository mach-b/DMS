package multicast;

import message.Message;
import com.google.gson.Gson;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

/**
 * BroadcastListener listens for DatagramPackets on a MulticastSocket,
 * it listens on the group address 224.2.2.3 port 8888.
 * 
 * @author Mark Burton, Kerry Powell
 * @version 1.0
 */
public abstract class BroadcastListener extends Thread {
    
    private Gson gson = new Gson();
    private final int PORT = 8888;
    private final String BROADCAST_HOST = "224.2.2.3";
    private final int BUFFER_SIZE = 1024;
    
    protected BroadcastListener(){
        
        this.start();
    }

    @Override
    public void run() {

        while (true) {
            try {
                //Prepare to join multicast group
                MulticastSocket socket = new MulticastSocket(PORT);
                InetAddress address = InetAddress.getByName(BROADCAST_HOST);
                socket.joinGroup(address);
                byte[] inBuf = new byte[BUFFER_SIZE];

                while (true) {
                    DatagramPacket inPacket = new DatagramPacket(inBuf, inBuf.length);
                    socket.receive(inPacket);
                    String packetString = new String(inBuf, 0, inPacket.getLength());
                    Message message = gson.fromJson(packetString, Message.class);
                    System.out.println("Message Recieved");
                    broadcastRecieved(message);
                }
            } catch (IOException e) {
                System.out.println(e);
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException ie) {
            }
        }
    }
    
    abstract public void broadcastRecieved(Message message);

}
