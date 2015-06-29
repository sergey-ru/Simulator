/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bgu.sim.data;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * @author Sergey
 */
public class MessageQueue {

    private final Queue<Message> _queue;
    private int _outMessagesCount;
    private final int _ownerId;

    public MessageQueue(int ownerId) {
        this._queue = new LinkedList<>();
        this._outMessagesCount = 0;
        this._ownerId = ownerId;
    }

    private boolean Poll(Message m) {
        if (_ownerId != m.getDestination()) {
            _outMessagesCount--;
        }
        return _queue.remove(m);
    }

    public Message Poll() {
        Message m = _queue.poll();
        if (m != null) {
            if (_ownerId != m.getDestination()) {
                _outMessagesCount--;
            }
        }
        return m;
    }

    public boolean hasMessages() {
        return _queue.size() > 0;
    }

    public boolean Push(Message m) {
        if (_ownerId != m.getDestination()) {
            _outMessagesCount++;
        }
        return this._queue.add(m);
    }

    public boolean PushAndRoute(Message m) {
        m.addRoute(_ownerId);
        return Push(m);
    }

    public boolean HasOutMessages() {
        return _outMessagesCount > 0;
    }

    public boolean HasOutMessages(Integer currentTick) {
        for (Message message : _queue) {
            if (message.getDestination() != _ownerId && message.getTickOfLastChange() < currentTick) {
                return true;
            }
        }
        return false;
    }

    public Message PollOutMessage() {
        for (Message message : _queue) {
            if (message.getDestination() != _ownerId) {
                Poll(message);
                return message;
            }
        }
        return null;
    }

    public Message PollOutMessage(int currentTick) {
        for (Message message : _queue) {
            if (message.getDestination() != _ownerId && message.getTickOfLastChange() < currentTick) {
                Poll(message);
                message.setTickOfLastChange(currentTick);
                return message;
            }
        }
        return null;
    }

    public boolean HasReceivedMessages() {
        return _outMessagesCount != _queue.size();
    }

    public Message PollReceivedMessage() {
        for (Message message : _queue) {
            if (message.getDestination() == _ownerId) {
                Poll(message);
                return message;
            }
        }
        return null;
    }

    public Iterator<Message> getQueueIterator() {
        return this._queue.iterator();
    }
}
