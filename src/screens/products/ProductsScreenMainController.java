/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package screens.products;

import assets.classes.AlertDialogs;
import static assets.classes.statics.DEFAULT_THEME;
import static assets.classes.statics.THEME;
import carzy.girl.CarzyGirl;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Optional;
import java.util.Random;
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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import org.krysalis.barcode4j.impl.code128.Code128Bean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;
import screens.products.assets.Products;

/**
 * FXML Controller class
 *
 * @author AHMED
 */
public class ProductsScreenMainController implements Initializable {

    @FXML
    private JFXTextField search;
    @FXML
    private TableView<Products> tab;
    @FXML
    private TableColumn<Products, String> tabCost;
    @FXML
    private TableColumn<Products, String> tabLessCost;
    @FXML
    private TableColumn<Products, String> tabAmount;
    @FXML
    private TableColumn<Products, String> tabBarcode;
    @FXML
    private TableColumn<Products, String> tabName;
    @FXML
    private TableColumn<Products, String> tabId;
    @FXML
    private Label id;
    @FXML
    private TextField name;
    @FXML
    private TextField lessCost;
    @FXML
    private TextField cost;
    @FXML
    private TextField barcode;
    @FXML
    private TextField amount;
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
    private JFXDrawer drawer;
    @FXML
    private JFXHamburger hamburg;

    Preferences prefs;
    @FXML
    private TableColumn<Products, String> tabBuyCost;
    @FXML
    private TextField buyCost;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        prefs = Preferences.userNodeForPackage(CarzyGirl.class);
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
                                    configDrawer();
                                    clear();
                                    intialColumn();
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
        tab.setOnMouseClicked((e) -> {
            if (tab.getSelectionModel().getSelectedIndex() == -1) {
            } else {
                New.setDisable(false);

                Delete.setDisable(false);

                Edite.setDisable(false);

                Add.setDisable(true);

                Products selected = tab.getSelectionModel().getSelectedItem();
                id.setText(Integer.toString(selected.getId()));
                name.setText(selected.getName());
                barcode.setText(selected.getBarcode());
                amount.setText(selected.getAmount());
                buyCost.setText(selected.getBuyCost());
                lessCost.setText(selected.getMinCost());
                cost.setText(selected.getCost());
            }
        });
    }

    private void intialColumn() {
        tabCost.setCellValueFactory(new PropertyValueFactory<>("cost"));

        tabLessCost.setCellValueFactory(new PropertyValueFactory<>("minCost"));

        tabAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));

        tabBuyCost.setCellValueFactory(new PropertyValueFactory<>("buyCost"));

        tabBarcode.setCellValueFactory(new PropertyValueFactory<>("barcode"));

        tabName.setCellValueFactory(new PropertyValueFactory<>("name"));

        tabId.setCellValueFactory(new PropertyValueFactory<>("id"));
    }

    private void clear() {
        getAutoNum();
        New.setDisable(true);

        Delete.setDisable(true);

        Edite.setDisable(true);

        Add.setDisable(false);
        name.setText("");
        barcode.setText("");
        buyCost.setText("");
        amount.setText("");
        lessCost.setText("");
        cost.setText("");
    }

    private void getAutoNum() {
        try {
            id.setText(Products.getAutoNum());
        } catch (Exception e) {
            AlertDialogs.showErrors(e);
        }
    }

    private void getData() {
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
                                    tab.setItems(Products.getData());

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
                items = tab.getItems();
                super.succeeded();
            }
        };
        service.start();
    }
    ObservableList<Products> items;

    private void configDrawer() {
        try {

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
    private void search(KeyEvent event) {
        FilteredList<Products> filteredData = new FilteredList<>(items, p -> true);

        filteredData.setPredicate(pa -> {

            if (search.getText() == null || search.getText().isEmpty()) {
                return true;
            }

            String lowerCaseFilter = search.getText().toLowerCase();

            return (pa.getName().contains(lowerCaseFilter)
                    || pa.getBarcode().contains(lowerCaseFilter)
                    || pa.getAmount().contains(lowerCaseFilter)
                    || pa.getBuyCost().contains(lowerCaseFilter)
                    || pa.getMinCost().contains(lowerCaseFilter)
                    || pa.getCost().contains(lowerCaseFilter));

        });

        SortedList<Products> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tab.comparatorProperty());
        tab.setItems(sortedData);
    }

    @FXML
    private void generateBarcode(MouseEvent event) {
        Random rand = new Random();

        long x = (long) (rand.nextDouble() * 100000000000000L);

        String s = String.valueOf("0") + String.format("%014d", x);
        barcode.setText(Long.toString(Long.valueOf(s)));
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

                                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                                    alert.setTitle("Deleting Products  ");
                                    alert.setHeaderText("سيتم حذف  المنتج ");
                                    alert.setContentText("هل انت متاكد؟");

                                    Optional<ButtonType> result = alert.showAndWait();
                                    if (result.get() == ButtonType.OK) {
                                        Products pr = new Products();
                                        pr.setId(Integer.parseInt(id.getText()));
                                        pr.Delete();
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
                    getData();
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
                                    if (name.getText().isEmpty() || name.getText().length() == 0) {
                                        AlertDialogs.showmessage("لا يجب ان يكون الاسم فارغ");
                                        ok = false;
                                    } else if (amount.getText().isEmpty() || name.getText().length() == 0) {
                                        AlertDialogs.showmessage("لا يجب ان يكون الكمية فارغ");
                                        ok = false;
                                    } else if (buyCost.getText().isEmpty() || buyCost.getText().length() == 0) {
                                        AlertDialogs.showmessage("لا يجب ان يكون سعر الشراء فارغ");
                                        ok = false;
                                    } else if (cost.getText().isEmpty() || name.getText().length() == 0) {
                                        AlertDialogs.showmessage("لا يجب ان يكون السعر فارغ");
                                        ok = false;
                                    } else if (lessCost.getText().isEmpty() || name.getText().length() == 0) {
                                        AlertDialogs.showmessage("لا يجب ان يكون اقل سعر فارغ");
                                        ok = false;
                                    } else if (barcode.getText().isEmpty() || name.getText().length() == 0) {
                                        AlertDialogs.showmessage("لا يجب ان يكون باركود فارغ");
                                        ok = false;
                                    } else {
                                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                                        alert.setTitle("Editing Products");
                                        alert.setHeaderText("سيتم تعديل المنتج ");
                                        alert.setContentText("هل انت متاكد؟");

                                        Optional<ButtonType> result = alert.showAndWait();

                                        if (result.get() == ButtonType.OK) {
                                            System.out.println("eeee");
                                            Products pr = new Products();
                                            pr.setId(Integer.parseInt(id.getText()));
                                            pr.setName(name.getText());
                                            pr.setBarcode(barcode.getText());
                                            pr.setBuyCost(buyCost.getText());
                                            pr.setAmount(amount.getText());
                                            pr.setCost(cost.getText());
                                            pr.setMinCost(lessCost.getText());
                                            pr.Edite();
                                        }
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
                    getData();
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
                                    if (name.getText().isEmpty() || name.getText().length() == 0) {
                                        AlertDialogs.showmessage("لا يجب ان يكون الاسم فارغ");
                                        ok = false;
                                    } else if (amount.getText().isEmpty() || name.getText().length() == 0) {
                                        AlertDialogs.showmessage("لا يجب ان يكون الكمية فارغ");
                                        ok = false;
                                    } else if (buyCost.getText().isEmpty() || buyCost.getText().length() == 0) {
                                        AlertDialogs.showmessage("لا يجب ان يكون سعر الشراء فارغ");
                                        ok = false;
                                    } else if (cost.getText().isEmpty() || name.getText().length() == 0) {
                                        AlertDialogs.showmessage("لا يجب ان يكون السعر فارغ");
                                        ok = false;
                                    } else if (lessCost.getText().isEmpty() || name.getText().length() == 0) {
                                        AlertDialogs.showmessage("لا يجب ان يكون اقل سعر فارغ");
                                        ok = false;
                                    } else if (barcode.getText().isEmpty() || name.getText().length() == 0) {
                                        AlertDialogs.showmessage("لا يجب ان يكون باركود فارغ");
                                        ok = false;
                                    } else {
                                        Products pr = new Products();
                                        pr.setId(Integer.parseInt(id.getText()));
                                        pr.setName(name.getText());
                                        pr.setBarcode(barcode.getText());
                                        pr.setAmount(amount.getText());
                                        pr.setCost(cost.getText());
                                        pr.setBuyCost(buyCost.getText());
                                        pr.setMinCost(lessCost.getText());
                                        pr.Add();
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
                    getData();
                }
                super.succeeded();
            }
        };
        service.start();
    }

    @FXML
    private void printBarcode(MouseEvent event) {
        if (tab.getSelectionModel().getSelectedIndex() == -1) {
            AlertDialogs.showError("اختار الصنف اولا");
        } else {
            try {
                Products selectedItem = tab.getSelectionModel().getSelectedItem();
                Code128Bean code128 = new Code128Bean();
                code128.setHeight(15);
                code128.setModuleWidth(0.5);
                code128.setQuietZone(5);
                code128.doQuietZone(true);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                BitmapCanvasProvider canvas = new BitmapCanvasProvider(baos, "image/x-png", 100, BufferedImage.TYPE_BYTE_BINARY, false, 0);
                code128.generateBarcode(canvas, tab.getSelectionModel().getSelectedItem().getBarcode());
                canvas.finish();

                BufferedImage createImageFromBytes = createImageFromBytes(baos.toByteArray());
                byte[] f = tab.getSelectionModel().getSelectedItem().getName().getBytes();
                HashMap hash = new HashMap();
                hash.put("bar", createImageFromBytes);
                hash.put("id", Integer.toString(selectedItem.getId()));
                InputStream a = getClass().getResourceAsStream("/screens/products/reports/barcode.jrxml");
                JasperDesign design = JRXmlLoader.load(a); 
                JasperReport jasperreport = JasperCompileManager.compileReport(design);
                JasperPrint jasperprint = JasperFillManager.fillReport(jasperreport, hash, db.get.getReportCon());
                JasperViewer.viewReport(jasperprint, false);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage());
            }
        }
    }

    private BufferedImage createImageFromBytes(byte[] imageData) {
        ByteArrayInputStream bais = new ByteArrayInputStream(imageData);
        try {
            return ImageIO.read(bais);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
