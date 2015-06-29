/*
 * Simulator Device type (Node)
 */
package bgu.sim.data;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Keren
 */
public class NetDevice extends SimulatedObj {

    private final List<Integer> _neighbors;
    private final List<Integer> _links;

    public NetDevice(int ObjectId) {
        super(ObjectId);
        _neighbors = new LinkedList<>();
        _links = new LinkedList<>();
    }

    public List<Integer> getNeighbors() {
        return _neighbors;
    }

    public void addNeighbor(int neighbor) {
        this._neighbors.add(neighbor);
    }
    
    public List<Integer> getLinks() {
        return _links;
    }

    public void addLink(int linkID) {
        this._links.add(linkID);
    }
}
