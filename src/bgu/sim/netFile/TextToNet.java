/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bgu.sim.netFile;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

/**
 *
 * @author sergei
 */
public class TextToNet {

    public static void TextToNetConvert(String textFile, String netFile) {
        BufferedReader br = null;
        BufferedWriter bw = null;
        String line;
        String[] splited;
        int count, s, d, t;
        count = 0;
        TreeMap<Integer, HashSet<Integer>> map = new TreeMap();
        TreeMap<Integer, HashSet<Integer>> edges = new TreeMap();
        TreeMap<Integer, HashSet<Integer>> arcs = new TreeMap();
        TreeMap<Integer,Integer> nodes = new TreeMap<>();
        System.out.println("Reading started ...   "+Calendar.getInstance().getTime());
        try {

            br = new BufferedReader(new FileReader(textFile));
            int nodeID=0;
            while ((line = br.readLine()) != null) {
                count++;
                splited = line.split("[\\t,]");
                if (splited.length == 2) {
                    s = Integer.parseInt(splited[0]);
                    d = Integer.parseInt(splited[1]);
                    if(!nodes.containsKey(s)){
                        nodeID++;
                        nodes.put(s,nodeID);
                    }
                    if(!nodes.containsKey(d)){
                        nodeID++;
                        nodes.put(d,nodeID);
                    }
                    if (map.get(s) == null) {
                        map.put(s, new HashSet<Integer>());
                    }
                    map.get(s).add(d);
                }
            }
            
            System.out.println("Started mapping ...   "+Calendar.getInstance().getTime());
            for (Integer source : map.keySet()) {
                for (Integer destination : map.get(source)) {
                    //check if Edges
                    if (map.get(destination) != null && map.get(destination).contains(source)) {
                        //check if contains edge with oposite direction
                        if (edges.get(destination) == null || (edges.get(destination) != null && !edges.get(destination).contains(source))) {
                            //add edge
                            if (!edges.containsKey(source)) {
                                edges.put(source, new HashSet<Integer>());
                            }
                            edges.get(source).add(destination);
                        }

                    } 
                    //check if Arc
                    else {
                        //add edge
                        if (!arcs.containsKey(source)) {
                            arcs.put(source, new HashSet<Integer>());
                        }
                        arcs.get(source).add(destination);
                    }
                }
            }
            
            map=null;
            System.out.println("Writing nodes ...   " + Calendar.getInstance().getTime());
            bw = new BufferedWriter(new FileWriter(netFile));
            bw.write("*Vertices "+nodes.size());
            bw.newLine();
            
            ValueComparator bvc =  new ValueComparator(nodes);
            TreeMap<Integer,Integer> sorted_map = new TreeMap<>(bvc);
            sorted_map.putAll(nodes);
            
            
            for (Map.Entry entry : sorted_map.entrySet()) {
                bw.write(entry.getValue() + " \"" +entry.getKey() +"\"");
                bw.newLine();
            }
            
            System.out.println("Writing edges ...   "+Calendar.getInstance().getTime());
            count =0;
            bw.write("*Edges");
            bw.newLine();
            for (Integer source : edges.keySet()) {
                for (Integer destination : edges.get(source)){
                    if(!Objects.equals(source, destination)){
                        count++;
                        bw.write(nodes.get(source) + " " +nodes.get(destination));
                        bw.newLine();
                    }
                }
            }
            System.out.println("Edges: "+count);
            count=0;
            
            System.out.println("Writing arcs ...   "+Calendar.getInstance().getTime());
            bw.write("*Arcs");
            bw.newLine();
            for (Integer source : arcs.keySet()) {
                for (Integer destination : arcs.get(source)){
                    if(!Objects.equals(source, destination)){
                        count++;
                        bw.write(nodes.get(source) + " " +nodes.get(destination));
                        bw.newLine();
                    }
                }
            }
            System.out.println("Arcs: "+count);
            System.out.println("DONE :)))))))))))))   "+Calendar.getInstance().getTime());

        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsString = sw.toString();
            System.err.println(exceptionAsString);
            
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                }
            }
            
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException e) {
                }
            }
        }
    }
}

class ValueComparator implements Comparator<Integer> {

    TreeMap<Integer,Integer> base;
    public ValueComparator(TreeMap<Integer,Integer> base) {
        this.base = base;
    }

    // Note: this comparator imposes orderings that are inconsistent with equals.    
    @Override
    public int compare(Integer a, Integer b) {
        if (base.get(a) >= base.get(b)) {
            return 1;
        } else {
            return -1;
        } // returning 0 would merge keys
    }
}
