/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Message;

/**
 *
 * @author markburton
 */
public enum MessageType {
    
    ELECTION("Election message"), FIND_PEERS("Find Peers message"), 
    PEER_TO_PEER("Peer to peer message"), DECLARE_LEADER("Declare leader message"),
    SELF_DISCOVERY("Self discovery");
    
    private final String name;
    
    private MessageType(String s) {
        this.name = s;
    }
    
    
}
