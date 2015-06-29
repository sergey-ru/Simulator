/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bgu.sim.api;

import static bgu.sim.Properties.StringsProperties.DATA_PATH;
import bgu.sim.core.Simulator;
import bgu.sim.data.NetDevice;
import bgu.sim.data.NetLink;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;

/**
 *
 * @author Keren Fruchter
 */
public class createJsonData {

    public createJsonData(String sessionId) throws Exception {

        JSONArray nodes = new JSONArray();
        JSONArray links = new JSONArray();
        //comment
        // get the net file path
        File netfile = Simulator.getInstance().getNetFile();
        if (netfile == null) {
            throw new NullPointerException("NetFile is null");
        }

//        try {
//            SimApi.parseNetFile(netfile);
//        } catch (IOException ex) {
//            System.err.println("Cant parse net file.");
//            throw new Exception("Cant parse net file.");
//        }

        // add nodes
        for (NetDevice netdevice : SimApi.getNodesArray()) {
            Map node = new LinkedHashMap();
            node.put("id", Integer.toString(netdevice.getObjectId()));
            node.put("type", netdevice.getInfo().get("rc") + "_" + netdevice.getInfo().get("nsp"));
            node.put("expanded", true);
            nodes.add(node);
        }

        // add links
        for (NetLink netlink : SimApi.getLinksArray()) {
            Map link = new LinkedHashMap();

            link.put("end", null);
            link.put("to", Integer.toString(netlink.getSourceID()));
            link.put("from", Integer.toString(netlink.getDestinationID()));
            link.put("type", "share");
            link.put("id", Integer.toString(netlink.getObjectId()));

            links.add(link);
        }

        JSONObject obj = new JSONObject();
        obj.put("nodes", nodes);
        obj.put("links", links);

        try {

            FileWriter file = new FileWriter(DATA_PATH + sessionId + ".json");
            file.write(obj.toJSONString());
            file.flush();
            file.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.print("Finished Writing JSON to: " + DATA_PATH + sessionId + ".json");
    }
}
