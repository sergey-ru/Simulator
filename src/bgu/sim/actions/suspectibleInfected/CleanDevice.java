/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bgu.sim.actions.suspectibleInfected;

import bgu.sim.data.NetDevice;
import bgu.sim.core.stat.StatisticCollector;
import bgu.sim.ruleEngine.action.AbsDeviceAction;
import bgu.sim.ruleEngine.action.IndicativeFeature;

/**
 *
 * @author Keren
 */
public class CleanDevice extends AbsDeviceAction {

    public CleanDevice() {
        super("This action removes virus from infected device if version of antivirus is higher than version of virus", new IndicativeFeature[]{IndicativeFeature.FILE_READ, IndicativeFeature.FILE_WRITE});
    }

    @Override
    public Integer execute(NetDevice input) {
        int count = 0;
        if ((int) input.getInfo().get("AVver") >= (int) input.getInfo().get("Virus")) {
            input.getInfo().set("Virus", 0);
            count++;
        }
        StatisticCollector.getInstance().increaseValue(this.getName(), count);
        return count;
    }
}
