/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bgu.sim.netFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sergei
 */
public class TickOrder {

    private static final HashMap<Integer, Integer> IdMap = new HashMap<>();

    public static void main(String[] args) {
        try {
            readFlickrData();

        } catch (IOException ex) {
            Logger.getLogger(TickOrder.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println("Done.");
    }

    private static void readFlickrData() throws FileNotFoundException, IOException {

        // read flickr data
        File reader = new File("C:\\Users\\admin\\Desktop\\Flickr\\temp.csv");
        FileWriter writer = new FileWriter("C:\\Users\\admin\\Desktop\\Flickr\\temp_tickOrder.csv");

        InputStream in = new FileInputStream(reader);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String line;
        String lastID = "";
        int counter = 0;

        while ((line = br.readLine()) != null) {
            String[] data = line.split(",");
            
            if (!data[1].equals(lastID)) {
                lastID = data[1];
                counter = 0;
            }
            counter++;
            int i = 0;
            for (String data1 : data) {
                i++;
                if (i == 3) {
                    data1 = Integer.toString(counter);
                }
                writer.append(data1 + ",");
            }
            writer.append('\n');

        }

        writer.flush();
        writer.close();
    }
}
