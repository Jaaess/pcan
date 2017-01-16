/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package peak.can;

import peak.can.MainJFrame;
import peak.can.TableDataRow;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

/**
 *
 * @author Aragorn
 */
public class FileSavingThread extends Thread{
    
    // Collection to store readed CAN Messages
    private HashMap<Integer, TableDataRow> dataRowCollection;
    
    public FileSavingThread(HashMap<Integer, TableDataRow> dataRowCollection){
        this.dataRowCollection = dataRowCollection;
    }
    
    public void run(){
        TableDataRow dataRow = null;
        HashMap<Integer, TableDataRow> datatmp;
                    
        // make a copy of data (can be modified by external threads)
        synchronized (MainJFrame.token)
        {
            datatmp = new HashMap<Integer, TableDataRow>(dataRowCollection);
            try{
                File file = new File("data.txt");//测试成功后改用对话框方式
                if(!file.exists()){
                    file.createNewFile();
                }
                FileWriter fileWriter = new FileWriter(file.getName(),true);
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                
                for(Object item : datatmp.values()){
                    dataRow = (TableDataRow)item;
                    String Type = dataRow.getMsgType();
                    int ID = dataRow.getMsgId();
                    int Length = dataRow.getMsgLength();
                    byte data[] = new byte[Length];
                    data = dataRow.getMsgData();
                    int Count = dataRow.getCounter();
                    //String RcvTime = dataRow.getRcvTimeAsString();
                    
                    bufferedWriter.write(Type+"   ");
                    bufferedWriter.write(ID+"   ");
                    bufferedWriter.write(Length+"   ");
                    bufferedWriter.write(data.toString()+"   ");
                    bufferedWriter.write(Count+"    ");
                    //bufferedWriter.write(RcvTime+"   ");
                    bufferedWriter.write("\r\n");
                }
                bufferedWriter.close();
            }
            catch(IOException e){
                e.printStackTrace();
            }                      
        }
    }
}
