/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Message;

/**
 *
 * @author Mark Burton and Kerry Powell
 */
public enum MessageType {
    
    ELECTION("Election message"), 
    ELECT("Elect"), 
    DECLARE_LEADER("Declare leader message"),
    PEER_LOST("Peer host has been dropped"),
    FIND_LEADER("Find the leader on the network");
    
    private final String name;
    
    private MessageType(String s) {
        this.name = s;
    }
    
    
}
