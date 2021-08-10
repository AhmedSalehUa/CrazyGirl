/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package screens.invoices.assets;

import carzy.girl.CarzyGirl;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.prefs.Preferences;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import screens.accounts.assets.Accounts;
import screens.accounts.assets.ClientsAccounts;
import screens.accounts.assets.ProvidersAccounts;
import screens.products.assets.Products;

/**
 *
 * @author AHMED
 */
public class InvoiceSell {

    int id;
    int client_id;
    String client;
    String date;
    String cost;
    String dicount;
    String discount_percent;
    String total_cost;
    String notes;
    InputStream doc;
    String ext;
    ObservableList<InvoiceSellDetails> details;
    Preferences prefs;
    String onNote;

    public InvoiceSell() {
        prefs = Preferences.userNodeForPackage(CarzyGirl.class);
    }

    public InvoiceSell(int id, String client, String date, String cost, String dicount, String discount_percent, String total_cost, String notes, String onNote) {
        this.id = id;
        this.client = client;
        this.date = date;
        this.cost = cost;
        this.dicount = dicount;
        this.discount_percent = discount_percent;
        this.total_cost = total_cost;
        this.notes = notes;
        this.onNote = onNote;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClient_id() {
        return client_id;
    }

    public void setClient_id(int client_id) {
        this.client_id = client_id;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
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

    public ObservableList<InvoiceSellDetails> getDetails() {
        return details;
    }

    public void setDetails(ObservableList<InvoiceSellDetails> details) {
        this.details = details;
    }

    public String getOnNote() {
        return onNote;
    }

    public void setOnNote(String onNote) {
        this.onNote = onNote;
    }

    public boolean AddDetails() throws Exception {
        PreparedStatement ps = db.get.Prepare("INSERT INTO `invoice_sell_details`(`invoice_id`, `product_id`, `cost`, `amount`, `total_cost`) VALUES (?,?,?,?,?)");

        for (InvoiceSellDetails a : details) {
            ps.setInt(1, id);
            Products b = (Products) a.getProducts().getSelectionModel().getSelectedItem();
            ps.setInt(2, b.getId());
            ps.setString(3, a.getCost().getText());
            ps.setString(4, a.getAmount().getText());
            ps.setString(5, Integer.toString(Integer.parseInt(a.getAmount().getText()) * Integer.parseInt(a.getCost().getText())));
            ps.addBatch();
            Products.removeAmount(b.getId(), a.getAmount().getText());

        }
        ps.executeBatch();
        return true;
    }

    public boolean AddToAccounts() throws Exception {
        ClientsAccounts pr = new ClientsAccounts();
        pr.setAmount(total_cost);
        pr.setDate(date);
        pr.setType("مستحق");
        pr.setInvoice_id(id);
        pr.setAcc_id(0);
        pr.setClient_id(client_id);
        pr.Add();
        return true;
    }

    public boolean Add() throws Exception {
        PreparedStatement ps = db.get.Prepare("INSERT INTO `invoice_sell`(`id`, `client_id`, `date`, `cost`, `discount`, `discount_percent`, `total`,`note`,`onNote`) VALUES (?,?,?,?,?,?,?,?,?)");
        ps.setInt(1, id);
        ps.setInt(2, client_id);
        ps.setString(3, date);
        ps.setString(4, cost);
        ps.setString(5, dicount);
        ps.setString(6, discount_percent);
        ps.setString(7, total_cost);
        ps.setString(8, notes);
        ps.setString(9, onNote);
        ps.execute();
        AddDetails();
        if (Boolean.parseBoolean(onNote)) {
            AddToAccounts();
        } else {
            Accounts.addToAccount(Integer.parseInt(prefs.get("MAIN_ACCOUNT_ID", "0")), total_cost);
        }

        return true;
    }

    public boolean Edite() throws Exception {
        PreparedStatement ps = db.get.Prepare("UPDATE `invoice_sell` SET `client_id`=?,`date`=?,`cost`=?,`discount`=?,`discount_percent`=?,`total`=?,`note`=?,`onNote`=? WHERE `id`=?");
        ps.setInt(1, client_id);
        ps.setString(2, date);
        ps.setString(3, cost);
        ps.setString(4, dicount);
        ps.setString(5, discount_percent);
        ps.setString(6, total_cost);
        ps.setString(7, notes);
        ps.setString(8, onNote);
        ps.setInt(9, id);

        DeleteDetails();
        ps.execute();
        AddDetails();
        return true;
    }

    public boolean DeleteDetails() throws Exception {
        ObservableList<InvoiceBuyDetails> data = InvoiceBuyDetails.getData(id);
        for (InvoiceBuyDetails a : data) {
            Products b = (Products) a.getProducts().getSelectionModel().getSelectedItem();
            Products.addAmount(b.getId(), a.getAmount().getText());
        }
        PreparedStatement ps = db.get.Prepare("DELETE FROM `invoice_sell_details` WHERE `invoice_id`=?");
        ps.setInt(1, id);
        ps.execute();
        return true;
    }

    public boolean Delete() throws Exception {
        PreparedStatement ps = db.get.Prepare("DELETE FROM `invoice_sell` WHERE `id`=?");
        ps.setInt(1, id);

        DeleteDetails();
        ClientsAccounts.rollback(id);
        ps.execute();
        return true;
    }

    public static ObservableList<InvoiceSell> getData() throws Exception {
        ObservableList<InvoiceSell> data = FXCollections.observableArrayList();
        ResultSet rs = db.get.getReportCon().createStatement().executeQuery("SELECT `invoice_sell`.`id`,`clients`.`name`, `invoice_sell`.`date`, `invoice_sell`.`cost`, `invoice_sell`.`discount`, `invoice_sell`.`discount_percent`,`invoice_sell`.`total`,`invoice_sell`.`note`,`invoice_sell`.`onNote` FROM `invoice_sell`,`clients` WHERE `clients`.`id` =`invoice_sell`.`client_id`");
        while (rs.next()) {
            data.add(new InvoiceSell(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9)));
        }
        return data;
    }

    public static ObservableList<InvoiceSell> getData(int id) throws Exception {
        ObservableList<InvoiceSell> data = FXCollections.observableArrayList();
        ResultSet rs = db.get.getReportCon().createStatement().executeQuery("SELECT `invoice_sell`.`id`,`clients`.`name`, `invoice_sell`.`date`, `invoice_sell`.`cost`, `invoice_sell`.`discount`, `invoice_sell`.`discount_percent`,`invoice_sell`.`total`,`invoice_sell`.`note`,`invoice_sell`.`onNote` FROM `invoice_sell`,`clients` WHERE `clients`.`id` =`invoice_sell`.`client_id` AND `invoice_sell`.`id`='" + id + "'");
        while (rs.next()) {
            data.add(new InvoiceSell(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9)));
        }
        return data;
    }

    public static ObservableList<InvoiceSell> getCutomData(String sql) throws Exception {
        ObservableList<InvoiceSell> data = FXCollections.observableArrayList();
        ResultSet rs = db.get.getReportCon().createStatement().executeQuery(sql);
        while (rs.next()) {
            data.add(new InvoiceSell(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(8)));
        }
        return data;
    }

    public static String getAutoNum() throws Exception {
        return db.get.getTableData("SELECT IFNULL(MAX(`id`)+1,1) FROM `invoice_sell`").getValueAt(0, 0).toString();
    }
}
