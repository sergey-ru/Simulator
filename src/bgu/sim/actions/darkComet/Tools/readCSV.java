/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bgu.sim.actions.darkComet.Tools;

import bgu.sim.data.SystemEvent;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Keren Fruchter
 */
public class readCSV {

    public List<SystemEvent> readCSV(String name) {
        List<SystemEvent> events = new LinkedList<>();
        String csvFile = "src\\Resources\\DarkComet\\" + name;
        BufferedReader br = null;
        String line;
        String cvsSplitBy = ",";
        int count = 0;

        try {

            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) {
                count++;
                if (count != 1) {
                    SystemEvent e = new SystemEvent(line);
                    events.add(e);
                }
            }

        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                }
            }
        }
        return events;
    }
}
