package bgu.sim.core.Routing.DB;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class BinaryFile {

    private static BinaryFile _instance = null;
    private boolean _ifDataExists = false;
    private final RandomAccessFile _file;
    public int _totalnodes;
    public final int _IntegerSise = Integer.SIZE / 8; // bytes

    protected BinaryFile(int totalnodes, String filePath) throws IOException {
        this._totalnodes = totalnodes;
        // check if data exists
        File checkFile = new File(filePath);
        _ifDataExists = checkFile.exists();
        // hold the data file
        _file = new RandomAccessFile(filePath, "rw");
    }

    public boolean isDataExists() {
        return _ifDataExists;
    }

    public static BinaryFile getInstance(int totalnodes, String filePath) throws IOException {
        if (_instance == null) {
            _instance = new BinaryFile(totalnodes, filePath);
        }
        
        return _instance;
    }

    // return next step by given source and targer
    public int readFromFile(int source, int target)
            throws IOException {
        int index = ((_totalnodes * (source - 1)) + (target - 1)) * _IntegerSise;
        _file.seek(index); // seek by bytes
        
        return _file.readInt();

    }

    public void writeToFile(int nextstep, int source, int target)
            throws IOException {
        long z;

        // tmp claculates - too big for one calculate.
        long x = _totalnodes * (source - 1);
        long y = x + (target - 1);
        z = y * _IntegerSise;

        _file.seek(z);
        _file.writeInt(nextstep);
    }

    public void close() throws IOException {
        _file.close();
    }
}
