package message;

/**
 * Message type enum for when sending messages
 * 
 * @author Mark Burton and Kerry Powell
 */
public enum MessageType {
    
    ELECTION, 
    ELECT, 
    DECLARE_LEADER,
    PEER_LOST,
    FIND_LEADER,
    REQUEST_SNAPSHOT,
    SUPPLY_TIMESTAMP;
}
