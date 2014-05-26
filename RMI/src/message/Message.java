package message;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Random;

/**
 * A class that stores the data for messages to be sent over the network
 * 
 * @author Mark Burton, Kerry Powell
 * @version 1.0
 */
public class Message {
    
    //private final double timeStamp;
    private final String senderIPAddress, recipientIPAddress, messageContent;
    private final MessageType messageType;
    private final long timeStamp;
    private String id;
    private static long timeStampCount = 0;
    private static String masterId = null;
    
    /**
     * A Message
     * 
     * @param messageType String The type of Message
     * @param senderIPAddress String The senders address
     * @param recipientIPAddress String The recipients address
     * @param messageContent String Any content to include
     */
    public Message(MessageType messageType, String senderIPAddress, String recipientIPAddress, String messageContent)  {
        this.messageType = messageType;
        this.senderIPAddress = senderIPAddress;
        this.recipientIPAddress = recipientIPAddress;
        this.timeStamp = getNewTimeStamp();
        this.messageContent = messageContent;  // perhaps a filename?
        this.id = getMasterID();
        messageCreated();
    }
    
    /**
     * A Message
     * 
     * @param messageType String The type of Message
     * @param messageContent String Any content to include
     */
    public Message(MessageType messageType, String messageContent) {
        this(messageType, "broadcast", messageContent);
    }
    
    /**
     * This constructor is primarily to respond to snapshot requests
     * 
     * @param messageType  String The type of Message
     * @param recipientIPAddress  Recipient IP Address 
     * @param messageContent String Any content to include
     */
    public Message(MessageType messageType, String recipientIPAddress, String messageContent) {
        this(messageType, getIPString(), recipientIPAddress , messageContent);
    }
    
    public synchronized static String getMasterID() {
        
        if (masterId == null) {
            masterId = getIPString() + ":" + new Random().nextInt();
        }
        return masterId;
    }
    
    public String getID() {
        return id;
    }
    
    private void messageCreated() {
        System.out.println("Message made, message type: " + messageType.toString());
    }
    
    private synchronized static long getNewTimeStamp() {
        return ++timeStampCount;
    }

    public String getRecipientIPAddress() {
        return recipientIPAddress;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public String getSenderIPAddress() {
        return senderIPAddress;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public String getMessageContent() {
        return messageContent;
    }
    
    public static String getIPString() {
        try {
            String s = InetAddress.getLocalHost().toString();
            String[] segments = s.split("/");
            return segments[segments.length-1];
        } catch (UnknownHostException ex) {
            return "";
        }
    }
    
    @Override
    public String toString() {
        return messageType + " from " + senderIPAddress + " # " + timeStamp ;
    }

}
