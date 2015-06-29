package bgu.sim.core;

import java.util.Random;

/**
 *
 * @author Keren
 */
public class RandomGenetor {

    private static Random _instance = null;

    protected RandomGenetor() {
    }

    public static Random getInstance() {
        if (_instance == null) {
            _instance = new Random();
        }
        return _instance;
    }

    public static void setInstance(Integer seed) {
        _instance = new Random();
        if(seed!=null){
            _instance.setSeed(seed);
        }
    }
}
