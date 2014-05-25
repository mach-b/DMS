package share;

/**
* Hold the file names and ip addresses in a convenient class to be passed 
* to other processes
* 
* @author Kerry Powell
* @version 1.0
*/
public class RemoteFiles extends Thread{

    private String ip = "";
    private String[] fileNames = new String[0];
    private String[][] array = new String[0][0];

    /**
     * Create an entry, this will populate the array ready for retrieval
     * 
     * @param ip of the remote process
     * @param fileNames list of file names available
     */
    public RemoteFiles(String ip, String[] fileNames) {
        
        this.ip = ip;
        this.fileNames = fileNames;
        this.start();
    }
    
    @Override
    public void run() {
        array = new String[fileNames.length][2];
        for (int i = 0; i < fileNames.length; i++) {
            array[i][0] = ip;
            array[i][1] = fileNames[i];
        }
    }
    
    public String getIp() {
        return ip;
    }
    
    public String[][] getArray() {
        return array;
    }
}
