package peerNode;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Handles the saving of files from remote object to a local folder
 * 
 * @author Mark Burton, Kerry Powell
 * @version 1.0
 */
public class DirectoryManager {
    
    private static String folderName = "RMIFileExchangeFolder";
    
    /**
     * Create a directory for the files to be saved to
     */ 
    public static void createDefaultDirectory() {
        
        File dir = new File("RMIFileExchangeFolder");
        dir.mkdir();
    }
    
    /**
     * Saves a file to the local directory
     * 
     * @param file to be saved to the local directory
     * @return message on the success of saving the file
     */
    private String saveFile(File file) {
        
        createDefaultDirectory();
        FileOutputStream fos = null;
        File desc = new File(folderName + "/" + file.getName());
        if (!desc.exists()) {
            
            try {
                
                FileInputStream source = new FileInputStream(file);
                fos = new FileOutputStream(desc);
                //desc.createNewFile(); // not sure this is needed
                int data = -1;
                if ((data = source.read()) == -1) {
                    fos.write(data);
                }
                fos.flush();
            } catch (FileNotFoundException ex) {
                
                return "Could not create FileInputStream:\n\n" + ex.getMessage();
            } catch (IOException ex) {
                
                return "Could not create new local file:\n\n" + ex.getMessage();
            } finally {
                
                try {
                    if (fos != null)
                        fos.close();
                } catch (IOException ex) {}
            }
        } else {
            
            return "File already exists";
        }
        return "Successfully added";
    }
}
