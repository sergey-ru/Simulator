/*
 * Edge
 */
package bgu.sim.data;

/**
 *
 * @author Keren
 */
public class NetLink extends SimulatedObj {

    private final int _sourceID; // Source 
    private final int _destinationID; // Target 
    private final boolean _isDirected;

    public NetLink(int ObjectId, int source, int destination, boolean isDirected) {
        super(ObjectId);
        this._sourceID = source;
        this._destinationID = destination;
        this._isDirected = isDirected;
    }

    public int getSourceID() {
        return _sourceID;
    }

    public int getDestinationID() {
        return _destinationID;
    }
    
    public boolean IsDirected(){
        return _isDirected;
    }
}
