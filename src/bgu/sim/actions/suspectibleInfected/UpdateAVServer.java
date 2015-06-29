/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bgu.sim.actions.suspectibleInfected;

import bgu.sim.core.RandomGenetor;
import bgu.sim.core.stat.StatisticCollector;
import bgu.sim.netFile.*;
import bgu.sim.ruleEngine.action.AbsExternalAction;
import bgu.sim.ruleEngine.action.IndicativeFeature;

/**
 *
 * @author Keren
 */
public class UpdateAVServer extends AbsExternalAction {

    public UpdateAVServer() {
        super("The action updates the external variable that represents remote antivirus server", new IndicativeFeature[]{});
    }

    //public XML property
    public double probability = 0.05;

    public double getProbability() {
        return probability;
    }

    public void setProbability(double probability) {
        this.probability = probability;
    }

    @Override
    public Integer execute(SimulatedEnvironment input) {

        if (RandomGenetor.getInstance().nextDouble() <= probability) {
            input.setExternalVariable(":ver", (int)input.getExternalVariable(":ver")+1);
            StatisticCollector.getInstance().increaseValue(this.getName(), 1);
            return 1;
        }

        return 0;
    }
}
