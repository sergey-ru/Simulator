/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bgu.sim.actions.fb;

import bgu.sim.data.NetDevice;
import bgu.sim.core.RandomGenetor;
import bgu.sim.core.stat.StatisticCollector;
import bgu.sim.data.NetLink;
import bgu.sim.netFile.SimulatedEnvironment;
import bgu.sim.ruleEngine.action.AbsDeviceAction;
import bgu.sim.ruleEngine.action.IndicativeFeature;

/**
 *
 * @author Keren
 */
public class Propogate extends AbsDeviceAction {

    // public XML property
    public double propogationProbability = 0.5;

    public Propogate() {
        super("The action propogates from infected mashine to neighbors with predefined probablility", new IndicativeFeature[]{IndicativeFeature.NET_TRAFIC_OUT, IndicativeFeature.FILE_READ});
    }

    @Override
    public Integer execute(NetDevice input) {
        int infectedWithVirus = 0;
        int infectedWithImmunity = 0;
        int potential_infectedWithVirus = 0;
        int potential_infectedWithImmunity = 0;

        SimulatedEnvironment env = SimulatedEnvironment.getInstance();

        for (int linkID : input.getLinks()) {
            NetLink link = env.getLink(linkID);
            NetDevice neighbor = null;

            if (link.getDestinationID() == input.getObjectId()) {
                neighbor = env.getNode(link.getSourceID());
            } else {
                neighbor = env.getNode(link.getDestinationID());
            }

            if (neighbor != null && neighbor.getInfo().get("Immunity") == null && neighbor.getInfo().get("Virus") == null) {

                // Virus
                if (RandomGenetor.getInstance().nextDouble() <= propogationProbability) {

                    // Virus
                    if (neighbor.getInfo().get("AV") == null) {
                        neighbor.getInfo().set("Virus", 1);
                        infectedWithVirus++;
                        if (neighbor.getInfo().get("Potential_infected") != null) {
                            potential_infectedWithVirus--;
                        }
                        if (neighbor.getInfo().get("Potential_immuned") != null) {
                            potential_infectedWithImmunity--;
                        }
                    } else if (neighbor.getInfo().get("Potential_infected") == null && neighbor.getInfo().get("Potential_immuned") == null) {
                        neighbor.getInfo().set("Potential_infected", 1);
                        potential_infectedWithVirus++;
                    }

                } // Immunity
                else if (neighbor.getInfo().get("AV") == null) {
                    neighbor.getInfo().set("Immunity", 1);
                    infectedWithImmunity++;
                    if (neighbor.getInfo().get("Potential_infected") != null) {
                        potential_infectedWithVirus--;
                    }
                    if (neighbor.getInfo().get("Potential_immuned") != null) {
                        potential_infectedWithImmunity--;
                    }
                }
            }
        }
        // Update Statistic
        StatisticCollector.getInstance().increaseValue("Infected", infectedWithVirus);
        StatisticCollector.getInstance().increaseValue("Immuned", infectedWithImmunity);
        StatisticCollector.getInstance().increaseValue("Potential_infected", potential_infectedWithVirus);
        StatisticCollector.getInstance().increaseValue("Potential_immuned", potential_infectedWithImmunity);

        return infectedWithVirus;
    }
}
