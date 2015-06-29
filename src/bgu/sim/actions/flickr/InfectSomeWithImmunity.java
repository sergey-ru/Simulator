/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bgu.sim.actions.flickr;

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
public class InfectSomeWithImmunity extends AbsInitAction {

    // public XML property
    public int propogationAmount = 10;
    // for use of this class only (helps choose fixed random nodes).
    private final double probability = 0.3;

    public InfectSomeWithImmunity() {
        super("The action infects some devices with immunity", new IndicativeFeature[]{});
    }

    @Override
    public Integer execute(SimulatedEnvironment input) {
        int count = 0;

        NetDevice[] k = input.getNodesArray();
        for (NetDevice netDevice : k) {

            double r = RandomGenetor.getInstance().nextDouble();
            if (r <= probability) {
                netDevice.getInfo().set("Immunity", 1);
                count++;
            }

            if (propogationAmount == count) {
                break;
            }
        }
        StatisticCollector.getInstance().setScenarioValue(this.getName(), "" + count);
        return count;
    }
}
