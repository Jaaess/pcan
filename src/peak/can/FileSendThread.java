/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package peak.can;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

/**
 *
 * @author Aragorn
 */
public class FileSendThread extends Thread{
    
    // Collection to store readed CAN Messages
    private HashMap<Integer, TableDataRow> dataRowCollection;
    
    public  FileSendThread(HashMap<Integer, TableDataRow> dataRowCollection){
        this.dataRowCollection = dataRowCollection;
    }
    
    public void run(){
        TableDataRow dataRow = null;
        HashMap<Integer, TableDataRow> datatmp;
        byte data[] = new byte[8];
        
        try {            
            Scanner scanner = new Scanner(new FileInputStream("E:\\data.txt"));
            int i = 0;
            while (scanner.hasNext()) {      
                dataRow = new TableDataRow();
                if(i % 12 == 0){
                    //类型
                    String Type = scanner.next();
                    
                }
                else if(i % 12 == 1){
                    //ID
                    int ID = scanner.nextInt();
                }
                else if(i % 12 == 2){
                    //Length
                    int Length = scanner.nextInt();
                }
                else if(i % 12 == 3){
                    //data有问题！！！！！！！！！！！！！
                    data[0] = scanner.nextByte();
                }
                else if(i % 12 == 4){
                    data[1] = scanner.nextByte();
                }
                else if(i % 12 == 5){
                    data[2] = scanner.nextByte();
                }
                else if(i % 12 == 6){
                    data[3] = scanner.nextByte();
                }
                else if(i % 12 == 7){
                    data[4] = scanner.nextByte();
                }
                else if(i % 12 == 8){
                    data[5] = scanner.nextByte();
                }
                else if(i % 12 == 9){
                    data[6] = scanner.nextByte();
                }
                else if(i % 12 == 10){
                    data[7] = scanner.nextByte();
                }
                else if(i % 12 == 11){
                    //Count
                    int Count = scanner.nextInt();
                }
                i++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
