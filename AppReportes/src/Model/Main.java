/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import Model.PobladorDB.CSVImport.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Patricio
 */
public class Main extends Application {
    private static Main instance;
    
    public static Main getInstance(){
        return instance;
    }
    
    private Stage primaryStage;
    
    @Override
    public void start(Stage stage) throws Exception {
        instance = this;
        primaryStage = stage;
        Parent root = FXMLLoader.load(getClass().getResource("/Views/Main.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        this.updateTitle();
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args){
        //launch(args);      
        
        //código para implementar exportación CSV->DB
        //IMPLEMENTAR
        //lecturaArchivoPrueba();
        
        new CSVImport_Cliente(new LocalDB(new DBConfig()), System.getProperty("user.home") + "/Desktop/",
                "clientes").procesarArchivo();
        System.exit(0);
    }
    
    public void updateTitle(){
        primaryStage.setTitle("Sistema de generación de reportes Agrosuper");
    }
    
    public String getTitle(){
        return primaryStage.getTitle();
    }
    
}
