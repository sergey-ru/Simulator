/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bgu.sim.actions.suspectibleInfected;

import bgu.sim.data.NetDevice;
import bgu.sim.core.stat.StatisticCollector;
import bgu.sim.data.Message;
import bgu.sim.ruleEngine.action.AbsDeviceAction;
import bgu.sim.ruleEngine.action.IndicativeFeature;
import java.util.ArrayList;

/**
 *
 * @author Keren
 */
public class ScrabingCenterAction extends AbsDeviceAction {

    public ScrabingCenterAction() {
        super("This action removes virus from infected device if the device has Scrabing Center.", new IndicativeFeature[]{IndicativeFeature.FILE_READ, IndicativeFeature.FILE_WRITE});
    }

    @Override
    public Integer execute(NetDevice input) {
        int count = 0;
        ArrayList<Message> listOfMessages = new ArrayList<>();
        
        if (input.getInfo().containsKey("ScrabingCenter"))  {
            if ((int) input.getInfo().get("ScrabingCenter") == 1) {
                
                // poll out all messages for checking its type
                Message message = input.getMessageQueue().Poll();
                while(message!=null) {
                    listOfMessages.add(message);
                    message = input.getMessageQueue().Poll();
                }
                
                // insert back all the Legitimate messages
                for (Message message1 : listOfMessages) {
                    if (message1.getType()==Message.MessageType.Legitimate)
                        input.getMessageQueue().Push(message1);
                }
                
                count++;
            }
        }
        StatisticCollector.getInstance().increaseValue(this.getName(), count);
        return count;
    }
}
