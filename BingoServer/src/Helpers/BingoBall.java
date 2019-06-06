/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Helpers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author yosef
 */
public class BingoBall {

    private char name;
    private int Value;
    private boolean isBallReady = false;
    private char[] BINGO = {'B', 'I', 'N', 'G', 'O'};
    private SecureRandom randomTitle, randomValue;
    private HashMap<Character, Integer> map;
    private ArrayList<String> lol;
    private String SelectedBall;
    public BingoBall() {
        map=new HashMap<>();
        lol=new ArrayList<>();
        loadBalls();
    }

    synchronized public int PickBall() {
        while (isBallReady) {
            try {
                wait();
            }catch (InterruptedException ex) {
                Logger.getLogger(BingoBall.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        //PickRandomBall();
        PickRandomBall2();
        isBallReady=true;
        notify();
        if(SelectedBall.equals("OVER")){
            return -1;
        }
        return 0;
        
    }

    synchronized public String displayBall() {
        while(!isBallReady){
            try {
                wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(BingoBall.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        //System.out.println(name+"-"+Value);
        isBallReady=false;
        notify();
        return SelectedBall;
    }

    private void PickRandomBall() {
        
            
            //creat a random number with seed of current time
            
            randomTitle = new SecureRandom();

            //generate random number from 0-5
            int val = randomTitle.nextInt(5);

            //pick character from BINGO array based on our val
            char Tiltle = BINGO[val];

            //generate ball number based on the selected BINGO Char
            switch (Tiltle) {
                case 'B':
                    generateNumber(Tiltle, 1, 15);
                    break;
                case 'I':
                    generateNumber(Tiltle, 16, 30);
                    break;
                case 'N':
                    generateNumber(Tiltle, 31, 45);
                    break;
                case 'G':
                    generateNumber(Tiltle, 46, 60);
                    break;
                case '0':
                    generateNumber(Tiltle, 61, 75);
                    break;

            }

        

    }
    
    synchronized private void generateNumber(char Tiltle, int low, int high) {
            randomValue=new SecureRandom();
            int BallNumber=randomValue.nextInt(high);
            
            if(BallNumber < low){
                BallNumber += low;
            }
            //System.out.println(Tiltle+" - "+BallNumber+" )");
            final String TextToDisplay=Tiltle+"-"+BallNumber;
            Value=BallNumber;
            name=Tiltle;
            if(lol.contains(TextToDisplay)){
                
            }else{
                lol.add(TextToDisplay);
            //System.out.println("Picked "+name+"-"+Value+" SIZE="+lol.size());
            
            
            }
            
            
        }

    private void loadBalls() {
        
        try (FileReader fileReader = new FileReader(new File("lol.txt"));
                BufferedReader reader=new BufferedReader(fileReader)){
            
            String Line;
            while((Line = reader.readLine()) != null){
                lol.add(Line);
                //System.out.println(Line);
            }
            reader.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(BingoBall.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(BingoBall.class.getName()).log(Level.SEVERE, null, ex);
        } 

    }

    private void PickRandomBall2() {
        SecureRandom random=new SecureRandom();
        int currentSize=lol.size();
        if(currentSize == 0){
            SelectedBall="OVER";
            return;
        }
        int index=random.nextInt(lol.size());
        SelectedBall=lol.get(index);
        //System.out.print(SelectedBall);
        lol.remove(index);
       // System.out.println(" Current Size = "+lol.size());
    }

}


   
