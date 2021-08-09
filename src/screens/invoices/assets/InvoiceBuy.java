/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package screens.invoices.assets;

import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import screens.products.assets.Products;

/**
 *
 * @author AHMED
 */
public class InvoiceBuy {

    int id;
    int provider_id;
    String provider;
    String date;
    String cost;
    String dicount;
    String discount_percent;
    String total_cost;
    String notes;
    InputStream doc;
    String ext;
    ObservableList<InvoiceBuyDetails> details;

    public InvoiceBuy() {
    }

    public InvoiceBuy(int id, String provider, String date, String cost, String dicount, String discount_percent, String total_cost, String notes) {
        this.id = id;
        this.provider = provider;
        this.date = date;
        this.cost = cost;
        this.dicount = dicount;
        this.discount_percent = discount_percent;
        this.total_cost = total_cost;
        this.notes = notes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProvider_id() {
        return provider_id;
    }

    public void setProvider_id(int provider_id) {
        this.provider_id = provider_id;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getDicount() {
        return dicount;
    }

    public void setDicount(String dicount) {
        this.dicount = dicount;
    }

    public String getDiscount_percent() {
        return discount_percent;
    }

    public void setDiscount_percent(String discount_percent) {
        this.discount_percent = discount_percent;
    }

    public String getTotal_cost() {
        return total_cost;
    }

    public void setTotal_cost(String total_cost) {
        this.total_cost = total_cost;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public InputStream getDoc() {
        return doc;
    }

    public void setDoc(InputStream doc) {
        this.doc = doc;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public ObservableList<InvoiceBuyDetails> getDetails() {
        return details;
    }

    public void setDetails(ObservableList<InvoiceBuyDetails> details) {
        this.details = details;
    }

    public boolean AddDetails() throws Exception {
        PreparedStatement ps = db.get.Prepare("INSERT INTO `invoice_buy_details`(`invoice_id`, `product_id`, `cost`, `amount`, `total_cost`) VALUES (?,?,?,?,?)");

        for (InvoiceBuyDetails a : details) {
            ps.setInt(1, id);
            Products b = (Products) a.getProducts().getSelectionModel().getSelectedItem();
            ps.setInt(2, b.getId());
            ps.setString(3, a.getCost().getText());
            ps.setString(4, a.getAmount().getText());
            ps.setString(5, Integer.toString(Integer.parseInt(a.getAmount().getText()) * Integer.parseInt(a.getCost().getText())));
            ps.addBatch();
        }
        ps.executeBatch();
        return true;
    }

    public boolean Add() throws Exception {
        PreparedStatement ps = db.get.Prepare("INSERT INTO `invoice_buy`(`id`, `provider_id`, `date`, `cost`, `discount`, `discount_percent`, `total`,`note`) VALUES (?,?,?,?,?,?,?,?)");
        ps.setInt(1, id);
        ps.setInt(2, provider_id);
        ps.setString(3, date);
        ps.setString(4, cost);
        ps.setString(5, dicount);
        ps.setString(6, discount_percent);
        ps.setString(7, total_cost);
        ps.setString(8, notes);
        ps.execute();
        AddDetails();
        return true;
    }

    public boolean Edite() throws Exception {
        PreparedStatement ps = db.get.Prepare("UPDATE `invoice_buy` SET `provider_id`=?,`date`=?,`cost`=?,`discount`=?,`discount_percent`=?,`total`=?,`note`=? WHERE `id`=?");
        ps.setInt(1, provider_id);
        ps.setString(2, date);
        ps.setString(3, cost);
        ps.setString(4, dicount);
        ps.setString(5, discount_percent);
        ps.setString(6, total_cost);
        ps.setString(7, notes);
        ps.setInt(8, id);

        DeleteDetails();
        ps.execute();
        AddDetails();
        return true;
    }

    public boolean DeleteDetails() throws Exception {
        PreparedStatement ps = db.get.Prepare("DELETE FROM `sl_offers_details` WHERE `offer_id`=?");
        ps.setInt(1, id);
        ps.execute();
        return true;
    }

    public boolean Delete() throws Exception {
        PreparedStatement ps = db.get.Prepare("DELETE FROM `sl_offers` WHERE `id`=?");
        ps.setInt(1, id);

        DeleteDetails();

        ps.execute();
        return true;
    }

    public static ObservableList<InvoiceBuy> getData() throws Exception {
        ObservableList<InvoiceBuy> data = FXCollections.observableArrayList();
        ResultSet rs = db.get.getReportCon().createStatement().executeQuery("SELECT `invoice_buy`.`id`,`providers`.`name`,`invoice_buy`.`date`, `invoice_buy`.`cost`, `invoice_buy`.`discount`, `invoice_buy`.`discount_percent`, `invoice_buy`.`total`, `invoice_buy`.`note` FROM `invoice_buy`,`providers` WHERE `providers`.`id` =`invoice_buy`.`provider_id`");
        while (rs.next()) {
            data.add(new InvoiceBuy(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8)));
        }
        return data;
    }

    public static ObservableList<InvoiceBuy> getCutomData(String sql) throws Exception {
        ObservableList<InvoiceBuy> data = FXCollections.observableArrayList();
        ResultSet rs = db.get.getReportCon().createStatement().executeQuery(sql);
        while (rs.next()) {
            data.add(new InvoiceBuy(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8)));
        }
        return data;
    }

    public static String getAutoNum() throws Exception {
        return db.get.getTableData("SELECT IFNULL(MAX(`id`)+1,1) FROM `invoice_buy`").getValueAt(0, 0).toString();
    }
}
