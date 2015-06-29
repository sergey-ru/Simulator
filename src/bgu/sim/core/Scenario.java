/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bgu.sim.core;

import bgu.sim.ruleEngine.rule.Rule;
import java.util.List;

/**
 *
 * @author Sergey
 */
public class Scenario {

    private String _name;
    private List<Rule> _rules;

    public List<Rule> getRules() {
        return _rules;
    }

    public void setRules(List<Rule> rules) {
        this._rules = rules;
    }

    public String getName() {
        return _name;
    }

    public void setName(String name) {
        this._name = name;
    }
}
