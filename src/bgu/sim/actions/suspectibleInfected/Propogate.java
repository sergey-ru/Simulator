/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bgu.sim.actions.suspectibleInfected;

import bgu.sim.data.NetDevice;
import bgu.sim.core.RandomGenetor;
import bgu.sim.core.stat.StatisticCollector;
import bgu.sim.netFile.SimulatedEnvironment;
import bgu.sim.ruleEngine.action.AbsDeviceAction;
import bgu.sim.ruleEngine.action.IndicativeFeature;

/**
 *
 * @author Keren
 */
public class Propogate extends AbsDeviceAction {

    // public XML property
    public double probability = 0.05;

    public Propogate() {
        super("The action propogates from infected mashine to neighbors with predefined probablility", new IndicativeFeature[]{IndicativeFeature.NET_TRAFIC_OUT, IndicativeFeature.FILE_READ});
    }

    @Override
    public Integer execute(NetDevice input) {
        int infected = 0;
        SimulatedEnvironment env = SimulatedEnvironment.getInstance();
        for (int neighborID : input.getNeighbors()) {
            NetDevice neighbor = env.getNode(neighborID);
            if (neighbor.getInfo().get("Virus") == null
                    && RandomGenetor.getInstance().nextDouble() <= probability) {
                neighbor.getInfo().set("Virus", 1);
                infected++;
            }
        }
        StatisticCollector.getInstance().increaseValue(this.getName(), infected);
        return infected;
    }
}
