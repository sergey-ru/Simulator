
package bgu.sim.ruleEngine.rule;

import static bgu.sim.Properties.StringsProperties.*;
import bgu.sim.ruleEngine.action.AbstractAction;
import bgu.sim.ruleEngine.property.Property;
import bgu.sim.ruleEngine.property.PropertyDefinition;
import com.google.common.collect.Iterators;
import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import nu.xom.Element;
import nu.xom.Elements;
import org.josql.QueryExecutionException;
import org.josql.QueryParseException;

public abstract class Rule<Input> implements Runnable, Comparable<Rule> {

    private final Precondition _select;
    private final AbstractAction<Input> _action;
    private Input[] _inputs;
    private List<Integer> _outputs;
    private final Integer _order;

    public Rule(Precondition select, AbstractAction<Input> action, Integer order) {
        this._select = select;
        this._action = action;
        this._order = order;
    }

    public Integer getOrder() {
        return this._order;
    }

    public void setInput(Input[] inputs) {
        this._inputs = inputs;
    }

    private <Input> List<Integer> executeAction(Precondition selectPart, AbstractAction<Input> action, Input[] inputs) throws QueryExecutionException {
        List<Integer> ans = new LinkedList<>();

        for (Input input : getMatchingInstances(selectPart, inputs)) {
            ans.add(action.executeAction(input));
        }

        return ans;
    }

    //returns all instances that mach precondition of rule
    private <Input> List<Input> getMatchingInstances(Precondition selectPart, Input[] inputs) throws QueryExecutionException {
        return selectPart.getQuery().execute(Iterators.forArray(inputs)).getResults();
    }

    public void updatePreconditionVariables(Map<String, Object> vars) {
        _select.getQuery().setVariables(vars);
    }

    @Override
    public void run() {
        try {
            _outputs = executeAction(this._select, this._action, this._inputs);
        } catch (QueryExecutionException ex) {
            System.err.println(ex.getMessage());
            _outputs = null;
        }
    }

    @Override
    public int compareTo(Rule rule) {
        if (this._order == null) {
            if (rule._order == null) {
                return 0;
            } else {
                return -1;
            }
        } else if (rule._order == null) {
            return 1;
        }
        return this._order - rule._order;
    }

    public void executeAction() throws QueryExecutionException {
        _outputs = executeAction(this._select, this._action, this._inputs);
    }

    public List<Integer> getOutputs() {
        return _outputs;
    }

    public String getActionName() {
        return _action.getName();
    }

    public String getDescription() {
        return this._action.getDescription();
        //return "select[" + _select + "]\t action[" + _action.getClass().getCanonicalName() + "]\t order[" + _order + "]";
    }

    //create rule object from XML
    public static Rule fromXML(Element root) throws ClassNotFoundException, InstantiationException, IllegalAccessException, QueryParseException, ParseException {
        String name = root.getAttributeValue(NAME_KEY);
        String actionClassName = root.getFirstChildElement(XML_TAG_ACTION_KEY).getAttributeValue(VALUE_KEY);
        String select = null;
        if (!XML_TAG_INIT.equals(root.getLocalName())) {
            select = root.getFirstChildElement(XML_TAG_SELECT_KEY).getAttributeValue(VALUE_KEY);
        }
        Integer order = null;
        try {
            order = Integer.parseInt(root.getFirstChildElement(XML_TAG_ORDER_KEY).getAttributeValue(VALUE_KEY));
        } catch (Exception e) {
            if(root.getLocalName() == null ? XML_TAG_INIT != null : !root.getLocalName().equals(XML_TAG_INIT)){
                throw new NumberFormatException("Rule order must be set!");
            }
        }
        AbstractAction action = AbstractAction.lookup(actionClassName);
        action.setName(name);
        Elements children = root.getFirstChildElement(XML_TAG_ACTION_KEY).getChildElements();
        
        addPropertiesToAction(children, action);
        
        return createRuleByType(root, action, select, order);        
    }

    private static Rule createRuleByType(Element root, AbstractAction action, String select, Integer order) throws ParseException, QueryParseException {
        switch (root.getLocalName()) {
            case XML_TAG_INIT:
                return new InitRule(action);
            case XML_TAG_EXTERNAL:
                return new ExternalVariableRule(new Precondition(select), action, order);
            case XML_TAG_DEVICE:
                return new DeviceRule(new Precondition(select), action, order);
            case XML_TAG_LINK:
                return new LinkRule(new Precondition(select), action, order);
            default:
                throw new ParseException("unknown child of rules:\n" + root.toXML(), -1);
        }
    }

    private static void addPropertiesToAction(Elements children, AbstractAction action) {
        if (children.size() > 0) {
            List<PropertyDefinition> properties = new LinkedList<>();
            for (int i = 0; i < children.size(); i++) {
                Element child = children.get(i);
                if (XML_TAG_P.equals(child.getLocalName())) {
                    properties.add(new PropertyDefinition(child.getAttributeValue(KEY), child.getAttributeValue(VALUE_KEY), null));
                }
            }
            Property.updateProperties(action, properties);
        }
    }
}
