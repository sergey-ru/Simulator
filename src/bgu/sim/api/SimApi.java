/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor....
 */
package bgu.sim.api;

import bgu.sim.data.NetDevice;
import bgu.sim.data.NetLink;
import bgu.sim.netFile.NetFileParser;
import bgu.sim.netFile.SimulatedEnvironment;
import bgu.sim.reflection.ClassesLister;
import bgu.sim.ruleEngine.property.Property;
import bgu.sim.core.Simulator;
import bgu.sim.core.stat.GUIListener;
import bgu.sim.data.Message;
import bgu.sim.data.StatisticsDataStruct;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.josql.QueryExecutionException;
import static bgu.sim.Properties.StringsProperties.*;
import bgu.sim.data.Info;

/**
 *
 * @author Keren Fruchter
 */
public class SimApi {

    private static Simulator _simTest;
    //private static SimulatedEnvironment _parser = null;

    public static String getSimulatorScenarioXmlPath() {
        return SIMULATOR_SCENARIO_XML_PATH;
    }

    public static void setSimulatorScenarioXmlPath(String newPath) {
        SIMULATOR_SCENARIO_XML_PATH = newPath;
    }

    /*
    get the properties list of a class 
     */
    public static List<String> getPropertyList(String name) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        ClassesLister allActions = ClassesLister.getInstance();
        List<Property> pList;
        pList = allActions.getClassProperties(name);
        List<String> pListNames = new ArrayList<>();

        for (Property property : pList) {
            pListNames.add(property.getMetadata().getName());
        }
        return pListNames;
    }

    public static List<Class> getActionsList() {
        ClassesLister allActions = ClassesLister.getInstance();
        return allActions.GetInitActions();
    }

    public static List<Class> getDevicesActionsList() {
        ClassesLister allActions = ClassesLister.getInstance();
        return allActions.GetDeviceActions();
    }

    public static List<Class> getExternalActionsList() {
        ClassesLister allActions = ClassesLister.getInstance();
        return allActions.GetExternalActions();
    }

    public static List<Class> getLinkActionsList() {
        ClassesLister allActions = ClassesLister.getInstance();
        return allActions.GetLinkActions();
    }

    public static List<Class> getStatisticListenersList() {
        ClassesLister allActions = ClassesLister.getInstance();
        return allActions.GetStatisticListeners();
    }

    public static List<Class> getRoutingAlgorithmsList() {
        ClassesLister allActions = ClassesLister.getInstance();
        return allActions.GetRoutingAlgorithms();
    }

    // base init
    public static void initBaseSim() throws Exception {
        _simTest = Simulator.fromXML(getSimulatorScenarioXmlPath());
//        _simTest.init();
//
//        File netfile = Simulator.getInstance().getNetFile();
//        parseNetFile(netfile);
    }

    public static void resetSim() throws Exception {
        Simulator.resetInstance();
        GUIListener.reset();
        initBaseSim();
    }

    public static boolean ifNextScenario() throws IOException, ClassNotFoundException, QueryExecutionException {
        return _simTest.ifNextScenario();
    }

    public static boolean nextTick() throws InterruptedException, QueryExecutionException {
        return _simTest.nextTick();
    }

    public static String getNextScenarioName() {
        return _simTest.getNextScenarioName();
    }

    public static String getFirstScenarioName() {
        return _simTest.getNextScenarioName();
    }

    public static boolean nextScenario() throws IOException, ClassNotFoundException, QueryExecutionException {
        return _simTest.nextScenario();
    }

    /*
    Return the current tick, else return false
    */
    public static String ifNextTick() throws InterruptedException, QueryExecutionException {
        return _simTest.ifNextTick();
    }

    public static void runFullScenario() throws IOException, ClassNotFoundException, QueryExecutionException, InterruptedException {
        _simTest.runScenarioToEnd();
    }

//    public static void parseNetFile(File netfile) throws IOException {
//        _parser = NetFileParser.read(netfile);
//    }

    public static NetDevice[] getNodesArray() {
        return _simTest.getSimEnvironment().getNodesArray();
    }

    public static NetLink[] getLinksArray() {
        return _simTest.getSimEnvironment().getLinksArray();
    }

    public static int getNumberOfNodes() {
        return _simTest.getSimEnvironment().getNodesArray().length;
    }

    public static String getNodeInfo(int nodeId) throws IOException, ClassNotFoundException, QueryExecutionException {
        String info;

        NetDevice[] nodes = _simTest.getSimEnvironment().getNodesArray();
        NetDevice node = nodes[nodeId - 1];
        info = node.getInfo().toString();

        return info;
    }

    public static Map<Integer, StatisticsDataStruct> getStatistics() {
        return GUIListener.getStatistics();
    }

    public static List<String> getChartStatistics(int scenario, int tick) {
        return GUIListener.getChartStatistics(scenario, tick);
    }

    public static int getNumberOfExistScenarios() {
        return GUIListener.getNumberOfExistScenarios();
    }

    public static List<Message> getMessages() {
        return GUIListener.getMessages();
    }

    public static String getCurrScenarioNumber() {
        return GUIListener.getCurrScenarioNumber();
    }
    
    public static int getCurrTick() {
        return _simTest.getTick();
    }

    public static int getNodesCount() {
        return _simTest.getSimEnvironment().getNodesArray().length;
    }

    public static String getNodeInfo(int nodeId, String nodeProp) {
        String val = "";

        NetDevice[] nodes = _simTest.getSimEnvironment().getNodesArray();
        NetDevice node = nodes[nodeId - 1];
        Info info = node.getInfo();
        if (info.containsKey(nodeProp)) {
            val = info.get(nodeProp).toString();
        } else {
            nodeProp = nodeProp.substring(0, 1).toLowerCase() + nodeProp.substring(1);
            if (info.containsKey(nodeProp)) {
                val = info.get(nodeProp).toString();
            }
        }

        return val;
    }
}
