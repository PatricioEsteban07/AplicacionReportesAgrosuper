/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 *
 * @author Patricio
 */
public final class CommandNames
{      
    public static void generaMensaje(String titulo, AlertType tipo, String mensaje, String detail)
    {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(mensaje);
        alert.setContentText(detail);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("/img/Agrosuper.png"));
        alert.showAndWait();
    }
    
    public static Alert generaMensaje(String titulo, AlertType tipo, String mensaje, String detail, boolean x)
    {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(mensaje);
        alert.setContentText(detail);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("/img/Agrosuper.png"));
        return alert;
    }
    
}
