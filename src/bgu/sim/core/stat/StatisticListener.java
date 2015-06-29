/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bgu.sim.core.stat;

import static bgu.sim.Properties.StringsProperties.KEY;
import static bgu.sim.Properties.StringsProperties.VALUE_KEY;
import static bgu.sim.Properties.StringsProperties.XML_TAG_P;
import bgu.sim.data.Message;
import bgu.sim.ruleEngine.property.Property;
import bgu.sim.ruleEngine.property.PropertyDefinition;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import nu.xom.Element;
import nu.xom.Elements;

/**
 *
 * @author Sergey
 * abstract class that all statistic must extend and override it's abstract functions 
 */
public abstract class StatisticListener {

    public abstract void TickEndedEventHandler(Map<String, StatisticCollector.RefInteger> snapshot, List<String> snapshotHeaders, List<StatisticCollector.RefInteger> snapshotData, List<Message> messages);

    public abstract void ScenarioStardedEventHandler(Map<String, String> scenarioData);

    public abstract void ScenarioEndedEventHandler();

    //creation of listener instance from XML
    public static StatisticListener fromXML(Element root) throws ClassNotFoundException, InstantiationException, IllegalAccessException {

        String listenerClassName = root.getAttributeValue(VALUE_KEY);

        StatisticListener listener = (StatisticListener) Class.forName(listenerClassName).newInstance();

        Elements children = root.getChildElements();
        if (children.size() > 0) {
            List<PropertyDefinition> properties = new LinkedList<>();
            for (int i = 0; i < children.size(); i++) {
                Element child = children.get(i);
                if (XML_TAG_P.equals(child.getLocalName())) {
                    properties.add(new PropertyDefinition(child.getAttributeValue(KEY), child.getAttributeValue(VALUE_KEY), null));
                }
            }
            Property.updateProperties(listener, properties);
        }
        return listener;
    }
}
