/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bgu.sim.netFile;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.sun.tools.javac.util.Pair;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;

/**
 *
 * @author sergei
 */
public class flickrIdToNetId {

    private static final HashMap<Integer, Integer> IdMap = new HashMap<>();
    private static final ListMultimap<String, Pair<String, String>> multimap = ArrayListMultimap.create();
    private static final Set<Integer> keys = new HashSet<>();
    private static final Set<String> imageskeys = new HashSet<>();
    private static Set<Integer> treeSet;

    public static void main1(String[] args) {
        File file = new File("C:\\Users\\admin\\Desktop\\Flickr\\flickr.net");
        try {
            // read net file and make dictionary of id's
            read(new FileInputStream(file));
            // read flickr
            readFlickrData();
            // make new file
            rewriteFlickrData();

        } catch (IOException ex) {
            Logger.getLogger(flickrIdToNetId.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println("Done.");
    }

    private static void readFlickrData() throws FileNotFoundException, IOException {

        // read flickr data
        File file = new File("C:\\Users\\admin\\Desktop\\aaa.txt");
        InputStream in = new FileInputStream(file);

        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String line;

        while ((line = br.readLine()) != null) {
            String[] data = line.split("\\s+");
            multimap.put(data[2], new Pair<>(data[0], data[1]));
            keys.add(Integer.parseInt(data[2]));
        }

        // sorting
        treeSet = new TreeSet<>(keys);
    }

    private static void rewriteFlickrData() throws IOException {
        int tickCounter = 0;
        int flickrId;
        try (FileWriter writer = new FileWriter("C:\\Users\\admin\\Desktop\\aaa.csv")) {
            for (Integer realTickFlickrData : treeSet) {
                if (multimap.containsKey("" + realTickFlickrData)) {
                    // get data list
                    List<Pair<String, String>> dataList = multimap.get("" + realTickFlickrData);

                    // update new tickCounter
                    tickCounter++;
                    boolean ifwrote = false;

                    for (int i = 0; i < dataList.size(); i++) {
                        Pair<String, String> data = dataList.get(i);

                        // flickr id
                        int flickrIdInt = Integer.parseInt(data.fst);
                        try {
                            flickrId = IdMap.get(flickrIdInt);

                            ifwrote = true;
                            writer.append("" + flickrId);
                            writer.append(',');

                            writer.append("" + data.snd);
                            writer.append(',');

                            writer.append("" + tickCounter);
                            writer.append(',');

                            if (imageskeys.contains(data.snd)) {
                                writer.append("" + 0);
                            } else {
                                imageskeys.add(data.snd);
                                writer.append("" + 1);
                            }

                            writer.append('\n');
                        } catch (Exception ex) {
                            System.err.println("could not find " + flickrIdInt);
                        }
                    }

                    if (!ifwrote) {
                        tickCounter--;
                    }

                    // remove it
                    multimap.removeAll("" + realTickFlickrData);
                }
            }

            writer.flush();
        }
    }

    public static void read(InputStream in) throws IOException {

        try (BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
            String line;
            Stage stage = Stage.NO_READ;

            Matcher m;
            Map<String, Object> additional = new HashMap<>();
            Map<String, Object> lineAdditional;
            while ((line = br.readLine()) != null) {

                line = line.trim();
                boolean done = line.isEmpty();
                while (!done) {
                    done = true;

                    switch (stage) {
                        case NO_READ:
                            if (GraphRegex.VERTICES_BEGINING.matcher(line).matches()) {
                                stage = Stage.READ_VERTICES;
                                System.out.println("READ_VERTICES");
                            } else if (GraphRegex.EDGES_BEGINING.matcher(line).matches()) {
                                stage = Stage.READ_EDGES;
                                System.out.println("READ_EDGES");
                            } else if (GraphRegex.ARCS_BEGINING.matcher(line).matches()) {
                                stage = Stage.READ_ARCS;
                                System.out.println("READ_ARCS");
                            } else {
                                throw new IOException("could not understand line: '" + line + "'");
                            }
                            break;
                        case READ_VERTICES:
                            m = GraphRegex.VERTICES_LINE_VERSION_2.matcher(line);
                            //additional.clear();
                            Integer id1;
                            Integer id2;
                            lineAdditional = new HashMap<>();

                            if (m.find()) {
                                id1 = Integer.parseInt(m.group(1).trim());
                                line = line.substring(m.end());
                                line = line.replace("\"", "");
                                id2 = Integer.parseInt(line.trim());

                                IdMap.put(id2, id1);
                            } else {
                                //m = GraphRegex.VERTICES_LINE_VERSION_2.matcher(line);
                                m = GraphRegex.VERTICES_LINE.matcher(line);
                                if (m.find()) {

                                    id1 = Integer.parseInt(m.group(1).trim());
                                    lineAdditional.put("label", m.group(2).trim());
                                    lineAdditional.put("coord_x", m.group(3).trim());
                                    lineAdditional.put("coord_y", m.group(4).trim());
                                    lineAdditional.put("coord_z", m.group(5).trim());
                                    line = line.substring(m.end());
                                } else {
                                    stage = Stage.NO_READ;
                                    done = false;
                                }
                            }
                            break;
                        case READ_EDGES:
                            break;
                        case READ_ARCS:
                            break;
                        default:
                            throw new AssertionError(stage.name());
                    }
                }
            }
        }
    }

    private static enum Stage {

        NO_READ,
        READ_VERTICES,
        READ_EDGES,
        READ_ARCS;
    }
}
