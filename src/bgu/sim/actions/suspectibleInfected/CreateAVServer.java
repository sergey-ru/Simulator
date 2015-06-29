/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bgu.sim.actions.suspectibleInfected;

import bgu.sim.core.stat.StatisticCollector;
import bgu.sim.netFile.SimulatedEnvironment;
import bgu.sim.ruleEngine.action.AbsInitAction;
import bgu.sim.ruleEngine.action.IndicativeFeature;

/**
 *
 * @author Keren
 */
public class CreateAVServer extends AbsInitAction{

    public CreateAVServer() {
        super("The action creates external variable of remote antivirus server", new IndicativeFeature[]{});
    }

    @Override
    public Integer execute(SimulatedEnvironment input) {
        input.addExternalVariable(":ver", 0);
        StatisticCollector.getInstance().setScenarioValue(this.getName(), "0");
        return 0;

    }

}
