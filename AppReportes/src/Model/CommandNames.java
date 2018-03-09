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
 * Clase que contiene métodos estáticos para generación de alertas, y donde se deben agregar variables estáticas de 
 * ser necesario.
 * @author Patricio
 */
public final class CommandNames
{      
    /**
    * Método que genera un Alert con un titulo, mensaje y tipo de alerta definido por el sistema.
    * @param titulo contiene el titulo que el Alert desplegará.
    * @param tipo contiene el tipo de Alert que necesita desplegar el sistema: AlertType.CONFIRMATION para continuar con 
    * una acción, AlertType.ERROR para notificar de un problema, AlertType.INFORMATION para mostrar estado del sistema.
    * @param mensaje contiene un subtítulo para el alert.
    * @param detail contiene el contenido específico que se desea informar.
    * */
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
    
    /**
    * Método que genera un Alert con un titulo, mensaje y tipo de alerta definido por el sistema. A diferencia del anterior, 
    * este retorna el objeto Alert para ser manipulado por otras clases.
    * @param titulo contiene el titulo que el Alert desplegará.
    * @param tipo contiene el tipo de Alert que necesita desplegar el sistema: AlertType.CONFIRMATION para continuar con 
    * una acción, AlertType.ERROR para notificar de un problema, AlertType.INFORMATION para mostrar estado del sistema.
    * @param mensaje contiene un subtítulo para el alert.
    * @param detail contiene el contenido específico que se desea informar.
    * @param x sólo para diferenciar del otro método.
    * */
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
