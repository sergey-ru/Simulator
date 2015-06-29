/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bgu.sim.core.Routing.DB;

import static bgu.sim.Properties.StringsProperties.*;
import bgu.sim.data.NetDevice;
import bgu.sim.netFile.SimulatedEnvironment;
import bgu.sim.ruleEngine.property.Property;
import bgu.sim.ruleEngine.property.PropertyDefinition;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import nu.xom.Element;
import nu.xom.Elements;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Sergey
 */
public abstract class AbsRoutingAlgorithm {

    private Map<Integer, dijkstraNetDevice> _graph;
    //public XML property and name must match XML property
    public String name;
    
    // Run the algorithm for finding all shortests paths from all to all
    public abstract void calculateAllShortestPaths() throws IOException, SQLException, ClassNotFoundException;

    // given source and target, return the next step from source to target
    public abstract int getNextStep(int Source, int Target) throws IOException, SQLException;

    protected Map<Integer, dijkstraNetDevice> getGraph() {
        return _graph;
    }

    protected String getName() {
        return name;
    }

    protected void setName(String name) {
        this.name = name;
    }

    public void setGraph(SimulatedEnvironment graph) {
        if (graph == null) {
            this._graph = null;
        } else {
            this._graph = new HashMap<>();
            for (NetDevice netDevice : graph.getNodesArray()) {
                this._graph.put(netDevice.getObjectId(), new dijkstraNetDevice(netDevice));
            }
        }
    }

    //creation router instance xrom XML
    public static AbsRoutingAlgorithm fromXML(Element root) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        String algClassName = root.getAttributeValue(VALUE_KEY);
        AbsRoutingAlgorithm alg = (AbsRoutingAlgorithm) Class.forName(algClassName).newInstance();
        Elements children = root.getChildElements();

        if (children.size() > 0) {
            List<PropertyDefinition> properties = new LinkedList<>();

            for (int i = 0; i < children.size(); i++) {
                Element child = children.get(i);

                if (XML_TAG_P.equals(child.getLocalName())) {
                    properties.add(new PropertyDefinition(child.getAttributeValue(KEY), child.getAttributeValue(VALUE_KEY), null));
                }
            }
            Property.updateProperties(alg, properties);
        }
        return alg;
    }
}
