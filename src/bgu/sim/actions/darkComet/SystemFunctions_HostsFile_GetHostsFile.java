package bgu.sim.actions.darkComet; 

import bgu.sim.core.stat.StatisticCollector;
import bgu.sim.data.NetDevice;
import bgu.sim.data.SystemEvent;
import bgu.sim.ruleEngine.action.AbsDeviceAction;
import bgu.sim.ruleEngine.action.IndicativeFeature;
import bgu.sim.actions.darkComet.Tools.*;
import java.util.List;

public class SystemFunctions_HostsFile_GetHostsFile extends AbsDeviceAction {

    private List<SystemEvent> events; 

    public List<SystemEvent> getEvents() {
	return events;
    }

    public SystemFunctions_HostsFile_GetHostsFile() {
	super("SystemFunctions_HostsFile_GetHostsFile", new IndicativeFeature[]{});
	readCSV rc = new readCSV();
	events = rc.readCSV("DM_SystemFunctions_HostsFile_GetHostsFile.CSV");
    }

    @Override
    public Integer execute(NetDevice input) {
	//input.getInfo().addEvents(events);
        input.getInfo().getListOfSystemEvents("events").addAll(events);
	StatisticCollector.getInstance().increaseValue(this.getName(), 1);
	return 1;
    }
}