/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package screens.accounts;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import screens.accounts.assets.Accounts;
import screens.accounts.assets.ProvidersAccounts;
import screens.providers.assets.Providers;

/**
 * FXML Controller class
 *
 * @author AHMED
 */
public class AccountsScreenProvidersController implements Initializable {

    @FXML
    private ComboBox<Providers> providersCombo;
    @FXML
    private JFXTextField search;
    @FXML
    private TableView<ProvidersAccounts> tab;
    @FXML
    private TableColumn<ProvidersAccounts,String> tabType;
    @FXML
    private TableColumn<ProvidersAccounts,String> tabAccount;
    @FXML
    private TableColumn<ProvidersAccounts,String> tabDate;
    @FXML
    private TableColumn<ProvidersAccounts,String> tabAmount;
    @FXML
    private TableColumn<ProvidersAccounts,String> tabInvoices;
    @FXML
    private TableColumn<ProvidersAccounts,String> tabId;
    @FXML
    private Label id;
    @FXML
    private TextField amount;
    @FXML
    private ComboBox<?> invoices;
    @FXML
    private ComboBox<Accounts> account;
    @FXML
    private JFXDatePicker date;
    @FXML
    private ComboBox<String> type;
    @FXML
    private ProgressIndicator progress;
    @FXML
    private Button New;
    @FXML
    private Button Delete;
    @FXML
    private Button Edite;
    @FXML
    private Button Add;
 
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void getAccounts(ActionEvent event) {
    }

    @FXML
    private void search(KeyEvent event) {
    }

    @FXML
    private void New(ActionEvent event) {
    }

    @FXML
    private void Delete(ActionEvent event) {
    }

    @FXML
    private void Edite(ActionEvent event) {
    }

    @FXML
    private void Add(ActionEvent event) {
    }
    
}
