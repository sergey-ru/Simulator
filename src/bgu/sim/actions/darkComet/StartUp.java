package bgu.sim.actions.darkComet;

import bgu.sim.core.stat.StatisticCollector;
import bgu.sim.data.NetDevice;
import bgu.sim.data.SystemEvent;
import bgu.sim.ruleEngine.action.AbsDeviceAction;
import bgu.sim.ruleEngine.action.IndicativeFeature;
import bgu.sim.actions.darkComet.Tools.*;
import java.util.List;

public class StartUp extends AbsDeviceAction {

    private List<SystemEvent> events;

    public List<SystemEvent> getEvents() {
        return events;
    }

    public StartUp() {
        super("StartUp", new IndicativeFeature[]{});
        readCSV rc = new readCSV();
        events = rc.readCSV("DM_StartUp.CSV");
    }

    @Override
    public Integer execute(NetDevice input) {
        //input.getInfo().addEvents(events);
        input.getInfo().getListOfSystemEvents("events").addAll(events);
        StatisticCollector.getInstance().increaseValue(this.getName(), 1);
        return 1;
    }
}
