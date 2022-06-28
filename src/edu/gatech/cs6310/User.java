package edu.gatech.cs6310;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class User {

    protected String account;
    protected String firstName;
    protected String lastName;
    protected String phoneNumber;

    public User() {}

    public User(String account) {
        this.account = account;
    }

    public User(String account, String firstname, String lastname,
            String phone) {
        this.account = account;
        this.firstName = firstname;
        this.lastName = lastname;
        this.phoneNumber = phone;
    }

    public static User getAccountByName(String account, Connection c) throws SQLException {
        PreparedStatement ps = c.prepareStatement("SELECT * FROM DeliveryServiceUser AS U WHERE U.account = ?");
        int counter = 1;
        ps.setString(counter++, account);
        ResultSet rs = ps.executeQuery();
        User u = null;
        if (rs.next()) {
            u = new User();
            u.account = rs.getString("account");
        }
        return u;

    }

    public String getAccount() {
        return account;
    }

    public void createUser(Connection c) throws SQLException {
        PreparedStatement preparedStatement = null;

        String query = "INSERT INTO DeliveryServiceUser (account)" + "value(?)";
        int counter = 1;
        preparedStatement = c.prepareStatement(query);
        preparedStatement.setString(counter++, account);
        preparedStatement.executeUpdate();
    }

}
