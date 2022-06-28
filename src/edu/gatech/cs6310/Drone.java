package edu.gatech.cs6310;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Drone {

    private Integer droneId;
    private Integer fuel;
    private Integer maximumLiftCapacity;
    private List<Order> orders; // Stores all current orders
    private Pilot pilot;
    private String store;

    public Drone() {orders = new ArrayList<>();}

    public Drone(Integer droneId, Integer capacity, Integer fuel, String store) {
        this.droneId = droneId;
        this.maximumLiftCapacity = capacity;
        this.fuel = fuel;
        this.store = store;
        this.orders = new ArrayList<>();
    }

    public static List<Drone> getDrones(String storeName, Connection c) throws SQLException {
        PreparedStatement ps = c.prepareStatement("SELECT * FROM Drone AS D WHERE store = ? ORDER BY droneId ASC");
        int counter = 1;
        ps.setString(counter++, storeName);
        ResultSet rs = ps.executeQuery();
        ArrayList<Drone> drones = new ArrayList<>();
        while (rs.next()) {
            Drone d = null;
            d = packDrone(rs, c);
            drones.add(d);
        }
        return drones;
    }

    public static Drone getDroneByIdAndStore(int droneId, String storeName, Connection c) throws SQLException {
        PreparedStatement ps = c.prepareStatement("SELECT * FROM Drone AS D WHERE D.droneId = ? AND D.store = ?");
        Integer counter = 1;
        ps.setInt(counter++, droneId);
        ps.setString(counter++, storeName);
        ResultSet rs = ps.executeQuery();
        Drone d = null;
        if (rs.next()) {
            d = packDrone(rs, c);
        }
        return d;
    }

    public static Drone getDroneByIdAndStoreForUpdate(int droneId, String storeName, Connection c) throws SQLException {
        PreparedStatement ps = c.prepareStatement("SELECT * FROM Drone AS D WHERE D.droneId = ? AND D.store = ? FOR UPDATE");
        Integer counter = 1;
        ps.setInt(counter++, droneId);
        ps.setString(counter++, storeName);
        ResultSet rs = ps.executeQuery();
        Drone d = null;
        if (rs.next()) {
            d = packDrone(rs, c);
        }
        return d;
    }


    public static Drone getDroneByIdAndStoreAndPilotForUpdate(String pilotAccount, Connection c) throws SQLException {
        PreparedStatement ps = c.prepareStatement("SELECT * FROM Drone AS D" +
                " WHERE pilotAccount = ? FOR UPDATE");
        Integer counter = 1;
        ps.setString(counter++, pilotAccount);
        ResultSet rs = ps.executeQuery();
        Drone d = null;
        if (rs.next()) {
            d = packDrone(rs, c);
        }
        return d;
    }

    private static Drone packDrone(ResultSet rs, Connection c) throws SQLException {
        Drone d = new Drone();
        d.droneId = rs.getInt("droneId");
        d.fuel = rs.getInt("fuel");
        d.maximumLiftCapacity = rs.getInt("maximumLiftCapacity");
        d.store = rs.getString("store");
        String pilotAccount = rs.getString("pilotAccount");

        d.orders = Order.getOrdersByDroneIdAndStore(d.droneId, d.store, c);

        if (pilotAccount != null) {
            d.pilot = Pilot.getPilotByAccount(pilotAccount, c);
        }

        return d;
    }

    public Integer getFuel() {
        return fuel;
    }

    public Pilot getPilot() {
        return pilot;
    }

    public void removePilot(Connection c) throws SQLException {

        //        UPDATE table_name
        //        SET column1 = value1, column2 = value2, ...
        //      WHERE condition;

        PreparedStatement preparedStatement = null;
        String query = "UPDATE Drone SET pilotAccount = NULL WHERE store = ? AND droneId = ?";
        Integer counter = 1;

        preparedStatement = c.prepareStatement(query);
        preparedStatement.setString(counter++, this.store);
        preparedStatement.setInt(counter++, this.droneId);

        preparedStatement.executeUpdate();

        this.pilot = null;
    }

    public void setPilot(Pilot pilot, Connection c) throws SQLException {

        //        UPDATE table_name
        //        SET column1 = value1, column2 = value2, ...
        //      WHERE condition;

        PreparedStatement preparedStatement = null;
        String query = "UPDATE Drone SET pilotAccount = ? WHERE store = ? AND droneId = ?";
        Integer counter = 1;

        preparedStatement = c.prepareStatement(query);
        preparedStatement.setString(counter++, pilot.getAccount());
        preparedStatement.setString(counter++, this.store);
        preparedStatement.setInt(counter++, this.droneId);

        preparedStatement.executeUpdate();

        this.pilot = pilot;
    }

    public void setFuel(int fuel, Connection c) throws SQLException {

        PreparedStatement preparedStatement = null;
        String query = "UPDATE Drone SET fuel = ? WHERE store = ? AND droneId = ?";
        Integer counter = 1;

        preparedStatement = c.prepareStatement(query);
        preparedStatement.setInt(counter++, fuel);
        preparedStatement.setString(counter++, this.store);
        preparedStatement.setInt(counter++, this.droneId);

        preparedStatement.executeUpdate();
    }

    @Override
    public String toString() {
        String ret_val = "droneID:" + this.droneId + ",total_cap:" + this.maximumLiftCapacity + ",num_orders:" + orders.size()
                + ",remaining_cap:" + this.getRemainingCapacity()
                + ",trips_left:" + this.fuel;
        if (pilot != null) {
            ret_val += ",flown_by:" + pilot.firstName + "_" + pilot.lastName;
        }
        return ret_val;
    }

    public Integer getCurrentWeight() {
        Integer totalWeight = 0;

        for (Order o : orders) {
            totalWeight += o.getTotalOrderWeight();
        }
        return totalWeight;
    }

    public Integer getRemainingCapacity() {
        return this.maximumLiftCapacity - this.getCurrentWeight();
    }

    public Integer getDroneId() {
        return droneId;
    }

    public void addOrder(Order o) {
        orders.add(Integer.parseInt(o.getOrderId()), o);
    } //TODO: I wrapped this with parseInt to make it happy -Arnesh

    public void reduceFuel(Integer numToReduce) {
        fuel -= numToReduce;
    }

    public void removeOrder(Order o) {
        orders.remove(o.getOrderId());
    }

    public void createDrone(Connection c) throws SQLException {
        PreparedStatement preparedStatement = null;
        String query = "INSERT INTO Drone (droneId, fuel, maximumLiftCapacity, store)"
                + "value(?,?,?,?)";
        Integer counter = 1;

        preparedStatement = c.prepareStatement(query);
        preparedStatement.setInt(counter++, this.droneId);
        preparedStatement.setInt(counter++, this.fuel);
        preparedStatement.setInt(counter++, this.maximumLiftCapacity);
        preparedStatement.setString(counter++, this.store);

        preparedStatement.executeUpdate();
    }

}
