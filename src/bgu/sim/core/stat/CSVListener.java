/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bgu.sim.core.stat;

import static bgu.sim.Properties.StringsProperties.SETSCENARIO_KEY;
import bgu.sim.core.Simulator;
import bgu.sim.data.Message;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author Sergey
 */
public class CSVListener extends StatisticListener {

    //public XML property and name must match XML property
    public String path;
    public int writeRate = 1000;
    public String addScenarioHeader = "true";

    private StringBuilder sb = new StringBuilder();
    private BufferedWriter _bufferWriterFinal = null;
    private BufferedWriter _bufferWriterData = null;
    private List<String> _headersList = new ArrayList<>();
    private BufferedWriter _bufferWriterScenarioHeader = null;
    private File file_Data, file_Headers, file_Final, file_ScenarioHeader;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public void TickEndedEventHandler(Map<String, StatisticCollector.RefInteger> snapshot, List<String> snapshotHeaders, List<StatisticCollector.RefInteger> snapshotData, List<Message> messages) {

        int tick = Simulator.getInstance().getTick();
        _headersList = snapshotHeaders;

        if (tick % writeRate == 0 || tick == Simulator.getInstance().getTotalTicks()) {
            try {
                // Tick Update
                sb.append(tick).append(',');

                for (StatisticCollector.RefInteger value : snapshotData) {
                    sb.append(value.getValue()).append(',');
                }
                sb = sb.replace(sb.length()-1, sb.length(), "");
                sb.append("\r\n");
                _bufferWriterData.write(sb.toString());
                sb = new StringBuilder();
                //System.out.println("Tick: " + tick + " " + new Date());
            } catch (IOException ex) {
                Logger.getLogger(CSVListener.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

    @Override
    public void ScenarioStardedEventHandler(Map<String, String> scenarioData) {
        try {
            CreateWriter(scenarioData);
            SortedSet<String> keys = new TreeSet<>(scenarioData.keySet());
            for (String key : keys) {
                try {
                    _bufferWriterScenarioHeader.append(key).append(',').append(scenarioData.get(key));
                    _bufferWriterScenarioHeader.newLine();

                } catch (IOException ex) {
                    Logger.getLogger(CSVListener.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            _bufferWriterScenarioHeader.close();
            _bufferWriterScenarioHeader = null;
        } catch (IOException ex) {
            Logger.getLogger(CSVListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //creates buffer writer if not exist
    private void CreateWriter(Map<String, String> scenarioData) {
        if (_bufferWriterFinal == null) {
            try {
                file_Data = new File(path + "Data_" + scenarioData.get(SETSCENARIO_KEY) + ".csv");
                file_Final = new File(path + "Final_" + scenarioData.get(SETSCENARIO_KEY) + ".csv");
                file_Headers = new File(path + "Headers_" + scenarioData.get(SETSCENARIO_KEY) + ".csv");
                file_ScenarioHeader = new File(path + "ScenarioHeader_" + scenarioData.get(SETSCENARIO_KEY) + ".csv");

                if (!file_Data.exists()) {
                    file_Data.createNewFile();
                }
                if (!file_Final.exists()) {
                    file_Final.createNewFile();
                }
                if (!file_Headers.exists()) {
                    file_Headers.createNewFile();
                }
                if (!file_ScenarioHeader.exists()) {
                    file_ScenarioHeader.createNewFile();
                }
                _bufferWriterData = new BufferedWriter(new FileWriter(file_Data.getAbsoluteFile()));
                _bufferWriterScenarioHeader = new BufferedWriter(new FileWriter(file_ScenarioHeader.getAbsoluteFile()));

            } catch (IOException e) {
                IOExeptionPrinter(e);
            }
        }
    }

    @Override
    public void ScenarioEndedEventHandler() {
        try {

            _bufferWriterData.close();
            _bufferWriterData = null;

            createFileForHeaders();
            if ("true".equals(addScenarioHeader)) {
                IOCopier.joinFiles(file_Final, new File[]{
                    file_ScenarioHeader, file_Headers, file_Data});
            } else {
                IOCopier.joinFiles(file_Final, new File[]{file_Headers, file_Data});
            }

            file_ScenarioHeader.delete();
            file_Headers.delete();
            file_Data.delete();

            //writeEndVerticaly();
        } catch (IOException e) {
            IOExeptionPrinter(e);
        }
    }

    private void writeEndVerticaly() {
        BufferedWriter _buffer = null;
        try {
            File file_Vertical = new File(path + "Data_Vertical.csv");
            if (!file_Vertical.exists()) {
                file_Vertical.createNewFile();
            }
            _buffer = new BufferedWriter(new FileWriter(file_Vertical.getAbsoluteFile()));

            List<StatisticCollector.RefInteger> data = StatisticCollector.getInstance().getCollectorData();
            int pointer = 0;
            StatisticCollector.getInstance().getCollectorHeaders().remove(0);
            for (String header : StatisticCollector.getInstance().getCollectorHeaders()) {
                _buffer.write(header + "," + data.get(pointer).getValue());
                _buffer.newLine();
                pointer++;
            }
        } catch (IOException ex) {
            Logger.getLogger(CSVListener.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                _buffer.close();
            } catch (IOException ex) {
                Logger.getLogger(CSVListener.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void IOExeptionPrinter(IOException e) {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        String exceptionAsString = sw.toString();
        System.err.println(exceptionAsString);
    }

    private void createFileForHeaders() {
        try {
            _headersList.add(0, "Tick");
            BufferedWriter _bufferWriterHeaders = new BufferedWriter(new FileWriter(file_Headers.getAbsoluteFile()));
            for (int i = 0; i < _headersList.size()-1; i++) {
                _bufferWriterHeaders.write(_headersList.get(i) + ",");
            }
            _bufferWriterHeaders.write(_headersList.get(_headersList.size()-1));
            
//            for (String string : _headersList) {
//                _bufferWriterHeaders.write(string + ",");
//            }
            _bufferWriterHeaders.newLine();
            _bufferWriterHeaders.close();

        } catch (IOException ex) {
            Logger.getLogger(CSVListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

class IOCopier {

    public static void joinFiles(File destination, File[] sources)
            throws IOException {
        OutputStream output = null;
        try {
            output = createAppendableStream(destination);
            for (File source : sources) {
                appendFile(output, source);
            }
        } finally {
            IOUtils.closeQuietly(output);
        }
    }

    private static BufferedOutputStream createAppendableStream(File destination)
            throws FileNotFoundException {
        return new BufferedOutputStream(new FileOutputStream(destination, true));
    }

    private static void appendFile(OutputStream output, File source)
            throws IOException {
        InputStream input = null;
        try {
            input = new BufferedInputStream(new FileInputStream(source));
            IOUtils.copy(input, output);
        } finally {
            IOUtils.closeQuietly(input);
        }
    }
}
