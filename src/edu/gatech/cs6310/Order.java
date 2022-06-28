package edu.gatech.cs6310;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Order {

    private String orderId;
    private String account;
    private String store;
    private int droneId;
    private TreeMap<String, Line> lines;

    public Order() {
        this.lines = new TreeMap<>();
    }

    public Order(String orderId, String store, String account, int droneId) {
        this.orderId = orderId;
        this.store = store;
        this.account = account;
        this.droneId = droneId;
        this.lines = new TreeMap<>();
    }

    public String getOrderId() {
        return orderId;
    }

    public int getDroneId() { return droneId; }

    public String getAccount() { return account; }

    public boolean addLine(Line line, Connection c) throws  SQLException {


        //c.getCredit() < (o.getTotalOrderPrice() + line.getTotalLinePrice()
        //System.out.println("ERROR:customer_cant_afford_new_item");

        // check this line
        this.getTotalOrderWeight();

        //d.getRemainingCapacity() < line.getTotalLineWeight()
        //System.out.println("ERROR:drone_cant_carry_new_item");
        line.createLine(c);
        this.lines.put(line.getCurrentItem().getItemName(), line);

        return true;
    }

    public Integer getTotalOrderWeight() {
        Integer totalWeight = 0;
        for (Map.Entry<String, Line> line_entry : lines.entrySet()) {
            totalWeight += line_entry.getValue().getTotalLineWeight();
        }
        return totalWeight;
    }

    public Integer getTotalOrderPrice() {
        Integer totalPrice = 0;
        for (Map.Entry<String, Line> line_entry : lines.entrySet()) {
            totalPrice += line_entry.getValue().getTotalLinePrice();
        }
        return totalPrice;
    }

    public boolean containsItem(String itemName) {
        return this.lines.containsKey(itemName);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("orderID:" + this.orderId + "\n");
        for (Map.Entry<String, Line> entry : this.lines.entrySet()) {
            sb.append(entry.getValue().toString() + "\n");
        }
        return sb.toString();
    }


    private static Order packOrder(ResultSet rs, Connection c) throws SQLException {
        Order o = new Order();
        o.orderId = rs.getString("orderId");
        o.account = rs.getString("customerAccount");
        o.droneId = rs.getInt("Drone_droneId");
        o.store = rs.getString("Store_storeName");
        o.lines = Line.getLinesByOrderIdAndStoreTM(o.orderId, o.store, c);
        return o;
    }

    public static List<Order> getOrdersByStore(String storeName, Connection c) throws SQLException {
        PreparedStatement ps = c.prepareStatement(
                "SELECT * FROM CustomerOrder AS O WHERE Store_storeName = ? ORDER BY orderId ASC"
        );

        int counter = 1;
        ps.setString(counter++, storeName);
        ResultSet rs = ps.executeQuery();
        ArrayList<Order> orders = new ArrayList<>();
        while (rs.next()) {
            Order o = null;
            o = packOrder(rs, c);
            orders.add(o);
        }
        return orders;
    }

    public static Order getOrderByIdAndStore(String orderId, String storeName, Connection c) throws SQLException {
        PreparedStatement ps = c.prepareStatement(
                "SELECT * FROM CustomerOrder AS O WHERE Store_storeName = ? AND orderId = ? ORDER BY orderId DESC"
        );
        Integer counter = 1;
        ps.setString(counter++, storeName);
        ps.setString(counter++, orderId);
        ResultSet rs = ps.executeQuery();
        Order o = null;
        if (rs.next()) {
            o = packOrder(rs, c);
        }
        return o;
    }

    public static List<Order> getOrdersByDroneIdAndStore(int droneId, String storeName, Connection c) throws SQLException {
        PreparedStatement ps = c.prepareStatement(
                "SELECT * FROM CustomerOrder AS O WHERE Drone_droneId = ? AND Store_storeName = ? ORDER BY orderId ASC"
        );
        Integer counter = 1;
        ps.setInt(counter++, droneId);
        ps.setString(counter++, storeName);
        ResultSet rs = ps.executeQuery();
        ArrayList<Order> orders = new ArrayList<>();
        while (rs.next()) {
            Order o = null;
            o = packOrder(rs, c);
            orders.add(o);
        }
        return orders;
    }

    public void createOrder(Connection c) throws SQLException {
        PreparedStatement preparedStatement = null;
        String query = "INSERT INTO CustomerOrder (orderId, customerAccount, Drone_droneId, Store_storeName)"
                + "value(?,?,?,?)";
        Integer counter = 1;

        preparedStatement = c.prepareStatement(query);
        preparedStatement.setString(counter++, this.orderId);
        preparedStatement.setString(counter++, this.account);
        preparedStatement.setInt(counter++, this.droneId);
        preparedStatement.setString(counter++, this.store);

        preparedStatement.executeUpdate();
    }

    public void removeOrder(Connection c) throws SQLException {
        PreparedStatement ps = c.prepareStatement(
                "DELETE FROM Line WHERE storeName = ? AND orderId = ?"
        );
        Integer counter = 1;
        ps.setString(counter++, this.store);
        ps.setString(counter++, this.orderId);
        ps.executeUpdate();

        counter = 1;

        PreparedStatement ps2 = c.prepareStatement(
                "DELETE FROM CustomerOrder WHERE Store_storeName = ? AND orderId = ?"
        );
        ps2.setString(counter++, this.store);
        ps2.setString(counter++, this.orderId);
        ps2.executeUpdate();
    }

}
