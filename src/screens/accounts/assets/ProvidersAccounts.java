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
public class ProvidersAccounts {

    int id;
    int provider_id;
    String provider;
    int invoice_id;
    int acc_id;
    String amount;
    String date;
    String type;

    public ProvidersAccounts() {
    }

    public ProvidersAccounts(int id, String provider, int invoice_id, int acc_id, String amount, String date, String type) {
        this.id = id;
        this.provider = provider;
        this.invoice_id = invoice_id;
        this.acc_id = acc_id;
        this.amount = amount;
        this.date = date;
        this.type = type;
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

    public int getInvoice_id() {
        return invoice_id;
    }

    public void setInvoice_id(int invoice_id) {
        this.invoice_id = invoice_id;
    }

    public int getAcc_id() {
        return acc_id;
    }

    public void setAcc_id(int acc_id) {
        this.acc_id = acc_id;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean Add() throws Exception {
        PreparedStatement ps = db.get.Prepare("INSERT INTO `providers_accounts`(`provider_id`, `invoice_id`, `acc_id`, `amount`, `date`, `type`) VALUES (?,?,?,?,?,?)");
        ps.setInt(1, provider_id);
        ps.setInt(2, invoice_id);
        ps.setInt(3, acc_id);
        ps.setString(4, amount);
        ps.setString(5, date);
        ps.setString(6, type);
        ps.execute();
        return true;
    }

    public boolean Edite() throws Exception {
        PreparedStatement ps = db.get.Prepare("UPDATE `providers_accounts` SET `provider_id`=?,`invoice_id`=?,`acc_id`=?,`amount`=?,`date`=?,`type`=? WHERE `id`=?");
        ps.setInt(1, provider_id);
        ps.setInt(2, invoice_id);
        ps.setInt(3, acc_id);
        ps.setString(4, amount);
        ps.setString(5, date);
        ps.setString(6, type);
        ps.setInt(7, id);
        ps.execute();
        return true;
    }

    public boolean Delete() throws Exception {
        PreparedStatement ps = db.get.Prepare("DELETE FROM `providers_accounts` WHERE `id`=?");
        ps.setInt(1, id);
        ps.execute();
        return true;
    }

    public static boolean rollback(int invoiceId) throws Exception {
        ObservableList<ProvidersAccounts> data = ProvidersAccounts.getDataByInvoice(invoiceId);
        for (ProvidersAccounts clientsAccounts : data) {
            if (clientsAccounts.getType().equals("مستحق")) {
                System.out.println("main not add or remove");
            } else {
                System.out.println("add : " + clientsAccounts.getAmount() + "  from :" + clientsAccounts.getAcc_id());
                Accounts.addToAccount(clientsAccounts.getAcc_id(), clientsAccounts.getAmount());
            }
        }
        PreparedStatement ps = db.get.Prepare("DELETE FROM `providers_accounts` WHERE `invoice_id`=?");
        ps.setInt(1, invoiceId);
        ps.execute();
        return true;
    }

    public static ObservableList<ProvidersAccounts> getData() throws Exception {
        ObservableList<ProvidersAccounts> data = FXCollections.observableArrayList();
        ResultSet rs = db.get.getReportCon().createStatement().executeQuery("SELECT `providers_accounts`.`id`, `providers`.`name`, `providers_accounts`.`invoice_id`, `providers_accounts`.`acc_id`, `providers_accounts`.`amount`, `providers_accounts`.`date`, `providers_accounts`.`type` FROM `providers_accounts`,`providers` WHERE `providers_accounts`.`provider_id` = `providers`.`id`");
        while (rs.next()) {
            data.add(new ProvidersAccounts(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getInt(4), rs.getString(5), rs.getString(6), rs.getString(7)));
        }
        return data;
    }

    public static ObservableList<ProvidersAccounts> getData(int id) throws Exception {
        ObservableList<ProvidersAccounts> data = FXCollections.observableArrayList();
        ResultSet rs = db.get.getReportCon().createStatement().executeQuery("SELECT `providers_accounts`.`id`, `providers`.`name`, `providers_accounts`.`invoice_id`, `providers_accounts`.`acc_id`, `providers_accounts`.`amount`, `providers_accounts`.`date`, `providers_accounts`.`type` FROM `providers_accounts`,`providers` WHERE `providers_accounts`.`provider_id` = `providers`.`id` AND `providers_accounts`.`provider_id` = '" + id + "'");
        while (rs.next()) {
            data.add(new ProvidersAccounts(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getInt(4), rs.getString(5), rs.getString(6), rs.getString(7)));
        }
        return data;
    }

    public static ObservableList<ProvidersAccounts> getDataByInvoice(int id) throws Exception {
        ObservableList<ProvidersAccounts> data = FXCollections.observableArrayList();
        ResultSet rs = db.get.getReportCon().createStatement().executeQuery("SELECT `providers_accounts`.`id`, `providers_accounts`.`provider_id`, `providers_accounts`.`invoice_id`, `providers_accounts`.`acc_id`, `providers_accounts`.`amount`, `providers_accounts`.`date`, `providers_accounts`.`type` FROM `providers_accounts` WHERE `providers_accounts`.`invoice_id` = '" + id + "'");
        while (rs.next()) {
            data.add(new ProvidersAccounts(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getInt(4), rs.getString(5), rs.getString(6), rs.getString(7)));
        }
        return data;
    }

    public static String getTotal(int id) throws Exception {
        return db.get.getTableData("SELECT SUM(amount) FROM "
                + "(" + "SELECT IFNULL(SUM(CAST(`amount` as UNSIGNED)),'0') as amount FROM `providers_accounts` WHERE  `type` = 'مستحق' AND  `provider_id`='" + id + "'"
                + " UNION ALL "
                + "SELECT IFNULL(0 - SUM(CAST(`amount` as UNSIGNED)),'0') as amount FROM `providers_accounts` WHERE  `type` = 'دفعة' AND  `provider_id`='" + id + "'"
                + ")a").getValueAt(0, 0).toString();

    }

    public static String getAutoNum() throws Exception {
        return db.get.getTableData("SELECT IFNULL(MAX(`id`)+1,1) FROM `providers_accounts`").getValueAt(0, 0).toString();
    }

}
