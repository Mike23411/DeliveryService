package edu.gatech.cs6310;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/* Stores Information about an item assigned to a Store */
public class StoreItem {

    private String itemName;
    private Integer itemWeight;
    private String storeName;

    public StoreItem() {}

    public StoreItem(String storeName, Integer itemWeight, String itemName) {
        this.storeName = storeName;
        this.itemWeight = itemWeight;
        this.itemName = itemName;
    }

    public static List<StoreItem> getItemByStore(String storeName, Connection c) throws SQLException {
        PreparedStatement ps = c.prepareStatement("SELECT * FROM StoreItem AS S WHERE storeName = ? ORDER BY S.itemName ASC");
        Integer counter = 1;
        ps.setString(counter++, storeName);
        ResultSet rs = ps.executeQuery();
        ArrayList<StoreItem> storeItems = new ArrayList<>();
        while (rs.next()) {
            StoreItem s = new StoreItem();
            s.storeName = rs.getString("storeName");
            s.itemWeight = rs.getInt("itemWeight");
            s.itemName = rs.getString("itemName");
            storeItems.add(s);
        }
        return storeItems;

    }

    public static StoreItem getItemByStoreAndId(String storeName, String itemName, Connection c) throws SQLException {
        PreparedStatement ps = c.prepareStatement("SELECT * FROM StoreItem AS S WHERE S.storeName = ? AND S.itemName = ?");
        int counter = 1;
        ps.setString(counter++, storeName);
        ps.setString(counter++, itemName);
        ResultSet rs = ps.executeQuery();
        StoreItem s = null;
        if (rs.next()) {

            s = new StoreItem();
            s.storeName = rs.getString("storeName");
            s.itemWeight = rs.getInt("itemWeight");
            s.itemName = rs.getString("itemName");
        }
        return s;
    }

    public Integer getItemWeight() {
        return itemWeight;
    }

    public String getItemName() {
        return itemName;
    }

    @Override
    public String toString() {
        return itemName + "," + itemWeight;
    }

    public void createStoreItem(Connection c) throws SQLException {
        PreparedStatement preparedStatement = null;

        String query = "INSERT INTO StoreItem (storeName,itemWeight, itemName)" + "value(?,?,?)";
        Integer counter = 1;
        preparedStatement = c.prepareStatement(query);
        preparedStatement.setString(counter++, this.storeName);
        preparedStatement.setInt(counter++, this.itemWeight);
        preparedStatement.setString(counter++, this.itemName);
        preparedStatement.executeUpdate();
    }

}
