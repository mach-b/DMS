package share;

import message.Message;
import message.MessageType;
import java.io.File;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import multicast.Broadcast;
import peerNode.Leader;
import rmi.SharedFilesRMI;

/**
 * Class that represents the files that can be shared from each application
 * 
 * @author Kerry Powell
 * @version 1.1
 */
public class SharedFiles extends UnicastRemoteObject implements SharedFilesRMI {

    private Set<File> files;
    private ArrayList<RemoteFiles> remoteFiles = new ArrayList<>();
    private static final int PORT = 1099; 
    private static final String CLASS_NAME = "SharedFiles";
    
    public SharedFiles() throws RemoteException {
        
        super();
        files = new HashSet<>();
        registerRMI();
    }
    
    /**
     * Gets a remote instance of the shared files RMI interface from the given 
     * ip address
     * 
     * @param ip of the machine that you want to get the interface from
     * @param fileName the name of the remote file
     * @return the RMI interface for shared files
     */
    public File getRemoteFile(String ip, String fileName) {
        
        try {
            
            Registry reg =  LocateRegistry.getRegistry(ip, PORT);
            SharedFilesRMI remoteFiles = (SharedFilesRMI) reg.lookup(CLASS_NAME);
            return remoteFiles.getFile(fileName);
            
        } catch (RemoteException ex) {
            
            System.out.println(ex);
        } catch (NotBoundException ex) {
            
            System.out.println(ex);
            Message message = new Message(MessageType.PEER_LOST, ip);
            Broadcast.sendBroadcast(message);
        }
        return null;
    }  
    
    /**
     * Creates a new array list for storing remote files effectively clearing 
     * the old list
     */
    public void clearRemoteFilesArray() {
        remoteFiles = new ArrayList<>();
    }
    
    /**
     * When the connection to a peer has been lost all remote files for that 
     * user are deleted from the remote files list
     * 
     * @param message the broadcasted message that was recieved
     */
    public void peerLost(Message message) {
        
        String ip = message.getMessageContent();
        for (int i = 0; i < remoteFiles.size(); i++) {
            
            String remoteIp = remoteFiles.get(i).getIp();
            if (remoteIp.equals(ip))
                remoteFiles.remove(i);
        }
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
                
                ArrayList<RemoteFiles> remoteFiles = rmi.getAllFileNames();
                int filesCount = 0;
                for (RemoteFiles files: remoteFiles) {
                    //Count how many files there are
                    if (!myIp.equals(files.getIp())) {
                        filesCount += files.getArray().length;
                    }
                }
                String[][] resultList = new String[filesCount][2];
                for (RemoteFiles files: remoteFiles) {
                    //Count how many files there are
                    if (!myIp.equals(files.getIp())) {
                        for (String fileName: files.getArray()) {
                            resultList[filesCount][0] = files.getIp();
                            resultList[filesCount][1] = fileName;
                            filesCount--;
                        }
                    }
                }
                return resultList;
            
            } catch (RemoteException ex) {
                // Connecting to the leader failed so an election has been requested
                System.out.println(ex);
                Message message = new Message(MessageType.ELECTION, 
                        "Remote SahredFilesRMI not found");
                Broadcast.sendBroadcast(message);
            }
        }
        return null;
    }
    
    /**
     * Go get the shared files on the leaders computer, if this app is the leader
     * it will return itself
     * 
     * @param leader the elected leader on the network
     * @return the SharedFilesRMI object on the remote computer
     */
    private SharedFilesRMI getSharedFilesRMI(Leader leader) {
        
        if (leader.isLeader()) {
            return this;
        } else if(leader.getLeaderIp() != null){
            try {
                Registry reg =  LocateRegistry.getRegistry(leader.getLeaderIp(), 1099);
                return (SharedFilesRMI) reg.lookup("SharedFiles");
            }catch (Exception e){
                System.out.println(e);
            }
        }
        return null;
    }
    
    
    /* ---------------------------------------------------------------------- */
    /*                          RMI Implimentations                           */
    /* ---------------------------------------------------------------------- */
    
    /**
     * Registers the class to be accessed via RMI
     */
    private void registerRMI() {

        try {
            // Create the registry and add this as an RMI
            Registry reg = LocateRegistry.createRegistry(PORT);
            reg.bind(CLASS_NAME, this);
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
    public synchronized ArrayList<RemoteFiles> getAllFileNames() {
         
        return remoteFiles;
    }

    @Override
    public synchronized void updateFileNames(RemoteFiles remoteFile) {
        // Get the ip of the sender adding their remote files
        String remoteIp = remoteFile.getIp();
        for (int i = 0; i < remoteFiles.size(); i++) {
            //Compare the IP of the remote and IPs in the list
            String listIp = remoteFiles.get(i).getIp();
            if (remoteIp.equals(listIp)) {
                remoteFiles.remove(i);
            }
        }
        if (remoteFile.getArray() != null) {
            //Only add RemoteFiles with active files to the list
            remoteFiles.add(remoteFile);
        }
    }
}
