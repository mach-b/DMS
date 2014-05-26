package share;

import Message.Message;

/**
* Hold the file names and ip addresses in a convenient class to be passed 
* to other processes
* 
* @author Kerry Powell
* @version 1.0
*/
public class RemoteFiles {

    private String ip = "";
    private String[] array = new String[0];

    /**
     * Create an entry, this will populate the array ready for retrieval
     * 
     * @param fileNames list of file names available
     */
    public RemoteFiles(String[] fileNames) {
        
        this.ip = Message.getIPString();
        this.array = fileNames;
    }
    
    public String getIp() {
        return ip;
    }
    
    public String[] getArray() {
        return array;
    }
}
