/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Navigator;


import assets.classes.AlertDialogs;
import assets.classes.statics;
import static assets.classes.statics.*;
import carzy.girl.CarzyGirl;
import db.User;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author ahmed
 */
public class SideNavigatorController implements Initializable {

    @FXML
    private Label mainLabel;
    @FXML
    private ImageView imgview;

    Preferences prefs; 
    @FXML
    private Button accountsButton; 
    @FXML
    private Button clientBtn; 
    @FXML
    private Button providerButton;
    @FXML
    private Button invoiceBtn;
    @FXML
    private Button productsButton;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        prefs = Preferences.userNodeForPackage(CarzyGirl.class);
        mainLabel.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
            try {

                Parent login = FXMLLoader.load(getClass().getResource("/Navigator/Home.fxml"));
                login.getStylesheets().add(getClass().getResource("/assets/styles/" + prefs.get(statics.THEME, statics.DEFAULT_THEME) + ".css").toExternalForm());
                Scene sc = new Scene(login);
                Stage st = (Stage) mainLabel.getScene().getWindow();
                st.getIcons().add(new Image(getClass().getResourceAsStream("/assets/icons/logo.png")));
                st.setTitle("Crazy Girl");
                st.centerOnScreen();
                st.setScene(sc);
                st.show();
            } catch (IOException ex) {
                AlertDialogs.showErrors(ex);
            }
        });
        clientBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
            if (User.canAccess("ClientScreen")) {
                try {

                    Parent login = FXMLLoader.load(getClass().getResource(ClientData));
                    login.getStylesheets().add(getClass().getResource("/assets/styles/" + prefs.get(statics.THEME, statics.DEFAULT_THEME) + ".css").toExternalForm());
                    Scene sc = new Scene(login);
                    Stage st = (Stage) mainLabel.getScene().getWindow();
                    st.getIcons().add(new Image(getClass().getResourceAsStream("/assets/icons/logo.png")));
                    st.setTitle("Crazy Girl (العملاء)");
                    st.centerOnScreen();
                    st.setScene(sc);
                    st.show();
                } catch (IOException ex) {
                    AlertDialogs.showErrors(ex);
                }
            } else {
                AlertDialogs.permissionDenied();
            }

        });
        providerButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
            if (User.canAccess("provider")) {
                try {

                    Parent login = FXMLLoader.load(getClass().getResource(ProviderScreen));
                    login.getStylesheets().add(getClass().getResource("/assets/styles/" + prefs.get(statics.THEME, statics.DEFAULT_THEME) + ".css").toExternalForm());
                    Scene sc = new Scene(login);
                    Stage st = (Stage) mainLabel.getScene().getWindow();
                    st.getIcons().add(new Image(getClass().getResourceAsStream("/assets/icons/logo.png")));
                    st.setTitle("Crazy Girl (الموردين)");
                    st.centerOnScreen();
                    st.setScene(sc);
                    st.show();
                } catch (IOException ex) {
                    AlertDialogs.showErrors(ex);
                }
            } else {
                AlertDialogs.permissionDenied();
            }

        });
        invoiceBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
            if (User.canAccess("invoices")) {
                try {

                    Parent login = FXMLLoader.load(getClass().getResource(InvoicesScreen));
                    login.getStylesheets().add(getClass().getResource("/assets/styles/" + prefs.get(statics.THEME, statics.DEFAULT_THEME) + ".css").toExternalForm());
                    Scene sc = new Scene(login);
                    Stage st = (Stage) mainLabel.getScene().getWindow();
                    st.getIcons().add(new Image(getClass().getResourceAsStream("/assets/icons/logo.png")));
                    st.setTitle("Crazy Girl (الفواتير)");
                    st.centerOnScreen();
                    st.setScene(sc);
                    st.show();
                } catch (IOException ex) {
                    AlertDialogs.showErrors(ex);
                }
            } else {
                AlertDialogs.permissionDenied();
            }

        });
        accountsButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
            if (User.canAccess("Accounts")) {
                try {

                    Parent login = FXMLLoader.load(getClass().getResource(AccountsScreen));
                    login.getStylesheets().add(getClass().getResource("/assets/styles/" + prefs.get(statics.THEME, statics.DEFAULT_THEME) + ".css").toExternalForm());
                    Scene sc = new Scene(login);
                    Stage st = (Stage) mainLabel.getScene().getWindow();
                    st.getIcons().add(new Image(getClass().getResourceAsStream("/assets/icons/logo.png")));
                    st.setTitle("Crazy Girl (الحسابات)");
                    st.centerOnScreen();
                    st.setScene(sc);
                    st.show();
                } catch (IOException ex) {
                    AlertDialogs.showErrors(ex);
                }
            } else {
                AlertDialogs.permissionDenied();
            }

        });
        productsButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
            if (User.canAccess("products")) {
                try {

                    Parent login = FXMLLoader.load(getClass().getResource(ProductsScreen));
                    login.getStylesheets().add(getClass().getResource("/assets/styles/" + prefs.get(statics.THEME, statics.DEFAULT_THEME) + ".css").toExternalForm());
                    Scene sc = new Scene(login);
                    Stage st = (Stage) mainLabel.getScene().getWindow();
                    st.getIcons().add(new Image(getClass().getResourceAsStream("/assets/icons/logo.png")));
                    st.setTitle("Crazy Girl (المنتجات)");
                    st.centerOnScreen();
                    st.setScene(sc);
                    st.show();
                } catch (IOException ex) {
                    AlertDialogs.showErrors(ex);
                }
            } else {
                AlertDialogs.permissionDenied();
            }
        });
        

    }

}
