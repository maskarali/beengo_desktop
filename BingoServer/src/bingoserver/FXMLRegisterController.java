/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bingoserver;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

/**
 *
 * @author yosef
 */
public class FXMLRegisterController implements Initializable{

    @FXML
    private JFXTextField Username;
    @FXML
    private JFXPasswordField Password;
    @FXML
    private JFXPasswordField Password1;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
         

    }

    @FXML
    private void SignIn(ActionEvent event) {
    }

    @FXML
    private void Register(ActionEvent event) {
    }
    
    
    
    
//    private void fadeEffect(Parent root){
//        FadeTransition transition=new FadeTransition();
//        transition.setDuration(Duration.millis(500));
//        transition.setNode(root);
//        transition.setFromValue(1);
//        transition.setToValue(0);
//        transition.setOnFinished((event) -> {
//            showScene();
//        });
//        transition.play();
//        
//    }
    
//    private void showScene(){
//        try {
//                Parent register=FXMLLoader.load(getClass().getResource("FXMLRegister.fxml"));
//                Scene scene=new Scene(register);
//                BingoServer.s.setScene(scene);
//            } catch (IOException ex) {
//                Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        
//    }
    
}
