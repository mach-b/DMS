package share;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Handles the saving of files from remote object to a local folder
 * 
 * @author Mark Burton, Kerry Powell
 * @version 1.0
 */
public class DirectoryManager {
    
    private static String FOLDER_NAME = "download";
    
    /**
     * Create a directory for the files to be saved to
     */ 
    public static void createDefaultDirectory() {
        
        File dir = new File(FOLDER_NAME);
        dir.mkdir();
    }
    
    /**
     * Saves a file to the local directory
     * 
     * @param remote to be saved to the local directory
     * @return message on the success of saving the file
     */
    public static String saveFile(File remote) {
        //Make the loacl download directory
        createDefaultDirectory();
        //Setup IO streams
        InputStream remoteStream = null;
        OutputStream loaclSream = null;
        //Set output file
        File local = new File(FOLDER_NAME + "/" + remote.getName());
        if (!local.exists()) {
            // If the file doesnt already exist
            try {
                // Creat stream objects and buffer
                local.createNewFile();
                remoteStream = new FileInputStream(remote);
                loaclSream = new FileOutputStream(local);
                byte[] buf = new byte[1024];
                int bytesRead;
                //Write bytes to file
                while ((bytesRead = remoteStream.read(buf)) > 0) {
                    loaclSream.write(buf, 0, bytesRead);
                }
            } catch (FileNotFoundException ex) {
                //Remote file could not be found
                return "Could not create FileInputStream:\n\n" + ex.getMessage();
            } catch (IOException ex) {
                //Error in copying the file
                return "Could not create new local file:\n\n" + ex.getMessage();    
            } finally {
                //Close the streams
                try {
                    if (loaclSream != null)
                        loaclSream.close();
                    if (remoteStream != null)
                        remoteStream.close();
                } catch (IOException ex) {
                    //Failed to close a stream
                    System.out.println(ex);
                }
            }
        } else {
            // The file was already in the local download directory
            return "File already exists";
        }
        return "Successfully added";
    }
}
