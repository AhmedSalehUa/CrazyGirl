/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package screens.providers.assets;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author AHMED
 */
public class Providers {

    int id;
    String name;
    String location;
    String accNum;
    String tele1;
    String tele2;

    public Providers() {
    }

    public Providers(int id, String name, String location, String accNum, String tele1, String tele2) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.accNum = accNum;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAccNum() {
        return accNum;
    }

    public void setAccNum(String accNum) {
        this.accNum = accNum;
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
        PreparedStatement ps = db.get.Prepare("INSERT INTO `providers`(`id`, `name`, `location`, `acc_num`, `tele1`, `tele2`) VALUES (?,?,?,?,?,?)");
        ps.setInt(1, id);
        ps.setString(2, name);
        ps.setString(3, location);
        ps.setString(4, accNum);
        ps.setString(5, tele1);
        ps.setString(6, tele2);
        ps.execute();
        return true;
    }

    public boolean Edite() throws Exception {
        PreparedStatement ps = db.get.Prepare("UPDATE `providers` SET `name`=?,`location`=?,`acc_num`=?,`tele1`=?,`tele2`=? WHERE `id`=?");
        ps.setString(1, name);
        ps.setString(2, location);
        ps.setString(3, accNum);
        ps.setString(4, tele1);
        ps.setString(5, tele2);
        ps.setInt(6, id);
        ps.execute();
        return true;
    }

    public boolean Delete() throws Exception {
        PreparedStatement ps = db.get.Prepare("DELETE FROM `providers` WHERE `id`=?");
        ps.setInt(1, id);
        ps.execute();
        return true;
    }

    public static ObservableList<Providers> getData() throws Exception {
        ObservableList<Providers> data = FXCollections.observableArrayList();
        ResultSet rs = db.get.getReportCon().createStatement().executeQuery("SELECT * FROM `providers`");
        while (rs.next()) {
            data.add(new Providers(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6)));
        }
        return data;
    }

    public static String getAutoNum() throws Exception {
        return db.get.getTableData("SELECT IFNULL(MAX(`id`)+1,1) FROM `providers`").getValueAt(0, 0).toString();
    }
}
