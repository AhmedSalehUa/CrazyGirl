/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package screens.controlpanel;


import assets.classes.AlertDialogs;
import static assets.classes.statics.DEFAULT_THEME;
import static assets.classes.statics.NoPermission;
import static assets.classes.statics.THEME;
import static assets.classes.statics.USER_ID;
import static assets.classes.statics.USER_ROLE;
import carzy.girl.CarzyGirl;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;
import db.User;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;
import javafx.util.StringConverter;
import javax.swing.JTable;
import org.controlsfx.control.CheckListView;
import screens.controlpanel.assets.StaticValues; 

/**
 * FXML Controller class
 *
 * @author AHMED
 */
public class ControlPanelController implements Initializable {

    @FXML
    private JFXDrawer drawer;
    @FXML
    private JFXHamburger hamburg;
    Preferences prefs;
    @FXML
    private Button resetPassword;
    @FXML
    private TableView<User> userTable;
    @FXML
    private TableColumn<User, String> userTabPriviliges;
    @FXML
    private TableColumn<User, String> userTabName;
    @FXML
    private TableColumn<User, String> userTabId;
    @FXML
    private Label userId;
    @FXML
    private TextField userName;
    @FXML
    private ComboBox<String> userPriviliges;
    @FXML
    private Button formNewUser;
    @FXML
    private Button formDeleteUser;
    @FXML
    private Button formEditeUser;
    @FXML
    private Button formAddUser;
    @FXML
    private ComboBox<User> userNameCombo;
    @FXML
    private Button showPriviliges;
    @FXML
    private Button savePrivilages;
    @FXML
    private CheckListView<String> privilegesList;
    @FXML
    private TableView<StaticValues> statictable;
    @FXML
    private TableColumn<StaticValues, String> staticTabValue;
    @FXML
    private TableColumn<StaticValues, String> statictabAttribute;
    @FXML
    private TextField attribute;
    @FXML
    private TextField value;
    @FXML
    private Button formNewStatic;
    @FXML
    private Button formDeleteStatic;
    @FXML
    private Button formEditeStatic;
    @FXML
    private Button formAddStatic;
    @FXML
    private AnchorPane valuesPanel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
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
                                    prefs = Preferences.userNodeForPackage(CarzyGirl.class);
                                    configDrawer();
                                    clear();
                                    intialColumn();
                                    getData();
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
                super.succeeded();
            }
        };
        service.start();
        userTable.setOnMouseClicked((e) -> {
            if (userTable.getSelectionModel().getSelectedIndex() == -1) {
            } else {
                formNewUser.setDisable(false);

                formDeleteUser.setDisable(false);

                formEditeUser.setDisable(false);

                formAddUser.setDisable(true);

                User selected = userTable.getSelectionModel().getSelectedItem();
                userId.setText(Integer.toString(selected.getId()));
                userName.setText(selected.getName());
                userPriviliges.getSelectionModel().select(selected.getRole());
            }
        });
        statictable.setOnMouseClicked((e) -> {
            if (statictable.getSelectionModel().getSelectedIndex() == -1) {
            } else {
                formNewStatic.setDisable(false);

                formDeleteStatic.setDisable(false);

                formEditeStatic.setDisable(false);

                formAddStatic.setDisable(true);

                StaticValues selected = statictable.getSelectionModel().getSelectedItem();
                attribute.setText(selected.getAttribute());
                value.setText(selected.getValus());

            }
        });
    }

    private void intialColumn() {
        userTabPriviliges.setCellValueFactory(new PropertyValueFactory<>("role"));

        userTabName.setCellValueFactory(new PropertyValueFactory<>("name"));

        userTabId.setCellValueFactory(new PropertyValueFactory<>("id"));

        staticTabValue.setCellValueFactory(new PropertyValueFactory<>("valus"));

        statictabAttribute.setCellValueFactory(new PropertyValueFactory<>("attribute"));
    }

    private void clear() {
        getAutoNum();
        formNewUser.setDisable(true);

        formDeleteUser.setDisable(true);

        formEditeUser.setDisable(true);

        formAddUser.setDisable(false);

        userName.setText("");
        userPriviliges.getSelectionModel().clearSelection();

        formNewStatic.setDisable(true);

        formDeleteStatic.setDisable(true);

        formEditeStatic.setDisable(true);

        formAddStatic.setDisable(false);
        attribute.setText("");
        value.setText("");

    }

    private void getAutoNum() {
        try {
            userId.setText(User.getAutoNum());
        } catch (Exception ex) {
            AlertDialogs.showErrors(ex);
        }
    }

    private void getData() {
        try {
            userTable.setItems(User.getData());
            statictable.setItems(StaticValues.getData());
        } catch (Exception ex) {
            AlertDialogs.showErrors(ex);
        }
    }

    private void fillCombo() {
        try {
            ObservableList<String> items = FXCollections.observableArrayList();
            ResultSet executeQuery = db.get.getReportCon().createStatement().executeQuery("SELECT * FROM `priviliges_name`");
            while (executeQuery.next()) {
                items.add(executeQuery.getString(1));
            }
            privilegesList.setItems(items);

            userPriviliges.getItems().add("super_admin");
            userPriviliges.getItems().add("admin");
            userPriviliges.getItems().add("user");
            userPriviliges.getItems().add("sales");
            userNameCombo.setItems(User.getData());
            userNameCombo.setConverter(new StringConverter<User>() {
                @Override
                public String toString(User patient) {
                    return patient.getName();
                }

                @Override
                public User fromString(String string) {
                    return null;
                }
            });
            userNameCombo.setCellFactory(cell -> new ListCell<User>() {

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
                protected void updateItem(User person, boolean empty) {
                    super.updateItem(person, empty);

                    if (!empty && person != null) {

                        // Update our Labels
                        lblid.setText("م: " + Integer.toString(person.getId()));
                        lblName.setText("الاسم: " + person.getName());

                        // Set this ListCell's graphicProperty to display our GridPane
                        setGraphic(gridPane);
                    } else {
                        // Nothing to display here
                        setGraphic(null);
                    }
                }
            });
        } catch (Exception ex) {
            AlertDialogs.showErrors(ex);
        }
    }

    @FXML
    private void resetPassword(ActionEvent event) {
        if (userTable.getSelectionModel().getSelectedIndex() == -1) {
            AlertDialogs.showError("اختار المستخدم اولا");
        } else if (userTable.getSelectionModel().getSelectedItem().getRole().equals("super_admin") && prefs.get(USER_ROLE, "admin").equals("admin")) {
            AlertDialogs.showError("لا يمكن تعديل super admin");
        } else {
            Dialog<Pair<String, String>> dialog = new Dialog<>();
            dialog.setTitle("Change Password");
            dialog.setHeaderText("You Are Abouy Changing Your Password");

            dialog.setGraphic(new ImageView(this.getClass().getResource("/assets/icons/icons8_password_200px_3.png").toString()));

            ButtonType loginButtonType = new ButtonType("Change", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20, 150, 10, 10));

            PasswordField username = new PasswordField();
            username.setPromptText("Password");
            PasswordField password = new PasswordField();
            password.setPromptText("Confirm Password");

            grid.add(new Label("Password:"), 0, 0);
            grid.add(username, 1, 0);
            grid.add(new Label("Confirm Password:"), 0, 1);
            grid.add(password, 1, 1);

            Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
            loginButton.setDisable(true);

            password.textProperty().addListener((observable, oldValue, newValue) -> {
                loginButton.setDisable(!password.getText().equals(username.getText()));
            });
            username.textProperty().addListener((observable, oldValue, newValue) -> {
                loginButton.setDisable(!password.getText().equals(username.getText()));
            });

            dialog.getDialogPane().setContent(grid);

            Platform.runLater(() -> username.requestFocus());

            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == loginButtonType) {
                    return new Pair<>(username.getText(), password.getText());
                }
                return null;
            });

            Optional<Pair<String, String>> result = dialog.showAndWait();

            result.ifPresent(usernamePassword -> {
                User us = new User();
                us.setId(userTable.getSelectionModel().getSelectedItem().getId());
                us.setPassword(usernamePassword.getKey());

                if (us.changePassword()) {
                    AlertDialogs.showmessage("تم");
                } else {
                    AlertDialogs.showError("حدث خطا");
                }
            });
        }
    }

    @FXML
    private void formNewUser(ActionEvent event) {
        clear();
    }

    @FXML
    private void formEditeUser(ActionEvent event) {
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
                                    if (userPriviliges.getSelectionModel().getSelectedItem().equals("super_admin") && prefs.get(USER_ROLE, "admin").equals("admin")) {
                                        AlertDialogs.showError("لا يمكن تعديل super admin");
                                    } else {
                                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                                        alert.setTitle("Editing  User");
                                        alert.setHeaderText("سيتم تعديل المستخدم ");
                                        alert.setContentText("هل انت متاكد؟");

                                        Optional<ButtonType> result = alert.showAndWait();
                                        if (result.get() == ButtonType.OK) {
                                            User us = new User();
                                            us.setId(Integer.parseInt(userId.getText()));
                                            us.setName(userName.getText());
                                            us.setRole(userPriviliges.getSelectionModel().getSelectedItem());
                                            us.Edite();
                                        }
                                    }

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

                clear();
                getData();
                super.succeeded();
            }
        };
        service.start();
    }

    @FXML
    private void formDeleteUser(ActionEvent event) {
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
                                    if (userPriviliges.getSelectionModel().getSelectedItem().equals("super_admin") && prefs.get(USER_ROLE, "admin").equals("admin")) {
                                        AlertDialogs.showError("لا يمكن حذف super admin");
                                    } else {
                                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                                        alert.setTitle("Deleting  User");
                                        alert.setHeaderText("سيتم حذف المستخدم ");
                                        alert.setContentText("هل انت متاكد؟");

                                        Optional<ButtonType> result = alert.showAndWait();
                                        if (result.get() == ButtonType.OK) {
                                            User us = new User();
                                            us.setId(Integer.parseInt(userId.getText()));
                                            us.Delete();
                                        }
                                    }
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

                clear();
                getData();
                super.succeeded();
            }
        };
        service.start();
    }

    @FXML
    private void formAddUser(ActionEvent event) {
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
                                    User us = new User();
                                    us.setId(Integer.parseInt(userId.getText()));
                                    us.setName(userName.getText());
                                    us.setRole(userPriviliges.getSelectionModel().getSelectedItem());
                                    us.Add();

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

                clear();
                getData();
                super.succeeded();
            }
        };
        service.start();
    }

    @FXML
    private void showPriviliges(ActionEvent event) {
        if (userNameCombo.getSelectionModel().getSelectedIndex() == -1) {
            AlertDialogs.showError("اختار مستخدم اولا");
        } else if (userNameCombo.getSelectionModel().getSelectedItem().getRole().equals("super_admin") && prefs.get(USER_ROLE, "admin").equals("admin")) {
            AlertDialogs.showError("لا يمكن تعديل صلاحيات super admin");
        } else {
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
                                        privilegesList.getCheckModel().clearChecks();
                                        JTable tab = db.get.getTableData("SELECT * FROM `users_permissions` WHERE `user_id`='" + userNameCombo.getSelectionModel().getSelectedItem().getId() + "' AND value='true'");
                                        for (int i = 0; i < tab.getRowCount(); i++) {
                                            privilegesList.getCheckModel().check(tab.getValueAt(i, 1).toString());

                                        }
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
                    super.succeeded();
                }
            };
            service.start();
        }
    }

    @FXML
    private void savePrivilages(ActionEvent event) {
        if (userNameCombo.getSelectionModel().getSelectedIndex() == -1) {
            AlertDialogs.showError("اختار مستخدم اولا");
        } else if (userNameCombo.getSelectionModel().getSelectedItem().getRole().equals("super_admin") && prefs.get(USER_ROLE, "admin").equals("admin")) {
            AlertDialogs.showError("لا يمكن تعديل صلاحيات super admin");
        } else {
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
                                        int id = userNameCombo.getSelectionModel().getSelectedItem().getId();
                                        db.get.getReportCon().createStatement().execute("DELETE FROM `users_permissions` WHERE `user_id`='" + id + "'");
                                        ObservableList<String> checkedItems = privilegesList.getCheckModel().getCheckedItems();
                                        ObservableList<String> items = privilegesList.getItems();
                                        PreparedStatement Prepare = db.get.Prepare("INSERT INTO `users_permissions`(`user_id`, `privileges`, `value`) VALUES (?,?,?)");
                                        for (String a : items) {
                                            Prepare.setInt(1, id);
                                            Prepare.setString(2, a);
                                            if (checkedItems.contains(a)) {
                                                Prepare.setString(3, "true");
                                            } else {
                                                Prepare.setString(3, "false");
                                            }

                                            Prepare.addBatch();
                                        }
                                        Prepare.executeBatch();
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
                    AlertDialogs.showmessage("تم");
                    super.succeeded();
                }
            };
            service.start();
        }
    }

    @FXML
    private void formNewStatic(ActionEvent event) {
        clear();
    }

    @FXML
    private void formDeleteStatic(ActionEvent event) {
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
                                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                                    alert.setTitle("Deleting  Static");
                                    alert.setHeaderText("سيتم حذف القيمة ");
                                    alert.setContentText("هل انت متاكد؟");

                                    Optional<ButtonType> result = alert.showAndWait();
                                    if (result.get() == ButtonType.OK) {
                                        StaticValues st = new StaticValues();
                                        st.setAttribute(attribute.getText());

                                        st.Delete();
                                    }
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

                clear();
                getData();
                super.succeeded();
            }
        };
        service.start();
    }

    @FXML
    private void formEditeStatic(ActionEvent event) {
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
                                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                                    alert.setTitle("Editting  Static");
                                    alert.setHeaderText("سيتم تعديل القيمة ");
                                    alert.setContentText("هل انت متاكد؟");

                                    Optional<ButtonType> result = alert.showAndWait();
                                    if (result.get() == ButtonType.OK) {
                                        StaticValues st = new StaticValues();
                                        st.setAttribute(attribute.getText());
                                        st.setValus(value.getText());
                                        st.setOldAttribute(statictable.getSelectionModel().getSelectedItem().getAttribute());
                                        st.Edite();
                                    }

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

                clear();
                getData();
                super.succeeded();
            }
        };
        service.start();
    }

    @FXML
    private void formAddStatic(ActionEvent event) {
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
                                    StaticValues st = new StaticValues();
                                    st.setAttribute(attribute.getText());
                                    st.setValus(value.getText());
                                    st.Add();

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

                clear();
                getData();
                super.succeeded();
            }
        };
        service.start();
    }

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

        } catch (Exception ex) {
            AlertDialogs.showErrors(ex);
        }
    }

}
