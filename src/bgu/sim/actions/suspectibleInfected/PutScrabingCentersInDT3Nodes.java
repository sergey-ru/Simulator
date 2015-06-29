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
public class PutScrabingCentersInDT3Nodes extends AbsInitAction {

    public PutScrabingCentersInDT3Nodes() {
        super("The action put Scrabing Centers on devices with property NSP=Dt", new IndicativeFeature[]{});
    }

    //public XML property
    public double amount = 0;

    public double getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public Integer execute(SimulatedEnvironment input) {
        int count = 3;

        NetDevice k1 = input.getNode(1);
        NetDevice k2 = input.getNode(2);
        NetDevice k3 = input.getNode(3);
        
        k1.getInfo().set("ScrabingCenter", 1);
        k2.getInfo().set("ScrabingCenter", 1);
        k3.getInfo().set("ScrabingCenter", 1);
        
        StatisticCollector.getInstance().setScenarioValue(this.getName(), "" + count);
        return count;
    }
}
