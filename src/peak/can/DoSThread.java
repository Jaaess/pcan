/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package peak.can;

import java.util.logging.Level;
import java.util.logging.Logger;
import peak.can.basic.PCANBasic;
import peak.can.basic.TPCANMsg;

/**
 *
 * @author Administrator
 */
public class DoSThread extends Thread {
    TPCANMsg msg=null;
    ChannelItem item = null;
    PCANBasic pcanBasic=null;
    public volatile boolean exit = false; 
            
    public DoSThread(TPCANMsg msg,ChannelItem item,PCANBasic pcanBasic){
        this.msg = msg;
        this.item=item;
        this.pcanBasic = pcanBasic;
    }
    protected void finalize() throws Throwable{}
    public void run(){
        while (!exit){
            synchronized(item){
                if ((item != null) && item.getWorking())
                    pcanBasic.Write(item.getHandle(),msg);
            }
            try {
                Thread.sleep(0);
            } catch (InterruptedException ex) {
                Logger.getLogger(DoSThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        System.out.println("jump");
    }
}
