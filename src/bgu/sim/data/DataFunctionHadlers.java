/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bgu.sim.data;

import bgu.sim.core.RandomGenetor;

/**
 *
 * @author Sergey
 */
public class DataFunctionHadlers {

    public Integer getInt(String key, SimulatedObj obj) {
        return obj.getInfo().getInt(key);
    }

    public Integer getInt(String key, NetDevice obj) {
        return obj.getInfo().getInt(key);
    }

    public Double getRandomDouble() {
        return RandomGenetor.getInstance().nextDouble();
    }

    public Boolean getBoolean(String key, SimulatedObj obj) {
        return obj.getInfo().getBoolean(key);
    }

    public Boolean getBoolean(String key, NetDevice obj) {
        return obj.getInfo().getBoolean(key);
    }
    
    public Boolean HasOutMessages(NetDevice obj){
        return obj.getMessageQueue().HasOutMessages();
    }
    
    public Boolean HasReceivedMessages(NetDevice obj){
        return obj.getMessageQueue().HasReceivedMessages();
    }
}
