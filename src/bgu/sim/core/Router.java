/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bgu.sim.core;

import bgu.sim.core.Routing.DB.AbsRoutingAlgorithm;
import java.io.IOException;
import java.sql.SQLException;

/**
 *
 * @author keren
 */
public class Router {

    private static Router _instance = null;          // only one instance of Router
    private final AbsRoutingAlgorithm _routingAlgo;  // Some algorithm for shortest path

    protected Router(AbsRoutingAlgorithm alg) {
        this._routingAlgo = alg;
    }

    public static void createInstance(AbsRoutingAlgorithm alg) throws IOException, SQLException, ClassNotFoundException {
        if (_instance != null) {
            return;
        }
        _instance = new Router(alg);
        _instance.calculateAllShortestPaths();
    }

    public static Router getInstance() {
        return _instance;
    }

    // Run the algorithm
    private void calculateAllShortestPaths() throws SQLException, IOException, ClassNotFoundException {
        _routingAlgo.calculateAllShortestPaths();
    }

    public int getNextStep(int Source, int Target) throws IOException, SQLException {
        return _routingAlgo.getNextStep(Source, Target);
    }
}
