package share;

import java.io.File;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import rmi.SharedFilesRMI;

/**
 *
 * @author kerrypowell
 */
public class SharedFiles extends UnicastRemoteObject implements SharedFilesRMI {

    Set<File> files;
    
    public SharedFiles() throws RemoteException{
        super();
        files = new HashSet<>();
        registerRMI();
    }
    
    private void registerRMI() {
        Registry reg;
        try {
            reg = LocateRegistry.createRegistry(1099);
            reg.bind(this.toString(), this);
            System.out.println("Server has started.");
        } catch (Exception ex) {
            System.err.println(ex);
        }    
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
    
    @Override
    public String toString() {
        
        return "SharedFiles";
    }
    
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
