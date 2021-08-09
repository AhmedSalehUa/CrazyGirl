/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package screens.clients.assets;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author AHMED
 */
public class Clients {

    int id;
    String name;
    String tele1;
    String tele2;

    public Clients() {
    }

    public Clients(int id, String name, String tele1, String tele2) {
        this.id = id;
        this.name = name;
        this.tele1 = tele1;
        this.tele2 = tele2;
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

    public String getTele1() {
        return tele1;
    }

    public void setTele1(String tele1) {
        this.tele1 = tele1;
    }

    public String getTele2() {
        return tele2;
    }

    public void setTele2(String tele2) {
        this.tele2 = tele2;
    }

    public boolean Add() throws Exception {
        PreparedStatement ps = db.get.Prepare("INSERT INTO `clients`(`id`, `name`, `tele1`, `tele2`) VALUES (?,?,?,?)");
        ps.setInt(1, id);
        ps.setString(2, name);
        ps.setString(3, tele1);
        ps.setString(4, tele2);
        ps.execute();
        return true;
    }

    public boolean Edite() throws Exception {
        PreparedStatement ps = db.get.Prepare("UPDATE `clients` SET `name`=?,`tele1`=?,`tele2`=? WHERE `id`=?");
        ps.setString(1, name);
        ps.setString(2, tele1);
        ps.setString(3, tele2);
        ps.setInt(4, id);
        ps.execute();
        return true;
    }

    public boolean Delete() throws Exception {
        PreparedStatement ps = db.get.Prepare("DELETE FROM `clients` WHERE `id`=?");
        ps.setInt(1, id);
        ps.execute();
        return true;
    }

    public static ObservableList<Clients> getData() throws Exception {
        ObservableList<Clients> data = FXCollections.observableArrayList();
        ResultSet rs = db.get.getReportCon().createStatement().executeQuery("SELECT * FROM `clients`");
        while (rs.next()) {
            data.add(new Clients(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4)));
        }
        return data;
    }

    public static String getAutoNum() throws Exception {
        return db.get.getTableData("SELECT IFNULL(MAX(`id`)+1,1) FROM `clients`").getValueAt(0, 0).toString();
    }
    
}
