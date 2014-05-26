package Message;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Mark Burton, Kerry Powell
 */
public class Message {
    
    //private final double timeStamp;
    private final String senderIPAddress, recipientIPAddress, messageContent;
    private final MessageType messageType;
    private static long timeStamp = 0;
    
    /**
     * A Message
     * @param messageType String The type of Message
     * @param senderIPAddress String The senders address
     * @param recipientIPAddress String The recipients address
     * @param timestamp The timestamp
     * @param messageContent String Any content to include
     */
    public Message(MessageType messageType, String senderIPAddress, String recipientIPAddress, long timestamp, String messageContent)  {
        this.messageType = messageType;
        this.senderIPAddress = senderIPAddress;
        this.recipientIPAddress = recipientIPAddress;
        this.timeStamp = timestamp;
        this.messageContent = messageContent;  // perhaps a filename?
    }
    
    public Message(MessageType messageType, String messageContent) {
        this.messageType = messageType;
        this.senderIPAddress = getIPString();
        this.recipientIPAddress = "broadcast";
        this.timeStamp = getTimeStamp();
        this.messageContent = messageContent;  // perhaps a filename?
    }
    
    /**
     * This constructor is primarily to respond to snapshot requests
     * @param messageType  
     * @param recipientIPAddress  Recipient IP Address
     * @param timeStamp  
     * @param messageContent 
     */
    public Message(MessageType messageType, String recipientIPAddress, long timeStamp) {
        this.messageType = messageType;
        this.senderIPAddress = getIPString();
        this.recipientIPAddress = recipientIPAddress;
        this.timeStamp = timeStamp;
    }
    
    private synchronized static long getNewTimeStamp() {
        return ++timeStamp;
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

}
