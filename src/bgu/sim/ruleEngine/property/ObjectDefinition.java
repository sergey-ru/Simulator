/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bgu.sim.ruleEngine.property;

import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;
import nu.xom.Attribute;
import nu.xom.Element;
import nu.xom.Elements;

/**
 *
 * @author bennyl
 */
public class ObjectDefinition implements XMLDefinition {

    String objectClass;
    List<PropertyDefinition> properties;

    public ObjectDefinition() {
        properties = new LinkedList<>();
    }

    public List<PropertyDefinition> getProperties() {
        return properties;
    }

    public String getObjectClass() {
        return objectClass;
    }

    @Override
    public void toXML(Element root) {
        root.addAttribute(new Attribute("class", objectClass));
        
        for (PropertyDefinition p : properties) {
            Element e = new Element("p");
            p.toXML(e);
            root.appendChild(e);
        }
    }

    @Override
    public ObjectDefinition fromXML(Element root) throws ParseException {
        objectClass = root.getAttributeValue("class");
        
        if (objectClass == null) {
            throw new ParseException("object is missing required attribute (class?):\n" + root.toXML(), -1);
        }

        Elements children = root.getChildElements();

        for (int i = 0; i < children.size(); i++) {
            Element child = children.get(i);
            properties.add(new PropertyDefinition().fromXML(child));
        }

        return this;
    }
}
