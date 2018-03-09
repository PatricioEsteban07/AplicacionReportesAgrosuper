/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Clase principal del sistema
 * @author Patricio
 */
public class Main extends Application
{
    private static Main instance;
    private Stage primaryStage;

    /**
     * Método que obtiene la instancia actual.
     */
    public static Main getInstance()
    {
        return instance;
    }

    /**
     * Método que inicializa la ventana inicial de la aplicación.
     * @exception Exception
     */
    @Override
    public void start(Stage stage) throws Exception
    {
        instance = this;
        primaryStage = stage;
        Parent root = FXMLLoader.load(getClass().getResource("/Views/Main.fxml"));

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/img/Agrosuper.png")));
        this.updateTitle();
        stage.show();
        stage.setResizable(false);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        launch(args);
    }

    /**
     * Método para actualizar el título de la ventana principal.
     */
    public void updateTitle()
    {
        primaryStage.setTitle("Sistema de generación de reportes Agrosuper");
    }

    /**
     * Método para obtener el título de la ventana principal.
     * @return un String que contiene el título de la ventana principal.
     */
    public String getTitle()
    {
        return primaryStage.getTitle();
    }
}
