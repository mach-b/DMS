package share;

import java.io.File;
import java.io.Serializable;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import message.Message;
import message.MessageType;
import multicast.Broadcast;
import peerNode.Leader;
import share.rmi.SharedFilesRMI;

/**
 * Class that represents the files that can be shared from each application
 * 
 * @author Kerry Powell
 * @version 1.1
 */
public class SharedFiles extends UnicastRemoteObject implements SharedFilesRMI, Serializable {

    private Set<File> files;
    private Map<String, RemoteFiles> remoteFiles = new HashMap<String, RemoteFiles>();
    private static int port = 0; 
    private static final String CLASS_NAME = "SharedFiles";
    
    public SharedFiles() throws RemoteException {
        
        super();
        files = new HashSet<>();
        registerRMI();
    }
    
    /**
     * Get the port number for the local object
     * 
     * @return the port number
     */
    private int getPort() {
        if (port == 0) {
            port = Integer.parseInt(Message.getMasterID().split(":")[1]);
        }
        return port;
    }
    
    /**
     * Gets a remote instance of the shared files RMI interface from the given 
     * ip address
     * 
     * @param id of the machine that you want to get the interface from
     * @param fileName the name of the remote file
     * @return the RMI interface for shared files
     */
    public File getRemoteFile(String id, String fileName) {
        
        try {
            String ip = id.split(":")[0];
            int port = Integer.parseInt(id.split(":")[1]);
            Registry reg =  LocateRegistry.getRegistry(ip, port);
            SharedFilesRMI remoteFiles = (SharedFilesRMI) reg.lookup(CLASS_NAME + id);
            return remoteFiles.getFile(fileName);
            
        } catch (RemoteException ex) {
            
            System.out.println(ex);
        } catch (NotBoundException ex) {
            
            System.out.println(ex);
            Message message = new Message(MessageType.PEER_LOST, id);
            Broadcast.sendBroadcast(message);
        }
        return null;
    }  
    
    /**
     * Creates a new array list for storing remote files effectively clearing 
     * the old list
     */
    public void clearRemoteFilesArray() {
        remoteFiles = new HashMap<String, RemoteFiles>();
    }
    
    /**
     * When the connection to a peer has been lost all remote files for that 
     * user are deleted from the remote files list
     * 
     * @param message the broadcasted message that was recieved
     */
    public void peerLost(Message message) {
        
        String ip = message.getMessageContent();
        remoteFiles.remove(ip);
    }
    
    /**
     * Get the file names for of all the locally available files
     * 
     * @return a String array for the local files
     */
    public String[] getFileNames() {
        
        String[] names = new String[files.size()];
        Iterator filesIterator = files.iterator();
        for (int i = 0 ; i < files.size() && filesIterator.hasNext(); i++) {
            
            File file = (File)filesIterator.next();
            names[i] = file.getName();
        }
        return names;
    }
    
    /**
     * Add a file to the local shared files
     * 
     * @param file to be added to the list
     * @return true if the file was added
     */
    public boolean addFile(File file) {
        return files.add(file);
    }
    
    /**
     * Get the shared files from a remote computer
     * 
     * @param leader representing the leader of the group
     * @return an array of strings for the ui to display
     */
    public String[][] getAllRemoteFiles(Leader leader) {
        // Connect to the remote RMI
        SharedFilesRMI rmi = getSharedFilesRMI(leader);
        
        if (rmi != null) {
        
            String myIp = Message.getIPString();
            
            try {
                // Updating the list held by the leader
                rmi.updateFileNames(new RemoteFiles(getFileNames()));
                
            } catch (RemoteException ex) {
                System.out.println("Failed to update leader:\n" + ex);
            }
            
            try {
                // Getting the list from the leader
                Map<String, RemoteFiles> remoteFiles = rmi.getAllFileNames();
                int filesCount = 0;
                for (Map.Entry entry: remoteFiles.entrySet()) {
                    //Count how many files there are
                    RemoteFiles files = (RemoteFiles)entry.getValue();
                    filesCount += files.getArray().length;
                }
                String[][] resultList = new String[filesCount][2];
                for (Map.Entry entry: remoteFiles.entrySet()) {
                    RemoteFiles files = (RemoteFiles)entry.getValue();
                    for (String fileName: files.getArray()) {
                        filesCount--;
                        resultList[filesCount][0] = files.getID();
                        resultList[filesCount][1] = fileName;
                    }
                }
                return resultList;
            
            } catch (RemoteException ex) {
                // Connecting to the leader failed so an election has been requested
                System.out.println("Failed to get update from leader:\n" + ex);
            }
        }
        return null;
    }
    
    /* ---------------------------------------------------------------------- */
    /*                          RMI Implimentations                           */
    /* ---------------------------------------------------------------------- */
    
    /**
    * Go get the shared files on the leaders computer, if this app is the leader
    * it will return itself
    * 
    * @param leader the elected leader on the network
    * @return the SharedFilesRMI object on the remote computer
    */
    private SharedFilesRMI getSharedFilesRMI(Leader leader) {
        
        if(leader.hasLeader()){
            try {
                int leaderPort = Integer.parseInt(leader.getLeaderId().split(":")[1]);
                Registry reg =  LocateRegistry.getRegistry(leader.getLeaderIp(), leaderPort);  
                return (SharedFilesRMI) reg.lookup(CLASS_NAME + leader.getLeaderId());
                
            }catch (Exception e){
                System.out.println("Failed to connect to Leader RMI:\n" + e);
                Message message = new Message(MessageType.ELECTION, 
                        "Remote SahredFilesRMI not found");
                Broadcast.sendBroadcast(message);
            }
        }
        return null;
    }
    
    /**
     * Registers the class to be accessed via RMI
     */
    private void registerRMI() {

        try {
            // Create the registry and add this as an RMI
            System.out.println("RMI Name: " + CLASS_NAME+Message.getMasterID());
            Registry reg = LocateRegistry.createRegistry(getPort());
            reg.bind(CLASS_NAME+Message.getMasterID(), this); 
        } catch (Exception ex) {                                
            System.err.println(ex);
        }    
    }

    @Override
    public File getFile(String fileName) {
        
        Iterator filesIterator = files.iterator();
        while (filesIterator.hasNext()) {
            File file = (File)filesIterator.next();
            if (file.getName().equals(fileName)) {
                
                return file;
            }
        }
        return null;
    }
    
    @Override
    public synchronized Map<String, RemoteFiles> getAllFileNames() {
         
        return remoteFiles;
    }

    @Override
    public synchronized void updateFileNames(RemoteFiles remoteFile) {
        // Get the ip of the sender adding their remote files
        String remoteID = remoteFile.getID();
        remoteFiles.put(remoteID, remoteFile);
        
    }
}
