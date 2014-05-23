/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Message;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 *
 * @author markburton
 */
public class Message {
    
    private final double timeStamp;
    private final String senderIPAddress, recipientIPAddress, messageContent;
    private final MessageType messageType;
    private static long timestamp = 0;
    
    /**
     * A Message
     * @param messageType String The type of Message
     * @param senderIPAddress String The senders address
     * @param recipientIPAddress String The recipients address
     * @param timestamp double The timestamp
     * @param messageContent String Any content to include
     */
    public Message(MessageType messageType, String senderIPAddress, String recipientIPAddress, double timestamp, String messageContent) throws UnknownHostException {
        this.messageType = messageType;
        this.senderIPAddress = senderIPAddress;
        this.recipientIPAddress = recipientIPAddress;
        this.timeStamp = timestamp;
        this.messageContent = messageContent;  // perhaps a filename?
    }
    
    public Message(MessageType messageType, String messageContent) throws UnknownHostException {
        this.messageType = messageType;
        this.senderIPAddress = InetAddress.getLocalHost().toString();
        this.recipientIPAddress = "broadcast";
        this.timeStamp = getTimeStamp();
        this.messageContent = messageContent;  // perhaps a filename?
    }
    
    private synchronized static long getNewTimeStamp() {
        return ++timestamp;
    }

    public String getRecipientIPAddress() {
        return recipientIPAddress;
    }

    public double getTimeStamp() {
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
    
    

}
