/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bgu.sim.actions.clientPlayers;


import bgu.sim.data.NetDevice;
import bgu.sim.netFile.SimulatedEnvironment;
import bgu.sim.ruleEngine.action.AbsExternalAction;
import bgu.sim.ruleEngine.action.IndicativeFeature;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sergei
 */
public class CSVPlayer extends AbsExternalAction {

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSSS");
    private BufferedReader br = null;
    private String line = "";
    private final String cvsSplitBy = ",";
    private String[] headers = null;
    private String[] values = null;
    private Date startDate = null;
    private Date endDate = null;
    private Date currentDate = null;
    
    private String filePath = "/home/sergei/Downloads/data.csv";
    private String startDateString = "2013-12-03 09:30:00.0";
    private String endDateString = "2013-12-03 15:30:00.0";
    
    
    
    public CSVPlayer() {
        super("The action updates parameters of devices from csv file.", new IndicativeFeature[]{});
        try {
            br = new BufferedReader(new FileReader(filePath));
            startDate = sdf.parse(startDateString);
            endDate = sdf.parse(endDateString);
            
        } catch (FileNotFoundException | ParseException ex) {
            Logger.getLogger(CSVPlayer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public Integer execute(SimulatedEnvironment input) {

        if(br!=null){
            if(headers==null){
                //first run reads headers of csv columns
                readHeaders();
                currentDate = new Date(startDate.getTime());
            }
            //reading the data lines of curent second
            
            try {
                boolean stop = false;
                while(!stop && br.ready()){
        
                    br.mark(1000);
                    line = br.readLine();

                    values = line.split(cvsSplitBy);
                    Date probDate = new Date(sdf.parse(values[1].substring(0, values[1].length()-3)).getTime());
                    if(probDate.after(startDate) && probDate.before(endDate))
                    {

                        if (probDate.after(currentDate)){
                            //reset and stop till next tick
                            br.reset();
                            stop=true;
                            currentDate.setTime(probDate.getTime()+1000);
                        }
                        else
                        {
                            //find the node and update props
                            NetDevice device = input.getNode(Integer.parseInt(values[0]));
                            
                            for (int i = 2; i < values.length; i++) {
                                device.getInfo().set(headers[i], Double.parseDouble(values[i]));
                            }
                        }
                    }
                }

            
            }
         catch (Exception e) {

            Logger.getLogger(CSVPlayer.class.getName()).log(Level.SEVERE, null, e);

        } 
        
//        finally {
//            if (br != null) {
//                try {
//                    br.close();
//                } catch (Exception e) {
//                    Logger.getLogger(CSVPlayer.class.getName()).log(Level.SEVERE, null, e);
//                }
//            }
//        }
        }
        return 0;
    }
    
    private void readHeaders(){
        try {
            line = br.readLine();
            headers = line.split(cvsSplitBy);
        } catch (IOException ex) {
            Logger.getLogger(CSVPlayer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
            

}