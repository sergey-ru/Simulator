/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bgu.sim.netFile;

import bgu.sim.data.NetDevice;
import bgu.sim.data.NetLink;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Sergey
 */
public class SimulatedEnvironment {
    
    private final NetLink[] _linksMap;
    private final NetDevice[] _nodesMap;
    private final Map<String, Object> _extVarMap;
    
    private SimulatedEnvironment(int nodesSize, int edgesSize) {
        _nodesMap = new NetDevice[nodesSize];
        _linksMap = new NetLink[edgesSize];
        _extVarMap = new HashMap<>();
    }
    
    public static SimulatedEnvironment CreateInstance(int nodesSize, int edgesSize) {
        SimulatedEnvironmentHolder.INSTANCE = new SimulatedEnvironment(nodesSize, edgesSize);
        return SimulatedEnvironmentHolder.INSTANCE;
    }
    
    public static SimulatedEnvironment getInstance() {
        return SimulatedEnvironmentHolder.INSTANCE;
    }
    
    // Get Object By Its Id
    public NetDevice getNode(Integer Id) {
        return this._nodesMap[Id-1];
    }

    public NetLink getLink(Integer Id) {
        return this._linksMap[Id-1];
    }
    
    public Object getExternalVariable(String key) {
        return this._extVarMap.get(key);
    }

    public Map getExternalVariables() {
        return this._extVarMap;
    }

    public void setExternalVariable(String key, Object value) {
        this._extVarMap.put(key, value);
    }

    public void addExternalVariable(String key, Object value) {
        this._extVarMap.put(key, value);
    }

    public NetDevice[] getNodesArray() {
        return _nodesMap;
    }

    public NetLink[] getLinksArray() {
        return _linksMap;
    }

    public int getMaxNodeId() {
        return _nodesMap.length;
    }
    
    public void addLink(NetLink link) {
        _linksMap[link.getObjectId()-1]=link;
    }

    public void addNode(NetDevice device) {
         _nodesMap[device.getObjectId()-1] = device;
    }
    
    private static class SimulatedEnvironmentHolder {

        private static SimulatedEnvironment INSTANCE = null;
    }
}
