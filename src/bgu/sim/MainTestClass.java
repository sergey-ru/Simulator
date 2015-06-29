/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bgu.sim;

import bgu.sim.core.Simulator;
import com.sun.tools.javac.util.Pair;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Sergey
 */
public class MainTestClass {

    public static void main(String[] args) {
        
        //CreateXMLFile();
        
        Date d  = Calendar.getInstance().getTime();
        
        //TextToNet.TextToNetConvert("/home/sergei/Downloads/soc-Epinions1.txt", "/home/sergei/Downloads/soc-Epinions1.net");
        //CleanNetFile("/home/sergei/Downloads/soc-Epinions1.net");
        
        try {
            System.out.println("Started...   " + d);
            //Simulator _simTest = Simulator.fromXML("C:\\Users\\sergeyru\\Desktop\\Flickr\\flickr.xml");
            Simulator _simTest = Simulator.fromXML("C:\\Users\\sergeyru\\Desktop\\FB\\sim.xml");
            //Simulator _simTest = Simulator.fromXML("C:\\Users\\sergeyru\\Desktop\\Epinions\\sim.xml");
            //Simulator _simTest = Simulator.fromXML("C:\\Users\\sergeyru\\Desktop\\Slashdot\\sim.xml");
            //Simulator _simTest = Simulator.fromXML("C:\\Users\\sergeyru\\Desktop\\Flickr\\sim.xml");
            //Simulator _simTest = Simulator.fromXML("/home/sergei/Downloads/fb.xml");
            
            System.out.println("Xml loaded ...   ");
            
            
            _simTest.firstScenario();
            
            int counter = 0;
            System.out.println("Scenario: " + counter++ + "\t" + new Date());
             _simTest.runScenarioToEnd();

             
             while(_simTest.nextScenario()){
                 System.out.println("Scenario: " + counter++ + "\t" + new Date());
                 _simTest.runScenarioToEnd();
             }

        } catch (Exception e) {
            // Simlation failed.
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsString = sw.toString();
            System.err.println(exceptionAsString);
            System.err.println(Calendar.getInstance().getTime());
        }
        System.out.println("Done: " + (Calendar.getInstance().getTime().getTime() - d.getTime()));

    }

    private static void CleanNetFile(String netFile) throws NumberFormatException {
        BufferedReader br = null;
        BufferedWriter bw = null;
        try {
            br = new BufferedReader(new FileReader(netFile));
            bw = new BufferedWriter(new FileWriter("/home/sergei/Downloads/cleaned.net"));

            String line;
            int state = 0; // 0 unknown
            // 1 vertexes 
            // 2 edges
            
            Integer source;
            Integer destination;
            String vertexKey;

            int vertexCount = 1;
            TreeMap<Integer,Integer> vertexMap = new TreeMap<>();
            LinkedList<Pair<Integer,Integer>> edgesList  = new LinkedList<>();
            HashSet<String> seenEdges = new HashSet<>();
            
            while ((line = br.readLine()) != null) {

                if (line.contains("*Vertices")) {
                    //bw.write(line+"\n");
                    state = 1;
                } 
                else if (line.contains("*Edges")) {
                    //bw.write(line+"\n");
                    state = 2;
                } 
                else {
                    String[] data = line.split("\\s+");
                    
                    if(state == 1)
                    {
                        vertexMap.put(Integer.parseInt(data[0]), vertexCount);
                        vertexCount++;
                        //bw.write(data[0]+"\n");
                    }
                    else if (state == 2)
                    {
                        source = Integer.parseInt(data[0]);
                        destination = Integer.parseInt(data[1]);
                        if(source<destination)
                        {
                            vertexKey = source+"_"+destination;
                            if(!seenEdges.contains(vertexKey)){
                                seenEdges.add(vertexKey);
                                edgesList.add(new Pair<>(source,destination));
                                
                            }
                            
                        }
                        else
                        {
                            vertexKey = destination+"_"+source;
                            if(!seenEdges.contains(vertexKey)){
                                seenEdges.add(vertexKey);
                                edgesList.add(new Pair<>(destination,source));
                                
                            }
                        }   
                        //bw.write(data[0]+" "+data[1]+"\n");
                    }
                }
                
            }
            
            
            bw.write("*Vertices " +vertexMap.size()+"\n");
            for (Integer integer : vertexMap.values()) {
                bw.write(integer+"\n");
            }
            bw.write("*Edges"+"\n");
            for (Pair<Integer, Integer> pair : edgesList) {
                bw.write(vertexMap.get(pair.fst)+" "+vertexMap.get(pair.snd)+"\n");
            }
            
            br.close();
            bw.close();

        } catch (FileNotFoundException ex) {
            Logger.getLogger(MainTestClass.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MainTestClass.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                br.close();
            } catch (IOException ex) {
                Logger.getLogger(MainTestClass.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private static void CreateXMLFile()
    {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("/home/sergei/Downloads/fb.xml"));
            bw.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>"+"\n");
            bw.write("<experiment>"+"\n");
            
            bw.write("<simulation netFilePath=\"/home/sergei/Downloads/fb_2.net\" seed=\"\" ticks=\"50\">"+"\n");
            bw.write("<StatisticListener value=\"bgu.sim.core.stat.CSVListener\">"+"\n");
            bw.write("<p key=\"path\" value=\"/home/sergei/Downloads/\" />"+"\n");
            bw.write("<p key=\"writeRate\" value=\"1\" />"+"\n");
            bw.write("<p key=\"addScenarioHeader\" value=\"false\" />"+"\n");
            bw.write("</StatisticListener>"+"\n");
            bw.write("</simulation>"+"\n");
            
            for (double prop = 0.1; prop <=1; prop=prop+0.1) {
                for (double av = 0; av <=1; av=av+0.1) {
                    for (int run = 0; run < 10; run++) {
                        
                        bw.write("<scenario name=\"fb av_"+av+" prop_"+prop+" run_"+run+"\">"+"\n");
                        
                        bw.write("<init name=\"Infect With Virus\">"+"\n");
                        bw.write("<action value=\"bgu.sim.actions.fb.InfectWithVirus\">"+"\n");
                        bw.write("<p key=\"propogationAmount\" value=\"1\" />"+"\n");
                        bw.write("</action>"+"\n");
                        bw.write("</init>"+"\n");
                        
                        bw.write("<init name=\"Install AntiVirus\">"+"\n");
                        bw.write("<action value=\"bgu.sim.actions.fb.InstallAV\">"+"\n");
                        bw.write("<p key=\"percentage\" value=\""+av+"\" />"+"\n");
                        bw.write("</action>"+"\n");
                        bw.write("</init>"+"\n");
                        
                        bw.write("<device name=\"Potential Propogate\">"+"\n");
                        bw.write("<order value=\"0\" />"+"\n");
                        bw.write("<select value=\"SELECT * FROM bgu.sim.data.NetDevice where getInt('Potential_Virus', :_currobj)=1\" />"+"\n");
                        bw.write("<action value=\"bgu.sim.actions.fb.PotentialPropogate\">"+"\n");
                        bw.write("<p key=\"propogationProbability\" value=\""+prop+"\" />"+"\n");
                        bw.write("</action>"+"\n");
                        bw.write("</device>"+"\n");
                        
                        
                        bw.write("<device name=\"Propogate\">"+"\n");
                        bw.write("<order value=\"1\" />"+"\n");
                        bw.write("<select value=\"SELECT * FROM bgu.sim.data.NetDevice where getInt('Virus', :_currobj)=1\" />"+"\n");
                        bw.write("<action value=\"bgu.sim.actions.fb.Propogate\">"+"\n");
                        bw.write("<p key=\"propogationProbability\" value=\""+prop+"\" />"+"\n");
                        bw.write("</action>"+"\n");
                        bw.write("</device>"+"\n");
                        
                        bw.write("</scenario>"+"\n");
                        
                        
                    }
                }
            }
            
            bw.write("</experiment>"+"\n");
            bw.close();
            
        } catch (IOException ex) {
            Logger.getLogger(MainTestClass.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
