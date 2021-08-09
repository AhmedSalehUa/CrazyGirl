/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carzy.girl;

import assets.classes.statics;
import com.sun.javafx.application.LauncherImpl;
import db.get;
import java.util.prefs.Preferences;
import javafx.application.Application;
import javafx.application.Preloader;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author AHMED
 */
public class CarzyGirl extends Application {
    
    Preferences prefs;
    int state = 1;

    @Override
    public void start(Stage stage) throws Exception {
        
            prefs = Preferences.userNodeForPackage(CarzyGirl.class);
            String propertyValue = prefs.get(statics.THEME, statics.DEFAULT_THEME);
            Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
//            root = FXMLLoader.load(getClass().getResource(HrScreenCalcAttend));

            Scene scene = new Scene(root);

            root.getStylesheets().add(getClass().getResource("/assets/styles/" + propertyValue + ".css").toExternalForm());
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/assets/icons/logo.png")));
            stage.setTitle("Crazy Girl");
            stage.setScene(scene);
            stage.toFront();
            stage.show();
        

    }

    @Override
    public void init() throws Exception {
//        LauncherImpl.notifyPreloader(this, new Preloader.ProgressNotification(4));
//        if (db.get.canCon()) {
//            LauncherImpl.notifyPreloader(this, new Preloader.ProgressNotification(5));
//        }
        LauncherImpl.notifyPreloader(this, new Preloader.ProgressNotification(0));
        
//        if (db.lite.setConnection()) {
//            LauncherImpl.notifyPreloader(this, new Preloader.ProgressNotification(1));
//        } else {
//            LauncherImpl.notifyPreloader(this, new Preloader.ProgressNotification(11));
//            state = 0;
//        }
        Thread.sleep(100);
        LauncherImpl.notifyPreloader(this, new Preloader.ProgressNotification(2));
        
        if (get.canCon()) {
            LauncherImpl.notifyPreloader(this, new Preloader.ProgressNotification(3));
        } else {
            LauncherImpl.notifyPreloader(this, new Preloader.ProgressNotification(13));
//            state = 0;
        }
//
//        for (int i = 1; i <= 10; i++) {
//            double progress = (double) i / 10;
//
//            LauncherImpl.notifyPreloader(this, new Preloader.ProgressNotification(progress));
//            Thread.sleep(100);
//        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        LauncherImpl.launchApplication(CarzyGirl.class, SplashScreenLoader.class, args);
    }
    
}
