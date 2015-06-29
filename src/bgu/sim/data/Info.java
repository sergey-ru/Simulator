/*
 * Info
 */
package bgu.sim.data;

import java.util.List;

/**
 *
 * @author Keren
 */
public interface Info {

    Object get(String key);

    Integer getInt(String key);

    Boolean getBoolean(String key);
    
    List<SystemEvent> getListOfSystemEvents(String key);
    
    Boolean containsKey(String key);
    
    Boolean RemoveKey(String key);

    void set(String key, Object value);
    
    void IncreaseInt(String key, int amount);
    
    void DecreaseInt(String key, int amount);
    
//    void AddEvent(SystemEvent event);
//    
//    void AddEvent(String p_CSVRow);
//    
//    void addEvents(List<SystemEvent> events);
//    
//    List<SystemEvent> getEvents();
}
