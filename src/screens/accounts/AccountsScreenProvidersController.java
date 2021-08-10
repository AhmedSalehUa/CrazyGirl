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
import screens.invoices.assets.InvoiceBuy;
import screens.invoices.assets.InvoiceSell;
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
    private TableColumn<ProvidersAccounts, String> tabType;
    @FXML
    private TableColumn<ProvidersAccounts, String> tabAccount;
    @FXML
    private TableColumn<ProvidersAccounts, String> tabDate;
    @FXML
    private TableColumn<ProvidersAccounts, String> tabAmount;
    @FXML
    private TableColumn<ProvidersAccounts, String> tabInvoices;
    @FXML
    private TableColumn<ProvidersAccounts, String> tabId;
    @FXML
    private Label id;
    @FXML
    private TextField amount;
    @FXML
    private ComboBox<InvoiceBuy> invoices;
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
    @FXML
    private Label totalAcc;
    Preferences prefs;

    DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    int PROVIDER_ID;

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

                ProvidersAccounts selected = tab.getSelectionModel().getSelectedItem();
                id.setText(Integer.toString(selected.getId()));
                date.setValue(LocalDate.parse(selected.getDate()));
                amount.setText(selected.getAmount());

                ObservableList<InvoiceBuy> items1 = invoices.getItems();
                for (InvoiceBuy a : items1) {
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
                            autoNum = ProvidersAccounts.getAutoNum();

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
            ObservableList<ProvidersAccounts> data;
            String total;

            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        try {
                            data = ProvidersAccounts.getData(id);
                            total = ProvidersAccounts.getTotal(id);
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
    ObservableList<ProvidersAccounts> items;

    private void fillCombo() {
        type.getItems().addAll("مستحق", "دفعة");
        progress.setVisible(true);
        Service<Void> service = new Service<Void>() {
            ObservableList<Accounts> AccountsData;
            ObservableList<Providers> ProvidersData;

            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        try {
                            ProvidersData = Providers.getData();
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
                providersCombo.setItems(ProvidersData);
                providersCombo.setConverter(new StringConverter<Providers>() {
                    @Override
                    public String toString(Providers patient) {
                        return patient.getName();
                    }

                    @Override
                    public Providers fromString(String string) {
                        return null;
                    }
                });
                providersCombo.setCellFactory(cell -> new ListCell<Providers>() {

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
                    protected void updateItem(Providers person, boolean empty) {
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
            invoices.setItems(InvoiceBuy.getDataForProvider(id));
            invoices.setConverter(new StringConverter<InvoiceBuy>() {
                @Override
                public String toString(InvoiceBuy patient) {
                    return patient.getDate();
                }

                @Override
                public InvoiceBuy fromString(String string) {
                    return null;
                }
            });
            invoices.setCellFactory(cell -> new ListCell<InvoiceBuy>() {

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
                protected void updateItem(InvoiceBuy person, boolean empty) {
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
            PROVIDER_ID = providersCombo.getSelectionModel().getSelectedItem().getId();
            getData(PROVIDER_ID);
            fillInvoicesCombo(PROVIDER_ID);

        } catch (Exception ex) {
            AlertDialogs.showErrors(ex);
        }
    }

    @FXML
    private void search(KeyEvent event) {
        FilteredList<ProvidersAccounts> filteredData = new FilteredList<>(items, p -> true);

        filteredData.setPredicate(pa -> {

            if (search.getText() == null || search.getText().isEmpty()) {
                return true;
            }

            String lowerCaseFilter = search.getText().toLowerCase();

            return (pa.getDate().contains(lowerCaseFilter)
                    || pa.getType().contains(lowerCaseFilter)
                    || pa.getAmount().contains(lowerCaseFilter));

        });

        SortedList<ProvidersAccounts> sortedData = new SortedList<>(filteredData);
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
            ProvidersAccounts ac;
            ProvidersAccounts selectedItem = tab.getSelectionModel().getSelectedItem();

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
                                        ac = new ProvidersAccounts();
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
                            Accounts.addToAccount(account.getSelectionModel().getSelectedItem().getId(), amount.getText());
                        } catch (Exception ex) {
                            AlertDialogs.showErrors(ex);
                        }
                    }
                    clear();
                    getData(PROVIDER_ID);

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
            ProvidersAccounts ac;
            ProvidersAccounts selectedItem = tab.getSelectionModel().getSelectedItem();

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
                                                Accounts.addToAccount(selectedItem.getAcc_id(), selectedItem.getAmount());
                                            } catch (Exception ex) {
                                                AlertDialogs.showErrors(ex);
                                            }
                                        }
                                        ac = new ProvidersAccounts();
                                        ac.setId(Integer.parseInt(id.getText()));
                                        ac.setProvider_id(PROVIDER_ID);
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
                                            Accounts.removeFromAccount(selectedItem.getAcc_id(), selectedItem.getAmount());
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
                            Accounts.removeFromAccount(account.getSelectionModel().getSelectedItem().getId(), amount.getText());
                        } catch (Exception ex) {
                            AlertDialogs.showErrors(ex);
                        }
                    }
                    clear();
                    getData(PROVIDER_ID);

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
            ProvidersAccounts ac;

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
                                    ac = new ProvidersAccounts();
                                    ac.setId(Integer.parseInt(id.getText()));
                                    ac.setProvider_id(PROVIDER_ID);
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
                            Accounts.removeFromAccount(account.getSelectionModel().getSelectedItem().getId(), amount.getText());
                        } catch (Exception ex) {
                            AlertDialogs.showErrors(ex);
                        }
                    }
                    clear();
                    getData(PROVIDER_ID);

                }
                super.succeeded();
            }
        };
        service.start();
    }

}
