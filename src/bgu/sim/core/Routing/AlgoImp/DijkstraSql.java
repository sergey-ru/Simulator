/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bgu.sim.core.Routing.AlgoImp;

import bgu.sim.core.Routing.DB.AbsRoutingDBSQL;
import bgu.sim.core.Routing.DB.dijkstraNetDevice;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import java.sql.SQLException;
import java.util.Collection;
import java.util.PriorityQueue;

/**
 *
 * @keren implementation of Dijkstra Algorithm
 */
public class DijkstraSql extends AbsRoutingDBSQL {

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

    private int getNextStepBySandV(dijkstraNetDevice s, dijkstraNetDevice v) {
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
        for (dijkstraNetDevice s : this.getGraph().values()) {
            s.MinDistance = Double.POSITIVE_INFINITY;
            s.Previous = null;
        }
    }

    // insert only one row of a node that has one or two neighbours
    private void insertSmartToSql(int s, Multimap<Integer, Integer> myMultimap) throws SQLException {
        int maxRow = 0;
        int index = -1;

        for (Integer nextStep : myMultimap.keySet()) { // map?
            if (maxRow < myMultimap.get(nextStep).size()) {
                maxRow = myMultimap.get(nextStep).size();
                index = nextStep;
            }
        }

        // if has a next step that appears many times
        if (maxRow > 5) {
            for (Integer nextStep : myMultimap.keySet()) {
                Collection<Integer> targets = myMultimap.get(nextStep);
                if (index != nextStep) {
                    for (Integer target : targets) {
                        writeToDataFile(s, target, nextStep);
                    }
                } else {
                    // this is the next step that appears a lot
                    writeToDataFile(s, 0, nextStep);
                }
            }
        } else {
            // if node has many neighbours, like node number 3
            for (Integer nextStep : myMultimap.keySet()) {
                Collection<Integer> col = myMultimap.get(nextStep);
                for (Integer target : col) {
                    writeToDataFile(s, target, nextStep);
                }
            }
        }
    }

    @Override
    public void calculateAllShortestPaths() throws SQLException, ClassNotFoundException {
        //if ()
        openDataFile(this.getGraph().values().size(), this.getName());
        if (!isDataExist()) {
            clearDataFile();

            int nextStep;
            Multimap<Integer, Integer> myMultimap;

            for (dijkstraNetDevice s : this.getGraph().values()) {
                myMultimap = ArrayListMultimap.create();
                initInfinityDistance();
                dijkstra(s);
                for (dijkstraNetDevice v : this.getGraph().values()) {
                    if (s != v) {
                        nextStep = getNextStepBySandV(s, v);
                        myMultimap.put(nextStep, v.getObjectId());
                    }
                }
                insertSmartToSql(s.getObjectId(), myMultimap);
            }
            closeDataFile();
        }
    }

    @Override
    public int getNextStep(int Source, int Target) throws SQLException {
        return readFromDataFile(Source, Target);
    }
}
