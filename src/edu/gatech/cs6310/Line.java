package edu.gatech.cs6310;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class Line {

    private Integer itemPrice;
    private Integer itemQuantity;
    private StoreItem currentItem;
    private String orderId;
    private String storeName;

    public Line() { }

    public Line(String orderId, String storeName, StoreItem currentItem,
                Integer itemPrice, Integer itemQuantity) {
        this.orderId = orderId;
        this.storeName = storeName;
        this.currentItem = currentItem;
        this.itemPrice = itemPrice;
        this.itemQuantity = itemQuantity;
    }

    public StoreItem getCurrentItem() {
        return currentItem;
    }

    public Integer getItemPrice() {
        return itemPrice;
    }

    public Integer getItemQuantity() {
        return itemQuantity;
    }

    @Override
    public String toString() {
        return "item_name:" + currentItem.getItemName() + ",total_quantity:" + itemQuantity
                + ",total_cost:" + getTotalLinePrice() + ",total_weight:" + getTotalLineWeight();
    }

    int getTotalLinePrice() {return itemPrice * itemQuantity;}

    Integer getTotalLineWeight() {return currentItem.getItemWeight() * itemQuantity;}

    private static Line packLine(ResultSet rs, Connection c) throws SQLException {
        Line l = new Line();
        l.itemPrice = rs.getInt("itemPrice");
        l.itemQuantity = rs.getInt("itemQuantity");
        l.storeName = rs.getString("storeName");
        String itemId = rs.getString("itemId");
        l.currentItem = StoreItem.getItemByStoreAndId(l.storeName, itemId, c);
        return l;
    }

    public static List<Line> getLinesByOrderIdAndStore(String orderId, String storeName, Connection c) throws SQLException {
        PreparedStatement ps = c.prepareStatement(
                "SELECT * FROM Line WHERE orderId = ? AND storeName = ? ORDER BY itemId ASC"
        );
        Integer counter = 1;
        ps.setString(counter++, storeName);
        ps.setString(counter++, orderId);
        ResultSet rs = ps.executeQuery();
        ArrayList<Line> lines = new ArrayList<>();
        while (rs.next()) {
            Line l = null;
            l = packLine(rs, c);
            lines.add(l);
        }
        return lines;
    }

    public static TreeMap<String, Line> getLinesByOrderIdAndStoreTM(String orderId, String storeName, Connection c) throws SQLException {
        PreparedStatement ps = c.prepareStatement(
                "SELECT * FROM Line WHERE orderId = ? AND storeName = ? ORDER BY itemId ASC"
        );
        Integer counter = 1;
        ps.setString(counter++, orderId);
        ps.setString(counter++, storeName);
        ResultSet rs = ps.executeQuery();
        TreeMap<String, Line> lines = new TreeMap<>();
        while (rs.next()) {
            Line l = null;
            l = packLine(rs, c);
            lines.put(l.getCurrentItem().getItemName(), l);
        }
        return lines;
    }

    public static Line getLineByOrderIdAndStoreAndItemId(String orderId, String storeName, String itemId, Connection c) throws SQLException {
        PreparedStatement ps = c.prepareStatement(
                "SELECT * FROM Line WHERE orderId = ? AND storeName = ? AND itemId = ? ORDER BY orderId DESC"
        );
        Integer counter = 1;
        ps.setString(counter++, orderId);
        ps.setString(counter++, storeName);
        ps.setString(counter++, itemId);
        ResultSet rs = ps.executeQuery();
        Line l = null;
        if (rs.next()) {
            l = packLine(rs, c);
        }
        return l;
    }

    public void createLine(Connection c) throws SQLException {
        PreparedStatement preparedStatement = null;
        String query = "INSERT INTO Line (itemPrice, itemQuantity, itemId, orderId, storeName)"
                + "value(?,?,?,?,?)";
        Integer counter = 1;

        preparedStatement = c.prepareStatement(query);
        preparedStatement.setInt(counter++, this.itemPrice);
        preparedStatement.setInt(counter++, this.itemQuantity);
        preparedStatement.setString(counter++, this.currentItem.getItemName());
        preparedStatement.setString(counter++, this.orderId);
        preparedStatement.setString(counter++, this.storeName);

        preparedStatement.executeUpdate();
    }


}
