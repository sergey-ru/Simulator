/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bgu.sim.ruleEngine.action;

import bgu.sim.data.NetLink;

/**
 *
 * @author Sergey
 */
public abstract class AbsLinkAction extends AbstractAction<NetLink>{

    public AbsLinkAction(String description, IndicativeFeature[] indicativeFeatures) {
        super(description, indicativeFeatures);
    }
    
}
