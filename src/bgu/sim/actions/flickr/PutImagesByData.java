/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bgu.sim.actions.flickr;

import bgu.sim.core.stat.StatisticCollector;
import bgu.sim.data.NetDevice;
import bgu.sim.core.Simulator;
import bgu.sim.netFile.SimulatedEnvironment;
import bgu.sim.ruleEngine.action.AbsInitAction;
import bgu.sim.ruleEngine.action.IndicativeFeature;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Keren
 */
public class PutImagesByData extends AbsInitAction {

    // public XML property
    public int propogationAmount = 10;
    public String dataPath = "";

    // private
    private String row = null;
    private BufferedReader reader = null;
    private String[] rowData = null;

    public PutImagesByData() {
        super("The action infects some devices with virus", new IndicativeFeature[]{});
    }

    @Override
    public Integer execute(SimulatedEnvironment input) {
        int infected = 0;
        try {

            reader = new BufferedReader(new FileReader(dataPath));

            while ((row = reader.readLine()) != null) {
                rowData = row.split(",");

                NetDevice device = input.getNode(Integer.parseInt(rowData[2]));
                if (!device.getInfo().containsKey("flickrImg")) {
                    device.getInfo().set("flickrImg", new HashSet<Integer>());
                }

                // add image to the hashset
                ((HashSet<Integer>) device.getInfo().get("flickrImg")).add(Integer.parseInt(rowData[0]));
                infected++;
            }
        } 
        catch (Exception ex) {
            Logger.getLogger(PropogateByData.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }

        StatisticCollector.getInstance().increaseValue(this.getName(), infected);

        return infected;
    }
}
