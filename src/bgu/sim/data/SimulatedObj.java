/*
 * Simulated Object
 */
package bgu.sim.data;

import java.io.Serializable;

/**
 *
 * @author Keren
 */
public abstract class SimulatedObj implements Serializable {

    private Info _info;
    private final int _objectId;
    private final MessageQueue _messageQueue;
    
    // Object in Simulator.
    public SimulatedObj(int ObjectId) {
        this._objectId = ObjectId;
        this._messageQueue = new MessageQueue(ObjectId);
    }

    public Info getInfo() {
        return _info;
    }

    public void setInfo(Info info) {
        this._info = info;
    }

    public int getObjectId() {
        return _objectId;
    }
    
    public MessageQueue getMessageQueue() {
        return _messageQueue;
    }
}
