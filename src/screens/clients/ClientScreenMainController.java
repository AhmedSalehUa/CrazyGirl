/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package screens.clients;

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
import screens.clients.assets.Clients;

/**
 * FXML Controller class
 *
 * @author AHMED
 */
public class ClientScreenMainController implements Initializable {

    @FXML
    private JFXDrawer drawer;
    @FXML
    private JFXHamburger hamburg;
    @FXML
    private JFXTextField search;
    @FXML
    private TableView<Clients> tab;
    @FXML
    private TableColumn<Clients, String> tabTele2;
    @FXML
    private TableColumn<Clients, String> tabTele1;
    @FXML
    private TableColumn<Clients, String> tabName;
    @FXML
    private TableColumn<Clients, String> tabId;
    @FXML
    private Label id;
    @FXML
    private TextField name;
    @FXML
    private TextField tele1;
    @FXML
    private TextField tele2;
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

                Clients selected = tab.getSelectionModel().getSelectedItem();
                id.setText(Integer.toString(selected.getId()));
                name.setText(selected.getName());
                tele1.setText(selected.getTele1());
                tele2.setText(selected.getTele2());
            }
        });
    }

    private void intialColumn() {
        tabTele2.setCellValueFactory(new PropertyValueFactory<>("tele2"));

        tabTele1.setCellValueFactory(new PropertyValueFactory<>("tele1"));

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
        tele1.setText("");
        tele2.setText("");
    }

    private void getAutoNum() {
        try {
            id.setText(Clients.getAutoNum());
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
                        final CountDownLatch latch = new CountDownLatch(1);
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    tab.setItems(Clients.getData());

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
    ObservableList<Clients> items;

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
        FilteredList<Clients> filteredData = new FilteredList<>(items, p -> true);

        filteredData.setPredicate(pa -> {

            if (search.getText() == null || search.getText().isEmpty()) {
                return true;
            }

            String lowerCaseFilter = search.getText().toLowerCase();

            return (pa.getName().contains(lowerCaseFilter)
                    || pa.getTele1().contains(lowerCaseFilter)
                    || pa.getTele2().contains(lowerCaseFilter));

        });

        SortedList<Clients> sortedData = new SortedList<>(filteredData);
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
                                    } else {
                                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                                        alert.setTitle("Deleting Clients ");
                                        alert.setHeaderText("سيتم حذف  العميل ");
                                        alert.setContentText("هل انت متاكد؟");

                                        Optional<ButtonType> result = alert.showAndWait();
                                        if (result.get() == ButtonType.OK) {
                                            Clients cl = new Clients();
                                            cl.setId(Integer.parseInt(id.getText()));
                                            cl.Delete();
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
                                        AlertDialogs.showmessage("لا يجب ان يكون الاسم فارغ"); ok = false;
                                    } else {
                                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                                        alert.setTitle("Editing Clients");
                                        alert.setHeaderText("سيتم  تعديل العميل ");
                                        alert.setContentText("هل انت متاكد؟");

                                        Optional<ButtonType> result = alert.showAndWait();
                                        if (result.get() == ButtonType.OK) {
                                            Clients cl = new Clients();
                                            cl.setId(Integer.parseInt(id.getText()));
                                            cl.setName(name.getText());
                                            cl.setTele1(tele1.getText());
                                            cl.setTele2(tele2.getText());
                                            cl.Edite();
                                        }
                                    }
                                } catch (Exception ex) {
                                    AlertDialogs.showErrors(ex); ok = false;
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
                                        AlertDialogs.showmessage("لا يجب ان يكون الاسم فارغ"); ok = false;
                                    } else {
                                        Clients cl = new Clients();
                                        cl.setId(Integer.parseInt(id.getText()));
                                        cl.setName(name.getText());
                                        cl.setTele1(tele1.getText());
                                        cl.setTele2(tele2.getText());
                                        cl.Add();
                                    }

                                } catch (Exception ex) {
                                    AlertDialogs.showErrors(ex); ok = false;
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
