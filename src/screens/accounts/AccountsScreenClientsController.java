/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package screens.accounts;

import assets.classes.AlertDialogs;
import carzy.girl.CarzyGirl;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.util.StringConverter;
import screens.accounts.assets.Accounts;
import screens.accounts.assets.ClientsAccounts;
import screens.accounts.assets.ProvidersAccounts;
import screens.clients.assets.Clients;
import screens.invoices.assets.InvoiceSell;

/**
 * FXML Controller class
 *
 * @author AHMED
 */
public class AccountsScreenClientsController implements Initializable {

    @FXML
    private ComboBox<Clients> clientsCombo;
    @FXML
    private JFXTextField search;
    @FXML
    private TableView<ClientsAccounts> tab;
    @FXML
    private TableColumn<ClientsAccounts, String> tabType;
    @FXML
    private TableColumn<ClientsAccounts, String> tabAccount;
    @FXML
    private TableColumn<ClientsAccounts, String> tabDate;
    @FXML
    private TableColumn<ClientsAccounts, String> tabAmount;
    @FXML
    private TableColumn<ClientsAccounts, String> tabInvoices;
    @FXML
    private TableColumn<ClientsAccounts, String> tabId;
    @FXML
    private Label id;
    @FXML
    private TextField amount;
    @FXML
    private ComboBox<InvoiceSell> invoices;
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

    Preferences prefs;

    DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    @FXML
    private Label totalAcc;

    int CLIENT_ID;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        prefs = Preferences.userNodeForPackage(CarzyGirl.class);
        date.setConverter(new StringConverter<LocalDate>() {

            @Override
            public String toString(LocalDate localDate) {
                if (localDate == null) {
                    return "";
                }
                return format.format(localDate);
            }

            @Override
            public LocalDate fromString(String dateString) {
                if (dateString == null || dateString.trim().isEmpty()) {
                    return null;
                }
                return LocalDate.parse(dateString, format);
            }
        });
        progress.setVisible(true);
        Service<Void> service = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        //Background work                       
                        final CountDownLatch latch = new CountDownLatch(1);
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    clear();
                                    intialColumn();
                                    fillCombo();

                                } catch (Exception ex) {
                                    AlertDialogs.showErrors(ex);
                                } finally {
                                    latch.countDown();
                                }
                            }

                        });
                        latch.await();

                        return null;
                    }
                };

            }

            @Override
            protected void succeeded() {
                progress.setVisible(false);

                super.succeeded();
            }
        };
        service.start();
        tab.setOnMouseClicked((e) -> {
            if (tab.getSelectionModel().getSelectedIndex() == -1) {
            } else {
                New.setDisable(false);

                Delete.setDisable(false);

                Edite.setDisable(false);

                Add.setDisable(true);

                ClientsAccounts selected = tab.getSelectionModel().getSelectedItem();
                id.setText(Integer.toString(selected.getId()));
                date.setValue(LocalDate.parse(selected.getDate()));
                amount.setText(selected.getAmount());

                ObservableList<InvoiceSell> items1 = invoices.getItems();
                for (InvoiceSell a : items1) {
                    if (a.getId() == selected.getInvoice_id()) {
                        invoices.getSelectionModel().select(a);
                    }
                }
                ObservableList<Accounts> items = account.getItems();
                for (Accounts a : items) {
                    if (a.getId() == selected.getAcc_id()) {
                        account.getSelectionModel().select(a);
                    }
                }
                type.getSelectionModel().select(selected.getType());
            }
        });
    }

    private void intialColumn() {
        tabType.setCellValueFactory(new PropertyValueFactory<>("type"));

        tabAccount.setCellValueFactory(new PropertyValueFactory<>("acc_id"));

        tabDate.setCellValueFactory(new PropertyValueFactory<>("date"));

        tabAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));

        tabInvoices.setCellValueFactory(new PropertyValueFactory<>("invoice_id"));

        tabId.setCellValueFactory(new PropertyValueFactory<>("id"));

    }

    private void clear() {
        getAutoNum();
        New.setDisable(true);

        Delete.setDisable(true);

        Edite.setDisable(true);

        Add.setDisable(false);

        date.setValue(null);

        amount.setText("");

        invoices.getSelectionModel().clearSelection();

        account.getSelectionModel().clearSelection();

        type.getSelectionModel().clearSelection();
    }

    private void getAutoNum() {
        progress.setVisible(true);
        Service<Void> service = new Service<Void>() {
            String autoNum;

            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        try {
                            autoNum = ClientsAccounts.getAutoNum();

                        } catch (Exception ex) {
                            AlertDialogs.showErrors(ex);
                        }
                        return null;
                    }
                };

            }

            @Override
            protected void succeeded() {
                progress.setVisible(false);
                id.setText(autoNum);
                super.succeeded();
            }
        };
        service.start();
    }

    private void getData(int id) {
        progress.setVisible(true);
        Service<Void> service = new Service<Void>() {
            ObservableList<ClientsAccounts> data;
            String total;

            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        try {
                            data = ClientsAccounts.getData(id);
                            total = ClientsAccounts.getTotal(id);

                        } catch (Exception ex) {
                            AlertDialogs.showErrors(ex);
                        }
                        return null;
                    }
                };

            }

            @Override
            protected void succeeded() {
                progress.setVisible(false);
                tab.setItems(data);
                items = data;
                totalAcc.setText(total);
                super.succeeded();
            }
        };
        service.start();
    }
    ObservableList<ClientsAccounts> items;

    private void fillCombo() {
        type.getItems().addAll("مستحق", "دفعة");
        progress.setVisible(true);
        Service<Void> service = new Service<Void>() {
            ObservableList<Accounts> AccountsData;
            ObservableList<Clients> clientData;

            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        try {
                            clientData = Clients.getData();
                            AccountsData = Accounts.getData();

                        } catch (Exception ex) {
                            AlertDialogs.showErrors(ex);
                        }
                        return null;
                    }
                };

            }

            @Override
            protected void succeeded() {
                progress.setVisible(false);
                clientsCombo.setItems(clientData);
                clientsCombo.setConverter(new StringConverter<Clients>() {
                    @Override
                    public String toString(Clients patient) {
                        return patient.getName();
                    }

                    @Override
                    public Clients fromString(String string) {
                        return null;
                    }
                });
                clientsCombo.setCellFactory(cell -> new ListCell<Clients>() {

                    GridPane gridPane = new GridPane();
                    Label lblid = new Label();
                    Label lblName = new Label();

                    {
                        gridPane.getColumnConstraints().addAll(
                                new ColumnConstraints(100, 100, 100),
                                new ColumnConstraints(100, 100, 100)
                        );

                        gridPane.add(lblid, 0, 1);
                        gridPane.add(lblName, 1, 1);

                    }

                    @Override
                    protected void updateItem(Clients person, boolean empty) {
                        super.updateItem(person, empty);

                        if (!empty && person != null) {

                            lblid.setText("م: " + Integer.toString(person.getId()));
                            lblName.setText("الاسم: " + person.getName());

                            setGraphic(gridPane);
                        } else {
                            setGraphic(null);
                        }
                    }
                });
                account.setItems(AccountsData);
                account.setConverter(new StringConverter<Accounts>() {
                    @Override
                    public String toString(Accounts patient) {
                        return patient.getName();
                    }

                    @Override
                    public Accounts fromString(String string) {
                        return null;
                    }
                });
                account.setCellFactory(cell -> new ListCell<Accounts>() {

                    GridPane gridPane = new GridPane();
                    Label lblid = new Label();
                    Label lblName = new Label();

                    {
                        gridPane.getColumnConstraints().addAll(
                                new ColumnConstraints(100, 100, 100),
                                new ColumnConstraints(100, 100, 100)
                        );

                        gridPane.add(lblid, 0, 1);
                        gridPane.add(lblName, 1, 1);

                    }

                    @Override
                    protected void updateItem(Accounts person, boolean empty) {
                        super.updateItem(person, empty);

                        if (!empty && person != null) {

                            lblid.setText("م: " + Integer.toString(person.getId()));
                            lblName.setText("الاسم: " + person.getName());

                            setGraphic(gridPane);
                        } else {
                            setGraphic(null);
                        }
                    }
                });
                super.succeeded();
            }
        };
        service.start();

    }

    private void fillInvoicesCombo(int id) {

        try {
            invoices.setItems(InvoiceSell.getData(id));
            invoices.setConverter(new StringConverter<InvoiceSell>() {
                @Override
                public String toString(InvoiceSell patient) {
                    return patient.getDate();
                }

                @Override
                public InvoiceSell fromString(String string) {
                    return null;
                }
            });
            invoices.setCellFactory(cell -> new ListCell<InvoiceSell>() {

                GridPane gridPane = new GridPane();
                Label lblid = new Label();
                Label lblName = new Label();

                {
                    gridPane.getColumnConstraints().addAll(
                            new ColumnConstraints(100, 100, 100),
                            new ColumnConstraints(100, 100, 100)
                    );

                    gridPane.add(lblid, 0, 1);
                    gridPane.add(lblName, 1, 1);

                }

                @Override
                protected void updateItem(InvoiceSell person, boolean empty) {
                    super.updateItem(person, empty);

                    if (!empty && person != null) {

                        lblid.setText("م: " + Integer.toString(person.getId()));
                        lblName.setText("التاريخ: " + person.getDate());

                        setGraphic(gridPane);
                    } else {
                        setGraphic(null);
                    }
                }
            });
        } catch (Exception ex) {
            AlertDialogs.showErrors(ex);
        }

    }

    @FXML
    private void getAccounts(ActionEvent event) {
        try {
            CLIENT_ID = clientsCombo.getSelectionModel().getSelectedItem().getId();
            getData(CLIENT_ID);
            fillInvoicesCombo(CLIENT_ID);

        } catch (Exception ex) {
            AlertDialogs.showErrors(ex);
        }
    }

    @FXML
    private void search(KeyEvent event) {
        FilteredList<ClientsAccounts> filteredData = new FilteredList<>(items, p -> true);

        filteredData.setPredicate(pa -> {

            if (search.getText() == null || search.getText().isEmpty()) {
                return true;
            }

            String lowerCaseFilter = search.getText().toLowerCase();

            return (pa.getDate().contains(lowerCaseFilter)
                    || pa.getType().contains(lowerCaseFilter)
                    || pa.getAmount().contains(lowerCaseFilter));

        });

        SortedList<ClientsAccounts> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tab.comparatorProperty());
        tab.setItems(sortedData);
    }

    @FXML
    private void New(ActionEvent event) {
        clear();
    }

    @FXML
    private void Delete(ActionEvent event) {
        progress.setVisible(true);
        Service<Void> service = new Service<Void>() {
            boolean ok = true;
            ClientsAccounts ac;
            ClientsAccounts selectedItem = tab.getSelectionModel().getSelectedItem();

            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        //Background work                       
                        final CountDownLatch latch = new CountDownLatch(1);
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                                    alert.setTitle("Editing Account");
                                    alert.setHeaderText("سيتم  تعديل الحساب ");
                                    alert.setContentText("هل انت متاكد؟");

                                    Optional<ButtonType> result = alert.showAndWait();
                                    if (result.get() == ButtonType.OK) {
                                        ac = new ClientsAccounts();
                                        ac.setId(Integer.parseInt(id.getText()));
                                        ac.Delete();
                                    }

                                } catch (Exception ex) {
                                    AlertDialogs.showErrors(ex);
                                    ok = false;

                                } finally {
                                    latch.countDown();
                                }
                            }

                        });
                        latch.await();

                        return null;
                    }
                };

            }

            @Override
            protected void succeeded() {
                progress.setVisible(false);
                if (ok) {
                    if (type.getSelectionModel().getSelectedItem().equals("دفعة")) {
                        try {
                            Accounts.removeFromAccount(account.getSelectionModel().getSelectedItem().getId(), amount.getText());
                        } catch (Exception ex) {
                            AlertDialogs.showErrors(ex);
                        }
                    }
                    clear();
                    getData(CLIENT_ID);

                }
                super.succeeded();
            }
        };
        service.start();
    }

    @FXML
    private void Edite(ActionEvent event) {
        progress.setVisible(true);
        Service<Void> service = new Service<Void>() {
            boolean ok = true;
            ClientsAccounts ac;
            ClientsAccounts selectedItem = tab.getSelectionModel().getSelectedItem();

            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        //Background work                       
                        final CountDownLatch latch = new CountDownLatch(1);
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                                    alert.setTitle("Editing Account");
                                    alert.setHeaderText("سيتم  تعديل الحساب ");
                                    alert.setContentText("هل انت متاكد؟");

                                    Optional<ButtonType> result = alert.showAndWait();
                                    if (result.get() == ButtonType.OK) {

                                        if (selectedItem.getType().equals("دفعة")) {
                                            try {
                                                Accounts.removeFromAccount(selectedItem.getAcc_id(), selectedItem.getAmount());
                                            } catch (Exception ex) {
                                                AlertDialogs.showErrors(ex);
                                            }
                                        }
                                        ac = new ClientsAccounts();
                                        ac.setId(Integer.parseInt(id.getText()));
                                        ac.setClient_id(CLIENT_ID);
                                        ac.setInvoice_id(invoices.getSelectionModel().getSelectedItem().getId());
                                        ac.setType(type.getSelectionModel().getSelectedItem());
                                        ac.setAcc_id(account.getSelectionModel().getSelectedItem().getId());
                                        ac.setAmount(amount.getText());
                                        ac.setDate(date.getValue().format(format));
                                        ac.Edite();
                                    }

                                } catch (Exception ex) {
                                    AlertDialogs.showErrors(ex);
                                    ok = false;
                                    if (selectedItem.getType().equals("دفعة")) {
                                        try {
                                            Accounts.addToAccount(selectedItem.getAcc_id(), selectedItem.getAmount());
                                        } catch (Exception sex) {
                                            AlertDialogs.showErrors(sex);
                                        }
                                    }
                                } finally {
                                    latch.countDown();
                                }
                            }

                        });
                        latch.await();

                        return null;
                    }
                };

            }

            @Override
            protected void succeeded() {
                progress.setVisible(false);
                if (ok) {
                    if (type.getSelectionModel().getSelectedItem().equals("دفعة")) {
                        try {
                            Accounts.addToAccount(account.getSelectionModel().getSelectedItem().getId(), amount.getText());
                        } catch (Exception ex) {
                            AlertDialogs.showErrors(ex);
                        }
                    }
                    clear();
                    getData(CLIENT_ID);

                }
                super.succeeded();
            }
        };
        service.start();
    }

    @FXML
    private void Add(ActionEvent event) {
        progress.setVisible(true);
        Service<Void> service = new Service<Void>() {
            boolean ok = true;
            ClientsAccounts ac;

            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        //Background work                       
                        final CountDownLatch latch = new CountDownLatch(1);
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    ac = new ClientsAccounts();
                                    ac.setId(Integer.parseInt(id.getText()));
                                    ac.setClient_id(CLIENT_ID);
                                    ac.setInvoice_id(invoices.getSelectionModel().getSelectedItem().getId());
                                    ac.setType(type.getSelectionModel().getSelectedItem());
                                    ac.setAcc_id(account.getSelectionModel().getSelectedItem().getId());
                                    ac.setAmount(amount.getText());
                                    ac.setDate(date.getValue().format(format));
                                    ac.Add();

                                } catch (Exception ex) {
                                    AlertDialogs.showErrors(ex);
                                    ok = false;
                                } finally {
                                    latch.countDown();
                                }
                            }

                        });
                        latch.await();

                        return null;
                    }
                };

            }

            @Override
            protected void succeeded() {
                progress.setVisible(false);
                if (ok) {
                    if (type.getSelectionModel().getSelectedItem().equals("دفعة")) {
                        try {
                            Accounts.addToAccount(account.getSelectionModel().getSelectedItem().getId(), amount.getText());
                        } catch (Exception ex) {
                            AlertDialogs.showErrors(ex);
                        }
                    }
                    clear();
                    getData(CLIENT_ID);

                }
                super.succeeded();
            }
        };
        service.start();
    }

}
