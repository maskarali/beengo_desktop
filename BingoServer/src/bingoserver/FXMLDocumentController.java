/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bingoserver;

import Helpers.NetworkConfig;
import Helpers.DatabaseHelper;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

/**
 *
 * @author yosef
 */
public class FXMLDocumentController implements Initializable {

    @FXML
    private JFXTextField Username;
    @FXML
    private JFXPasswordField Password;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        DatabaseHelper.init();
        if(!isConfigured()){
            CreatConfiguration();
        }else{
            showNotification("lala", "sss", "INFORMATION");
        }
        
        
    }    

    @FXML
    private void SignIn(ActionEvent event) {
        String uname=Username.getText().trim(); //get username value
        String pass=Password.getText().trim();  //get password value
        
        //check if the username and password isn't empty
        if(uname.equals("") || pass.equals("")){
            if(uname.equals("")){
                showNotification("Please Enter your Username", "Empty Username", "ERROR");
            }else{
                showNotification("Please Enter your Password", "Empty Password", "ERROR");
            }
            
        }else{
            String result=DatabaseHelper.login(uname, pass);
            if(result == null){
                System.err.println("Eroor");
            }else{
                System.err.println(result);
                Home home = new Home();
                BingoServer.s.close();
            }
        }
    }
    
    private boolean isConfigured(){
        Path path = FileSystems.getDefault().getPath("NetworkConfig.bingo");
        return Files.exists(path);
    }
    
    private void showNotification(String Message, String Title, String Type) {
        Notifications notification = Notifications.create().
                title(Title).
                text(Message).
                graphic(new Label("kkkkkkkk")).
                hideAfter(Duration.seconds(5)).
                position(Pos.TOP_RIGHT).
                darkStyle();
        switch (Type) {
            case "ERROR":
                notification.showError();
                break;
            case "CONFIRM":
                notification.showConfirm();
                break;
            case "WARNING":
                notification.showWarning();
                break;
            case "INFORMATION":
                //notification.showInformation();
                
                break;
            default:
                notification.show();
                break;
        }

    }

    private void CreatConfiguration() {
        //File file=new File("config/NetworkConfig.bingo");
        try {
            FileOutputStream fos=new FileOutputStream("NetworkConfig.bingo");
            try (ObjectOutputStream oos = new ObjectOutputStream(fos)) {
                oos.writeObject(new NetworkConfig());
            }
                    
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}
