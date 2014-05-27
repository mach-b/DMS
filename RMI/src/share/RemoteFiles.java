package share;

import java.io.Serializable;
import message.Message;

/**
* Hold the file names and ip addresses in a convenient class to be passed 
* to other processes
* 
* @author Kerry Powell
* @version 1.0
*/
public class RemoteFiles implements Serializable{

    private String id = "";
    private String[] array = new String[0];

    /**
     * Create an entry, this will populate the array ready for retrieval
     * 
     * @param fileNames list of file names available
     */
    public RemoteFiles(String[] fileNames) {
        
        this.id = Message.getMasterID();
        this.array = fileNames;
    }
    
    public String getID() {
        return id;
    }
    
    public String[] getArray() {
        return array;
    }
}
