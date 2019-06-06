/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Helpers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author yosef
 */
public class NetworkHelper {

    private String IP;
    private final String SERVER_NAME="BINGO Server";
    private int Port;
    private final String CONFIG_URI = "NetworkConfig.bingo";
    private String Status;
    //private ServerSocket serverSocket;
    private Thread clientThread;
    private boolean isRunning=true;
    private ArrayList<PrintWriter> clienList;
    private ExecutorService executorService;
    public NetworkHelper() {
        clienList=new ArrayList<>();
        executorService=Executors.newCachedThreadPool();
        init();
        CreatServer();
    }

    private void init() {

        try {
            FileInputStream fis = new FileInputStream(new File(CONFIG_URI));
            try (ObjectInputStream ois = new ObjectInputStream(fis)) {
                NetworkConfig config=(NetworkConfig)ois.readObject();
                IP=config.getIP();
                Port=config.getPort();
            }

        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(NetworkHelper.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void CreatServer() {
        try {
            ServerSocket serverSocket=new ServerSocket(Port);
            System.out.println("Waiting for the client.");
            clientThread= new Thread(new ConnectionHandler(serverSocket));
            clientThread.start();
            
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        

    }
    
    private class ConnectionHandler implements Runnable{
        
        private ServerSocket ss;
        
        public ConnectionHandler(ServerSocket serverSocket){
            ss=serverSocket;
            Status="Running";
        }
        @Override
        public void run() {
            try {
                while(isRunning){
                    System.out.println("Waiting for client.");
                    Socket client=ss.accept();
                    System.out.print("New connection");
                    PrintWriter writer = new PrintWriter(client.getOutputStream());
                        
                    clienList.add(writer);
                    executorService.execute(new ClientHandler(client,writer));
                    
                }
                executorService.shutdown();
                
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
            System.out.println("Good-bye.");
        }
        
        
    
    } 
    
    private class ClientHandler implements Runnable{
        
        private Socket client;
        private BufferedReader reader;
        
        public ClientHandler(Socket s,PrintWriter writer){
            System.out.println(" @ "+s.getPort());
            writer.write("dddddddddd");
            writer.flush();
            client=s;
            try {
                InputStreamReader r = new InputStreamReader(client.getInputStream());
                reader=new BufferedReader(r);
            } catch (IOException ex) {
                Logger.getLogger(NetworkHelper.class.getName()).log(Level.SEVERE, null, ex);
            }
           
        }
        
        @Override
        public void run() {
            try {
                String msg;
                while((msg = reader.readLine()) != null){
                    System.out.println(msg+" --- ");
                }
            } catch (IOException ex) {
                System.err.println(ex.getMessage());
            }
        }
    
    
    }
    
    public void AnounceBallNumber(String ballNumber){
        System.out.println(ballNumber+"--");
//        Iterator<PrintWriter> itr=clienList.iterator();
//        while(itr.hasNext()){
//                PrintWriter pw = (PrintWriter)itr.next();
//                pw.write(ballNumber);
//                pw.flush();
//            
//        }
    }
    
    public String getStatus(){
        return Status;
    }
    
    public String getIP(){
        return IP;
    }
    
    public String getName(){
        return SERVER_NAME;
    }
    
    public int getPort(){
        return Port;
    }

    public void KillProcess(){
        if(clientThread != null){
            clientThread.interrupt();
        }
    }
}
