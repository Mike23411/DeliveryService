package edu.gatech.cs6310;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DeliveryManager {

    Logger logger = Logger.getLogger(Store.class.getName());
    private TreeMap<String, Customer> customers; // Associate customer name to customer
    private TreeMap<String, Store> stores; // Associate store with store name
    private TreeMap<String, Pilot> pilotLicenses; // Associate pilot license to pilot
    private TreeMap<String, Pilot> pilots; // Associate pilot account name to pilot

    DeliveryManager() throws SQLException {
        this.customers = new TreeMap<>();
        this.stores = new TreeMap<>();
        this.pilotLicenses = new TreeMap<>();
        this.pilots = new TreeMap<>();
    }

    public static Connection makeConnection() throws SQLException {

        String url = "jdbc:mysql://mysql:3306/DeliveryService";
        String username = "root";
        String password = "password";
        Connection connection = DriverManager.getConnection(url, username, password);
        return connection;
    }

    public String makeStore(String name, Integer revenue) {
        Connection c = null;
        String ret_string = "ERROR:cannot_complete_request";
        try {
            c = makeConnection();
            c.setAutoCommit(false);

            Store store = Store.getStoreByName(name, c);
            if (store == null) {
                Store newStore = new Store(name, revenue);
                newStore.createStore(c);
                ret_string = "OK:change_completed";
            } else {
                ret_string = "ERROR:store_identifier_already_exists";
            }
            c.commit();
        } catch (SQLException e) {
            logger.warning(e.toString());
            //JDBCTutorialUtilities.printSQLException(e);
            if (c != null) {
                try {
                    System.err.print("Transaction is being rolled back");
                    c.rollback();
                } catch (SQLException excep) {
                    //JDBCTutorialUtilities.printSQLException(excep);
                    System.out.print(e);
                }
            }
        } catch (Exception e) {
            logger.warning(e.toString());
        } finally {
            try {
                c.close();
            } catch (SQLException e) {
                System.out.println("ERROR:cannot_close_connection_properly: " + e);
            }
        }

        return ret_string;

/*
        if (!this.stores.containsKey(name)) {
            Store store = new Store(name, revenue);
            this.stores.put(store.getName(), store);
            System.out.println("OK:change_completed");
            PreparedStatement preparedStatement = null;
            String query="insert into store (storeName,revenue)" + "value(?,?)";
            try {
                preparedStatement= connection.prepareStatement(query);
                preparedStatement.setString(1,name);
                preparedStatement.setInt(2,revenue);
                preparedStatement.execute();
            } catch(SQLException ex){
                Logger.getLogger(Store.class.getName()).log(Level.SEVERE,null,ex);
            }
            return true;
        }
        else{
            System.out.println("ERROR:store_identifier_already_exists");
            return false;
        }
*/
    }

    public String displayStores() throws SQLException {
        Connection c = null;
        StringBuilder sb = null;
        try {
            c = makeConnection();
            sb = new StringBuilder();
            List<Store> stores = Store.getStores(c);
            for (Store s : stores) {
                sb.append(s.toString() + "\n");
            }
        } catch (Exception e) {
            logger.warning(e.toString());
        } finally {
            try {
                c.close();
            } catch (SQLException e) {
                System.out.println("ERROR:cannot_close_connection_properly: " + e);
            }
        }
        if (sb == null) {
            return null;
        } else {
            return sb.toString();
        }
    }

    public String sellItem(String storeName, String itemName, Integer weight) throws SQLException {
        Connection c = null;
        String ret_string = "ERROR:cannot_complete_request";
        try {
            c = makeConnection();
            c.setAutoCommit(false);

            Store store = Store.getStoreByName(storeName, c);
            if (store != null) {

                StoreItem storeItem = StoreItem.getItemByStoreAndId(storeName, itemName, c);
                if (storeItem == null) {
                    StoreItem newStoreItem = new StoreItem(storeName, weight, itemName);
                    newStoreItem.createStoreItem(c);
                    ret_string = "OK:change_completed";
                } else {
                    ret_string = "ERROR:item_identifier_already_exists";
                }
            } else {
                ret_string = "ERROR:store_identifier_does_not_exist";
            }
            c.commit();
        } catch (SQLException e) {
            logger.warning(e.toString());
            //JDBCTutorialUtilities.printSQLException(e);
            if (c != null) {
                try {
                    System.err.print("Transaction is being rolled back");
                    c.rollback();
                } catch (SQLException excep) {
                    //JDBCTutorialUtilities.printSQLException(excep);
                    System.out.print(e);
                }
            }
        } catch (Exception e) {
            logger.warning(e.toString());
        } finally {
            try {
                c.close();
            } catch (SQLException e) {
                logger.warning("ERROR:cannot_close_connection_properly: " + e.toString());
            }
        }

        return ret_string;
    }

    public String displayItems(String storeName) {
        Connection c = null;
        StringBuilder sb = null;
        try {
            c = makeConnection();
            sb = new StringBuilder();
            Store store = Store.getStoreByName(storeName, c);
            if (store != null) {
                List<StoreItem> storeItems = StoreItem.getItemByStore(storeName, c);
                for (StoreItem storeItem : storeItems) {
                    sb.append(storeItem.toString() + "\n");
                }
                sb.append("OK:display_completed");
            } else {
                sb.append("ERROR:store_identifier_does_not_exist");
            }
        } catch (Exception e) {
            logger.warning(e.toString());
        } finally {
            try {
                c.close();
            } catch (SQLException e) {
                System.out.println("ERROR:cannot_close_connection_properly: " + e);
            }
        }
        if (sb == null) {
            return null;
        } else {
            return sb.toString();
        }
    }

    public String makePilot(String pilotId, String firstname, String lastname,
            String phone, String tax, String license, Integer experience) throws SQLException {
        Connection c = null;
        String ret_string = "ERROR:cannot_complete_request";
        try {
            c = makeConnection();
            c.setAutoCommit(false);

            User user = User.getAccountByName(pilotId, c);
            if (user == null) {
                user = new User(pilotId);
                user.createUser(c);

                Pilot pilot = Pilot.getPilotByLicense(license, c);
                if (pilot == null) {
                    // This WILL be null
                    pilot = new Pilot(pilotId, firstname, lastname,
                            phone, tax, license, experience);
                    pilot.createPilot(c);
                    ret_string = "OK:change_completed";
                } else {
                    c.rollback();
                    ret_string = "ERROR:pilot_license_already_exists";
                }
            } else {
                ret_string = "ERROR:pilot_identifier_already_exists";
            }
            c.commit();
        } catch (SQLException e) {
            logger.warning(e.toString());
            //JDBCTutorialUtilities.printSQLException(e);

            if (c != null) {
                try {
                    System.err.print("Transaction is being rolled back");
                    c.rollback();
                } catch (SQLException excep) {
                    //JDBCTutorialUtilities.printSQLException(excep);
                    System.out.print(e);
                }
            }
        } catch (Exception e) {
            logger.warning(e.toString());
        } finally {
            try {
                c.close();
            } catch (SQLException e) {
                System.out.println("cant close connection properly: " + e);
            }
        }

        return ret_string;
    }

    public String displayPilots() {
        Connection c = null;
        StringBuilder sb = null;
        try {
            c = makeConnection();
            sb = new StringBuilder();
            List<Pilot> pilots = Pilot.getPilots(c);
            for (Pilot pilot : pilots) {
                sb.append(pilot.toString() + "\n");
            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            try {
                c.close();
            } catch (SQLException e) {
                System.out.println("ERROR:cannot_close_connection_properly: " + e);
            }
        }
        if (sb == null) {
            return null;
        } else {
            return sb.toString();
        }
    }

    public String makeDrone(String store, Integer droneId, Integer capacity, Integer fuel) throws SQLException {

        Connection c = null;
        String ret_string = "ERROR:cannot_complete_request";
        try {
            c = makeConnection();
            c.setAutoCommit(false);
            Store s = Store.getStoreByName(store, c);
            if (s != null) {
                Drone d = Drone.getDroneByIdAndStore(droneId, store, c);
                if (d == null) {
                    d = new Drone(droneId, capacity, fuel, store);
                    d.createDrone(c);
                    ret_string = "OK:change_completed";
                } else {
                    ret_string = "ERROR:drone_identifier_already_exists";
                }
            } else {
                ret_string = "ERROR:store_identifier_does_not_exist";
            }
            c.commit();
        } catch (SQLException e) {
            logger.warning(e.toString());
            //JDBCTutorialUtilities.printSQLException(e);

            if (c != null) {
                try {
                    System.err.print("Transaction is being rolled back");
                    c.rollback();
                } catch (SQLException excep) {
                    //JDBCTutorialUtilities.printSQLException(excep);
                    System.out.print(e);
                }
            }
        } catch (Exception e) {
            logger.warning(e.toString());
        } finally {
            try {
                c.close();
            } catch (SQLException e) {
                System.out.println("cant close connection properly: " + e);
            }
        }

        return ret_string;
    }

    public String displayDrones(String store) {
        Connection c = null;
        StringBuilder sb = null;
        try {
            c = makeConnection();
            Store s = Store.getStoreByName(store, c);
            sb = new StringBuilder();
            if (s != null) {
                List<Drone> drones = Drone.getDrones(store, c);
                for (Drone drone : drones) {
                    sb.append(drone.toString() + "\n");
                }
                sb.append("OK:display_completed");
            } else {
                sb.append("ERROR:store_identifier_does_not_exist");
            }
        } catch (Exception e) {
            logger.warning(e.toString());
        } finally {
            try {
                c.close();
            } catch (SQLException e) {
                System.out.println("ERROR:cannot_close_connection_properly: " + e);
            }
        }

        if (sb == null) {
            return null;
        } else {
            return sb.toString();
        }
    }

    public String flyDrone(String storeName, Integer droneId, String pilotAccount) {

        Connection c = null;
        String ret_string = "ERROR:cannot_complete_request";
        try {
            c = makeConnection();
            c.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            c.setAutoCommit(false);

            Store s = Store.getStoreByName(storeName, c);
            if (s == null) {
                return "ERROR:store_identifier_does_not_exist";
            }

            Drone d = Drone.getDroneByIdAndStore(droneId, storeName, c);
            if (d == null) {
                return "ERROR:drone_identifier_does_not_exist";
            }

            Pilot p = Pilot.getPilotByAccount(pilotAccount, c);
            if (p == null) {
                return "ERROR:pilot_identifier_does_not_exist";
            }

            Drone dInitial = Drone.getDroneByIdAndStoreAndPilotForUpdate(pilotAccount, c);
            if (dInitial != null) {
                dInitial.removePilot(c);
            }

            try {
                d.setPilot(p, c);
                ret_string = "OK:change_completed";
            } catch (SQLException e) {
                return "ERROR:pilot_identifier_already_assigned";
            }

            c.commit();
        } catch (SQLException e) {
            logger.warning(e.toString());
            //JDBCTutorialUtilities.printSQLException(e);

            if (c != null) {
                try {
                    System.err.print("Transaction is being rolled back");
                    c.rollback();
                } catch (SQLException excep) {
                    //JDBCTutorialUtilities.printSQLException(excep);
                    System.out.print(e);
                }
            }
        } catch (Exception e) {
            logger.warning(e.toString());
        } finally {
            try {
                c.close();
            } catch (SQLException e) {
                System.out.println("ERROR:cannot_close_connection_properly: " + e);
            }
        }

        return ret_string;
    }

    public String makeCustomer(String customerId, String firstname, String lastname,
            String phone, Integer rating, Integer credit) {
        Connection c = null;
        String ret_string = "ERROR:cannot_complete_request";
        try {
            c = makeConnection();
            c.setAutoCommit(false);

            User user = User.getAccountByName(customerId, c);
            if (user == null) {
                user = new User(customerId);
                user.createUser(c);

                // This WILL be null
                Customer customer = new Customer(customerId, firstname, lastname,
                        phone, rating, credit);
                customer.createCustomer(c);
                ret_string = "OK:change_completed";
            } else {
                ret_string = "ERROR:customer_identifier_already_exists";
            }
            c.commit();
        } catch (SQLException e) {
            logger.warning(e.toString());
            //JDBCTutorialUtilities.printSQLException(e);

            if (c != null) {
                try {
                    System.err.print("Transaction is being rolled back");
                    c.rollback();
                } catch (SQLException excep) {
                    //JDBCTutorialUtilities.printSQLException(excep);
                    System.out.print(e);
                }
            }
        } catch (Exception e) {
            logger.warning(e.toString());
        } finally {
            try {
                c.close();
            } catch (SQLException e) {
                System.out.println("ERROR:cannot_close_connection_properly: " + e);
            }
        }

        return ret_string;
    }

    public String displayCustomers() {
        Connection c = null;
        StringBuilder sb = null;
        try {
            c = makeConnection();
            sb = new StringBuilder();
            List<Customer> customers = Customer.getCustomers(c);
            for (Customer customer : customers) {
                sb.append(customer.toString() + "\n");
            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            try {
                c.close();
            } catch (SQLException e) {
                System.out.println("ERROR:cannot_close_connection_properly: " + e);
            }
        }
        if (sb == null) {
            return null;
        } else {
            return sb.toString();
        }
    }

    public String startOrder(String storeName, String orderName, Integer droneId, String customerAccount) {

        Connection c = null;
        String ret_string = "ERROR:cannot_complete_request";
        try {
            c = makeConnection();
            c.setAutoCommit(false);

            Store s = Store.getStoreByName(storeName, c);
            if (s == null) {
                return "ERROR:store_identifier_does_not_exist";
            }

            Drone d = Drone.getDroneByIdAndStore(droneId, storeName, c);
            if (d == null) {
                return "ERROR:drone_identifier_does_not_exist";
            }

            Customer customer = Customer.getCustomerByAccount(customerAccount, c);
            if (customer == null) {
                return "ERROR:customer_identifier_does_not_exist";
            }

            Order o = Order.getOrderByIdAndStore(orderName, storeName, c);
            if (o == null) {
                o = new Order(orderName, storeName, customerAccount, droneId);
                o.createOrder(c);
                ret_string = "OK:change_completed";
            } else {
                return "ERROR:order_identifier_already_exists";
            }

            c.commit();
        } catch (SQLException e) {
            logger.warning(e.toString());
            //JDBCTutorialUtilities.printSQLException(e);

            if (c != null) {
                try {
                    System.err.print("Transaction is being rolled back");
                    c.rollback();
                } catch (SQLException excep) {
                    //JDBCTutorialUtilities.printSQLException(excep);
                    System.out.print(e);
                }
            }
        } catch (Exception e) {
            logger.warning(e.toString());
        } finally {
            try {
                c.close();
            } catch (SQLException e) {
                System.out.println("cant close connection properly: " + e);
            }
        }

        return ret_string;

    }

    public String displayOrders(String storeName) {

        Connection c = null;
        StringBuilder sb = null;
        try {
            c = makeConnection();
            Store s = Store.getStoreByName(storeName, c);
            sb = new StringBuilder();
            if (s != null) {
                List<Order> orders = Order.getOrdersByStore(storeName, c);
                for (Order order : orders) {
                    sb.append(order.toString());
                }
                sb.append("OK:display_completed");
            } else {
                sb.append("ERROR:store_identifier_does_not_exist");
            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            try {
                c.close();
            } catch (SQLException e) {
                System.out.println("ERROR:cannot_close_connection_properly: " + e);
            }
        }

        if (sb == null) {
            return null;
        } else {
            return sb.toString();
        }
    }

    public void cleanUp() throws SQLException {
        Connection connection = makeConnection();
        PreparedStatement preparedStatement = null;
        String fileName = new SimpleDateFormat("'orders-'yyyyMMddHHmmss'.csv'", Locale.getDefault()).format(new Date());

        String archiveQuery = "SELECT\n" +
                "'orderDate', 'orderId', 'storeName', 'customerId', 'firstName', 'lastName', 'phoneNumber', 'pilotId', 'droneId', 'itemId', 'itemPrice', 'itemQuantity'\n" +
                "UNION ALL\n" +
                "SELECT co.orderDate, co.orderId, s.storeName, c.account, c.firstName, c.lastName, c.phoneNumber, d.pilotAccount, d.droneId, l.itemId, l.itemPrice, l.itemQuantity\n" +
                "FROM CustomerOrder co\n" +
                "LEFT JOIN Drone d ON d.droneId = co.Drone_droneId\n" +
                    "AND d.store = co.Store_storeName\n" +
                "LEFT JOIN Store s ON s.storeName = co.Store_storeName\n" +
                "LEFT JOIN Customer c ON c.account = co.customerAccount\n" +
                "LEFT JOIN Line l ON l.orderId = co.orderId \n" +
                    "AND l.storeName = co.Store_storeName\n" +
                //"WHERE co.orderDate < DATE_ADD(NOW(), INTERVAL - 3 MONTH)\n" +
                "INTO OUTFILE '" + fileName + "'\n" +
                "FIELDS TERMINATED BY ','\n" +
                "ENCLOSED BY '\"'\n" +
                "LINES TERMINATED BY '\n'";

        String deleteLineQuery = "DELETE\n" +
                "FROM Line l\n"/* +
                "WHERE l.orderDate < DATE_ADD(NOW(), INTERVAL - 3 MONTH)"*/;

        String deleteCustomerOrderQuery = "DELETE\n" +
                "FROM CustomerOrder co\n"/* +
                "WHERE co.orderDate < DATE_ADD(NOW(), INTERVAL - 3 MONTH)"*/;

        try {
            preparedStatement = connection.prepareStatement(archiveQuery);
            preparedStatement.execute();
            preparedStatement = connection.prepareStatement(deleteLineQuery);
            preparedStatement.execute();
            preparedStatement = connection.prepareStatement(deleteCustomerOrderQuery);
            preparedStatement.execute();
            System.out.println("OK:data_archived");
            return;
        } catch (SQLException ex) {
            Logger.getLogger(Store.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

//    public void killAll() throws SQLException {
//        Connection connection = makeConnection();
//        PreparedStatement preparedStatement = null;
//        String query = "SELECT *\n" +
//                "CONCAT('KILL ', id, ';')\n" +
//                "FROM INFORMATION_SCHEMA.PROCESSLIST\n" +
//                "WHERE `User` = 'root'\n" +
//                "AND `Host` = '127.0.0.1'\n" +
//                "AND `db` = 'DeliveryService'";
//
//        try {
//            preparedStatement = connection.prepareStatement(query);
//            preparedStatement.execute();
//            System.out.println("OK:connections_killed");
//            return;
//        } catch (SQLException ex) {
//            Logger.getLogger(Store.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }

    public String requestItem(String storeName, String orderName, String itemName, Integer quantity, Integer unitPrice) {

        Connection c = null;
        String ret_string = "ERROR:cannot_complete_request";
        StringBuilder sb = new StringBuilder();
        try {
            c = makeConnection();
            c.setAutoCommit(false);

            Store s = Store.getStoreByName(storeName, c);
            if (s == null) {
                return "ERROR:store_identifier_does_not_exist";
            }

            Order order = Order.getOrderByIdAndStore(orderName, storeName, c);
            if (order == null) {
                return "ERROR:order_identifier_does_not_exist";
            }

            Customer customer = Customer.getCustomerByAccountForUpdate(order.getAccount(), c);
            if (customer == null) {
                return "ERROR:customer_identifier_does_not_exist";
            }

            StoreItem item = StoreItem.getItemByStoreAndId(storeName, itemName, c);
            if (item == null) {
                return "ERROR:item_identifier_does_not_exist";
            }

            Drone drone = Drone.getDroneByIdAndStoreForUpdate(order.getDroneId(), storeName, c);
            if (drone == null) {
                return "ERROR:drone_identifier_does_not_exist";
            }

            Line l = Line.getLineByOrderIdAndStoreAndItemId(orderName, storeName, itemName, c);
            if (l == null) {
                l = new Line(orderName, storeName, item, unitPrice, quantity);
                if ((order.getTotalOrderPrice() + (unitPrice * quantity)) < customer.getCredit()) {
                    if ((item.getItemWeight() * quantity) <= drone.getRemainingCapacity()) {
                        order.addLine(l, c);
                        c.commit();
                        sb.append("OK:change_completed\n");

                    } else {
                        return "ERROR:drone_cant_carry_new_item";
                    }
                } else {
                    return "ERROR:customer_cant_afford_new_item";
                }
            } else {
                return "ERROR:item_already_ordered";
            }

            return "OK:change_completed";
        } catch (SQLException e) {
            logger.warning(e.toString());
            //JDBCTutorialUtilities.printSQLException(e);

            if (c != null) {
                try {
                    System.err.print("Transaction is being rolled back");
                    c.rollback();
                } catch (SQLException excep) {
                    //JDBCTutorialUtilities.printSQLException(excep);
                    System.out.print(e);
                }
            }
        } catch (Exception e) {
            logger.warning(e.toString());
        } finally {
            try {
                c.close();
            } catch (SQLException e) {
                System.out.println("cant close connection properly: " + e);
            }
        }

        return ret_string;
        /*
        Connection c = null;
        StringBuilder sb = null;
        try {
            c = makeConnection();
            sb = new StringBuilder();
            Store store = Store.getStoreByName(storeName, c);
            if (store != null) {
                stores.get(storeName).requestItem(orderName, itemName, quantity, unitPrice);
                sb.append("OK:change_completed\n");
            } else {
                sb.append("ERROR:store_identifier_does_not_exist\n");
            }
        } catch (Exception e) {
            logger.warning(e.toString());
        } finally {
            try {
                c.close();
            } catch (SQLException e) {
                System.out.println("ERROR:cannot_close_connection_properly: " + e);
            }
        }
        if (sb == null) {
            return null;
        } else {
            return sb.toString();
        }
        */

    }

    public String purchaseOrder(String storeName, String orderId) {
        Connection c = null;
        StringBuilder sb = null;
        try {
            c = makeConnection();
            c.setAutoCommit(false);
            sb = new StringBuilder();
            Store store = Store.getStoreByName(storeName, c);
            if (store == null) {
                return "ERROR:store_identifier_does_not_exist";
            }
            Order order = Order.getOrderByIdAndStore(orderId, storeName, c);
            if (order == null) {
                return "ERROR:order_identifier_does_not_exist";
            }
            Customer customer = Customer.getCustomerByAccount(order.getAccount(), c);
            if (customer == null) {
                return "ERROR:customer_identifier_does_not_exist";
            }
            Drone drone = Drone.getDroneByIdAndStoreForUpdate(order.getDroneId(), storeName, c);
            if (drone == null) {
                return "ERROR:drone_identifier_does_not_exist";
            }
            if (drone.getPilot() == null) {
                return "ERROR:drone_needs_pilot";
            }

            Pilot pilot = Pilot.getPilotByAccount(drone.getPilot().getAccount(), c);

            if (drone.getFuel() > 0) {
                store.setRevenue(storeName, order.getTotalOrderPrice(), c);
                drone.setFuel(drone.getFuel() - 1, c);
                customer.setCredit(order.getTotalOrderPrice(), c);
                pilot.setNumberSuccessfulDeliveries(1, c);
                order.removeOrder(c);
                sb.append("OK:change_completed");
                c.commit();
                //stores.get(storeName).purchaseOrder(orderId);
            } else {
                return "ERROR:drone_needs_fuel";
            }
        } catch (Exception e) {
            logger.warning(e.toString());
        } finally {
            try {
                c.close();
            } catch (SQLException e) {
                System.out.println("ERROR:cannot_close_connection_properly: " + e);
            }
        }
        if (sb == null) {
            return null;
        } else {
            return sb.toString();
        }
    }

    public String cancelOrder(String storeName, String orderId) {
        Connection c = null;
        StringBuilder sb = null;
        try {
            c = makeConnection();
            c.setAutoCommit(false);

            sb = new StringBuilder();
            Store store = Store.getStoreByName(storeName, c);
            if (store == null) {
                return "ERROR:store_identifier_does_not_exist";
            }

            Order order = Order.getOrderByIdAndStore(orderId, storeName, c);
            if (order == null) {
                return "ERROR:order_identifier_does_not_exist";
            }

            Drone drone = Drone.getDroneByIdAndStore(order.getDroneId(), storeName, c);
            if (drone == null) {
                return "ERROR:drone_identifier_does_not_exist";
            }

            order.removeOrder(c);
            sb.append("OK:change_completed");
            c.commit();
        } catch (Exception e) {
            logger.warning(e.toString());
        } finally {
            try {
                c.close();
            } catch (SQLException e) {
                System.out.println("ERROR:cannot_close_connection_properly: " + e);
            }
        }
        if (sb == null) {
            return null;
        } else {
            return sb.toString();
        }
    }

}
