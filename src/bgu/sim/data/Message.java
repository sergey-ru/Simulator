/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bgu.sim.data;

import java.util.AbstractMap;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Sergey
 */
public class Message {

    private int _source;
    private int _destination;
    private final AbstractMap.SimpleEntry<String, Object> _data;
    private List<Integer> _route;
    private final MessageType _type;
    // parameter for OneStepOnTick (in order not to rout the message few times on one tick)
    private int _tickOfLastChange;

    public Message(int source, int destination, AbstractMap.SimpleEntry<String, Object> data, MessageType type) {
        this._source = source;
        this._destination = destination;
        this._data = data;
        this._route = new LinkedList<>();
        this._type = type;
        this._tickOfLastChange = -1;
    }
    
    public Message(int source, int destination, AbstractMap.SimpleEntry<String, Object> data, MessageType type,int tickOfLastChange) {
        this._source = source;
        this._destination = destination;
        this._data = data;
        this._route = new LinkedList<>();
        this._type = type;
        this._tickOfLastChange = tickOfLastChange;
    }

    
    //just for GUI not used in SIM !!!
    public Message(int source, int destination, AbstractMap.SimpleEntry<String, Object> data, MessageType type, LinkedList<Integer> _route) {
        this._source = source;
        this._destination = destination;
        this._data = data;
        this._route = _route;
        this._type = type;
        this._tickOfLastChange = -1;
    }

    public LinkedList<Integer> getRouteList() {
        return (LinkedList<Integer>) _route;
    }

    public MessageType getType() {
        return _type;
    }

    public String getRoute() {
        String path = "";
        for (Integer node : _route) {
            path += node + ",";
        }
        return path.substring(0, path.length() - 1);
    }

    public AbstractMap.SimpleEntry<String, Object> getData() {
        return _data;
    }

    public int getSource() {
        return _source;
    }

    public void setSource(int source) {
        _source = source;
    }

    public void setDestination(int _destination) {
        this._destination = _destination;
    }

    public void addRoute(int source) {
        _route.add(source);
    }

    public int getDestination() {
        return _destination;
    }
    
    public int getTickOfLastChange() {
        return _tickOfLastChange;
    }
    
    public void setTickOfLastChange(int _tickOfLastChange) {
        this._tickOfLastChange = _tickOfLastChange;
    }

    public enum MessageType {

        Virus, Worm, DOS, Legitimate
    }
}
