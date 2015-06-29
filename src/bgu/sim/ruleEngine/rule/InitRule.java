/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bgu.sim.ruleEngine.rule;

import static bgu.sim.Properties.StringsProperties.SELECT_OBJECTS_RULES;
import bgu.sim.ruleEngine.action.AbstractAction;
import org.josql.QueryParseException;

/**
 *
 * @author Sergey
 * @param <Input>
 */
public class InitRule<Input> extends Rule<Input> {

    public InitRule(AbstractAction action) throws QueryParseException {
        super(new Precondition(SELECT_OBJECTS_RULES), action, null);
    }
}