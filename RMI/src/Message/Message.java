/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Message;

import java.io.Serializable;

/**
 *
 * @author markburton
 */
public class Message {
    
    private final double timeStamp;
    private final String senderIPAddress, recipientIPAddress;
    private final MessageType messageType;
    
    public Message(MessageType messageType, String senderIPAddress, String recipientIPAddress, double timestamp) {
        this.messageType = messageType;
        this.senderIPAddress = senderIPAddress;
        this.recipientIPAddress = recipientIPAddress;
        this.timeStamp = timestamp;
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

}
