/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bgu.sim.netFile;

import static bgu.sim.netFile.flickrIdToNetId.read;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;

/**
 *
 * @author sergei
 */
public class flickrIdToNetIdWithImg {

    private static final HashMap<Integer, Integer> IdMap = new HashMap<>();

    public static void main1(String[] args) {
        File file = new File("C:\\Users\\admin\\Desktop\\Flickr\\flickr.net");
        try {
            // read net file and make dictionary of id's
            read(new FileInputStream(file));
            readFlickrData();

        } catch (IOException ex) {
            Logger.getLogger(flickrIdToNetIdWithImg.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println("Done.");
    }

    private static void readFlickrData() throws FileNotFoundException, IOException {

        // read flickr data
        File reader = new File("C:\\Users\\admin\\Desktop\\Flickr2\\flickr-all-photos.txt");
        FileWriter writer = new FileWriter("C:\\Users\\admin\\Desktop\\Flickr2\\flickr-all-photos.csv");

        InputStream in = new FileInputStream(reader);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String line;

        while ((line = br.readLine()) != null) {
            String[] data = line.split("\\s+");
            if (!IdMap.containsKey(Integer.parseInt(data[2]))) {
                System.err.println("Key " + data[2] + " is not found.");
            } else {
                int i = 0;
                for (String data1 : data) {
                    i++;
                    if (i == 3) {
                        data1 = IdMap.get(Integer.parseInt(data1)).toString();
                    }
                    writer.append(data1 + ",");
                }
                writer.append('\n');
            }
        }
        
        writer.flush();
        writer.close();
    }

    public static void read(InputStream in) throws IOException {

        try (BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
            String line;
            Stage stage = Stage.NO_READ;

            Matcher m;
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
