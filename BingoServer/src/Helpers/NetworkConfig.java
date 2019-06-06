/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Helpers;

import java.io.Serializable;

/**
 *
 * @author yosef
 */
public final class NetworkConfig implements Serializable{
    
    private String IP;
    private int Port;
    
    public NetworkConfig(){
        setPort(1789);
        setIP("127.0.0.1");
    }

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public int getPort() {
        return Port;
    }

    public void setPort(int Port) {
        this.Port = Port;
    }
    
    
}
