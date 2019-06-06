/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bingoserver;

import Helpers.BingoBall;
import Helpers.NetworkHelper;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author yosef
 */
public class HomeController implements Initializable {

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    private NetworkHelper helper;
    @FXML
    private Label serverName;
    @FXML
    private Label serverIP;
    @FXML
    private Label serverPort;
    @FXML
    private Label serverStatus;
    @FXML
    private Label DisplayLabel;
    
    private ExecutorService executorService;
    
    private ArrayList<PrintWriter> clients;
    @FXML
    private VBox OnlineUsers;
    
    private boolean isPlaying=true;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        clients=new ArrayList<>();
        executorService=Executors.newCachedThreadPool();
        executorService.execute(new networkHandler());
        
        
    }

    @FXML
    private void StartGame(ActionEvent event) {
        BingoBall ball = new BingoBall();
        NumberPicker picker = new NumberPicker(ball);
        NumberDisplaer displaer = new NumberDisplaer(ball);
       
        executorService.execute(picker);
        executorService.execute(displaer);
        executorService.shutdown();
        
    }
    
    class networkHandler implements Runnable{
        
        ServerSocket serverSocket;
        private final int PORT=1789;
        
        public networkHandler() {
            
            try {
                serverSocket=new ServerSocket(PORT);
            } catch (IOException ex) {
                Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
        
        
        @Override
        public void run() {
            while(true){
                try {
                    System.out.println("Waiting for client");
                    Socket s=serverSocket.accept();
                   
                    Platform.runLater(() -> {
                        addUser();
                    });
                    
                    PrintWriter writer = new PrintWriter(s.getOutputStream());
                    clients.add(writer);
                    executorService.execute(new ClientMessageHandler(s));
//                    writer.println("hello from server");
//                    writer.flush();
                    
                } catch (IOException ex) {
                    Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        
        
    }
    
    private class ClientMessageHandler implements Runnable{

        private Socket s;
        private BufferedReader reader;
        public ClientMessageHandler(Socket socket) {
            
            this.s=socket;
            System.out.println("new client @ port : "+s.getLocalPort());
            try {
                reader=new BufferedReader(new InputStreamReader(s.getInputStream()));
            } catch (IOException ex) {
                Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        
        @Override
        public void run() {
            try {
                String line;
                while ((line=reader.readLine()) != null) {
                    String [] datas=line.split(":");
                    System.out.println(line);
                    isPlaying=false;
                    
                    AnounceWinner(datas[1]);
                    
                }
            } catch (IOException ex) {
                Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        
    }
    
    private void addUser() {
         HBox box=new HBox(20);
//         FontAwesomeIcon awesomeIcon=new FontAwesomeIcon();
//         awesomeIcon.setGlyphName("CLOSE");
//         awesomeIcon.setSize("20");
         Label name=new Label("User name");
         //name.getStylesheets().add("-fx-font-style: bold");
         box.getChildren().add(name);
         OnlineUsers.getChildren().add(box);

    }

    class NumberPicker implements Runnable {

        //private Thread t;
        BingoBall ball;

        public NumberPicker(BingoBall b) {
            //t = new Thread(this, "NumberPicker");
            this.ball = b;
           // t.start();
        }

        @Override
        public void run() {
            int i = 0;
            while (isPlaying) {
                try {
                    Thread.sleep(4000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
                }
                int result=ball.PickBall();
                if(result == -1)
                    break;
                
            }
            System.out.println("Done Picking ball");

        }

    }

    class NumberDisplaer implements Runnable {

        //private Thread t;
        BingoBall ball;

        public NumberDisplaer(BingoBall b) {

            //t = new Thread(this, "NumberPicker");
            this.ball = b;
            //t.start();
        }

        @Override
        public void run() {
            int i = 0;
            while (isPlaying) {
                try {
                    Thread.sleep(4000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
                }
                final String result=ball.displayBall();
                
                Platform.runLater(() -> {
                    DisplayLabel.setText(result);
                    AnounceBallNumber(result);
                    
                });
                if(result.equals("OVER")){
                    break;
                }
                
            }
             System.out.println("Done Displaying balls");


        }

    }
    public void AnounceBallNumber(String ballNumber){
       // System.out.println(ballNumber+"--");
        Iterator<PrintWriter> itr=clients.iterator();
        while(itr.hasNext()){
                PrintWriter pw = (PrintWriter)itr.next();
                pw.println("BALL:"+ballNumber);
                pw.flush();
            
        }
    }
    
    private void AnounceWinner(String name){
        Iterator<PrintWriter> itr=clients.iterator();
        while(itr.hasNext()){
                PrintWriter pw = (PrintWriter)itr.next();
                pw.println("WIN:"+name);
                pw.flush();
            
        }
    }

}
