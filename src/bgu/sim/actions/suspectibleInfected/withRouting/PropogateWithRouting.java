/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bgu.sim.actions.suspectibleInfected.withRouting;

import bgu.sim.Properties.StringsProperties;
import bgu.sim.core.RandomGenetor;
import bgu.sim.core.Simulator;
import bgu.sim.core.stat.StatisticCollector;
import bgu.sim.data.*;
import bgu.sim.netFile.SimulatedEnvironment;
import bgu.sim.ruleEngine.action.AbsDeviceAction;
import bgu.sim.ruleEngine.action.IndicativeFeature;
import java.util.AbstractMap;

/**
 *
 * @author Keren
 */
public class PropogateWithRouting extends AbsDeviceAction {

    //public XML property
    public double probability = 0.05;
    public int propogationAmount = 10;

    public PropogateWithRouting() {
        super("The action propogates from infected machine to a number of machines (propogationAmount) with predefined probablility", new IndicativeFeature[]{IndicativeFeature.NET_TRAFIC_OUT, IndicativeFeature.FILE_READ});
    }

    @Override
    public Integer execute(NetDevice input) {
        int infected = 0;
        SimulatedEnvironment env = SimulatedEnvironment.getInstance();
        int maxNodeId = env.getMaxNodeId();

        for (int i = 0; i < propogationAmount; i++) {
            // randon node (any)
            int nodeId = RandomGenetor.getInstance().nextInt(maxNodeId) + 1;
            NetDevice rndNode = env.getNode(nodeId);

            if (RandomGenetor.getInstance().nextDouble() <= probability
                    && nodeId != input.getObjectId()
                    && env.getNode(nodeId).getInfo().get("Virus") == null) {
                
                // Send Virus Message
                Message message = new Message(input.getObjectId(), nodeId, new AbstractMap.SimpleEntry<String, Object>("Virus", 1), Message.MessageType.Virus,Simulator.getInstance().getTick());

                // log the message for checking the graph
                //MyLogger.getInstance().writeToLog("source: " + message.getSource() + "  target: " + message.getDestination());

                // update input
                input.getMessageQueue().PushAndRoute(message);
                input.getInfo().IncreaseInt(StringsProperties.STATISTIC_DEVICE_OUT_MESSAGES, 1);
                
                infected++;
            }
        }
        StatisticCollector.getInstance().increaseValue(this.getName(), infected);
        return infected;

    }
}
