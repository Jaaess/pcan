package peak.can;

import peak.can.basic.TPCANMsg;
import peak.can.basic.PCANBasic;
import peak.can.basic.TPCANHandle;
import peak.can.basic.TPCANStatus;

/**
 * The CANSendThread class extends Thread class and is used to send CAN Messages.
 */
public class CANSendThread extends Thread 
{
    private PCANBasic pcanBasic;
    private ChannelItem item = null;
    private int SendInterval;
    private long times; 
    private TPCANMsg canMessage = null;
    private TPCANStatus ret;
    
    
    public CANSendThread(PCANBasic pcanbasic, ChannelItem item, TPCANMsg canMessage, int SendInterval, long times)
    {
        this.pcanBasic = pcanbasic;
        this.item = item;
        this.canMessage = canMessage;
        this.SendInterval=SendInterval;
        this.times=times;
    }  

    public int getSendInterval()
    {
        return SendInterval;
    }

    public void setSendInterval(int interval)
    {
        SendInterval = interval;
    }
    
    public long getSendTimes(){
        return times;
    }
    
    public void setSendTimes(long ts){
        times=ts;
    }

    protected void finalize() throws Throwable
    {
      // Free local variables
       canMessage = null;
     }    
    
    /**
     * Starts thread process
     */
    public void run()
    {
        while (times>0)
        {
            synchronized (item) {     
                if ((item !=null) && (item.getWorking()))
	// Call the PCANBasic Send Function
	callAPIFunctionSend(item.getHandle());
            }
            // Sleep Time
            try
            {
                Thread.sleep(SendInterval);
            }
            catch (InterruptedException e)
            {
                return;
            }
            times--;
        }
    }

    /**
     * Calls the PCANBasic Send Function 
     *
     * @param handle The handle of a PCAN Channel
     */
    public void callAPIFunctionSend(TPCANHandle handle)
    {
        try
        {
            // We execute the "Write" function of the PCANBasic
            ret = pcanBasic.Write(handle, canMessage);
            //Process result
            if (ret == TPCANStatus.PCAN_ERROR_OK)
            {
              //Critical Area
              synchronized (MainJFrame.token)
              {
                //Put Message In the dataRowCollection
              }
            }
        }
        catch (Exception e)
        {
            System.out.println("CANSendThread Exception:" + e.getMessage());
            e.printStackTrace();
            System.exit(0);
        }
    }
}
