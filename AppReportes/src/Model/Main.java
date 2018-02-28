/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import Model.ProcesadoresExcel.ProcesadorExcel_MaestroMateriales;
import Model.ProcesadoresExcel.ProcesadorExcel_ReporteFugaFS;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
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
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/img/Agrosuper.png")));
        this.updateTitle();
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args){
        launch(args);      
        
        //new ProcesadorExcel_ReporteFugaFS(System.getProperty("user.home")+"/Desktop/"+"llamadosCC.xls")
         //       .obtieneDatosXLS("Datos", 1, 1);
         /*
        new ProcesadorExcel_MaestroMateriales(System.getProperty("user.home")+"/Desktop/"+"Maestro Materiales Full.xlsx")
                .obtieneDatosXLSX("HojaEstatica");
        System.exit(0);
        */
    }
    
    public void updateTitle(){
        primaryStage.setTitle("Sistema de generación de reportes Agrosuper");
    }
    
    public String getTitle(){
        return primaryStage.getTitle();
    }
    
}
