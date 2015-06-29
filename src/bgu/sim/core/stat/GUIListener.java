/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bgu.sim.core.stat;

import bgu.sim.core.Simulator;
import bgu.sim.data.Message;
import bgu.sim.data.StatisticsDataStruct;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Keren Fruchter
 */
public class GUIListener extends StatisticListener {

    private static int mapIndex = 0;
    private static int rowIndex = 0;
    private boolean ifFirst = true;
    private static int maxTicks = 0;
    private static int scenarioNum = 0;
    private static List<Message> messages;
    private static String currScenarioData;
    private static HashMap<Integer, StatisticsDataStruct> statistics = new HashMap<>();
    private static HashMap<Integer, List<String>> statisticsForChart = new HashMap<>();

    public static int getNumberOfExistScenarios() {
        return scenarioNum;
    }

    public static Map<Integer, StatisticsDataStruct> getStatistics() {
        return statistics;
    }

    public static List<String> getChartStatistics(int scenario, int index) {
        int key = ((scenario) * maxTicks + index);
        if (statisticsForChart.containsKey(key)) {
            return statisticsForChart.get(key);
        } else {
            return null;
        }
    }

    public static List<Message> getMessages() {
        return messages;
    }

    public static String getCurrScenarioNumber() {
        return currScenarioData;
    }

    public static void reset() {
        mapIndex = 0;
        rowIndex = 0;
        maxTicks = 0;
        scenarioNum = 0;
        statistics = new HashMap<>();
        statisticsForChart = new HashMap<>();
    }

    //To KEREN 
    @Override
    public void TickEndedEventHandler(Map<String, StatisticCollector.RefInteger> snapshot, List<String> snapshotHeaders, List<StatisticCollector.RefInteger> snapshotData, List<Message> messages) {
        List<String> newList;
        GUIListener.messages = messages;

        // Init. get headers and initialize maxTicks
        if (ifFirst) {
            maxTicks = Simulator.getInstance().getTotalTicks();

            // add headers
            newList = new LinkedList<>();
            for (String key : snapshot.keySet()) {
                newList.add(key);
            }

            statistics.put(mapIndex, new StatisticsDataStruct(newList, scenarioNum));
            statisticsForChart.put(0, newList);
            rowIndex++;

            mapIndex++;
            ifFirst = !ifFirst;
        }

        // get values
        newList = new LinkedList<>();
        for (StatisticCollector.RefInteger value : snapshot.values()) {
            newList.add(Integer.toString(value.getValue()));
        }

        // map for Chart
        // key function : (scenario)*maxTicks + tick (row number).
        // key 0 : headers 
        int key = ((scenarioNum) * maxTicks + rowIndex);
        statisticsForChart.put(key, newList);
        rowIndex++;

        //map for csv
        statistics.put(mapIndex, new StatisticsDataStruct(newList, scenarioNum));
        mapIndex++;

    }

    @Override
    public void ScenarioStardedEventHandler(Map<String, String> scenarioData) {
        currScenarioData = scenarioData.get("scenarioName");
        scenarioNum++;
        ifFirst = true;
        rowIndex = 0;
    }

    @Override
    public void ScenarioEndedEventHandler() {

    }
}
