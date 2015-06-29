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
public class PotentialPropogate extends AbsDeviceAction {

    // public XML property
    public double propogationProbability = 0.5;
    //public double infectionProbability = 0.5;

    public PotentialPropogate() {
        super("The action propogates from infected mashine to neighbors with predefined probablility", new IndicativeFeature[]{IndicativeFeature.NET_TRAFIC_OUT, IndicativeFeature.FILE_READ});
    }

    @Override
    public Integer execute(NetDevice input) {
        int infectedWithVirus = 0;
        int infectedWithImunity = 0;

        SimulatedEnvironment env = SimulatedEnvironment.getInstance();

        
        for (int linkID : input.getLinks()) {
            NetLink link = env.getLink(linkID);
            NetDevice neighbor = null;

            if (link.getDestinationID() == input.getObjectId()) {
                neighbor = env.getNode(link.getSourceID());
            } else {
                neighbor = env.getNode(link.getDestinationID());
            }
            
            if (neighbor != null 
                && neighbor.getInfo().get("Immunity") == null 
                && neighbor.getInfo().get("Virus") == null 
                //&& neighbor.getInfo().get("AV") == null
                && neighbor.getInfo().get("Potential_infected") == null
                && neighbor.getInfo().get("Potential_immuned") == null) {
                
                //Potential_infected
                if (RandomGenetor.getInstance().nextDouble() <= propogationProbability) {
                    neighbor.getInfo().set("Potential_infected", 1);
                    infectedWithVirus++; 
                }
                //Potential_immuned
                else
                {
                    neighbor.getInfo().set("Potential_immuned", 1);
                    infectedWithImunity++;
                }
            }
        }
        
        // Update Statistic
        StatisticCollector.getInstance().increaseValue("Potential_infected", infectedWithVirus );
        StatisticCollector.getInstance().increaseValue("Potential_immuned", infectedWithImunity );

        return infectedWithVirus;
    }
}
