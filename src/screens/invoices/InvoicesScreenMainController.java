/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package screens.invoices;

import assets.classes.AlertDialogs;
import static assets.classes.statics.DEFAULT_THEME;
import static assets.classes.statics.THEME;
import carzy.girl.CarzyGirl;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
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
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
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
import screens.invoices.assets.InvoiceBuy;
import screens.invoices.assets.InvoiceBuyDetails;
import screens.invoices.assets.InvoiceSell;
import screens.invoices.assets.InvoiceSellDetails;
import screens.products.assets.Products;
import screens.providers.assets.Providers;

/**
 * FXML Controller class
 *
 * @author AHMED
 */
public class InvoicesScreenMainController implements Initializable {

    @FXML
    private TextArea notes;
    @FXML
    private Label id;
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
    private AnchorPane returnedSell;
    @FXML
    private AnchorPane buy;
    @FXML
    private AnchorPane returnedBuy;
    @FXML
    private JFXDrawer drawer;
    @FXML
    private JFXHamburger hamburg;
    Preferences prefs;
    @FXML
    private CheckBox onNote;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        prefs = Preferences.userNodeForPackage(CarzyGirl.class);
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
                        //Background work                       
                        final CountDownLatch latch = new CountDownLatch(1);
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    configDrawer();
                                    clear();
                                    intialColumn();
                                    fillCombo();
                                    getData();
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

    private void getData() {
        try {
            ObservableList<Products> data = Products.getData();
            ObservableList<InvoiceSellDetails> list = FXCollections.observableArrayList();
            list.add(new InvoiceSellDetails(1, data, "0", "0", "0", "0"));
            invoiceTable.setItems(list);
            invoiceTable.setOnKeyReleased((event) -> {

                if (event.getCode() == KeyCode.ENTER) {
                    setTotal("");
                }
                if (event.getCode() == KeyCode.ENTER && invoiceTable.getSelectionModel().getSelectedItem().getProducts().getSelectionModel().getSelectedIndex() != -1
                        && !invoiceTable.getSelectionModel().getSelectedItem().getAmount().getText().equals("0")
                        && !invoiceTable.getSelectionModel().getSelectedItem().getCost().getText().equals("0")) {
                    setTotal("");
                    invoiceTable.getItems().add(new InvoiceSellDetails(invoiceTable.getItems().size() + 1, data, "0", "0", "0", "0"));
                    invoiceTable.getSelectionModel().clearAndSelect(invoiceTable.getItems().size() - 1);
                }

            });
        } catch (Exception ex) {
            AlertDialogs.showErrors(ex);
        }
    }

    private void clear() {
        try {
            getAutoNum();
            notes.setText("");
            invoiceTotal.setText("");
            invoiceDiscPercent.setText("");
            invoiceLastTotal.setText("");
            invoicedisc.setText("");
            date.setValue(null);
            clients.getSelectionModel().clearSelection();
            ObservableList<Products> data = Products.getData();
            ObservableList<InvoiceSellDetails> list = FXCollections.observableArrayList();
            list.add(new InvoiceSellDetails(1, data, "0", "0", "0", "0"));
            invoiceTable.setItems(list);
            invoiceTable.setOnKeyReleased((event) -> {

                if (event.getCode() == KeyCode.ENTER) {
                    setTotal("");
                }
                if (event.getCode() == KeyCode.ENTER && invoiceTable.getSelectionModel().getSelectedItem().getProducts().getSelectionModel().getSelectedIndex() != -1
                        && !invoiceTable.getSelectionModel().getSelectedItem().getAmount().getText().equals("0")
                        && !invoiceTable.getSelectionModel().getSelectedItem().getCost().getText().equals("0")) {
                    setTotal("");
                    invoiceTable.getItems().add(new InvoiceSellDetails(invoiceTable.getItems().size() + 1, data, "0", "0", "0", "0"));
                    invoiceTable.getSelectionModel().clearAndSelect(invoiceTable.getItems().size() - 1);
                }

            });
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

    private void getAutoNum() {
        try {
            id.setText(InvoiceSell.getAutoNum());
        } catch (Exception ex) {
            AlertDialogs.showErrors(ex);
        }
    }

    private void fillCombo() {
        try {
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
                                            in.setId(Integer.parseInt(id.getText()));
                                            DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                                            in.setDate(date.getValue().format(format));
                                            in.setClient_id(clients.getSelectionModel().getSelectedItem().getId());
                                            in.setCost(invoiceTotal.getText());
                                            in.setDicount(invoicedisc.getText().isEmpty() ? "0" : invoicedisc.getText());
                                            in.setDiscount_percent(invoiceDiscPercent.getText().isEmpty() ? "0" : invoiceDiscPercent.getText());
                                            in.setTotal_cost(invoiceLastTotal.getText());
                                            in.setNotes(notes.getText().isEmpty() ? "لايوجد" : notes.getText());

                                            items.remove(items.size() - 1);
                                            in.setDetails(items);
                                            in.setOnNote(Boolean.toString(onNote.isSelected()));
                                            in.Add();

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

                    AlertDialogs.showmessage("تم");
                    printRecept(Integer.parseInt(id.getText()));
                    clear();
                }

                super.succeeded();
            }

        };
        service.start();
    }

    private void printRecept(int parseInt) {
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
            hash.put("id",Integer.toString( parseInt));
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

    private void configDrawer() {
        try {
            buy.getChildren().clear();
            buy.getChildren().add(FXMLLoader.load(getClass().getResource("InvoicesScreenBuy.fxml")));

            returnedBuy.getChildren().clear();
            returnedBuy.getChildren().add(FXMLLoader.load(getClass().getResource("InvoicesScreenReturnedBuy.fxml")));

            returnedSell.getChildren().clear();
            returnedSell.getChildren().add(FXMLLoader.load(getClass().getResource("InvoicesScreenReturnedSell.fxml")));

            AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/Navigator/SideNavigator.fxml"));

            anchorPane.getStylesheets().add(getClass().getResource("/assets/styles/" + prefs.get(THEME, DEFAULT_THEME) + ".css").toExternalForm());

            drawer.setSidePane(anchorPane);

            double drawerx = drawer.getLayoutX();
            double drawery = drawer.getLayoutY();
            drawer.setLayoutX(drawerx + 250);
            drawer.setLayoutY(drawery);
            drawer.setVisible(false);
            drawer.setMaxWidth(0);

            drawer.setOnDrawerOpening(event
                    -> {
                drawer.setLayoutX(drawerx);
                drawer.setLayoutY(drawery);
                drawer.setMaxWidth(250);
            });

            drawer.setOnDrawerClosed(event
                    -> {
                drawer.setLayoutX(drawerx + 250);
                drawer.setLayoutY(drawery);
                drawer.setVisible(false);
                drawer.setMaxWidth(0);
            });
            for (Node node : anchorPane.getChildren()) {

                node.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {

                });

            }
            HamburgerBackArrowBasicTransition nav = new HamburgerBackArrowBasicTransition(hamburg);
            nav.setRate(nav.getRate() * -1);
            nav.play();
            hamburg.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
                nav.setRate(nav.getRate() * -1);
                nav.play();
                drawer.setVisible(true);
                if (drawer.isOpened()) {
                    drawer.close();
                } else {
                    drawer.open();
                }
            });
        } catch (Exception e) {
            AlertDialogs.showErrors(e);
        }
    }

    @FXML
    private void addClient(MouseEvent event) {

        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle("Add Cat Name");
        dialog.setHeaderText("اضافة عميل جديد");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            if (result.get().isEmpty() || result.get() == null) {
                AlertDialogs.showError("خطا!! يرجي ادخال اسم عميل");
            } else {
                final String results = result.get();
                try {
                    Service service = new Service() {
                        @Override
                        protected Task createTask() {
                            return new Task() {
                                @Override
                                protected Object call() throws Exception {

                                    try {
                                        Clients.AddShort(results);
                                    } catch (Exception ex) {
                                        AlertDialogs.showErrors(ex);
                                    }

                                    return null;
                                }

                                @Override
                                protected void succeeded() {
                                    try {
                                        fillCombo();
                                    } catch (Exception ex) {
                                        AlertDialogs.showErrors(ex);
                                    }
                                }
                            };
                        }
                    };
                    service.start();

                } catch (Exception ex) {
                    AlertDialogs.showErrors(ex);
                }
            }
        }
    }

}
