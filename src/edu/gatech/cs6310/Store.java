package edu.gatech.cs6310;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Store {

    private TreeMap<Integer, Drone> drones;
    private TreeMap<Integer, Pilot> droneToPilotAssoc;
    private String name;
    private TreeMap<String, Order> orders;
    private TreeMap<String, Customer> orderToCustomer;
    private TreeMap<String, Drone> orderToDroneAssoc;
    private TreeMap<String, StoreItem> storeItems;
    private Integer totalRevenue = 0;

    // Default constructor
    public Store() {}

    public Store(String name, Integer totalRevenue) {
        this.totalRevenue = totalRevenue;
        this.name = name;
        this.storeItems = new TreeMap<>();
        this.drones = new TreeMap<>();
        this.droneToPilotAssoc = new TreeMap<>();
        this.orderToDroneAssoc = new TreeMap<>();
        this.orderToCustomer = new TreeMap<>();
        this.orders = new TreeMap<>();
    }

    public static List<Store> getStores(Connection c) throws SQLException {
        PreparedStatement ps = c.prepareStatement("SELECT * FROM Store AS S ORDER BY S.storeName ASC");
        Integer counter = 1;
        ResultSet rs = ps.executeQuery();
        ArrayList<Store> stores = new ArrayList<>();
        while (rs.next()) {
            Store s = new Store();
            s.name = rs.getString("storeName");
            s.totalRevenue = rs.getInt("revenue");
            stores.add(s);
        }
        return stores;
    }

    public static Store getStoreByName(String name, Connection c) throws SQLException {
        PreparedStatement ps = c.prepareStatement("SELECT * FROM Store AS S WHERE S.storeName = ?");
        int counter = 1;
        ps.setString(counter++, name);
        ResultSet rs = ps.executeQuery();
        Store s = null;
        if (rs.next()) {
            s = new Store();
            s.name = rs.getString("storeName");
            s.totalRevenue = rs.getInt("revenue");
        }
        return s;
    }

    public static void setRevenue(String name, int revenue, Connection c) throws SQLException {
        PreparedStatement ps = c.prepareStatement("UPDATE Store AS S SET revenue = revenue + ? WHERE S.storeName = ?");
        int counter = 1;
        ps.setInt(counter++, revenue);
        ps.setString(counter++, name);
        ps.executeUpdate();
    }

    public Integer sellItem(StoreItem storeItem) {
        storeItems.put(storeItem.getItemName(), storeItem);
        return 0;
    }

    public String getName() {
        return name;
    }

    public boolean checkOrderExists(String orderId) {
        if (this.orders.containsKey(orderId)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean checkDroneExists(Integer droneId) {
        if (this.drones.containsKey(droneId)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean checkDroneAssigned(Integer droneId) {
        if (this.drones.containsKey(droneId)) {
            if (this.droneToPilotAssoc.containsKey(droneId)) {
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    public TreeMap<String, StoreItem> getStoreItems() {
        return storeItems;
    }

    public Integer buyDrone(Drone d) {
        drones.put(d.getDroneId(), d);
        return 0;
    }

    public void displayItems() {
        for (Map.Entry<String, StoreItem> entry : storeItems.entrySet()) {
            System.out.println(entry.getValue().toString());
        }
    }

    public void displayDrones() {
        for (Map.Entry<Integer, Drone> entry : drones.entrySet()) {
            System.out.println(entry.getValue().toString());
        }
    }
/*
    public void assignDroneToPilot(Integer droneId, Pilot pilot) {
        Drone pastDrone = pilot.getCurrentDrone();

        if (pastDrone != null) {
            pastDrone.setPilot(null);
        }

        Drone currentDrone = drones.get(droneId);
        pilot.setStore(this);
        pilot.setCurrentDrone(currentDrone);
        currentDrone.setPilot(pilot);
        droneToPilotAssoc.put(droneId, pilot);
    }
*/
    @Override
    public String toString() {
        return "name:" + name + ",revenue:" + totalRevenue;
    }

    public boolean startOrder(String orderName, Integer droneId, Customer customer) {
/*        Order order = new Order(orderName);
        Drone d = drones.get(droneId);
        d.addOrder(order);
        orders.put(order.getOrderId(), order);
        orderToCustomer.put(order.getOrderId(), customer);
        orderToDroneAssoc.put(order.getOrderId(), d);
*/
        return true;
    }

    /*
    public boolean requestItem(String orderName, String itemName, Integer quantity, Integer unitPrice) {
        if (!(this.orders.containsKey(orderName) && this.orderToCustomer.containsKey(orderName))) {
            System.out.println("ERROR:order_identifier_does_not_exist");
            return false;
        } else if (!(this.storeItems.containsKey(itemName))) {
            System.out.println("ERROR:item_identifier_does_not_exist");
            return false;
        } else {
            StoreItem storeItem = storeItems.get(itemName);
            Line line = new Line(storeItem, unitPrice, quantity);
            Customer c = orderToCustomer.get(orderName);
            Order o = orders.get(orderName);
            Drone d = orderToDroneAssoc.get(orderName);
            if (o.containsItem(itemName)) {
                System.out.println("ERROR:item_already_ordered");
                return false;
            } else if (c.getCredit() < (o.getTotalOrderPrice() + line.getTotalLinePrice())) {
                System.out.println("ERROR:customer_cant_afford_new_item");
                return false;
            } else if (d.getRemainingCapacity() < line.getTotalLineWeight()) {
                System.out.println("ERROR:drone_cant_carry_new_item");
                return false;
            } else {
                o.addLine(line);
                return true;
            }
        }
    }
    */

    public void increaseRevenue(Integer numToIncrease) {
        this.totalRevenue += numToIncrease;
    }

    public boolean purchaseOrder(String orderName) {
        if (!(this.orders.containsKey(orderName) && this.orderToCustomer.containsKey(orderName))) {
            System.out.println("ERROR:order_identifier_does_not_exist");
            return false;
        } else {
            Customer c = orderToCustomer.get(orderName);
            Order o = orders.get(orderName);
            Drone d = orderToDroneAssoc.get(orderName);
            Pilot p = droneToPilotAssoc.get(d.getDroneId());

            if (!(d.getFuel() > 0)) {
                System.out.println("ERROR:drone_needs_fuel");
                return false;
            } else if (!(p != null)) {
                System.out.println("ERROR:drone_needs_pilot");
                return false;
            } else {
                c.reduceCredit(o.getTotalOrderPrice());
                this.increaseRevenue(o.getTotalOrderPrice());
                d.reduceFuel(1);
                p.increaseNumSuccessfulDeliveries(1);

                // Removal segment
                d.removeOrder(o);
                orderToDroneAssoc.remove(o.getOrderId());
                orderToCustomer.remove(o.getOrderId());
                orders.remove(o.getOrderId());
            }

            return true;
        }
    }

    public boolean cancelOrder(String orderName) {
        if (!(this.orders.containsKey(orderName) && this.orderToCustomer.containsKey(orderName))) {
            System.out.println("ERROR:order_identifier_does_not_exist");
            return false;
        } else {
            Order o = orders.get(orderName);
            // Removal segment
            Drone d = orderToDroneAssoc.get(o.getOrderId());
            d.removeOrder(o);
            orderToDroneAssoc.remove(o.getOrderId());
            orderToCustomer.remove(o.getOrderId());
            orders.remove(o.getOrderId());
            return true;
        }
    }

    public void createStore(Connection c) throws SQLException {
        PreparedStatement preparedStatement = null;

        String query = "INSERT INTO Store (storeName,revenue)" + "value(?,?)";
        int counter = 1;
        preparedStatement = c.prepareStatement(query);
        preparedStatement.setString(counter++, name);
        preparedStatement.setInt(counter++, totalRevenue);
        preparedStatement.executeUpdate();
    }

}
