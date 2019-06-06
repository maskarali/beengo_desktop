/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bingoserver;

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
    private static Stage s;
    
    public Home(){
        try {
            Parent root=FXMLLoader.load(getClass().getResource("Home.fxml"));
            Scene scene=new Scene(root);
            s=new Stage();
            s.setScene(scene);
            s.resizableProperty().set(false);
            s.setOnCloseRequest((event) -> {
               event.consume();
               System.exit(0);
            });
            s.setTitle("Bingo Game V.1.0");
            s.show();
        } catch (IOException ex) {
            Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
}
