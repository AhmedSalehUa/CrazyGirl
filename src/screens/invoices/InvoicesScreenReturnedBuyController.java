/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package screens.invoices;

import com.jfoenix.controls.JFXDatePicker;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author AHMED
 */
public class InvoicesScreenReturnedBuyController implements Initializable {

    @FXML
    private TextArea notes;
    @FXML
    private Button print;
    @FXML
    private Button showInvoice;
    @FXML
    private ComboBox<?> invoiceId;
    @FXML
    private ComboBox<?> providers;
    @FXML
    private JFXDatePicker date;
    @FXML
    private TableView<?> invoiceTable;
    @FXML
    private MenuItem deleteRow;
    @FXML
    private TableColumn<?, ?> invoiceTabCost;
    @FXML
    private TableColumn<?, ?> invoiceTabAmount;
    @FXML
    private TableColumn<?, ?> invoiceTabMedicine;
    @FXML
    private TableColumn<?, ?> invoiceTabId;
    @FXML
    private Button invoiceDelete;
    @FXML
    private Button invoiveAdd;
    @FXML
    private ProgressIndicator progress;
    @FXML
    private TextField invoiceTotal;
    @FXML
    private TextField invoicedisc;
    @FXML
    private TextField invoiceDiscPercent;
    @FXML
    private TextField invoiceLastTotal;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void printInvoices(ActionEvent event) {
    }

    @FXML
    private void showInvoice(ActionEvent event) {
    }

    @FXML
    private void deleteRow(ActionEvent event) {
    }

    @FXML
    private void invoiceDelete(ActionEvent event) {
    }

    @FXML
    private void invoiveAdd(ActionEvent event) {
    }
    
}
