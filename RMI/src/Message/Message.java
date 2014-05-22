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
public class Message implements Serializable {
    
    private double timeStamp;
    private String senderIPAddress, recipientIPAddress;
    private MessageType messageType;
    private String additionalContent;
    
    public Message(MessageType messageType, String senderIPAddress, String recipientIPAddress, double timestamp) {
        this.messageType = messageType;
        this.senderIPAddress = senderIPAddress;
        this.timeStamp = timestamp;
    }

    public Message(double timeStamp, String senderIPAddress, String recipientIPAddress, MessageType messageType, String additionalContent) {
        this.timeStamp = timeStamp;
        this.senderIPAddress = senderIPAddress;
        this.messageType = messageType;
        this.additionalContent = additionalContent;
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

    public String getAdditionalContent() {
        return additionalContent;
    }
    
}
