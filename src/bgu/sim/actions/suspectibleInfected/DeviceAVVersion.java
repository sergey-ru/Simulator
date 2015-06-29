/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bgu.sim.actions.suspectibleInfected;

import bgu.sim.data.NetDevice;
import bgu.sim.core.stat.StatisticCollector;
import bgu.sim.netFile.SimulatedEnvironment;
import bgu.sim.ruleEngine.action.AbsDeviceAction;
import bgu.sim.ruleEngine.action.IndicativeFeature;

/**
 *
 * @author Keren
 */
public class DeviceAVVersion extends AbsDeviceAction {

    public DeviceAVVersion() {
        super("The actions checks updetes local antivirus version", new IndicativeFeature[]{IndicativeFeature.NET_TRAFIC_IN, IndicativeFeature.NET_TRAFIC_OUT, IndicativeFeature.FILE_WRITE});
    }

    @Override
    public Integer execute(NetDevice input) {
        input.getInfo().set("AVver", SimulatedEnvironment.getInstance().getExternalVariable(":ver"));
        StatisticCollector.getInstance().increaseValue(this.getName(), 1);
        return 1;
    }
}
