package bgu.sim.core.Routing.DB;

import bgu.sim.data.NetDevice;
import bgu.sim.netFile.SimulatedEnvironment;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author keren
 */
public class dijkstraNetDevice implements Comparable<dijkstraNetDevice> {

    public double MinDistance = Double.POSITIVE_INFINITY;   // for dij algo
    public dijkstraNetDevice Previous;                      // for dij algo
    private int _ObjectId;
    protected List<Integer> _neighbors = new LinkedList<>();

    public dijkstraNetDevice(NetDevice netDevice) {
        this._ObjectId = netDevice.getObjectId();
        this.MinDistance = 0;
        for (int neighborID : netDevice.getNeighbors()) {
            this._neighbors.add(neighborID);
        }
    }

    public double getMinDistance() {
        return MinDistance;
    }

    public void setMinDistance(double minDistance) {
        this.MinDistance = minDistance;
    }

    public dijkstraNetDevice getPrevious() {
        return Previous;
    }

    public void setPrevious(dijkstraNetDevice previous) {
        this.Previous = previous;
    }

    public int getObjectId() {
        return _ObjectId;
    }

    public void setObjectId(int ObjectId) {
        this._ObjectId = ObjectId;
    }

    public List<Integer> getNeighbors() {
        return _neighbors;
    }

    public void setNeighbors(List<Integer> neighbors) {
        this._neighbors = neighbors;
    }

    @Override
    public int compareTo(dijkstraNetDevice o) {
        if (this.MinDistance - o.MinDistance > 0) {
            return 1;
        } else if (this.MinDistance - o.MinDistance < 0) {
            return -1;
        } else {
            return 0;
        }
    }
}
