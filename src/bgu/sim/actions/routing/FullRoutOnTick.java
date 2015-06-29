/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bgu.sim.actions.routing;

import bgu.sim.Properties.StringsProperties;
import bgu.sim.core.Router;
import bgu.sim.core.Simulator;
import bgu.sim.core.stat.StatisticCollector;
import bgu.sim.data.Message;
import bgu.sim.data.NetDevice;
import bgu.sim.data.NetLink;
import bgu.sim.netFile.SimulatedEnvironment;
import bgu.sim.ruleEngine.action.AbsDeviceAction;
import bgu.sim.ruleEngine.action.IndicativeFeature;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Sergey
 */
public class FullRoutOnTick extends AbsDeviceAction {

    private Router router;
    private SimulatedEnvironment env;

    public FullRoutOnTick() {
        super("The action moves the messages from source throw devices and links to destination on one tick", new IndicativeFeature[]{IndicativeFeature.NET_TRAFIC_OUT, IndicativeFeature.NET_TRAFIC_IN});
    }

    @Override
    public Integer execute(NetDevice input) {
        if (input.getMessageQueue().HasOutMessages()) {
            StatisticCollector.getInstance().increaseValue(this.getName(), 1);
            executeDevice(input);
            return 1;
        } else {
            return 0;
        }
    }

    private void executeDevice(NetDevice input) {
        // for one message, split it to many message depends on the path to the destination 
        env = SimulatedEnvironment.getInstance();
        router = Router.getInstance();
        try {
            int nextID;
            Message m;

            //get all messages of the input device 
            while (input.getMessageQueue().HasOutMessages()) {
            //while (input.getMessageQueue().HasOutMessages(Simulator.getInstance().getTick())) {

                // message
                //m = input.getMessageQueue().PollOutMessage(Simulator.getInstance().getTick());
                m = input.getMessageQueue().PollOutMessage();
                if (m != null) {
                    input.getInfo().IncreaseInt(StringsProperties.STATISTIC_DEVICE_OUT_MESSAGES, 1);

                    // next step
                    nextID = router.getNextStep(input.getObjectId(), m.getDestination());

                    // Update message array for GUI
                    if (m.getDestination() == nextID) {
                        insertMessageToStatisticCollector(m);
                    }

                    //get all links of the device
                    for (Integer linkID : input.getLinks()) {
                        //if second node of link is next destination put the message on the link
                        if ((env.getLink(linkID).getSourceID() == nextID)
                                || (env.getLink(linkID).getDestinationID() == nextID)) {
                            env.getLink(linkID).getMessageQueue().Push(m);
                            env.getLink(linkID).getInfo().IncreaseInt(StringsProperties.STATISTIC_LINK_MESSAGES, 1);
                            executeLink(env.getLink(linkID), nextID);
                            break;
                        }
                    }
                }
            }
        } catch (IOException | SQLException ex) {
            Logger.getLogger(OneStepOnTick.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void insertMessageToStatisticCollector(Message m) throws NumberFormatException {
        // add message for js GUI
        StatisticCollector.getInstance().addMessage(m);
    }

    private void executeLink(NetLink input, int nextStepId) {
        Message m;

        // move all messages on the link to one of it's sides 
        while (input.getMessageQueue().hasMessages()) {
            // the current message
            m = input.getMessageQueue().Poll();
            NetDevice dev;

            if (nextStepId == input.getSourceID()) {
                // send message from edge 1 to edge 2
                dev = env.getNode(input.getSourceID());
                //m.setSource(dev.getObjectId());
                dev.getMessageQueue().PushAndRoute(m);
                dev.getInfo().IncreaseInt(StringsProperties.STATISTIC_DEVICE_IN_MESSAGES, 1);

                executeDevice(dev);
            } else {
                // send message from edge 2 to edge 1
                dev = env.getNode(input.getDestinationID());
                //m.setSource(dev.getObjectId());
                dev.getMessageQueue().PushAndRoute(m);
                dev.getInfo().IncreaseInt(StringsProperties.STATISTIC_DEVICE_IN_MESSAGES, 1);

                executeDevice(dev);
            }
        }
    }
}
