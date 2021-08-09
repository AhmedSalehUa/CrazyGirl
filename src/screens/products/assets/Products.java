/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package screens.products.assets;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author AHMED
 */
public class Products {

    int id;
    String name;
    String barcode;
    String amount;
    String buyCost;
    String minCost;
    String cost;

    public Products() {
    }

    public Products(int id, String name, String barcode, String amount, String buyCost, String minCost, String cost) {
        this.id = id;
        this.name = name;
        this.barcode = barcode;
        this.amount = amount;
        this.buyCost = buyCost;
        this.minCost = minCost;
        this.cost = cost;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getBuyCost() {
        return buyCost;
    }

    public void setBuyCost(String buyCost) {
        this.buyCost = buyCost;
    }

    public String getMinCost() {
        return minCost;
    }

    public void setMinCost(String minCost) {
        this.minCost = minCost;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public boolean Add() throws Exception {
        PreparedStatement ps = db.get.Prepare("INSERT INTO `products`(`id`, `name`, `barcode`, `amount`,`buy_cost`, `min_cost`, `cost`) VALUES (?,?,?,?,?,?)");
        ps.setInt(1, id);
        ps.setString(2, name);
        ps.setString(3, barcode);
        ps.setString(4, amount);
        ps.setString(5, buyCost);
        ps.setString(6, minCost);
        ps.setString(7, cost);
        ps.execute();
        return true;
    }

    public boolean Edite() throws Exception {
        PreparedStatement ps = db.get.Prepare("UPDATE `products` SET `name`=?,`barcode`=?,`amount`=?,`buy_cost`=?,`min_cost`=?,`cost`=? WHERE `id`=?");
        ps.setString(1, name);
        ps.setString(2, barcode);
        ps.setString(3, amount);
        ps.setString(4, buyCost);
        ps.setString(5, minCost);
        ps.setString(6, cost);
        ps.setInt(7, id);
        ps.execute();
        return true;
    }

    public boolean Delete() throws Exception {
        PreparedStatement ps = db.get.Prepare("DELETE FROM `products` WHERE `id`=?");
        ps.setInt(1, id);
        ps.execute();
        return true;
    }

    public static ObservableList<Products> getData() throws Exception {
        ObservableList<Products> data = FXCollections.observableArrayList();
        ResultSet rs = db.get.getReportCon().createStatement().executeQuery("SELECT * FROM `products`");
        while (rs.next()) {
            data.add(new Products(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7)));
        }
        return data;
    }

    public static String getAutoNum() throws Exception {
        return db.get.getTableData("SELECT IFNULL(MAX(`id`)+1,1) FROM `products`").getValueAt(0, 0).toString();
    }
}
