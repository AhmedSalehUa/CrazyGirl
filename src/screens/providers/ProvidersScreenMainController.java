/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package screens.providers;

import assets.classes.AlertDialogs;
import static assets.classes.statics.DEFAULT_THEME;
import static assets.classes.statics.THEME;
import carzy.girl.CarzyGirl;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;
import java.net.URL;
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
import screens.providers.assets.Providers;

/**
 * FXML Controller class
 *
 * @author AHMED
 */
public class ProvidersScreenMainController implements Initializable {

    @FXML
    private JFXTextField search;
    @FXML
    private TableView<Providers> tab;
    @FXML
    private TableColumn<Providers, String> tabTele2;
    @FXML
    private TableColumn<Providers, String> tabTele1;
    @FXML
    private TableColumn<Providers, String> tabAcc;
    @FXML
    private TableColumn<Providers, String> tabLocation;
    @FXML
    private TableColumn<Providers, String> tabName;
    @FXML
    private TableColumn<Providers, String> tabId;
    @FXML
    private Label id;
    @FXML
    private TextField name;
    @FXML
    private TextField tele1;
    @FXML
    private TextField tele2;
    @FXML
    private TextField location;
    @FXML
    private TextField accNum;
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

                Providers selected = tab.getSelectionModel().getSelectedItem();
                id.setText(Integer.toString(selected.getId()));
                name.setText(selected.getName());
                location.setText(selected.getLocation());
                accNum.setText(selected.getAccNum());
                tele1.setText(selected.getTele1());
                tele2.setText(selected.getTele2());
            }
        });
    }

    private void intialColumn() {
        tabTele2.setCellValueFactory(new PropertyValueFactory<>("tele2"));

        tabTele1.setCellValueFactory(new PropertyValueFactory<>("tele1"));

        tabAcc.setCellValueFactory(new PropertyValueFactory<>("accNum"));

        tabLocation.setCellValueFactory(new PropertyValueFactory<>("location"));

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
        location.setText("");
        accNum.setText("");
        tele1.setText("");
        tele2.setText("");
    }

    private void getAutoNum() {
        try {
            id.setText(Providers.getAutoNum());
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
                                    tab.setItems(Providers.getData());

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
    ObservableList<Providers> items;

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
        FilteredList<Providers> filteredData = new FilteredList<>(items, p -> true);

        filteredData.setPredicate(pa -> {

            if (search.getText() == null || search.getText().isEmpty()) {
                return true;
            }

            String lowerCaseFilter = search.getText().toLowerCase();

            return (pa.getName().contains(lowerCaseFilter)
                    || pa.getLocation().contains(lowerCaseFilter)
                    || pa.getAccNum().contains(lowerCaseFilter)
                    || pa.getTele1().contains(lowerCaseFilter)
                    || pa.getTele2().contains(lowerCaseFilter));

        });

        SortedList<Providers> sortedData = new SortedList<>(filteredData);
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
        Service<Void> service = new Service<Void>() { boolean ok = true;
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
                                        AlertDialogs.showmessage("لا يجب ان يكون الاسم فارغ");ok = false;
                                    } else {
                                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                                        alert.setTitle("Deleting Providers  ");
                                        alert.setHeaderText("سيتم حذف  المورد ");
                                        alert.setContentText("هل انت متاكد؟");

                                        Optional<ButtonType> result = alert.showAndWait();
                                        if (result.get() == ButtonType.OK) {
                                            Providers pr = new Providers();
                                            pr.setId(Integer.parseInt(id.getText()));
                                            pr.Delete();
                                        }
                                    }
                                } catch (Exception ex) {
                                    AlertDialogs.showErrors(ex);ok = false;
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
        Service<Void> service = new Service<Void>() { boolean ok = true;
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
                                        AlertDialogs.showmessage("لا يجب ان يكون الاسم فارغ");ok = false;
                                    } else {
                                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                                        alert.setTitle("Editing Providers");
                                        alert.setHeaderText("سيتم تعديل المورد ");
                                        alert.setContentText("هل انت متاكد؟");

                                        Optional<ButtonType> result = alert.showAndWait();
                                        if (result.get() == ButtonType.OK) {
                                            Providers pr = new Providers();
                                            pr.setId(Integer.parseInt(id.getText()));
                                            pr.setName(name.getText());
                                            pr.setLocation(location.getText());
                                            pr.setAccNum(accNum.getText());
                                            pr.setTele1(tele1.getText());
                                            pr.setTele2(tele2.getText());
                                            pr.Edite();
                                        }
                                    }
                                } catch (Exception ex) {
                                    AlertDialogs.showErrors(ex);ok = false;
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
        Service<Void> service = new Service<Void>() { boolean ok = true; 
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
                                        AlertDialogs.showmessage("لا يجب ان يكون الاسم فارغ");ok = false;
                                    } else {
                                        Providers pr = new Providers();
                                        pr.setId(Integer.parseInt(id.getText()));
                                        pr.setName(name.getText());
                                        pr.setLocation(location.getText());
                                        pr.setAccNum(accNum.getText());
                                        pr.setTele1(tele1.getText());
                                        pr.setTele2(tele2.getText());
                                        pr.Add();
                                    }

                                } catch (Exception ex) {
                                    AlertDialogs.showErrors(ex);ok = false;
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

}
