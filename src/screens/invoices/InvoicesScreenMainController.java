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
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.CountDownLatch;
import java.util.prefs.Preferences;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

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
    private ComboBox<?> clients;
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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        prefs = Preferences.userNodeForPackage(CarzyGirl.class);
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
                                    try {
                                        configDrawer();
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                        AlertDialogs.showErrors(ex);

                                    }
                                } catch (Exception ex) {
                                    AlertDialogs.showErrors(ex);
                                } finally {
                                    latch.countDown();
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

                        });
                        latch.await();

                        return null;
                    }
                };

            }

            @Override
            protected void succeeded() {
                super.succeeded();
            }
        };
        service.start();
    }

    @FXML
    private void deleteRow(ActionEvent event) {
    }

    @FXML
    private void invoiveAdd(ActionEvent event) {
    }

}
