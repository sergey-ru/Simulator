/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bgu.sim.ruleEngine.rule;

import bgu.sim.ruleEngine.action.AbstractAction;

/**
 *
 * @author Sergey
 * @param <Input>
 */
public class DeviceRule<Input> extends Rule<Input> {

    public DeviceRule(Precondition select, AbstractAction action, Integer order) {
        super(select, action, order);
    }
}
