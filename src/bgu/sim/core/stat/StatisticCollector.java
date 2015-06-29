/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bgu.sim.core.stat;

import bgu.sim.data.Message;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Sergey
 */
public class StatisticCollector {

    private Map<String, RefInteger> _collector = new HashMap<>();
    private List<String> _collectorHeaders = new ArrayList<>();
    private List<RefInteger> _collectorData = new ArrayList<>();
    private Map<String, String> _scenarioData = new HashMap<>();
    private List<Message> _messages = new LinkedList();
    private final List<StatisticListener> _listeners = new LinkedList<>();
    private int counterForScenarioData = 1;
    
    
    public List<String> getCollectorHeaders() {
        return _collectorHeaders;
    }

    public List<RefInteger> getCollectorData() {
        return _collectorData;
    }
    

    
    private StatisticCollector(){
    }
    
    public static StatisticCollector getInstance() {
        return StatisticCollectorHolder.INSTANCE;
    }

    private static class StatisticCollectorHolder {

        private static StatisticCollector INSTANCE = new StatisticCollector();
    }
    
    //add listener that will recieve statistics events
    public void addListener(StatisticListener listener) {
        _listeners.add(listener);
    }

    //remove listener that recieves statistics events
    public void removeListener(StatisticListener listener) {
        List<StatisticListener> toRemove = new LinkedList<>();

        for (StatisticListener statisticListener : _listeners) {
            if (statisticListener.getClass() == listener.getClass()) {
                toRemove.add(statisticListener);
            }
        }
        _listeners.removeAll(toRemove);
    }

    //removes all listeners
    public void removeAllListeners() {
        _listeners.clear();
    }

    //fire event scenario creation is finished 
    public void ScenarioStartedEvent() {

        for (StatisticListener statisticListener : _listeners) {
            statisticListener.ScenarioStardedEventHandler(_scenarioData);
        }
    }

    //fire event tick execution finished
    public void TickEndedEvent() {

        for (StatisticListener statisticListener : _listeners) {
            statisticListener.TickEndedEventHandler(_collector,_collectorHeaders,_collectorData, _messages);
        }
        _messages = new LinkedList();
    }

    //fire event simulation ended
    public void ScenarioEndedEvent() {

        for (StatisticListener statisticListener : _listeners) {
            statisticListener.ScenarioEndedEventHandler();
        }
    }

    //
    public void addMessage(Message m) {
        _messages.add(m);
    }

    //increase simulated value if key not exist it is created 
    public void increaseValue(String key, Integer value) {

        if (_collector.containsKey(key)) {
            RefInteger refInt = _collector.get(key);
            refInt.setValue(value+refInt.getValue());
            //_collector.put(key, _collector.get(key) + value);
        } else {
            RefInteger refInt = new RefInteger(value);
            _collector.put(key, refInt);
            _collectorHeaders.add(key);
            _collectorData.add(refInt);
        }
    }

    //decrease simulated value if key not exist it is created
    public void decreaseValue(String key, Integer value) {
        if (_collector.containsKey(key)) {
            RefInteger refInt = _collector.get(key);
            refInt.setValue(refInt.getValue()-value);
        } else {
            RefInteger refInt = new RefInteger(-value);
            _collector.put(key, refInt);
            _collectorHeaders.add(key);
            _collectorData.add(refInt);
        }
    }

    public void setScenarioValue(String key, String value) {
        _scenarioData.put(key, value);
    }

    public String getScenarioValue(String key) {
        return _scenarioData.get(key);
    }

    public void ClearStatistics() {
        _collector = new HashMap<>();
        _collectorHeaders = new ArrayList<>();
        _collectorData = new ArrayList<>();
        _scenarioData = new HashMap<>();
    }
    
    public class RefInteger 
    {
        private int _value;
        
        public RefInteger(Integer value)
        {
            this._value = value;
        }
        
        public RefInteger(int value)
        {
            this._value = value;
        }
        
        public int getValue() {
            return _value;
        }

        public void setValue(int value) {
            this._value = value;
        }
    }
}
