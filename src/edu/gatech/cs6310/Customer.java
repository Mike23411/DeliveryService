package edu.gatech.cs6310;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Customer extends User {

    private Integer credit;
    private Integer rating;

    public Customer() {}

    public Customer(String account, String firstname, String lastname,
            String phone, Integer rating, Integer credit) {
        super(account, firstname, lastname, phone);
        this.rating = rating;
        this.credit = credit;
    }

    public static List<Customer> getCustomers(Connection c) throws SQLException {
        PreparedStatement ps = c.prepareStatement("SELECT * FROM Customer AS C ORDER BY C.account ASC");
        ResultSet rs = ps.executeQuery();
        ArrayList<Customer> customers = new ArrayList<>();
        while (rs.next()) {
            Customer customer = null;
            customer = packCustomer(rs);
            customers.add(customer);
        }
        return customers;
    }

    public static Customer getCustomerByAccount(String account, Connection c) throws SQLException {
        PreparedStatement ps = c.prepareStatement("SELECT * FROM Customer AS C WHERE C.account = ?");
        Integer counter = 1;
        ps.setString(counter++, account);
        ResultSet rs = ps.executeQuery();
        Customer customer = null;
        if (rs.next()) {
            customer = packCustomer(rs);
        }
        return customer;
    }

    public static Customer getCustomerByAccountForUpdate(String account, Connection c) throws SQLException {
        PreparedStatement ps = c.prepareStatement("SELECT * FROM Customer AS C WHERE C.account = ? FOR UPDATE");
        Integer counter = 1;
        ps.setString(counter++, account);
        ResultSet rs = ps.executeQuery();
        Customer customer = null;
        if (rs.next()) {
            customer = packCustomer(rs);
        }
        return customer;
    }

    private static Customer packCustomer(ResultSet rs) throws SQLException {
        Customer c = new Customer();
        c.account = rs.getString("account");
        c.firstName = rs.getString("firstName");
        c.lastName = rs.getString("lastName");
        c.phoneNumber = rs.getString("phoneNumber");
        c.credit = rs.getInt("customerCredit");
        c.rating = rs.getInt("rating");
        return c;
    }

    public int getCredit() throws SQLException { return this.credit;}

    public void setCredit(int creditRemoved, Connection c) throws SQLException {

        PreparedStatement preparedStatement = null;
        String query = "UPDATE Customer SET customerCredit = customerCredit - ? WHERE account = ?";
        Integer counter = 1;

        preparedStatement = c.prepareStatement(query);
        preparedStatement.setInt(counter++, creditRemoved);
        preparedStatement.setString(counter++, this.account);

        preparedStatement.executeUpdate();

        this.credit += creditRemoved;
    }

    @Override
    public String toString() {
        return "name:" + this.firstName + "_" + this.lastName + ",phone:" + this.phoneNumber
                + ",rating:" + this.rating + ",credit:" + this.credit;
    }

    public void reduceCredit(Integer numToReduce) {
        this.credit -= numToReduce;
    }

    public void createCustomer(Connection c) throws SQLException {
        PreparedStatement preparedStatement = null;

        String query = "INSERT INTO Customer (account, firstName, lastName, phoneNumber, customerCredit, rating)"
                + "value(?,?,?,?,?,?)";
        Integer counter = 1;

        preparedStatement = c.prepareStatement(query);
        preparedStatement.setString(counter++, this.account);
        preparedStatement.setString(counter++, this.firstName);
        preparedStatement.setString(counter++, this.lastName);
        preparedStatement.setString(counter++, this.phoneNumber);
        preparedStatement.setInt(counter++, this.credit);
        preparedStatement.setInt(counter++, this.rating);

        preparedStatement.executeUpdate();
    }

}
