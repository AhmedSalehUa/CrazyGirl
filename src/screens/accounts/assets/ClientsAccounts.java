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
public class ClientsAccounts {

    int id;
    int client_id;
    String client;
    int invoice_id;
    int acc_id;
    String amount;
    String date;
    String type;

    public ClientsAccounts() {
    }

    public ClientsAccounts(int id, String client, int invoice_id, int acc_id, String amount, String date, String type) {
        this.id = id;
        this.client = client;
        this.invoice_id = invoice_id;
        this.acc_id = acc_id;
        this.amount = amount;
        this.date = date;
        this.type = type;
    }

    public ClientsAccounts(int id, int client_id, int invoice_id, int acc_id, String amount, String date, String type) {
        this.id = id;
        this.client_id = client_id;
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
        PreparedStatement ps = db.get.Prepare("INSERT INTO `clients_accounts`(`client_id`, `invoice_id`, `acc_id`, `amount`, `date`, `type`) VALUES (?,?,?,?,?,?)");
        ps.setInt(1, client_id);
        ps.setInt(2, invoice_id);
        ps.setInt(3, acc_id);
        ps.setString(4, amount);
        ps.setString(5, date);
        ps.setString(6, type);
        ps.execute();

        return true;
    }

    public boolean Edite() throws Exception {
        PreparedStatement ps = db.get.Prepare("UPDATE `clients_accounts` SET `client_id`=?,`invoice_id`=?,`acc_id`=?,`amount`=?,`date`=?,`type`=? WHERE `id`=?");
        ps.setInt(1, client_id);
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
        PreparedStatement ps = db.get.Prepare("DELETE FROM `clients_accounts` WHERE `id`=?");
        ps.setInt(1, id);
        ps.execute();
        return true;
    }

    public static boolean rollback(int invoiceId) throws Exception {
        ObservableList<ClientsAccounts> data = ClientsAccounts.getDataByInvoice(invoiceId);
        for (ClientsAccounts clientsAccounts : data) {
            if (clientsAccounts.getType().equals("مستحق")) {
                System.out.println("main not add or remove");
            } else {
                System.out.println("remove : " + clientsAccounts.getAmount() + "  from :" + clientsAccounts.getAcc_id());
                Accounts.removeFromAccount(clientsAccounts.getAcc_id(), clientsAccounts.getAmount());
            }
        }
        PreparedStatement ps = db.get.Prepare("DELETE FROM `clients_accounts` WHERE `invoice_id`=?");
        ps.setInt(1, invoiceId);
        ps.execute();
        return true;
    }

    public static ObservableList<ClientsAccounts> getData(int id) throws Exception {
        ObservableList<ClientsAccounts> data = FXCollections.observableArrayList();
        ResultSet rs = db.get.getReportCon().createStatement().executeQuery("SELECT `clients_accounts`.`id`, `clients`.`name`, `clients_accounts`.`invoice_id`, `clients_accounts`.`acc_id`, `clients_accounts`.`amount`, `clients_accounts`.`date`, `clients_accounts`.`type` FROM `clients_accounts`,`clients` WHERE `clients_accounts`.`client_id` = `clients`.`id` AND `clients_accounts`.`client_id` ='" + id + "'");
        while (rs.next()) {
            data.add(new ClientsAccounts(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getInt(4), rs.getString(5), rs.getString(6), rs.getString(7)));
        }
        return data;
    }

    public static ObservableList<ClientsAccounts> getDataByInvoice(int id) throws Exception {
        ObservableList<ClientsAccounts> data = FXCollections.observableArrayList();
        ResultSet rs = db.get.getReportCon().createStatement().executeQuery("SELECT `clients_accounts`.`id`, `clients_accounts`.`client_id`, `clients_accounts`.`invoice_id`, `clients_accounts`.`acc_id`, `clients_accounts`.`amount`, `clients_accounts`.`date`, `clients_accounts`.`type` FROM `clients_accounts`  WHERE `clients_accounts`.`invoice_id` ='" + id + "'");
        while (rs.next()) {
            data.add(new ClientsAccounts(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getInt(4), rs.getString(5), rs.getString(6), rs.getString(7)));
        }
        return data;
    }

    public static String getTotal(int id) throws Exception {
        return db.get.getTableData("SELECT SUM(amount) FROM "
                + "(" + "SELECT IFNULL(SUM(CAST(`amount` as UNSIGNED)),'0') as amount FROM `clients_accounts` WHERE  `type` = 'مستحق' AND  `client_id`='" + id + "'"
                + " UNION ALL "
                + "SELECT IFNULL(0 - SUM(CAST(`amount` as UNSIGNED)),'0') as amount FROM `clients_accounts` WHERE  `type` = 'دفعة' AND  `client_id`='" + id + "'"
                + ")a").getValueAt(0, 0).toString();

    }

    public static String getAutoNum() throws Exception {
        return db.get.getTableData("SELECT IFNULL(MAX(`id`)+1,1) FROM `clients_accounts`").getValueAt(0, 0).toString();
    }

}
