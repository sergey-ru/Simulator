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
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;

/**
 *
 * @author sergei
 */
public class top500flickrToCsv {

    private static final Set<String> imgId = new HashSet<>();

    public static void main(String[] args) {
        try {
            // make 500 top dictionary
            readTop500Data() ;
            reWriteData();

        } catch (IOException ex) {
            Logger.getLogger(top500flickrToCsv.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println("Done.");
    }
    
        private static void readTop500Data() throws FileNotFoundException, IOException {

        // read flickr data
        File file = new File("C:\\Users\\admin\\Desktop\\Flickr\\top_500.csv");
        InputStream in = new FileInputStream(file);

        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String line;

        while ((line = br.readLine()) != null) {
            String[] data = line.split(",");
            imgId.add(data[0]);
        }
    }

    private static void reWriteData() throws FileNotFoundException, IOException {

        // read flickr data
        File reader = new File("C:\\Users\\admin\\Desktop\\Flickr\\flickr-all-photos.csv");
        FileWriter writer = new FileWriter("C:\\Users\\admin\\Desktop\\Flickr\\top_500_flickr-all-photos.csv");

        InputStream in = new FileInputStream(reader);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String line;

        while ((line = br.readLine()) != null) {
            String[] data = line.split(",");
            if (!imgId.contains(data[0])) {
                //System.err.println("Image ID " + data[1] + " is not found.");
            } else {
                writer.append(line);
                writer.append('\n');
            }
        }
        
        writer.flush();
        writer.close();
    }
}
