/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package screens.accounts.assets;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author AHMED
 */
public class Accounts {

    int id;
    String name;
    String balance;
    String owner;

    public Accounts() {
    }

    public Accounts(int id, String name, String balance, String owner) {
        this.id = id;
        this.name = name;
        this.balance = balance;
        this.owner = owner;
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

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public static boolean addToAccount(int id, String amount) throws Exception {
        Double a = Double.parseDouble(db.get.getTableData("select balance from accounts where id='" + id + "'").getValueAt(0, 0).toString()) + Double.parseDouble(amount);
        PreparedStatement ps = db.get.Prepare("UPDATE `accounts` SET `balance`=? WHERE `id`=?");
        ps.setString(1, Double.toString(a));
        ps.setInt(2, id);
        ps.execute();
        return true;
    }

    public static boolean removeFromAccount(int id, String amount) throws Exception {
        Double a = Double.parseDouble(db.get.getTableData("select balance from accounts where id='" + id + "'").getValueAt(0, 0).toString()) - Double.parseDouble(amount);
        if (a < 0) {
            throw new Exception("الحساب لا يكفي لخصم المبلغ");
        } else {
            PreparedStatement ps = db.get.Prepare("UPDATE `accounts` SET `balance`=? WHERE `id`=?");
            ps.setString(1, Double.toString(a));
            ps.setInt(2, id);
            ps.execute();
            return true;
        }
    }

    public boolean Add() throws Exception {
        PreparedStatement ps = db.get.Prepare("INSERT INTO `accounts`(`id`, `name`, `balance`, `owner`) VALUES (?,?,?,?)");
        ps.setInt(1, id);
        ps.setString(2, name);
        ps.setString(3, balance);
        ps.setString(4, owner);
        ps.execute();
        return true;
    }

    public boolean Edite() throws Exception {
        PreparedStatement ps = db.get.Prepare("UPDATE `accounts` SET `name`=?,`balance`=?,`owner`=? WHERE `id`=?");
        ps.setString(1, name);
        ps.setString(2, balance);
        ps.setString(3, owner);
        ps.setInt(4, id);
        ps.execute();
        return true;
    }

    public boolean Delete() throws Exception {
        PreparedStatement ps = db.get.Prepare("DELETE FROM `accounts` WHERE `id`=?");
        ps.setInt(1, id);
        ps.execute();
        return true;
    }

    public static ObservableList<Accounts> getData() throws Exception {
        ObservableList<Accounts> data = FXCollections.observableArrayList();
        ResultSet rs = db.get.getReportCon().createStatement().executeQuery("SELECT * FROM `accounts`");
        while (rs.next()) {
            data.add(new Accounts(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4)));
        }
        return data;
    }

    public static String getAutoNum() throws Exception {
        return db.get.getTableData("SELECT IFNULL(MAX(`id`)+1,1) FROM `accounts`").getValueAt(0, 0).toString();
    }
}
