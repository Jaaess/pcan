package peak.can;

import peak.can.basic.TPCANMessageType;
import peak.can.basic.TPCANMsg;
import peak.can.basic.TPCANTimestamp;

/**
 * The TableDataRow class is a structure to store all provided info by a CAN Message
 *
 * @version 1.10
 * @LastChange $Date: 2016-05-13 14:54:19 +0200 (ven. 13 mai 2016) $
 * @author Jonathan Urban/Uwe Wilhelm/Fabrice Vergnaud
 *
 * @Copyright (C) 1999-2014  PEAK-System Technik GmbH, Darmstadt
 * more Info at http://www.peak-system.com
 */
public class TableDataRow
{

    //Private fields
    private TPCANTimestamp rcvTime;
    private int counter;
    private TPCANMsg message;

    /**
     * Gets number of times the CAN Message was readed
     * @return number of times
     */
    public int getCounter()
    {
        return counter;
    }

    /**
     * Sets number of times the CAN Message was readed
     * @param counter number of times the CAN Message was readed
     */
    public void setCounter(int counter)
    {
        this.counter = counter;
    }

    /**
     * Sets wrapped TPCANMsg
     * @param message wrapped TPCANMsg
     */
    public void setMessage(TPCANMsg message)
    {
        this.message = message;
    }
    
    
    /**
     * Sets wrapped TPCANTimestamp
     * @param rcvTime wrapped TPCANTimestamp
     */
    public void setRcvTime(TPCANTimestamp rcvTime)
    {
        this.rcvTime = rcvTime;
    }
    
    
    
    public String getMsgType() {
        String result;
        
        result = "";
        if (message != null) {
            if (message.getType() == TPCANMessageType.PCAN_MESSAGE_STANDARD.getValue())
                result = "Standard";
            else if (message.getType() == TPCANMessageType.PCAN_MESSAGE_EXTENDED.getValue())
                result = "Extended";
            else
                result = Byte.toString(message.getType());
        }
        return result;
    }
    
//    public void setMsgType(String type){
//        if(type == "Standard"){
//            this.
//        }
//    }
    
    public int getMsgLength() {
        if (message != null) {
            return message.getLength();
        }
        return 0;
    }
    
    public int getMsgId() {        
        if (message != null) {
            return message.getID();
        }
        return 0;
    }
    
    public byte[] getMsgData() {        
        if (message != null) {
            return message.getData();
        }
        return null;
    }
    
    public String getRcvTimeAsString() {        
        if (rcvTime != null) {
            return String.valueOf(rcvTime.getMillis()) + "." + String.valueOf(rcvTime.getMicros());
        }
        return null;
    }
}
