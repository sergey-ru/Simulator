package bgu.sim.netFile;

import bgu.sim.data.GenericInfo;
import bgu.sim.data.NetLink;
import bgu.sim.data.NetDevice;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

/**
 *
 * @author bennyl
 */
public class NetFileParser {

    public static SimulatedEnvironment read(File file) throws IOException {
        return read(new FileInputStream(file));
    }

    public static SimulatedEnvironment read(InputStream in) throws IOException {
        int nextEdgeId = 1;
        Map<Integer, NetDevice> deviceMap = new HashMap<>();
        List<NetLink> edgeList = new LinkedList<>();
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
                                //System.out.println("READ_VERTICES");
                            } else if (GraphRegex.EDGES_BEGINING.matcher(line).matches()) {
                                stage = Stage.READ_EDGES;
                                //System.out.println("READ_EDGES");
                            } else if (GraphRegex.ARCS_BEGINING.matcher(line).matches()) {
                                stage = Stage.READ_ARCS;
                                //System.out.println("READ_ARCS");
                            } else {
                                throw new IOException("could not understand line: '" + line + "'");
                            }
                            break;
                        case READ_VERTICES:
                            //m = GraphRegex.VERTICES_LINE.matcher(line);
                            m = GraphRegex.VERTICES_LINE_VERSION_2.matcher(line);
                            //additional.clear();
                            Integer idx = null;
                            lineAdditional = new HashMap<>();

                            if (m.find()) {
                                idx = Integer.parseInt(m.group(1).trim());
                                line = line.substring(m.end());
                            } else {
                                //m = GraphRegex.VERTICES_LINE_VERSION_2.matcher(line);
                                m = GraphRegex.VERTICES_LINE.matcher(line);
                                if (m.find()) {

                                    idx = Integer.parseInt(m.group(1).trim());
                                    lineAdditional.put("label", m.group(2).trim());
                                    lineAdditional.put("coord_x", m.group(3).trim());
                                    lineAdditional.put("coord_y", m.group(4).trim());
                                    lineAdditional.put("coord_z", m.group(5).trim());
                                    line = line.substring(m.end());
                                } else {
                                    stage = Stage.NO_READ;
                                    done = false;
                                    continue;
                                }
                            }

                            readOptionalInfo(line, additional);
                            //nextEdgeId = Math.max(nextEdgeId, idx + 1);
                            lineAdditional.putAll(additional);
                            //result.setAdditionalProperties(idx, lineAdditional);

                            // new object
                            NetDevice newDevice = new NetDevice(idx);

                            // set the info
                            GenericInfo newInfo = new GenericInfo();
                            newInfo.setAttributes(lineAdditional);
                            // attached the info to the object
                            newDevice.setInfo(newInfo);
                            // add the Vertex / Object to map
                            deviceMap.put(newDevice.getObjectId(), newDevice);
                            //System.out.println(deviceMap.size());
                            break;
                        case READ_EDGES:
                            m = GraphRegex.SIMPLE_EDGE_LINE.matcher(line);
                            lineAdditional = new HashMap<>();
                            Integer id = null;
                            Integer source,
                             target;
                            if (m.find()) {
                                id = nextEdgeId++;
                                source = Integer.parseInt(m.group(1).trim());
                                target = Integer.parseInt(m.group(2).trim());
                                line = line.substring(m.end());
                            } else {
                                m = GraphRegex.EDGE_LINE.matcher(line);
                                if (m.find()) {
                                    id = nextEdgeId++;
                                    source = Integer.parseInt(m.group(1).trim());
                                    target = Integer.parseInt(m.group(2).trim());
                                    lineAdditional.put("bandwidth", m.group(3).trim());
                                    line = line.substring(m.end());
                                } else {
                                    stage = Stage.NO_READ;
                                    done = false;
                                    continue;
                                }
                            }

                            readOptionalInfo(line, lineAdditional);
                            //result.addEdge(source, target, id);
                            //result.setAdditionalProperties(id, lineAdditional);

                            NetDevice device1 = deviceMap.get(source);
                            NetDevice device2 = deviceMap.get(target);

                            if (device2 == null || device1 == null) {
                                System.err.println("Incorrect edge, one of the nodes (" + source + ", " + target + ") not exist.");
                            } else {
                                // update neighbors
                                device1.addNeighbor(device2.getObjectId());
                                device2.addNeighbor(device1.getObjectId());

                                // new edge
                                NetLink newEdge = new NetLink(id, device1.getObjectId(), device2.getObjectId(), false);

                                device1.addLink(id);
                                device2.addLink(id);
                                // set the info
                                GenericInfo newInfo2 = new GenericInfo();
                                newInfo2.setAttributes(lineAdditional);
                                // attached the info to the edge
                                newEdge.setInfo(newInfo2);
                                // add the new edge
                                edgeList.add(newEdge);
                            }
                            break;
////////////////////////////////////////////////////////////
                        case READ_ARCS:
                            m = GraphRegex.SIMPLE_ARC_LINE.matcher(line);
                            lineAdditional = new HashMap<>();
                            Integer arc_id = null;
                            Integer arc_source,
                             arc_destination;
                            if (m.find()) {
                                arc_id = nextEdgeId++;
                                arc_source = Integer.parseInt(m.group(1).trim());
                                arc_destination = Integer.parseInt(m.group(2).trim());
                                line = line.substring(m.end());
                            } else {
                                m = GraphRegex.ARC_LINE.matcher(line);
                                if (m.find()) {
                                    arc_id = nextEdgeId++;
                                    arc_source = Integer.parseInt(m.group(1).trim());
                                    arc_destination = Integer.parseInt(m.group(2).trim());
                                    lineAdditional.put("bandwidth", m.group(3).trim());
                                    line = line.substring(m.end());
                                } else {
                                    stage = Stage.NO_READ;
                                    done = false;
                                    continue;
                                }
                            }

                            readOptionalInfo(line, lineAdditional);
                            //result.addEdge(source, target, id);
                            //result.setAdditionalProperties(id, lineAdditional);

                            NetDevice arc_source_device = deviceMap.get(arc_source);
                            NetDevice arc_destination_device = deviceMap.get(arc_destination);

                            if (arc_destination_device == null || arc_source_device == null) {
                                System.err.println("Incorrect arc, one of the nodes (" + arc_source + ", " + arc_destination + ") not exist.");
                            } else {
                                // update neighbors
                                arc_source_device.addNeighbor(arc_destination_device.getObjectId());

                                // new arc
                                NetLink newArc = new NetLink(arc_id, arc_source_device.getObjectId(), arc_destination_device.getObjectId(), true);

                                arc_source_device.addLink(arc_id);
                                arc_destination_device.addLink(arc_id);
                                // set the info
                                GenericInfo newArcInfo = new GenericInfo();
                                newArcInfo.setAttributes(lineAdditional);
                                // attached the info to the edge
                                newArc.setInfo(newArcInfo);
                                // add the new edge
                                edgeList.add(newArc);
                            }
                            break;
/////////////////////////////////////////////////////////////////////////////////////////////////////////////                            
                        default:
                            throw new AssertionError(stage.name());
                    }
                }
            }
        }

        SimulatedEnvironment result = SimulatedEnvironment.CreateInstance(deviceMap.size(), edgeList.size());

        for (NetDevice netDevice : deviceMap.values()) {
            result.addNode(netDevice);
        }

        for (NetLink netEdge : edgeList) {
            result.addLink(netEdge);
        }

        return result;
    }

    private static void readOptionalInfo(String line, Map<String, Object> additional) {
        Matcher m;
        m = GraphRegex.OPTIONAL_INFO.matcher(line);
        while (m.find()) {
            String v = m.group(2).trim();

            if (v.startsWith("'")) {
                v = v.substring(1, v.length() - 1);
            }
            additional.put(m.group(1).trim().toLowerCase(), v.toLowerCase());
        }
    }

    private static enum Stage {

        NO_READ,
        READ_VERTICES,
        READ_EDGES,
        READ_ARCS;
    }
}
