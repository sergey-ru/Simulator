/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bgu.sim.ruleEngine.action;

import bgu.sim.netFile.SimulatedEnvironment;

/**
 *
 * @author Sergey
 */
public abstract class AbsInitAction extends AbstractAction<SimulatedEnvironment>{

    public AbsInitAction(String description, IndicativeFeature[] indicativeFeatures) {
        super(description, indicativeFeatures);
    }
    
}
