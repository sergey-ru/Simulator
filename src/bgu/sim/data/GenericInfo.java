package bgu.sim.data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Keren
 */
public class GenericInfo implements Info, Serializable {

    // All Attributes
    Map<String, Object> _attributes;
    //List<SystemEvent> _eventsLIst;
    
    public GenericInfo() {
        _attributes = new HashMap<>();
        // _eventsLIst = new LinkedList<>();
    }
    
    // set the map attributes
    public void setAttributes(Map<String, Object> attributes) {
        this._attributes = attributes;
    }

    @Override // get attribute
    public Object get(String key) {
        return _attributes.get(key);
    }

    @Override
    public Integer getInt(String key) {
        if (_attributes.containsKey(key)) {
            return (Integer) _attributes.get(key);
        } else {
            return null;
        }
    }

    @Override
    public Boolean getBoolean(String key) {
        if (_attributes.containsKey(key)) {
            return (Boolean) _attributes.get(key);
        } else {
            return null;
        }
    }

    @Override
    public List<SystemEvent> getListOfSystemEvents(String key) {
        if (!_attributes.containsKey(key)) {
            this.set(key, new LinkedList<SystemEvent>());
        }
        return (List<SystemEvent>) _attributes.get(key);
    }

    @Override // add property / attribute
    public void set(String key, Object value) {
        _attributes.put(key, value);
    }

    @Override
    public String toString() {
        return _attributes + "";
    }

    @Override
    public void IncreaseInt(String key, int amount) {
        if (_attributes.containsKey(key)) {
            _attributes.put(key, (Integer) _attributes.get(key) + amount);
        } else {
            _attributes.put(key, amount);
        }
    }

    @Override
    public void DecreaseInt(String key, int amount) {
        if (_attributes.containsKey(key)) {
            _attributes.put(key, (Integer) _attributes.get(key) - amount);
        } else {
            _attributes.put(key, -amount);
        }
    }

    @Override
    public Boolean containsKey(String key) {
        return _attributes.containsKey(key);
    }

    @Override
    public Boolean RemoveKey(String key) {
        return _attributes.remove(key) != null;
    }
}
