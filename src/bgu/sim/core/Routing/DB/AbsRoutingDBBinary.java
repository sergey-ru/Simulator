/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bgu.sim.core.Routing.DB;

import java.io.IOException;

/**
 *
 * @author keren
 */
public abstract class AbsRoutingDBBinary extends AbsRoutingAlgorithm {

    private BinaryFile _file;

    public void openDataFile(int VertexCount, String filePath) throws IOException {
        _file = BinaryFile.getInstance(VertexCount, filePath);
    }

    public void writeToDataFile(int Source, int Target, int NextStep) throws IOException {
        _file.writeToFile(NextStep, Source, Target);
    }

    public int readFromDataFile(int Source, int Target) throws IOException {
        return _file.readFromFile(Source, Target);
    }

    public void closeDataFile() throws IOException {
        _file.close();
    }

    public boolean isDataExist() {
        return _file.isDataExists();
    }
}
