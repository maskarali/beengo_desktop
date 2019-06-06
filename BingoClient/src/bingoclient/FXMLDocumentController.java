/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bingoclient;

import Helpers.DatabaseHelper;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
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
    
    private Label label;
    @FXML
    private JFXTextField Username;
    @FXML
    private JFXPasswordField Password;
    
    private void handleButtonAction(ActionEvent event) {
        System.out.println("You clicked me!");
        label.setText("Hello World!");
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        DatabaseHelper.init();
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
                //System.out.println(result);
                Home home=new Home();
                BingoClient.s.close();
            }
        }
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
    
}
