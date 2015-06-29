package bgu.sim.core.Routing.DB;

import java.io.File;
import java.sql.*;

/**
 *
 * @author keren
 */
public class SqlLiteData {

    private Connection _c = null;
    private Statement _stmt = null;
    private static SqlLiteData _instance = null;
    private final String CONNECTIONSTRING = "jdbc:sqlite:";
    private PreparedStatement _prep;
    private final String TABLENAME = "SH";
    private final String _dbName;
    private final boolean _ifDataExists;

    protected SqlLiteData(String dbName) throws ClassNotFoundException, SQLException {
        this._dbName = dbName;

        // Add classes & Library for sqlite
        Class.forName("org.sqlite.JDBC");
        _c = DriverManager.getConnection(CONNECTIONSTRING + dbName + ".db");
        _c.setAutoCommit(false);

        // check id data exist already
        File file = new File(dbName + ".db");
        _ifDataExists = file.exists();

        _stmt = _c.createStatement();
        String sql = "CREATE table if not exists " + TABLENAME + " ("
                + " Source1 INT NOT NULL,"
                + " Target1 INT NOT NULL,"
                + " Next1 INT,"
                + " PRIMARY KEY (Source1, Target1))";

        _stmt.executeUpdate(sql);
        _stmt.close();
        _c.commit();
        _stmt = _c.createStatement();
    }

    public void cleanDB() throws SQLException {
        _stmt.executeUpdate("DELETE FROM " + TABLENAME + ";");
    }

    public static SqlLiteData getInstance(String dbName) throws ClassNotFoundException, SQLException {
        if (_instance == null) {
            _instance = new SqlLiteData(dbName);
        }
        
        return _instance;
    }

    public void insert(int source, int target, int nextstep) throws SQLException {
        _stmt.executeUpdate("INSERT INTO " + TABLENAME + " (Source1,Target1,Next1) "
                + "VALUES (" + source + "," + target + "," + nextstep + ");");
    }

    public void createIndex() throws SQLException {
        _stmt.executeQuery("CREATE INDEX index_name on " + TABLENAME + " (Source1, Target1);");
    }

    public void initBeforeSelect() throws SQLException {
        _prep = _c.prepareStatement("SELECT Target1, Next1 FROM " + TABLENAME + " WHERE Source1 = ? AND Target1 = ?;");
    }

    public int selectNextstep(int source, int target) throws SQLException {
        int nextStep = 0;

        _prep.setInt(1, source);
        _prep.setInt(2, target);

        ResultSet rs1 = _prep.executeQuery();
        if (rs1.next()) {
            nextStep = rs1.getInt("Next1");
        } else {
            _prep.setInt(1, source);
            _prep.setInt(2, 0);

            ResultSet rs2 = _prep.executeQuery();
            if (rs2.next()) {
                nextStep = rs2.getInt("Next1");
            }
        }

        return nextStep;
    }

    public void close() throws SQLException {
        _c.commit();
        _c.close();
    }

    public void open() throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        _c = DriverManager.getConnection(CONNECTIONSTRING + _dbName);
        _c.setAutoCommit(false);
        //System.out.println("Opened database successfully");
        _stmt = _c.createStatement();
    }

    public boolean isDataExists() {
        return _ifDataExists;
    }
}
