/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bgu.sim.actions.suspectibleInfected;

import bgu.sim.core.stat.StatisticCollector;
import bgu.sim.data.NetDevice;
import bgu.sim.core.RandomGenetor;
import bgu.sim.netFile.SimulatedEnvironment;
import bgu.sim.ruleEngine.action.AbsInitAction;
import bgu.sim.ruleEngine.action.IndicativeFeature;

/**
 *
 * @author Keren
 */
public class PutDos extends AbsInitAction {

    public PutDos() {
        super("The action put DOS on ONE device", new IndicativeFeature[]{});
    }

    //public XML property
    public int amount = 1;
    // for use of this class only (helps choose fixed random nodes).
    private final double probability = 0.1;

    @Override
    public Integer execute(SimulatedEnvironment input) {

        int count = 0;

        NetDevice[] k = input.getNodesArray();
        for (NetDevice netDevice : k) {

            double r = RandomGenetor.getInstance().nextDouble();
            if (r <= probability) {
                netDevice.getInfo().set("DOS", 1);
                count++;
            }

            if (amount == count) {
                break;
            }
        }
        StatisticCollector.getInstance().setScenarioValue(this.getName(), "" + count);
        return count;
    }
}
