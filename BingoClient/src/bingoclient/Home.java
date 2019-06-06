/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bingoclient;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author yosef
 */
public class Home {
    
    public Stage s;
    private Parent root;
    public Home() {
        
        s=new Stage();
        try {
            root=FXMLLoader.load(getClass().getResource("Home.fxml"));
        } catch (IOException ex) {
            Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Scene scene=new Scene(root);
        s.setScene(scene);
        s.resizableProperty().set(Boolean.FALSE);
        s.setOnCloseRequest((event) -> {
            event.consume();
            System.exit(0);
        });
        s.show();
        
    }
  
    
}
