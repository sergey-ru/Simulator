/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bgu.sim.actions.clientPlayers;

import bgu.sim.netFile.SimulatedEnvironment;
import bgu.sim.ruleEngine.action.AbsExternalAction;
import bgu.sim.ruleEngine.action.IndicativeFeature;

/**
 *
 * @author sergei
 */
public class MSSQLPlayer extends AbsExternalAction {

    public MSSQLPlayer(String description, IndicativeFeature[] indicativeFeatures) {
        super(description, indicativeFeatures);
    }

    @Override
    public Integer execute(SimulatedEnvironment input) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
}
