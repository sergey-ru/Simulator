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
public class PutScrabingCentersInDT extends AbsInitAction {

    public PutScrabingCentersInDT() {
        super("The action put Scrabing Centers on devices with property NSP=Dt", new IndicativeFeature[]{});
    }

    //public XML property
    public double amount = 0;
    // private to this rule
    private final double probability = 0.3;

    public double getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public Integer execute(SimulatedEnvironment input) {
        int count = 0;

        NetDevice[] k = input.getNodesArray();
        for (NetDevice netDevice : k) {
            double r = RandomGenetor.getInstance().nextDouble();
            if (r <= probability) {
                if (netDevice.getInfo().containsKey("nsp")) {
                    if (netDevice.getInfo().get("nsp").equals("dt")) {
                        netDevice.getInfo().set("ScrabingCenter", 1);
                        count++;
                    }
                }
            }
            if (count >= amount) {
                break;
            }
        }

        StatisticCollector.getInstance().setScenarioValue(this.getName(), "" + count);
        return count;
    }
}
