/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bingoclient;

import com.jfoenix.controls.JFXToggleNode;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

/**
 * FXML Controller class
 *
 * @author yosef
 */
public class HomeController implements Initializable {

    @FXML
    private Label DisplayLabel;

    private Socket sok;
    private PrintWriter writer;
    //private BufferedReader reader;
    private ExecutorService executorService;
    @FXML
    private GridPane BingoGrid;
    private JFXToggleNode[] lists;
    BingoCardItem[][] items;
    private boolean isPlaying = true;
    private ArrayList<String> B, I, N, G, O;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        B = new ArrayList<>();
        I = new ArrayList<>();
        N = new ArrayList<>();
        G = new ArrayList<>();
        O = new ArrayList<>();
        init_card_lables();
        init_card();
        try {
            sok = new Socket("127.0.0.1", 1789);
            InputStream is = sok.getInputStream();
            InputStreamReader r = new InputStreamReader(is);
            BufferedReader reader = new BufferedReader(r);
            writer = new PrintWriter(sok.getOutputStream());
            executorService = Executors.newCachedThreadPool();
            executorService.execute(new IncomingMessageListner(reader));

            executorService.shutdown();
        } catch (IOException ex) {
            System.out.println("Failed to connect");
        }
    }

    private void init_card() {
        items = new BingoCardItem[5][5];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                SecureRandom random = new SecureRandom();
                String s = null;
                switch (j) {
                    case 0: {
                        int selectedIndex = random.nextInt(B.size());
                        s = B.get(selectedIndex);
                        B.remove(selectedIndex);
                        break;
                    }
                    case 1: {
                        int selectedIndex = random.nextInt(I.size());
                        s = I.get(selectedIndex);
                        I.remove(selectedIndex);
                        break;
                    }
                    case 2: {
                        int selectedIndex = random.nextInt(N.size());
                        s = N.get(selectedIndex);
                        N.remove(selectedIndex);
                        break;
                    }
                    case 3: {
                        int selectedIndex = random.nextInt(G.size());
                        s = G.get(selectedIndex);
                        G.remove(selectedIndex);
                        break;
                    }
                    case 4: {
                        int selectedIndex = random.nextInt(O.size());
                        s = O.get(selectedIndex);
                        O.remove(selectedIndex);
                        break;
                    }
                    default:

                        break;
                }

                BingoCardItem cardItem = new BingoCardItem(s);
                BingoGrid.add(cardItem, j, i);
                items[i][j] = cardItem;
            }
        }
    }

    private void init_card_lables() {
        for (int i = 1; i <= 15; i++) {
            B.add("B-" + i);
            I.add("I-" + (15 + i));
            N.add("N-" + (30 + i));
            G.add("G-" + (45 + i));
            O.add("O-" + (60 + i));
        }
//        for(int i=0;i<15;i++){
//            System.out.print(B.get(i)+"  ");
//            System.out.print(I.get(i)+"  ");
//            System.out.print(N.get(i)+"  ");
//            System.out.print(G.get(i)+"  ");
//            System.out.print(O.get(i)+"  ");
//            System.out.println("");
//        }

    }

    private class BingoCardItem extends JFXToggleNode {

        public BingoCardItem(String ItemLable) {
            setText(ItemLable);
            setPrefWidth(100);
            setPrefHeight(100);
            setStyle("-fx-font-weight:bold;-fx-font-size:18px");
            addEventHandler(MouseEvent.MOUSE_CLICKED, (event) -> {
                if (DisplayLabel.getText().endsWith(getText())) {
                    setSelected(Boolean.TRUE);
                    boolean isWin = checkWin();
                    if (!isWin) {
                        System.out.println("no win");
                    } else {
                        writer.println("Win:yosef");
                        writer.flush();
                    }

                } else {
                    setSelected(Boolean.FALSE);
                    System.err.println("Invalid move!");
                }

            });
        }

    }

    private boolean checkWin() {
        //check horizontal win
        int Rowcounter = 0;
        for (int i = 0; i < 5; i++) {
            if (Rowcounter == 5) {
                System.out.println("Bingo : horizontal @ row " + (i - 1));
                return true;
            }
            Rowcounter = 0;
            for (int j = 0; j < 5; j++) {
                if (!items[i][j].isSelected()) {
                    break;
                }
                Rowcounter++;
            }
        }

        //check vertical win
        int Columncounter = 0;
        for (int i = 0; i < 5; i++) {
            if (Columncounter == 5) {
                System.out.println("Bingo : vertical @ column " + (i - 1));
                return true;
            }
            Columncounter = 0;
            for (int j = 0; j < 5; j++) {
                if (!items[j][i].isSelected()) {
                    break;
                }
                Columncounter++;
            }
        }

        //check diagonal left win
        int DiagonalLeftCounter = 0;
        for (int i = 0; i < 5; i++) {
            if (!items[i][i].isSelected()) {
                break;
            }
            ++DiagonalLeftCounter;

            if (DiagonalLeftCounter == 5) {
                System.out.println("Bingo : Diagonal left ");
                return true;
            }

        }

        //check diagonal right win
        int DiagonalRightCounter = 0;
        int row = 0;
        for (int i = 4; i >= 0; i--) {
            if (!items[row][i].isSelected()) {
                break;
            }
            ++row;
            ++DiagonalRightCounter;

            if (DiagonalRightCounter == 5) {
                System.out.println("Bingo : Diagonal right ");
                return true;
            }
            // System.out.println(""+DiagonalRightCounter);
        }

        return false;
    }

    class IncomingMessageListner implements Runnable {

        BufferedReader reader;

        public IncomingMessageListner(BufferedReader br) {
            this.reader = br;
        }

        @Override
        public void run() {
            try {
                String msg;
                while ((msg = reader.readLine()) != null) {
                    String[] data = msg.split(":");
                    if (data[1].equals("WIN")) {
                        DisplayLabel.setText("WIN");
                        isPlaying = false;
                    } else {
                        if (isPlaying) {
                            final String ballNum = data[1];
                            Platform.runLater(() -> {
                                DisplayLabel.setText(ballNum);
                            });
                        }

                    }

                }
            } catch (IOException ex) {
                Logger.getLogger(HomeController.class.getName()).
                        log(Level.SEVERE, null, ex);
            }

        }

    }
}
