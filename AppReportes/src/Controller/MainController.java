/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;

/**
 * FXML Controller class
 *
 * @author Patricio
 */
public class MainController implements Initializable {
    
    @FXML private MenuItem menu_close;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        this.menu_close.setOnAction(new EventHandler() {

            @Override
            public void handle(Event event) {
                Platform.exit();
            }
        });
    }  
    
    @FXML
    public void infoApp()
    {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Información de Aplicación");
        alert.setHeaderText("Sistema de Generación de Reportes");
        alert.setContentText("Aplicación en proceso de desarrollo :)");

        alert.showAndWait();  
    }

    
}
