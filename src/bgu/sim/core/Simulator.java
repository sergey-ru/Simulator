/*
 * Tests
 */
package bgu.sim.core;

import bgu.sim.ruleEngine.rule.ExternalVariableRule;
import bgu.sim.ruleEngine.rule.Rule;
import bgu.sim.ruleEngine.rule.LinkRule;
import bgu.sim.ruleEngine.rule.DeviceRule;
import bgu.sim.ruleEngine.rule.InitRule;
import bgu.sim.core.Routing.DB.AbsRoutingAlgorithm;
import static bgu.sim.Properties.StringsProperties.*;
import bgu.sim.core.stat.*;
import bgu.sim.data.NetDevice;
import bgu.sim.netFile.*;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import nu.xom.Builder;
import nu.xom.Element;
import nu.xom.Elements;
import org.josql.QueryExecutionException;
import org.josql.QueryParseException;

/**
 *
 * @author Sergei :P
 */
public class Simulator {

    private int _ticks = 0;
    private Integer _seed = null;
    private Integer _curentTick = 0;
    private SimulatedEnvironment _simEnvironment;

    private SimulatedEnvironment[] _simEnvArray = new SimulatedEnvironment[]{_simEnvironment};
    private List<Rule> _sequentiallRules;
    private List<Rule> _initRules;
    private List<Scenario> _scenarios;
    private File _netFile;

    private Simulator() {
    }

    // keren
    public SimulatedEnvironment getSimEnvironment() {
        return _simEnvironment;
    }

    
    public int getTotalTicks() {
        return _ticks;
    }
    
    public File getNetFile() {
        return _netFile;
    }

    public static Simulator getInstance() {
        return SimulatorHolder.INSTANCE;
    }
    
    public static void resetInstance() {
        SimulatorHolder.INSTANCE = new Simulator();
        StatisticCollector.getInstance().removeAllListeners();
    }

    private static class SimulatorHolder {

        private static Simulator INSTANCE = new Simulator();
    }

    // base init simulator (used by keren - for the web)
    public void init() throws IOException {
        if (_scenarios.size() > 0) {
            this._simEnvironment = NetFileParser.read(_netFile);
        }
    }

    //checking if rule has order or not and adding them to siquential or multi thread execution list
    private void setRules(List<Rule> allRules) {
        this._sequentiallRules = new LinkedList<>();
        this._initRules = new LinkedList<>();

        //set simulation rules
        for (Rule rule : allRules) {
            if (rule instanceof InitRule) {
                this._initRules.add(rule);
            } else {
                this._sequentiallRules.add(rule);
            }
            WriteStatisticOfSimulatedRules(rule);
        }
        Collections.sort(_sequentiallRules);
    }

    //writing statistics about rules that are used in scenario
    private void WriteStatisticOfSimulatedRules(Rule rule) {

        if (rule instanceof InitRule) {
            StatisticCollector.getInstance().setScenarioValue(STATISTIC_SCENARIO_TITLE_INIT_RULE + rule.getActionName(), rule.getDescription());
        } else if (rule instanceof ExternalVariableRule) {
            StatisticCollector.getInstance().setScenarioValue(STATISTIC_SCENARIO_TITLE_EXTERNAL_RULE + rule.getActionName(), rule.getDescription());
            StatisticCollector.getInstance().increaseValue(rule.getActionName(), 0);
        } else if (rule instanceof DeviceRule) {
            StatisticCollector.getInstance().setScenarioValue(STATISTIC_SCENARIO_TITLE_DEVICE_RULE + rule.getActionName(), rule.getDescription());
            StatisticCollector.getInstance().increaseValue(rule.getActionName(), 0);
        } else if (rule instanceof LinkRule) {
            StatisticCollector.getInstance().setScenarioValue(STATISTIC_SCENARIO_TITLE_LINK_RULE + rule.getActionName(), rule.getDescription());
            StatisticCollector.getInstance().increaseValue(rule.getActionName(), 0);
        }
    }

    //execution of initialization rules before the simulation begins
    public void runInitRules() throws QueryExecutionException {

        _simEnvArray = new SimulatedEnvironment[]{_simEnvironment};

        for (Rule rule : this._initRules) {
            rule.setInput(_simEnvArray);
            rule.executeAction();
        }

        StatisticCollector.getInstance().ScenarioStartedEvent();
    }

    //used to run initialization rules when simulation stoped/paused to change environment during executuon
    public void updateEnvironmentByInitRules(List<Rule> initRules) throws QueryExecutionException {
        _simEnvArray = new SimulatedEnvironment[]{_simEnvironment};

        for (Rule rule : initRules) {
            rule.setInput(_simEnvArray);
            rule.executeAction();
        }

        StatisticCollector.getInstance().ScenarioStartedEvent();
    }

    //execution of rules that have order value siquentialy by the order
    private void nextTickSiquential() throws InterruptedException, QueryExecutionException {

        for (Rule rule : _sequentiallRules) {
            setInput(rule);
            rule.executeAction();
        }
    }

    //updates input variables to the rules
    private void setInput(Rule rule) {
        rule.updatePreconditionVariables(_simEnvironment.getExternalVariables());
        if (rule instanceof DeviceRule) {
            rule.setInput(_simEnvironment.getNodesArray());
        } else if (rule instanceof LinkRule) {
            rule.setInput(_simEnvironment.getLinksArray());
        } else if (rule instanceof ExternalVariableRule) {
            rule.setInput(_simEnvArray);
        }
    }

    //running the next simulation cycle and increase tick value
    public boolean nextTick() throws InterruptedException, QueryExecutionException {
        if (_curentTick < _ticks) {
            _curentTick++;
            //System.out.println("Tick: " + _curentTick + " from "+ _ticks + " Time:" + new Date());
            this._simEnvironment.setExternalVariable(SETEXTERNALVARIABLE_TICK, _curentTick);
            nextTickSiquential();
            StatisticCollector.getInstance().TickEndedEvent();

            return true;
        }

        StatisticCollector.getInstance().ScenarioEndedEvent();

        return false;
    }

    public String ifNextTick() throws InterruptedException, QueryExecutionException {
        if (_curentTick < _ticks) {
            return _curentTick.toString();
        }
        return Boolean.toString(false);
    }

    public Integer getTick() {
        return this._curentTick;
    }

    public String getNextScenarioName() {
        if (_scenarios.size() > 0) {
            return _scenarios.get(0).getName();
        }
        return null;
    }

    //runing next simulation scenario if exists
    public boolean nextScenario() throws IOException, ClassNotFoundException, QueryExecutionException {

        if (_scenarios.size() > 0) {
            //this._simEnvironment = SimulatedEnvironment.fromBinaryFile(_systemFolder);
            this._simEnvironment = NetFileParser.read(_netFile);
            
            StatisticCollector.getInstance().ClearStatistics();
            return firstScenario();
        }

        return false;
    }

        public boolean firstScenario() throws IOException, ClassNotFoundException, QueryExecutionException {

        if (_scenarios.size() > 0) {
            this._curentTick = 0;
            RandomGenetor.setInstance(_seed);
            Scenario curentScenario = _scenarios.remove(0);
            StatisticCollector.getInstance().setScenarioValue(SETSCENARIO_KEY, curentScenario.getName());
            setRules(curentScenario.getRules());
            runInitRules();

            return true;
        }

        return false;
    }
    
    public boolean ifNextScenario() throws IOException, ClassNotFoundException, QueryExecutionException {
        return _scenarios.size() > 0;
    }

    //runs next scenario to the end
    @SuppressWarnings("empty-statement")
    public boolean runNextScenario() throws IOException, ClassNotFoundException, QueryExecutionException, InterruptedException {
        if (nextScenario()) {
            while (nextTick());
            return true;
        }
        return false;
    }

    @SuppressWarnings("empty-statement")
    public void runScenarioToEnd() throws IOException, ClassNotFoundException, QueryExecutionException, InterruptedException {
        while (nextTick());
    }

    //runs all scenarios in expiriment to the end
    @SuppressWarnings("empty-statement")
    public void runExpiriment() throws IOException, ClassNotFoundException, QueryExecutionException, InterruptedException {
        while (runNextScenario());
    }

    //creation of simulator object from XML
    public static Simulator fromXML(String path) throws Exception {
        Builder parser = new Builder();
        Element root = parser.build(new File(path)).getRootElement();
        Simulator sim = Simulator.getInstance();
        Elements children = root.getChildElements();
        parseExperiment(children, sim);

        return sim;
    }

    //parsing "expiriment" part
    private static void parseExperiment(Elements children, Simulator sim) throws InstantiationException, IOException, ClassNotFoundException, ParseException, IllegalAccessException, SQLException, QueryParseException {
        for (int i = 0; i < children.size(); i++) {

            Element child = children.get(i);

            // check the child of the scenario ( 2 types )
            switch (child.getLocalName()) {
                case XML_TAG_SIMULATION:
                    sim.setSimulationParams(child);
                    break;
                case XML_TAG_SCENARIO:
                    sim.readScenario(child);
                    break;
                default:
                    throw new ParseException("Unknown child of scenario:\n" + child.toXML(), -1);

            }
        }
    }

    //parsing "simulation" variables
    private void setSimulationParams(Element root) throws ParseException, ClassNotFoundException, InstantiationException, IllegalAccessException, IOException, SQLException {

        try {
            this._ticks = Integer.parseInt(root.getAttributeValue(XML_TAG_TICKS));
            StatisticCollector.getInstance().setScenarioValue(XML_TAG_TICKS, "" + this._ticks);
        } catch (NumberFormatException e) {
            throw new ParseException("Ticks property not defined", -1);
        }

        try {
            _seed = Integer.parseInt(root.getAttributeValue(XML_TAG_SEED));
            StatisticCollector.getInstance().setScenarioValue(XML_TAG_SEED, "" + _seed);
        } catch (NumberFormatException e) {
            //seed may not been defined
        }
        parseNetFile(root);

        Elements children = root.getChildElements();
        ReadStatListenersAndRoutingAlgorithm(children);

    }

    //parsing "StatisticListener" and "RoutingAlgorithm"
    private void ReadStatListenersAndRoutingAlgorithm(Elements children) throws IOException, InstantiationException, IllegalAccessException, SQLException, ClassNotFoundException {
        for (int i = 0; i < children.size(); i++) {
            Element child = children.get(i);
            switch (child.getLocalName()) {
                case XML_TAG_STATISTICLISTENER:
                    StatisticCollector.getInstance().addListener(StatisticListener.fromXML(child));
                    break;
                case XML_TAG_ROUTINGALGORITHM:
                    AbsRoutingAlgorithm alg = AbsRoutingAlgorithm.fromXML(child);
                    alg.setGraph(_simEnvironment);
                    Router.createInstance(alg);
                    alg.setGraph(null);
                    break;
            }
        }
        //this._simEnvironment = null;
    }

    //parsing "scenario" parts
    private void readScenario(Element root) throws ParseException, ClassNotFoundException, InstantiationException, IllegalAccessException, QueryParseException {
        if (_scenarios == null) {
            _scenarios = new LinkedList<>();
        }

        Scenario scen = new Scenario();
        scen.setName(root.getAttributeValue(NAME_KEY));
        List<Rule> allRules = new LinkedList<>();
        Elements children = root.getChildElements();

        for (int i = 0; i < children.size(); i++) {
            Element child = children.get(i);
            allRules.add(Rule.fromXML(child));
        }

        scen.setRules(allRules);
        _scenarios.add(scen);
    }

    private void parseNetFile(Element root) throws ParseException {

        try {
            // Net File Read & Parse
            _netFile = new File(root.getAttributeValue(XML_TAG_NETFILEPATH));
            StatisticCollector.getInstance().setScenarioValue(XML_TAG_NETFILEPATH, _netFile.getPath());
            this._simEnvironment = NetFileParser.read(_netFile);
//            //save topology to binary file for next scenario execution
//            this._simEnvironment.toBinaryFile(_systemFolder);

        } catch (IOException e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsString = sw.toString();
            System.err.println(exceptionAsString);
            throw new ParseException("Unable to read or parse net file", -1);
        }
    }
}
