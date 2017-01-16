/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package peak.can;

import java.util.HashMap;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import peak.can.basic.IRcvEventProcessor;
import peak.can.basic.PCANBasic;
import peak.can.basic.TPCANHandle;
import peak.can.basic.TPCANMsg;
import peak.can.basic.TPCANStatus;
import peak.can.basic.TPCANTimestamp;

/**
 *
 * @author Aragorn
 */
public class CANReceiveThread extends Thread implements IRcvEventProcessor{
    
    // PCANBasic instance used to call read functions
    private PCANBasic pcanBasic;
    // Collection which stores all connected channels
    private ChannelItem item = null;
    private JTable table;
    // Used to read CAN Messages with its Time stamp
    private Boolean readTimeStamp = false;

    /**
     * @return states if timestamp is used when reading CAN messages
     */
    public Boolean getReadTimeStamp()
    {
        return readTimeStamp;
    }
    /**
     * @param useReadEx states if timestamp must be used when reading CAN messages
     */
    public void setReadTimeStamp(Boolean useReadEx)
    {
        this.readTimeStamp = useReadEx;
    }

    /**
     *
     * @param pcanbasic PCANBasic instance used to call read functions
     * @param item Reference to the connected channels
     * @param dataRowCollection Reference to the Collection which store readed CAN Messages
     */
    public CANReceiveThread(PCANBasic pcanbasic, ChannelItem item, JTable table)//, HashMap<Integer, TableDataRow> dataRowCollection)
    {
        this.pcanBasic = pcanbasic;
        this.item = item;
        this.table = table;
    }

    /**
     * Starts thread process
     */
    public void run()
    {        
        while (true)
        {
            synchronized(item){
                if ((item != null) && (item.getWorking())) {
                    callAPIFunctionRead(item.getHandle());
                }
            }
            // Sleep Time
            try
            {
                Thread.sleep(5);
            }
            catch (InterruptedException e)
            {
                return;
            }
        }
    }

    /**
     * Calls the PCANBasic Read Function according the readTimeStamp parameter
     *
     * @param handle The handle of a PCAN Channel
     */
    public void callAPIFunctionRead(TPCANHandle handle)
    {
        //Local variables
        TPCANMsg canMessage = null;
        TPCANTimestamp rcvTime = null;
        TPCANStatus ret;

        //Variables
        String msgIDStr = "";
        String msgLength = "";
        String msgType = "";
        String msgData = "";
        String blockData = "";
        String msgCount = "";
        String msgRcvTime = "";
        Object[] msgTableObect = null;

        //Retrieve JTable Model
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        
        try
        {
            do
            {
                // Create new CAN Message
                canMessage = new TPCANMsg();
                //If TimeStamp is needed
                if (readTimeStamp){
                    // Create new TimeStamp object
                    rcvTime = new TPCANTimestamp();
                    // We execute the "Read" function of the PCANBasic
                    ret = pcanBasic.Read(handle, canMessage, rcvTime);
                }
                //If TimeStamp is not needed
                else
                    // We execute the "Read" function of the PCANBasic
                    ret = pcanBasic.Read(handle, canMessage, null);

                //Process result
                if (ret == TPCANStatus.PCAN_ERROR_OK)
                {
                    
                    //Critical Area: dataRowCollection is used in multiple threads
                    synchronized (MainJFrame.token)
                    {
                        msgType = String.valueOf(canMessage.getType());
                        msgLength = String.valueOf(canMessage.getLength());
                        msgIDStr = Integer.toHexString(canMessage.getID());
                        //Message Data       
                        for (int dataIndex = 0; dataIndex < canMessage.getLength(); dataIndex++)
                        {
                            msgData = msgData + canMessage.getData()[dataIndex]+" ";
                        }
                        //Message Count
                        msgCount = String.valueOf(1);               
                        msgRcvTime = (rcvTime!=null)? String.valueOf(rcvTime.getMillis()) + "." + String.valueOf(rcvTime.getMicros()) :null;

                        //Construct JTable Object
                        msgTableObect = new Object[]{ msgType, msgIDStr, msgLength, msgData, msgCount, msgRcvTime};
                        model.addRow(msgTableObect);
                    }
                 }
            }while(ret!= TPCANStatus.PCAN_ERROR_QRCVEMPTY || ret== TPCANStatus.PCAN_ERROR_OK );
            // Free local variables
            canMessage = null;
            rcvTime = null;
        }
        catch (Exception e)
        {
            System.out.println("CANReadThread Exception:" + e.getMessage());
            e.printStackTrace();
            System.exit(0);
        }
    }

    // This function is called by the JNI library when a CAN Receive-Event is detected
    public void processRcvEvent(TPCANHandle channel)
    {
            if (item.getHandle() == channel)
            {           
                // Process a PCANBasic read call
                callAPIFunctionRead(channel);
                return;
            }
    }
}
