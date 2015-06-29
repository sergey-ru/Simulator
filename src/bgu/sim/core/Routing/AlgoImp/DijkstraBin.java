/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bgu.sim.core.Routing.AlgoImp;

import bgu.sim.core.Routing.DB.AbsRoutingDBBinary;
import bgu.sim.core.Routing.DB.dijkstraNetDevice;
import java.io.IOException;
import java.util.PriorityQueue;

/**
 *
 * @author keren implementation of Dijkstra Algorithm
 */
public class DijkstraBin extends AbsRoutingDBBinary {

    private void dijkstra(dijkstraNetDevice source) {
        source.MinDistance = 0.;
        PriorityQueue<dijkstraNetDevice> vertexQueue = new PriorityQueue<>();
        vertexQueue.add(source);

        while (!vertexQueue.isEmpty()) {
            dijkstraNetDevice u = vertexQueue.poll();
            
            // Visit each edge exiting u
            for (Integer vID : u.getNeighbors()) {
                dijkstraNetDevice v = this.getGraph().get(vID);
                double distanceThroughU = u.MinDistance + 1;
                
                if (distanceThroughU < v.MinDistance) {
                    vertexQueue.remove(v);
                    v.MinDistance = distanceThroughU;
                    v.Previous = u;
                    vertexQueue.add(v);
                }
            }
        }
    }

    private int getNextStepBySAndV(dijkstraNetDevice s, dijkstraNetDevice v) {
        dijkstraNetDevice x = v;
        dijkstraNetDevice res = null;
        
        while (x != s) {
            res = x;
            if (x.Previous == null) {
                return -1;
            }
            x = x.Previous;
        }
        
        if (res != null) {
            return res.getObjectId();
        } else {
            return -1;
        }
    }

    private void initInfinityDistance() {
        // init nodes
        for (dijkstraNetDevice s : this.getGraph().values()) {
            s.MinDistance = Double.POSITIVE_INFINITY;
            s.Previous = null;
        }
    }

    @Override
    public void calculateAllShortestPaths() throws IOException {
        // open bin file
        openDataFile(this.getGraph().values().size(), this.getName());
        // id data exists, dont run the algorithm
        if (!isDataExist()) {
            int nextStep;

            for (dijkstraNetDevice s : this.getGraph().values()) {
                initInfinityDistance();
                dijkstra(s);
                
                for (dijkstraNetDevice v : this.getGraph().values()) {
                    if (s != v) {
                        nextStep = getNextStepBySAndV(s, v);
                        writeToDataFile(s.getObjectId(), v.getObjectId(), nextStep);
                    }
                }
            }
        }
    }

    @Override
    public int getNextStep(int Source, int Target) throws IOException {
        return readFromDataFile(Source, Target);
    }
}
