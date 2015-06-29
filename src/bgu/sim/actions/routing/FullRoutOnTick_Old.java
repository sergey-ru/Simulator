/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bgu.sim.actions.routing;

import bgu.sim.Properties.StringsProperties;
import bgu.sim.core.Router;
import bgu.sim.core.stat.StatisticCollector;
import bgu.sim.data.Message;
import bgu.sim.data.NetDevice;
import bgu.sim.data.NetLink;
import bgu.sim.netFile.SimulatedEnvironment;
import bgu.sim.ruleEngine.action.AbsDeviceAction;
import bgu.sim.ruleEngine.action.IndicativeFeature;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Sergey
 */
public class FullRoutOnTick_Old extends AbsDeviceAction {

    private Router router;
    private SimulatedEnvironment env;//= SimulatedEnvironment.getInstance();

    public FullRoutOnTick_Old() {
        super("The action moves the messages from source throw devices and links to destination on one tick", new IndicativeFeature[]{IndicativeFeature.NET_TRAFIC_OUT, IndicativeFeature.NET_TRAFIC_IN});
    }

    @Override
    public Integer execute(NetDevice input) {
        if (input.getMessageQueue().HasOutMessages()) {
            StatisticCollector.getInstance().increaseValue(this.getName(), 1);
            Iterator iter = input.getMessageQueue().getQueueIterator();
            while (iter.hasNext()) {
                StatisticCollector.getInstance().addMessage((Message) iter.next());
            }
            executeDevice(input);
            return 1;
        } else {
            return 0;
        }

    }

    private void executeDevice(NetDevice input) {
        env = SimulatedEnvironment.getInstance();
        router = Router.getInstance();
        try {
            int nextID;
            Message m;
            //get all messages of the input device 
            while (input.getMessageQueue().HasOutMessages()) {
                m = input.getMessageQueue().PollOutMessage();
                input.getInfo().IncreaseInt(StringsProperties.STATISTIC_DEVICE_OUT_MESSAGES, 1);
                m.setSource(input.getObjectId());
                nextID = router.getNextStep(input.getObjectId(), m.getDestination());
                //get all links of the device
                for (Integer linkID : input.getLinks()) {
                    //if second node of link is next destination put the message on the link
                    if ((env.getLink(linkID).getSourceID()== nextID)
                            || (env.getLink(linkID).getDestinationID()== nextID)) {
                        env.getLink(linkID).getMessageQueue().Push(m);
                        env.getLink(linkID).getInfo().IncreaseInt(StringsProperties.STATISTIC_LINK_MESSAGES, 1);
                        executeLink(env.getLink(linkID));
                        break;
                    }
                }
            }
        } catch (IOException | SQLException ex) {
            Logger.getLogger(FullRoutOnTick_Old.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void executeLink(NetLink input) {
        Message m;
        //move all messages on the link to one of it's sides 
        while (input.getMessageQueue().hasMessages()) {
            m = input.getMessageQueue().Poll();
            NetDevice dev;
            if (m.getSource() == input.getSourceID()) {
                dev = env.getNode(input.getDestinationID());
                dev.getMessageQueue().PushAndRoute(m);
                dev.getInfo().IncreaseInt(StringsProperties.STATISTIC_DEVICE_IN_MESSAGES, 1);
                executeDevice(dev);
            } else {
                dev = env.getNode(input.getDestinationID());
                dev.getMessageQueue().PushAndRoute(m);
                dev.getInfo().IncreaseInt(StringsProperties.STATISTIC_DEVICE_IN_MESSAGES, 1);
                executeDevice(dev);
            }
        }
    }

}
