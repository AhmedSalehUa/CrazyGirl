/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package screens.invoices;

import assets.classes.AlertDialogs;
import com.jfoenix.controls.JFXDatePicker;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.CountDownLatch;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.util.StringConverter;
import javax.imageio.ImageIO;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import screens.clients.assets.Clients;
import screens.invoices.assets.InvoiceSell;
import screens.invoices.assets.InvoiceSellDetails;
import screens.products.assets.Products;

/**
 * FXML Controller class
 *
 * @author AHMED
 */
public class InvoicesScreenReturnedSellController implements Initializable {

    @FXML
    private TextArea notes;
    @FXML
    private Button print;
    @FXML
    private Button showInvoice;
    @FXML
    private ComboBox<InvoiceSell> invoiceId;
    @FXML
    private ComboBox<Clients> clients;
    @FXML
    private JFXDatePicker date;
    @FXML
    private TableView<InvoiceSellDetails> invoiceTable;
    @FXML
    private MenuItem deleteRow;
    @FXML
    private TableColumn<InvoiceSellDetails, String> invoiceTabCost;
    @FXML
    private TableColumn<InvoiceSellDetails, String> invoiceTabAmount;
    @FXML
    private TableColumn<InvoiceSellDetails, String> invoiceTabMedicine;
    @FXML
    private TableColumn<InvoiceSellDetails, String> invoiceTabId;
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
    @FXML
    private CheckBox onNote;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        date.setConverter(new StringConverter<LocalDate>() {
            private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            @Override
            public String toString(LocalDate localDate) {
                if (localDate == null) {
                    return "";
                }
                return dateTimeFormatter.format(localDate);
            }

            @Override
            public LocalDate fromString(String dateString) {
                if (dateString == null || dateString.trim().isEmpty()) {
                    return null;
                }
                return LocalDate.parse(dateString, dateTimeFormatter);
            }
        });
        progress.setVisible(true);
        Service<Void> service = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
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
    }

    private void intialColumn() {
        invoiceTabCost.setCellValueFactory(new PropertyValueFactory<>("cost"));

        invoiceTabAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));

        invoiceTabMedicine.setCellValueFactory(new PropertyValueFactory<>("products"));

        invoiceTabId.setCellValueFactory(new PropertyValueFactory<>("id"));
    }

    private void getData(int id) {
        try {
//            ObservableList<InvoiceBuy> s = InvoiceBuy.getData(id);
//            for (InvoiceBuy a : s) {
//                System.out.println(a.getNotes() +"   " + a.getCost() +"   " + a.getDiscount_percent()+"   " +  a.getTotal_cost());
//            }
            InvoiceSell in = InvoiceSell.getData(id).get(0);
            notes.setText(in.getNotes());
            invoiceTotal.setText(in.getCost());
            invoiceDiscPercent.setText(in.getDiscount_percent());
            invoiceLastTotal.setText(in.getTotal_cost());
            invoicedisc.setText(in.getDicount());
            date.setValue(LocalDate.parse(in.getDate()));
            onNote.setSelected(Boolean.parseBoolean(in.getOnNote()));

            ObservableList<Clients> items1 = clients.getItems();
            for (Clients a : items1) {
                if (a.getName().equals(in.getClient())) {
                    clients.getSelectionModel().select(a);
                }
            }

            ObservableList<Products> data = Products.getData();
            invoiceTable.setItems(InvoiceSellDetails.getData(id));
            invoiceTable.getItems().add(new InvoiceSellDetails(1, data, "0", "0", "0", "0"));
            invoiceTable.setOnKeyReleased((event) -> {

                if (event.getCode() == KeyCode.ENTER) {
                    setTotal("");
                }
                if (event.getCode() == KeyCode.ENTER && invoiceTable.getSelectionModel().getSelectedItem().getProducts().getSelectionModel().getSelectedIndex() != -1
                        && !invoiceTable.getSelectionModel().getSelectedItem().getAmount().getText().equals("0")
                        && !invoiceTable.getSelectionModel().getSelectedItem().getCost().getText().equals("0")) {
                    setTotal("");
                    invoiceTable.getItems().add(new InvoiceSellDetails(invoiceTable.getItems().size() + 1, data, "0", "0", "0", null));
                    invoiceTable.getSelectionModel().clearAndSelect(invoiceTable.getItems().size() - 1);
                }

            });
        } catch (Exception ex) {
            AlertDialogs.showErrors(ex);
        }
    }

    private void clear() {
        try {
            notes.setText("");
            invoiceTotal.setText("");
            invoiceDiscPercent.setText("");
            invoiceLastTotal.setText("");
            invoicedisc.setText("");
            date.setValue(null);
            clients.getSelectionModel().clearSelection();
            invoiceTable.setItems(null);
        } catch (Exception ex) {
            AlertDialogs.showErrors(ex);
        }
    }

    public void setTotal(String toString) {
        try {

            ObservableList<InvoiceSellDetails> items1 = invoiceTable.getItems();
            double total = 0;
            for (InvoiceSellDetails a : items1) {
                total += Double.parseDouble(a.getAmount().getText()) * Double.parseDouble(a.getCost().getText());
            }
            invoiceTotal.setText(Double.toString(total));

            double discount = 0;
            double discountPercent = 0;
            if (invoicedisc.getText().isEmpty()) {
            } else {

                discount = Double.parseDouble(invoicedisc.getText().isEmpty() ? "0" : invoicedisc.getText());

            }
            if (invoiceDiscPercent.getText().isEmpty()) {
            } else {
                String a = invoiceDiscPercent.getText().isEmpty() ? "0" : invoiceDiscPercent.getText();
                discountPercent = ((Double.parseDouble(a) * total) / 100);

            }

            invoiceLastTotal.setText(Double.toString(total - discount - discountPercent));
        } catch (Exception e) {
            AlertDialogs.showErrors(e);
        }
    }

    private void fillCombo() {
        try {
            invoiceId.setItems(InvoiceSell.getData());
            invoiceId.setConverter(new StringConverter<InvoiceSell>() {
                @Override
                public String toString(InvoiceSell patient) {
                    return patient.getDate();
                }

                @Override
                public InvoiceSell fromString(String string) {
                    return null;
                }
            });
            invoiceId.setCellFactory(cell -> new ListCell<InvoiceSell>() {

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
            clients.setItems(Clients.getData());
            clients.setConverter(new StringConverter<Clients>() {
                @Override
                public String toString(Clients patient) {
                    return patient.getName();
                }

                @Override
                public Clients fromString(String string) {
                    return null;
                }
            });
            clients.setCellFactory(cell -> new ListCell<Clients>() {

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
        } catch (Exception ex) {
            AlertDialogs.showErrors(ex);
        }
    }

    @FXML
    private void deleteRow(ActionEvent event) {
        if (invoiceTable.getSelectionModel().getSelectedIndex() == -1) {
            AlertDialogs.showError("اختار الصف اولا");
        } else {
            if (invoiceTable.getSelectionModel().getSelectedItem().getProducts().getSelectionModel().getSelectedIndex() != -1
                    && !invoiceTable.getSelectionModel().getSelectedItem().getAmount().getText().equals("0")
                    && !invoiceTable.getSelectionModel().getSelectedItem().getCost().getText().equals("0")) {
                invoiceTable.getItems().remove(invoiceTable.getSelectionModel().getSelectedIndex());
            }
        }
    }

    @FXML
    private void invoiveAdd(ActionEvent event) {

        progress.setVisible(true);
        Service<Void> service = new Service<Void>() {
            boolean ok = true;
            InvoiceSell in = new InvoiceSell();

            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        final CountDownLatch latch = new CountDownLatch(1);
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    setTotal("");
                                    if (date.getValue() == null) {
                                        AlertDialogs.showError("برجاء ادخال تاريخ الفاتورة");
                                    } else if (clients.getSelectionModel().getSelectedIndex() == -1) {
                                        AlertDialogs.showError("برجاء اختيار العميل");
                                    } else if (invoiceTable.getItems().isEmpty()) {
                                        AlertDialogs.showError("لا يجب ان يكون الجدول فارغ");
                                    } else if (invoiceTable.getItems().size() == 1 && invoiceTotal.getText().equals("0") || invoiceTotal.getText().equals("0.0")) {
                                        AlertDialogs.showError("لا يجب ان يكون الجدول فارغ");
                                    } else if (invoiceTotal.getText().equals("0")) {
                                        setTotal("");
                                    } else if (invoiceTable.getItems().size() == 1) {
                                        AlertDialogs.showError("لا يجب ان يكون الجدول فارغ");
                                    } else {
                                        ObservableList<InvoiceSellDetails> items = invoiceTable.getItems();

                                        if (items.size() - 1 == 0) {
                                            AlertDialogs.showError("اضغط Enter اذا كان الجدول غير فارغ على اخر خانة");
                                        } else {
                                            in = new InvoiceSell();
                                            in.setId(invoiceId.getSelectionModel().getSelectedItem().getId());
                                            DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                                            in.setDate(date.getValue().format(format));
                                            in.setClient_id(clients.getSelectionModel().getSelectedItem().getId());
                                            in.setCost(invoiceTotal.getText());
                                            in.setDicount(invoicedisc.getText().isEmpty() ? "0" : invoicedisc.getText());
                                            in.setDiscount_percent(invoiceDiscPercent.getText().isEmpty() ? "0" : invoiceDiscPercent.getText());
                                            in.setTotal_cost(invoiceLastTotal.getText());
                                            in.setNotes(notes.getText().isEmpty() ? "لايوجد" : notes.getText());
                                            in.setOnNote(Boolean.toString(onNote.isSelected()));
                                            items.remove(items.size() - 1);
                                            in.setDetails(items);

                                            in.Edite();
                                        }
                                    }
                                } catch (Exception ex) {
                                    AlertDialogs.showErrors(ex);
                                    ok = false;
                                    try {

                                        in.Delete();
                                    } catch (Exception ex1) {
                                        AlertDialogs.showErrors(ex);
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
                    clear();
                    AlertDialogs.showmessage("تم");
                }

                super.succeeded();
            }

        };
        service.start();
    }

    @FXML
    private void printInvoices(ActionEvent event) {
          try {
            ObservableList<InvoiceSellDetails> items = invoiceTable.getItems();
            int total = 0;
            for (InvoiceSellDetails item : items) {
                total += Integer.parseInt(item.getAmount().getText());
            }
            DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            HashMap hash = new HashMap();
            BufferedImage image = ImageIO.read(getClass().getResource("/assets/icons/logo.png"));
            hash.put("logo", image);
            hash.put("id", Integer.toString(invoiceId.getSelectionModel().getSelectedItem().getId()));
            hash.put("date", date.getValue().format(format));
            hash.put("name", clients.getSelectionModel().getSelectedItem().getName());
            hash.put("totalNum", Integer.toString(total));
            hash.put("totalCost", invoiceLastTotal.getText());
            hash.put("disc", invoicedisc.getText().isEmpty() ? "0" : invoicedisc.getText());
            hash.put("cost", invoiceTotal.getText());
            hash.put("notes", notes.getText().isEmpty() ? "لايوجد" : notes.getText());  
            InputStream a = getClass().getResourceAsStream("/screens/invoices/report/invoiceSell.jrxml");
            JasperDesign design = JRXmlLoader.load(a);
            JasperReport jasperreport = JasperCompileManager.compileReport(design);
            JasperPrint jasperprint = JasperFillManager.fillReport(jasperreport, hash, db.get.getReportCon());
            JasperViewer.viewReport(jasperprint, false);
        } catch (Exception ex) {
            AlertDialogs.showErrors(ex);
        }
    }

    @FXML
    private void showInvoice(ActionEvent event) {
        getData(invoiceId.getSelectionModel().getSelectedItem().getId());
    }

    @FXML
    private void invoiceDelete(ActionEvent event) {
        progress.setVisible(true);
        Service<Void> service = new Service<Void>() {
            boolean ok = true;

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
                                    alert.setTitle("Deleting Invoices  ");
                                    alert.setHeaderText("سيتم حذف  الفاتورة ");
                                    alert.setContentText("هل انت متاكد؟");

                                    Optional<ButtonType> result = alert.showAndWait();
                                    if (result.get() == ButtonType.OK) {
                                        InvoiceSell in = new InvoiceSell();
                                        in.setId(invoiceId.getSelectionModel().getSelectedItem().getId());
                                        in.Delete();
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
                    clear();
                    AlertDialogs.showmessage("تم");
                }
                super.succeeded();
            }
        };
        service.start();

    }

}
