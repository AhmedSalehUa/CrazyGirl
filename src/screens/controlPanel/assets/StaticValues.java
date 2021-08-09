/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package screens.controlpanel.assets;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author ahmed
 */
public class StaticValues {

    String attribute;
    String valus;
    String oldAttribute;

    public StaticValues() {
    }

    public StaticValues(String attribute, String valus) {
        this.attribute = attribute;
        this.valus = valus;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public String getValus() {
        return valus;
    }

    public void setValus(String valus) {
        this.valus = valus;
    }

    public String getOldAttribute() {
        return oldAttribute;
    }

    public void setOldAttribute(String oldAttribute) {
        this.oldAttribute = oldAttribute;
    }

    public boolean Add() throws Exception {
        PreparedStatement ps = db.get.Prepare("INSERT INTO `static_values`(`attribute`, `value`) VALUES (?,?)");
        ps.setString(1, attribute);
        ps.setString(2, valus);
        ps.execute();
        return true;
    }

    public boolean Edite() throws Exception {
        PreparedStatement ps = db.get.Prepare("UPDATE `static_values` SET `attribute`=?,`value`=? WHERE `attribute`=?");
        ps.setString(1, attribute);
        ps.setString(2, valus);
        ps.setString(3, oldAttribute);
        ps.execute();
        return true;
    }

    public boolean Delete() throws Exception {
        PreparedStatement ps = db.get.Prepare("DELETE FROM `static_values` WHERE `attribute``=?");
        ps.setString(1, attribute);

        ps.execute();
        return true;
    }

    public static ObservableList<StaticValues> getData() throws Exception {
        ObservableList<StaticValues> data = FXCollections.observableArrayList();
        ResultSet rs = db.get.getReportCon().createStatement().executeQuery("SELECT * FROM `static_values`");
        while (rs.next()) {
            data.add(new StaticValues(rs.getString(1), rs.getString(2)));
        }
        return data;
    }

}
