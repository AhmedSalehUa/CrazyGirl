/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package screens.settings;
  
import static assets.classes.statics.*; 
import carzy.girl.CarzyGirl;
import carzy.girl.LoginController;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author ahmed
 */
public class SettingsController implements Initializable {

    @FXML
    private ComboBox<String> colorSelection;

    Preferences prefs;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        prefs = Preferences.userNodeForPackage(CarzyGirl.class);
        colorSelection.getItems().add("lightMode");
        colorSelection.getItems().add("darkMode");
        colorSelection.getSelectionModel().select(prefs.get(THEME, DEFAULT_THEME)); 
    }

    @FXML
    private void apply(ActionEvent event) {
        prefs.put(THEME, colorSelection.getSelectionModel().getSelectedItem());
        home(event);
    }

    @FXML
    private void home(ActionEvent e) {
        try {

            Parent mainMember = FXMLLoader.load(getClass().getResource("/Navigator/Home.fxml"));

            mainMember.getStylesheets().add(getClass().getResource("/assets/styles/" + prefs.get(THEME, DEFAULT_THEME) + ".css").toExternalForm());
            Scene sc = new Scene(mainMember);
            Stage st = (Stage) ((Node) e.getSource()).getScene().getWindow();
            st.getIcons().add(new Image(getClass().getResourceAsStream("/assets/icons/logo.png")));
            st.setTitle("Crazy Girl");
            st.setY(0);
            st.setX(0);
            st.setScene(sc);
        } catch (IOException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
