/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package screens.invoices.assets;

import java.sql.ResultSet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.util.StringConverter;
import screens.invoices.InvoicesScreenBuyController; 
import screens.invoices.InvoicesScreenMainController;
import screens.products.assets.Products;

/**
 *
 * @author AHMED
 */
public class InvoiceSellDetails {
    
    int id;
    ComboBox products;
    TextField amount;
    TextField cost;

    InvoicesScreenMainController pa;

    String product;
    String amountString;
    String costString;
    String totalCostString;
    int productID;

    public InvoiceSellDetails(int id, String product, String amountString, String costString, String totalCostString) {
        this.id = id;
        this.product = product;
        this.amountString = amountString;
        this.costString = costString;
        this.totalCostString = totalCostString;
    }

    public InvoiceSellDetails(int id, ObservableList<Products> data, String selectedpro, String amount, String cost) {
        this.id = id;
        this.products = new ComboBox(data);
        products.setConverter(new StringConverter<Products>() {
            @Override
            public String toString(Products contract) {
                return contract.getName();
            }

            @Override
            public Products fromString(String string) {
                return null;
            }
        });
        products.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        for (Products a : data) {
            if (a.getName().equals(selectedpro)) {
                products.getSelectionModel().select(a);

            }
        }
        products.setCellFactory(cell -> new ListCell<Products>() {

            GridPane gridPane = new GridPane();
            Label lblid = new Label();
            Label lblName = new Label();
            Label lblQuali = new Label();

            {

                gridPane.getColumnConstraints().addAll(
                        new ColumnConstraints(100, 100, 100), new ColumnConstraints(100, 100, 100),
                        new ColumnConstraints(100, 100, 100)
                );

                gridPane.add(lblid, 0, 1);
                gridPane.add(lblName, 1, 1);
                gridPane.add(lblQuali, 2, 1);

            }

            @Override
            protected void updateItem(Products person, boolean empty) {
                super.updateItem(person, empty);

                if (!empty && person != null) {

                    
                    lblid.setText("الاسم: " + person.getName());
                    lblName.setText("سعر الشراء: " + person.getBuyCost());
                    lblQuali.setText("سعر البيع: " + person.getCost()); 
                    setGraphic(gridPane);
                } else {
                    // Nothing to display here
                    setGraphic(null);
                }
            }
        });
        this.amount = new TextField(amount);
        this.cost = new TextField(cost);
    }

    public InvoiceSellDetails(int id, ObservableList<Products> data, String selectedpro, String amount, String cost, String totalCostString) {
        this.id = id;
        this.products = new ComboBox(data);
        products.setConverter(new StringConverter<Products>() {
            @Override
            public String toString(Products contract) {
                return contract.getName();
            }

            @Override
            public Products fromString(String string) {
                return null;
            }
        });
        products.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        for (Products a : data) {
            if (a.getName().equals(selectedpro)) {
                products.getSelectionModel().select(a);

            }
        }
        products.setCellFactory(cell -> new ListCell<Products>() {

            GridPane gridPane = new GridPane();
            Label lblid = new Label();
            Label lblName = new Label();
            Label lblQuali = new Label();

            {

                gridPane.getColumnConstraints().addAll(
                        new ColumnConstraints(100, 100, 100),
                        new ColumnConstraints(100, 100, 100),
                        new ColumnConstraints(100, 100, 100)
                );

                gridPane.add(lblid, 0, 1);
                gridPane.add(lblName, 1, 1);
                gridPane.add(lblQuali, 2, 1);

            }

            @Override
            protected void updateItem(Products person, boolean empty) {
                super.updateItem(person, empty);

                if (!empty && person != null) {

                    // Update our Labels
                    lblid.setText("الاسم: " + person.getName());
                    lblName.setText("سعر الشراء: " + person.getBuyCost());
                    lblQuali.setText("سعر البيع: " + person.getCost()); 
                    // Set this ListCell's graphicProperty to display our GridPane
                    setGraphic(gridPane);
                } else {
                    // Nothing to display here
                    setGraphic(null);
                }
            }
        });
        this.amount = new TextField(amount);
        this.cost = new TextField(cost);
        this.totalCostString = totalCostString;
    }

    public InvoicesScreenMainController getPa() {
        return pa;
    }

    public void setPa(InvoicesScreenMainController pa) {
        this.pa = pa;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ComboBox getProducts() {
        return products;
    }

    public void setProducts(ComboBox products) {
        this.products = products;
    }

    public TextField getAmount() {
        return amount;
    }

    public void setAmount(TextField amount) {
        this.amount = amount;
    }

    public TextField getCost() {
        return cost;
    }

    public void setCost(TextField cost) {
        this.cost = cost;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getAmountString() {
        return amountString;
    }

    public void setAmountString(String amountString) {
        this.amountString = amountString;
    }

    public String getCostString() {
        return costString;
    }

    public void setCostString(String costString) {
        this.costString = costString;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public String getTotalCostString() {
        return totalCostString;
    }

    public void setTotalCostString(String totalCostString) {
        this.totalCostString = totalCostString;
    }

    public static ObservableList<InvoiceSellDetails> getData(int id) throws Exception {
        ObservableList<InvoiceSellDetails> data = FXCollections.observableArrayList();
        ObservableList<Products> data1 = Products.getData();
        ResultSet rs = db.get.getReportCon().createStatement().executeQuery("SELECT `invoice_sell_details`.`id`,`products`.`name`, `invoice_sell_details`.`amount`, `invoice_sell_details`.`cost`,cast(`invoice_sell_details`.`amount` as UNSIGNED) * cast(`invoice_sell_details`.`cost` as UNSIGNED) as 'total' FROM `invoice_sell_details`,`products` WHERE `products`.`id` =`invoice_sell_details`.`product_id` AND `invoice_sell_details`.`invoice_id`='"+id+"'");
        while (rs.next()) {
            data.add(new InvoiceSellDetails(rs.getInt(1),data1, rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5)));
        }
        return data;
    }
}
