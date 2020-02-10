/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package todolist;

import javafx.scene.control.ListView;

/**
 *
 * @author zpierucci
 */
public class List implements java.io.Serializable {
    
    private ListView entries;
            
    public List() {
        
    }
    
    public void setEntries(ListView gender) {
        this.entries = gender;
    }
    
    public ListView getEntries() {
        return entries;
    }
}
