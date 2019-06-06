/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Helpers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author yosef
 */
public final class DatabaseHelper {
    private static final String DB_URL="jdbc:mysql://localhost/Bingo";
    private static final String USERNAME="root";
    private static final String PASSWORD="0913712463";
    private static Connection connection;
    private static Statement statement;
    private static int UID;
    private static String uname;
    
    public static void init(){
        try {
            if(connection == null){
                connection=DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
                statement=connection.createStatement();
                System.out.println("Connected");
            }else{
                System.out.println("already Connected");
            }
            
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }
    
    public static String login(String uname,String pass){
        String Login_query="SELECT * FROM Users WHERE Username='"+
                uname+"' AND Password ='"+pass+"'";
        try {
            ResultSet rs=statement.executeQuery(Login_query);
            if(rs.next()){
                UID=rs.getInt("Uid");
                uname=rs.getString("Username");
                return uname;
            }
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
        
        return null;
        
    }
    
    public static String getName(){
        return uname;
    }
    
    public static int getUid(){
        return UID;
    }
    
    
}
