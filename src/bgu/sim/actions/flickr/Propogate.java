/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bgu.sim.actions.flickr;

import bgu.sim.actions.suspectibleInfected.*;
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
    public double propProbability = 0.5;
    public double infectProbability = 0.5;

    public Propogate() {
        super("The action propogates from infected mashine to neighbors with predefined probablility", new IndicativeFeature[]{IndicativeFeature.NET_TRAFIC_OUT, IndicativeFeature.FILE_READ});
    }

    @Override
    public Integer execute(NetDevice input) {
        int infectedWithVirus = 0;
        int infectedWithImmunity = 0;

        SimulatedEnvironment env = SimulatedEnvironment.getInstance();
        
        for (int linkID : input.getLinks()) {
            NetLink link = env.getLink(linkID);
            NetDevice neighbor = null;
            if(link.IsDirected() && link.getDestinationID() == input.getObjectId()){
                neighbor = env.getNode(link.getSourceID());
            }
            else if (!link.IsDirected()){
                if(link.getDestinationID() == input.getObjectId()){
                    neighbor = env.getNode(link.getSourceID());
                }
                else{
                    neighbor = env.getNode(link.getDestinationID());
                }
            }
            
            if ( neighbor!=null && RandomGenetor.getInstance().nextDouble() <= propProbability) {

                if (neighbor.getInfo().get("Immunity") == null && neighbor.getInfo().get("Virus") == null) {
                    // Virus
                    if (RandomGenetor.getInstance().nextDouble() <= infectProbability) {
                        neighbor.getInfo().set("Virus", 1);
                        infectedWithVirus++;
                    } // Immunity
                    else {
                        neighbor.getInfo().set("Immunity", 1);
                        infectedWithImmunity++;
                    }
                }
            }
            
        }
        
        for (int neighborID : input.getNeighbors()) {

            if (RandomGenetor.getInstance().nextDouble() <= propProbability) {
                
                
                NetDevice neighbor = env.getNode(neighborID);

                if (neighbor.getInfo().get("Immunity") == null && neighbor.getInfo().get("Virus") == null) {
                    // Virus
                    if (RandomGenetor.getInstance().nextDouble() <= infectProbability) {
                        neighbor.getInfo().set("Virus", 1);
                        infectedWithVirus++;
                    } // Immunity
                    else {
                        neighbor.getInfo().set("Immunity", 1);
                        infectedWithImmunity++;
                    }
                }
            }

        }
        
        // Update Statistic
        StatisticCollector.getInstance().increaseValue(this.getName() + "WithVirus", infectedWithVirus);
        StatisticCollector.getInstance().increaseValue(this.getName() + "WithImmunity", infectedWithImmunity);
        StatisticCollector.getInstance().increaseValue(this.getName(), infectedWithVirus + infectedWithImmunity);

        return infectedWithVirus;
    }
}
