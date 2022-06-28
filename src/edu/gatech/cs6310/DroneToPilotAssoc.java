package edu.gatech.cs6310;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class DroneToPilotAssoc {

    private Integer droneId;
    private String pilot;
    private String store;

    public DroneToPilotAssoc() { }

    public DroneToPilotAssoc(String store, Integer droneId, String pilot) {
        this.droneId = droneId;
        this.pilot = pilot;
        this.store = store;
    }

    private static DroneToPilotAssoc packDronePilotAssoc(ResultSet rs) throws SQLException {
        DroneToPilotAssoc d = new DroneToPilotAssoc();
        d.store = rs.getString("storeName");
        d.droneId = rs.getInt("droneId");
        d.pilot = rs.getString("pilotAccount");
        return d;
    }

    public static DroneToPilotAssoc getDronePilotAssoc(
            String storeName, int droneId, String pilotAccount, Connection c) throws SQLException {
        PreparedStatement ps = c.prepareStatement(
                "SELECT * FROM DroneToPilotAssoc AS D WHERE storeName = ? AND droneId = ? AND pilotAccount = ?");
        int counter = 1;
        ps.setString(counter++, storeName);
        ps.setInt(counter++, droneId);
        ps.setString(counter++, pilotAccount);
        ResultSet rs = ps.executeQuery();
        DroneToPilotAssoc dpa = null;
        if (rs.next()) {
            dpa = packDronePilotAssoc(rs);
        }
        return dpa;
    }

    public void createDroneToPilotAssoc(Connection c) throws SQLException {
        PreparedStatement preparedStatement = null;
        String query = "INSERT INTO DroneToPilotAssoc (storeName, droneId, pilotAccount)"
                + "value(?,?,?)";
        Integer counter = 1;

        preparedStatement = c.prepareStatement(query);
        preparedStatement.setString(counter++, this.store);
        preparedStatement.setInt(counter++, this.droneId);
        preparedStatement.setString(counter++, this.pilot);

        preparedStatement.executeUpdate();
    }

}
