package Navigator;
 

import assets.animation.ZoomInLeft;
import assets.animation.ZoomInRight;
import assets.classes.AlertDialogs;
import static assets.classes.statics.*;
import carzy.girl.CarzyGirl;
import db.User; 
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.CountDownLatch;
import java.util.prefs.Preferences;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Pair; 

public class HomeController implements Initializable {

    @FXML
    private AnchorPane root;
    @FXML
    private Button Accounts;  

    Preferences prefs;
    @FXML
    private MenuButton userNode;
    @FXML
    private Label userName;  
    @FXML
    private Button clients; 
    @FXML
    private Button products;
    @FXML
    private Button provider;
    @FXML
    private Button invoices;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        prefs = Preferences.userNodeForPackage(CarzyGirl.class);
        new ZoomInLeft(products).play();
        new ZoomInRight(provider).play();
        new ZoomInLeft(Accounts).play();
        new ZoomInRight(clients).play();
        new ZoomInLeft(invoices).play(); 
        userName.setText(prefs.get(USER_NAME, USERNAME_DEFAULT).toUpperCase());
        MenuItem logout = new MenuItem("Log Out");
        MenuItem changePassword = new MenuItem("change password");
        MenuItem settings = new MenuItem("settings");
        MenuItem controlPanel = new MenuItem("Control Panel");
        userNode.getItems().add(logout);
        userNode.getItems().add(changePassword);
        userNode.getItems().add(settings);
        if (!prefs.get(USER_ROLE, "user").equals("user")) {
            userNode.getItems().add(controlPanel);
        }
        logout.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                try {
                    Parent login = FXMLLoader.load(getClass().getResource(Login));
                    System.err.println(prefs.get(THEME, DEFAULT_THEME));
                    login.getStylesheets().add(getClass().getResource("/assets/styles/" + prefs.get(THEME, DEFAULT_THEME) + ".css").toExternalForm());

                    Stage st = (Stage) clients.getScene().getWindow();
                    st.getIcons().add(new Image(getClass().getResourceAsStream("/assets/icons/logo.png")));
                    st.setTitle("Crazy Girl");

                    double width = 350;
                    double height = 530;

                    Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
                    st.setX((screenBounds.getWidth() - width) / 2);
                    st.setY((screenBounds.getHeight() - height) / 2);

                    Scene scene = new Scene(login, width, height);
                    st.setScene(scene);
                    st.show();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    Alert dialod = new Alert(AlertType.ERROR);
                    dialod.setContentText(ex.getMessage());
                    dialod.show();
                }
            }
        });

        changePassword.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                Dialog<Pair<String, String>> dialog = new Dialog<>();
                dialog.setTitle("Change Password");
                dialog.setHeaderText("You Are Abouy Changing Your Password");

                dialog.setGraphic(new ImageView(this.getClass().getResource("/assets/icons/icons8_password_200px_3.png").toString()));

                ButtonType loginButtonType = new ButtonType("Change", ButtonData.OK_DONE);
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
                    us.setId(Integer.parseInt(prefs.get(USER_ID, "0")));
                    us.setPassword(usernamePassword.getKey());

                    if (us.changePassword()) {
                        AlertDialogs.showmessage("تم");
                    } else {
                        AlertDialogs.showError("حدث خطا");
                    }
                });
            }
        });
        clients.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
            if (User.canAccess("ClientScreen")) {
                Service<Void> service = new Service<Void>() {
                    @Override
                    protected Task<Void> createTask() {
                        return new Task<Void>() {
                            @Override
                            protected Void call() throws Exception {
                                //Background work                       
                                final CountDownLatch latch = new CountDownLatch(1);
                                Platform.runLater(() -> {
                                    try {
                                        try {
                                            Parent mainData = FXMLLoader.load(getClass().getResource(ClientData)); 
                                            mainData.getStylesheets().add(getClass().getResource("/assets/styles/" + prefs.get(THEME, DEFAULT_THEME) + ".css").toExternalForm());
                                            Scene sc = new Scene(mainData);
                                            Stage st = (Stage) ((Node) e.getSource()).getScene().getWindow();
                                            st.getIcons().add(new Image(getClass().getResourceAsStream("/assets/icons/logo.png")));
                                            st.setTitle("Crazy Girl (العملاء)");
                                            st.setScene(sc);
                                        } catch (IOException ex) {
                                            ex.printStackTrace();
                                            AlertDialogs.showErrors(ex);
                                        }
                                    } finally {
                                        latch.countDown();
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
            } else {
                AlertDialogs.permissionDenied();
            }
        });
        invoices.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
            if (User.canAccess("invoices")) {
                Service<Void> service = new Service<Void>() {
                    @Override
                    protected Task<Void> createTask() {
                        return new Task<Void>() {
                            @Override
                            protected Void call() throws Exception {
                                //Background work                       
                                final CountDownLatch latch = new CountDownLatch(1);
                                Platform.runLater(() -> {
                                    try {
                                        try {
                                            Parent mainData = FXMLLoader.load(getClass().getResource(InvoicesScreen)); 
                                            mainData.getStylesheets().add(getClass().getResource("/assets/styles/" + prefs.get(THEME, DEFAULT_THEME) + ".css").toExternalForm());
                                            Scene sc = new Scene(mainData);
                                            Stage st = (Stage) ((Node) e.getSource()).getScene().getWindow();
                                            st.getIcons().add(new Image(getClass().getResourceAsStream("/assets/icons/logo.png")));
                                            st.setTitle("Crazy Girl (الفواتير)");
                                            st.setScene(sc);
                                        } catch (IOException ex) {
                                            ex.printStackTrace();
                                            AlertDialogs.showErrors(ex);
                                        }
                                    } finally {
                                        latch.countDown();
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
            } else {
                AlertDialogs.permissionDenied();
            }
        });
        provider.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
            if (User.canAccess("provider")) {
                Service<Void> service = new Service<Void>() {
                    @Override
                    protected Task<Void> createTask() {
                        return new Task<Void>() {
                            @Override
                            protected Void call() throws Exception {
                                //Background work                       
                                final CountDownLatch latch = new CountDownLatch(1);
                                Platform.runLater(() -> {
                                    try {
                                        try {
                                            Parent mainData = FXMLLoader.load(getClass().getResource(ProviderScreen));  
                                            mainData.getStylesheets().add(getClass().getResource("/assets/styles/" + prefs.get(THEME, DEFAULT_THEME) + ".css").toExternalForm());
                                            Scene sc = new Scene(mainData);
                                            Stage st = (Stage) ((Node) e.getSource()).getScene().getWindow();
                                            st.getIcons().add(new Image(getClass().getResourceAsStream("/assets/icons/logo.png")));
                                            st.setTitle("Crazy Girl (المنتجات)");
                                            st.setScene(sc);
                                        } catch (IOException ex) {
                                            ex.printStackTrace();
                                            AlertDialogs.showErrors(ex);
                                        }
                                    } finally {
                                        latch.countDown();
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
            } else {
                AlertDialogs.permissionDenied();
            }

        });
        Accounts.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
            if (User.canAccess("Accounts")) {
                Service<Void> service = new Service<Void>() {
                    @Override
                    protected Task<Void> createTask() {
                        return new Task<Void>() {
                            @Override
                            protected Void call() throws Exception {
                                //Background work                       
                                final CountDownLatch latch = new CountDownLatch(1);
                                Platform.runLater(() -> {
                                    try {
                                        try {
                                            Parent mainData = FXMLLoader.load(getClass().getResource(AccountsScreen));  
                                            mainData.getStylesheets().add(getClass().getResource("/assets/styles/" + prefs.get(THEME, DEFAULT_THEME) + ".css").toExternalForm());
                                            Scene sc = new Scene(mainData);
                                            Stage st = (Stage) ((Node) e.getSource()).getScene().getWindow();
                                            st.getIcons().add(new Image(getClass().getResourceAsStream("/assets/icons/logo.png")));
                                            st.setTitle("Crazy Girl (الحسابات)");
                                            st.setScene(sc);
                                        } catch (IOException ex) { 
                                            ex.printStackTrace(); 
                                            AlertDialogs.showErrors(ex);
                                        }
                                    } finally {
                                        latch.countDown();
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
            } else {
                AlertDialogs.permissionDenied();
            }

        });
        products.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
            if (User.canAccess("products")) {
                Service<Void> service = new Service<Void>() {
                    @Override
                    protected Task<Void> createTask() {
                        return new Task<Void>() {
                            @Override
                            protected Void call() throws Exception {
                                //Background work                       
                                final CountDownLatch latch = new CountDownLatch(1);
                                Platform.runLater(() -> {
                                    try {
                                        try {
                                            Parent mainData = FXMLLoader.load(getClass().getResource(ProductsScreen));
                                            mainData.getStylesheets().add(getClass().getResource("/assets/styles/" + prefs.get(THEME, DEFAULT_THEME) + ".css").toExternalForm());
                                            Scene sc = new Scene(mainData);
                                            Stage st = (Stage) ((Node) e.getSource()).getScene().getWindow();
                                            st.getIcons().add(new Image(getClass().getResourceAsStream("/assets/icons/logo.png")));
                                            st.setTitle("Crazy Girl (الاصناف)");
                                            st.setScene(sc);
                                        } catch (IOException ex) {
                                            ex.printStackTrace();
                                            AlertDialogs.showErrors(ex);
                                        }
                                    } finally {
                                        latch.countDown();
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
            } else {
                AlertDialogs.permissionDenied();
            }

        });
          
        settings.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                try {
                    Parent login = FXMLLoader.load(getClass().getResource("/screens/settings/Settings.fxml"));
                    System.err.println(prefs.get(THEME, DEFAULT_THEME));
                    login.getStylesheets().add(getClass().getResource("/assets/styles/" + prefs.get(THEME, DEFAULT_THEME) + ".css").toExternalForm());

                    Stage st = (Stage) clients.getScene().getWindow();
                    st.getIcons().add(new Image(getClass().getResourceAsStream("/assets/icons/logo.png")));
                    st.setTitle("Crazy Girl");

                    double width = 350;
                    double height = 530;

                    Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
                    st.setX((screenBounds.getWidth() - width) / 2);
                    st.setY((screenBounds.getHeight() - height) / 2);

                    Scene scene = new Scene(login, width, height);
                    st.setScene(scene);
                    st.show();

                } catch (Exception ex) {
                    ex.printStackTrace();
                    Alert dialod = new Alert(AlertType.ERROR);
                    dialod.setContentText(ex.getMessage());
                    dialod.show();
                }
            }
        });
        controlPanel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                try {
                    Parent login = FXMLLoader.load(getClass().getResource(controlPanelScreen));
                    System.err.println(prefs.get(THEME, DEFAULT_THEME));
                    login.getStylesheets().add(getClass().getResource("/assets/styles/" + prefs.get(THEME, DEFAULT_THEME) + ".css").toExternalForm());

                    Stage st = (Stage) clients.getScene().getWindow();
                    st.getIcons().add(new Image(getClass().getResourceAsStream("/assets/icons/logo.png")));
                   st.setTitle("Crazy Girl");

                    double width = 1360;
                    double height = 768;

                    Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
                    st.setX((screenBounds.getWidth() - width) / 2);
                    st.setY((screenBounds.getHeight() - height) / 2);

                    Scene scene = new Scene(login, width, height);
                    st.setScene(scene);
                    st.show();

                } catch (Exception ex) {
                    ex.printStackTrace();
                    Alert dialod = new Alert(AlertType.ERROR);
                    dialod.setContentText(ex.getMessage());
                    dialod.show();
                }
            }
        });

    }

}
