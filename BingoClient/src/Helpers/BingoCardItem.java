/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Helpers;

import com.jfoenix.controls.JFXToggleNode;
import javafx.scene.paint.Color;

/**
 *
 * @author yosef
 */
public class BingoCardItem extends JFXToggleNode{
    
    private String itemLable;
    
    public BingoCardItem(String ItemLable){
        this.itemLable=ItemLable;
        setUnSelectedColor(Color.CADETBLUE);
        textFillProperty().set(Color.BLACK);
        setWidth(600);
        setHeight(100);
    }
    
    public String getItemLable(){
        return itemLable;
    }
    
 }
