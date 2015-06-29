

package bgu.sim.ruleEngine.action;

import bgu.sim.data.SimulatedObj;

public abstract class AbstractAction<Input> {

    private String _name;
    private final String _description;
    private final IndicativeFeature[] _indicativeFeatures;

    public AbstractAction(String description, IndicativeFeature[] indicativeFeatures) {
        this._description = description;
        this._indicativeFeatures = indicativeFeatures;
    }

    public abstract Integer execute(Input input);

    public Integer executeAction(Input input) {

        if (input instanceof SimulatedObj) {

            for (IndicativeFeature feature : _indicativeFeatures) {
                ((SimulatedObj) input).getInfo().set(feature.toString(), 1);
            }
        }

        return execute(input);
    }

    public String getName() {
        return _name;
    }

    public void setName(String name) {
        this._name = name;
    }

    public String getDescription() {
        return this._description;
    }

    @Override
    public String toString() {
        return "Action [name=" + _name + "]";
    }

    public static AbstractAction lookup(String className) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        return (AbstractAction) Class.forName(className).newInstance();
    }

}
