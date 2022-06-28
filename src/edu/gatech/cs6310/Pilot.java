package edu.gatech.cs6310;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Pilot extends Employee {

    private Drone currentDrone;
    private String licenseNumber;
    private Integer numberSuccessfulDeliveries;
    private Store store;

    public Pilot() {
        super();
    }

    public Pilot(String account, String firstname, String lastname,
            String phone, String tax, String license, Integer experience) {
        super(account, firstname, lastname,
                phone, tax);
        this.licenseNumber = license;
        this.numberSuccessfulDeliveries = experience;
    }

    public Integer getNumberSuccessfulDeliveries() { return this.numberSuccessfulDeliveries; }

    public void setNumberSuccessfulDeliveries(int numToIncrease, Connection c) throws SQLException {

        PreparedStatement preparedStatement = null;
        String query = "UPDATE Pilot SET numberSuccessfulDeliveries = numberSuccessfulDeliveries + ? WHERE account = ?";
        Integer counter = 1;

        preparedStatement = c.prepareStatement(query);
        preparedStatement.setInt(counter++, numToIncrease);
        preparedStatement.setString(counter++, this.account);

        preparedStatement.executeUpdate();

    }

    public static List<Pilot> getPilots(Connection c) throws SQLException {
        PreparedStatement ps = c.prepareStatement("SELECT * FROM Pilot AS P ORDER BY account ASC");
        ResultSet rs = ps.executeQuery();
        ArrayList<Pilot> pilots = new ArrayList<>();
        while (rs.next()) {
            Pilot p = null;
            p = packPilot(rs);
            pilots.add(p);
        }
        return pilots;
    }

    public static Pilot getPilotByAccount(String account, Connection c) throws SQLException {
        PreparedStatement ps = c.prepareStatement("SELECT * FROM Pilot AS P WHERE P.account = ?");
        Integer counter = 1;
        ps.setString(counter++, account);
        ResultSet rs = ps.executeQuery();
        Pilot p = null;
        if (rs.next()) {
            p = packPilot(rs);
        }
        return p;
    }

    public static Pilot getPilotByLicense(String licenseNumber, Connection c) throws SQLException {
        PreparedStatement ps = c.prepareStatement("SELECT * FROM Pilot AS P WHERE P.license = ?");
        Integer counter = 1;
        ps.setString(counter++, licenseNumber);
        ResultSet rs = ps.executeQuery();
        Pilot p = null;
        if (rs.next()) {
            p = packPilot(rs);
        }
        return p;
    }

    private static Pilot packPilot(ResultSet rs) throws SQLException {
        Pilot p = new Pilot();
        p.account = rs.getString("account");
        p.firstName = rs.getString("firstName");
        p.lastName = rs.getString("lastName");
        p.phoneNumber = rs.getString("phoneNumber");
        p.uniqueTaxIdentifier = rs.getString("tax");
        p.licenseNumber = rs.getString("license");
        p.numberSuccessfulDeliveries = rs.getInt("numberSuccessfulDeliveries");

        return p;
    }

    public String getLicenseNumber() {
        return this.licenseNumber;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public void increaseNumSuccessfulDeliveries(Integer numIncrease) {
        numberSuccessfulDeliveries += numIncrease;
    }

    @Override
    public String toString() {
        return "name:" + this.firstName + "_" + this.lastName + ",phone:" + this.phoneNumber
                + ",taxID:" + this.uniqueTaxIdentifier + ",licenseID:" + this.licenseNumber
                + ",experience:" + this.numberSuccessfulDeliveries;
    }

    public Drone getCurrentDrone() {
        return this.currentDrone;
    }

    public void setCurrentDrone(Drone drone) {
        this.currentDrone = drone;
    }

    public void createPilot(Connection c) throws SQLException {
        PreparedStatement preparedStatement = null;
        String query = "INSERT INTO Pilot (account, firstName, lastName, phoneNumber, tax, license, numberSuccessfulDeliveries)"
                + "value(?,?,?,?,?,?,?)";
        Integer counter = 1;

        preparedStatement = c.prepareStatement(query);
        preparedStatement.setString(counter++, this.account);
        preparedStatement.setString(counter++, this.firstName);
        preparedStatement.setString(counter++, this.lastName);
        preparedStatement.setString(counter++, this.phoneNumber);
        preparedStatement.setString(counter++, this.uniqueTaxIdentifier);
        preparedStatement.setString(counter++, this.licenseNumber);
        preparedStatement.setInt(counter++, this.numberSuccessfulDeliveries);

        preparedStatement.executeUpdate();
    }

}
