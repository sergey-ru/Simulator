/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bgu.sim.actions.flickr;

import bgu.sim.core.stat.StatisticCollector;
import bgu.sim.core.RandomGenetor;
import bgu.sim.data.Info;
import bgu.sim.netFile.SimulatedEnvironment;
import bgu.sim.ruleEngine.action.AbsInitAction;
import bgu.sim.ruleEngine.action.IndicativeFeature;

/**
 *
 * @author Keren
 */
public class InstallAV extends AbsInitAction {

    public InstallAV() {
        super("The action installs antivirus om some devices", new IndicativeFeature[]{});
    }

    //public XML property
    public double percentage = 0.05;

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    @Override
    public Integer execute(SimulatedEnvironment input) {
        
        int howMany = 0;
        
        for (int i = 1; i <= input.getNodesArray().length; i++) {
            if (RandomGenetor.getInstance().nextDouble() <= percentage && !input.getNode(i).getInfo().containsKey("flickrImg")) {
                // put AV
                Info addToInfo = input.getNode(i).getInfo();
                addToInfo.set("AV", true);
                howMany++;
            }
        }
        StatisticCollector.getInstance().setScenarioValue(this.getName(), "" + howMany);
        return howMany;
    }
}
