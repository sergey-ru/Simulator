/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bgu.sim.actions.flickr;

import bgu.sim.core.Simulator;
import bgu.sim.core.stat.StatisticCollector;
import bgu.sim.data.GenericInfo;
import bgu.sim.data.NetDevice;
import bgu.sim.netFile.SimulatedEnvironment;
import bgu.sim.ruleEngine.action.AbsExternalAction;
import bgu.sim.ruleEngine.action.IndicativeFeature;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Keren Fruchter
 */
public class PropogateByData extends AbsExternalAction {

    // public XML property
    public String dataPath = "";

    //private int lastTick = 0;
    private String row = null;
    private BufferedReader reader = null;
    private String[] rowData = null;

    public PropogateByData() throws FileNotFoundException {
        super("The action propogates shared images to the devices by the flickr data file.", new IndicativeFeature[]{IndicativeFeature.NET_TRAFIC_OUT, IndicativeFeature.FILE_READ});
        StatisticCollector.getInstance().increaseValue("PropogateByData", 0);
    }

    @Override
    public Integer execute(SimulatedEnvironment input) {
        int infected = 0;
        try {
            if (reader == null) {
                reader = new BufferedReader(new FileReader(dataPath));
            }
            if (row == null) {
                row = reader.readLine();
                if (row != null) {
                    rowData = row.split(",");
                }
            }

            if (row != null) {
                int currTick = Simulator.getInstance().getTick();

                while (Integer.parseInt(rowData[2]) == currTick && row != null) {
                    boolean propogate = false;
                    NetDevice deviceSharedPhoto = input.getNode(Integer.parseInt(rowData[0]));
                    int flickrImgID = Integer.parseInt(rowData[1]);
                    if (!deviceSharedPhoto.getInfo().containsKey("AV")) {
//                        propogate = true;
                        for (Integer neighborID : deviceSharedPhoto.getNeighbors()) {
                            HashSet<Integer> hashSetOfNeighbor = (HashSet<Integer>) input.getNode(neighborID).getInfo().get("flickrImg");
                            if (hashSetOfNeighbor != null && hashSetOfNeighbor.contains(flickrImgID)) {
                                propogate = true;
                                break;
                            }
                        }
                    }
                 

                    if (propogate) {
                        if (!deviceSharedPhoto.getInfo().containsKey("flickrImg")) {
                            deviceSharedPhoto.getInfo().set("flickrImg", new HashSet<Integer>());
                        }

                        // add image to the hashset
                        ((HashSet<Integer>) deviceSharedPhoto.getInfo().get("flickrImg")).add(flickrImgID);
                        infected++;
                        StatisticCollector.getInstance().increaseValue(rowData[1], 1);
                        StatisticCollector.getInstance().increaseValue(this.getName(), 1);
                    }
                    row = reader.readLine();
                    if (row != null) {
                        rowData = row.split(",");
                    }
                }
            }

        } //reader.close();
        catch (Exception ex) {
            Logger.getLogger(PropogateByData.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
        return infected;
    }
}
