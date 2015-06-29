/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bgu.sim.actions.suspectibleInfected.withRouting;

import bgu.sim.Properties.StringsProperties;
import bgu.sim.core.Simulator;
import bgu.sim.core.stat.StatisticCollector;
import bgu.sim.data.*;
import bgu.sim.netFile.SimulatedEnvironment;
import bgu.sim.ruleEngine.action.AbsDeviceAction;
import bgu.sim.ruleEngine.action.IndicativeFeature;

/**
 *
 * @author Keren
 */
public class DosAttackWithRouting extends AbsDeviceAction {

    //public XML property
    public int target = 0;

    public DosAttackWithRouting() {
        super("The action sending messages to the target", new IndicativeFeature[]{IndicativeFeature.NET_TRAFIC_OUT, IndicativeFeature.FILE_READ});
    }

    @Override
    public Integer execute(NetDevice input) {
        SimulatedEnvironment env = SimulatedEnvironment.getInstance();

        // Send Virus Message
        Message message = new Message(input.getObjectId(), target, null, Message.MessageType.DOS, Simulator.getInstance().getTick());

        // log the message for checking the graph
        //MyLogger.getInstance().writeToLog("source: " + message.getSource() + "  target: " + message.getDestination());

        // update input
        input.getMessageQueue().PushAndRoute(message);
        input.getInfo().IncreaseInt(StringsProperties.STATISTIC_DEVICE_OUT_MESSAGES, 1);

        StatisticCollector.getInstance().increaseValue(this.getName(), 1);
        return target;

    }
}
