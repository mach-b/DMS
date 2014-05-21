package share;

import java.io.File;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import rmi.SharedFilesRMI;

/**
 * Class that represents the files that can be shared from each application
 * 
 * @author Kerry Powell
 * @version 1.0
 */
public class SharedFiles extends UnicastRemoteObject implements SharedFilesRMI {

    Set<File> files;
    
    public SharedFiles() throws RemoteException {
        
        super();
        files = new HashSet<>();
        registerRMI();
    }
    
    /**
     * Registers the class to be accessed via RMI
     */
    private void registerRMI() {
        
        Registry reg;
        try {
            reg = LocateRegistry.createRegistry(1099);
            reg.bind("SharedFiles", this);
            System.out.println("Server has started.");
        } catch (Exception ex) {
            System.err.println(ex);
        }    
    }
    
    /**
     * Gets a remote instance of the shared files RMI interface from the given 
     * ip address
     * 
     * @param ip of the machine that you want to get the interface from
     * @return the RMI interface for shared files
     * @throws RemoteException if the remote registry cannot be found
     * @throws NotBoundException if the remote registry doesn't have an interface 
     */
    public static SharedFilesRMI getRemoteFiles(String ip) 
            throws RemoteException, NotBoundException {
        
        Registry reg =  LocateRegistry.getRegistry(ip, 1099);
        return (SharedFilesRMI) reg.lookup("SharedFiles");
    }  
    
    @Override
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

    @Override
    public File getFile(String fileName) throws RemoteException {
        
        Iterator filesIterator = files.iterator();
        while (filesIterator.hasNext()) {
            File file = (File)filesIterator.next();
            if (file.getName().equals(fileName)) {
                return file;
            }
        }
        return null;
    }
}
