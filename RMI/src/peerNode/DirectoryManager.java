/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package peerNode;

import java.io.File;

/**
 *
 * @author markburton
 */
public class DirectoryManager {
    
    /**
     *
     */ 
    public void createDefaultDirectory() {
        File dir = new File("RMIFileExchangeFolder");
        dir.mkdir();
        
    }
    
  
    
}
