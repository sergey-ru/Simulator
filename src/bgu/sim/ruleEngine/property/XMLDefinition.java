/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bgu.sim.ruleEngine.property;

import java.text.ParseException;
import nu.xom.Element;

/**
 *
 * @author bennyl
 */
public interface XMLDefinition {
    public void toXML(Element root);
    public XMLDefinition fromXML(Element root) throws ParseException;
}
