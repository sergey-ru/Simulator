/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bgu.sim.ruleEngine.action;

import bgu.sim.data.NetDevice;

/**
 *
 * @author Sergey
 */
public abstract class AbsDeviceAction extends AbstractAction<NetDevice>{

    public AbsDeviceAction(String description, IndicativeFeature[] indicativeFeatures) {
        super(description, indicativeFeatures);
    }
    
}
