/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bgu.sim.core.Routing.DB;

import java.sql.SQLException;

/**
 *
 * @author keren
 */
public abstract class AbsRoutingDBSQL extends AbsRoutingAlgorithm {

    private SqlLiteData _file;

    public void openDataFile(int VertexCount, String dbName) throws ClassNotFoundException, SQLException {
        _file = SqlLiteData.getInstance(dbName);
    }

    public void writeToDataFile(int Source, int Target, int NextStep) throws SQLException {
        _file.insert(Source, Target, NextStep);
    }

    public int readFromDataFile(int Source, int Target) throws SQLException {
        return _file.selectNextstep(Source, Target);
    }

    public void clearDataFile() throws SQLException {
        _file.cleanDB();
    }

    public void closeDataFile() throws SQLException {
        _file.close();
    }

    public boolean isDataExist() {
        return _file.isDataExists();
    }
}
