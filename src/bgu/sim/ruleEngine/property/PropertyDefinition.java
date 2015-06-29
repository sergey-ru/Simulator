/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bgu.sim.ruleEngine.property;

import java.text.ParseException;
import nu.xom.Attribute;
import nu.xom.Element;
import nu.xom.Elements;

/**
 *
 * @author bennyl
 */
public class PropertyDefinition implements XMLDefinition {

    private String key;
    private Object value;
    private String expose;

    public PropertyDefinition(String key, String value, String expose) {
        this.key = key;
        this.value = value;
        this.expose = expose;
    }

    public PropertyDefinition() {
    }

    @Override
    public void toXML(Element roote) {
        roote.addAttribute(new Attribute("key", key));
        if (isExposed()) {
            roote.addAttribute(new Attribute("expose", expose));
        } else {
            if (isComplex()) {
                Element e = new Element("object");
                getComplexValue().toXML(e);
                roote.appendChild(e);
            } else {
                roote.addAttribute(new Attribute("value", getValue()));

            }
        }
    }

    @Override
    public PropertyDefinition fromXML(Element roote) throws ParseException {
        this.key = roote.getAttributeValue("key");
        if (key == null) {
            throw new ParseException("property is defined without a key:\n" + roote.toXML(), -1);
        }
        this.expose = roote.getAttributeValue("expose");
        this.value = roote.getAttributeValue("value");
        
        Elements children = roote.getChildElements();
        if (children.size() > 0){
            Element child = children.get(0);
            if (!child.getLocalName().equals("object")){
                throw new ParseException("unknonwn children of p:\n" + child.toXML(), -1);
            }
            
            value = new ObjectDefinition().fromXML(child);
        }
        return this;
    }

    public boolean isComplex() {
        return !(value instanceof String);
    }

    public String getExpose() {
        return expose;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return (String) value;
    }

    public ObjectDefinition getComplexValue() {
        return (ObjectDefinition) value;
    }

    public boolean isExposed() {
        return expose != null;
    }
}
