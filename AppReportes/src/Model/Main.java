/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;
import Model.ProcesadoresExcel.*;
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
        stage.setResizable(false);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args){
        launch(args);      
        
        /*
        new CSVImport_DespachoFaltante(new LocalDB(new DBConfig()), System.getProperty("user.home")+"/Desktop/"+"despacho_faltante.csv",
            "despacho_faltante").procesarArchivo();  
        new ProcesadorExcel_MaestroMateriales(System.getProperty("user.home")+"/Desktop/"+"Maestro Materiales Full.xlsx")
                .obtieneDatosXLSX("HojaEstatica");
        new ProcesadorExcel_ReporteFugaFS_Club(System.getProperty("user.home")+"/Desktop/"+"DatosClub.xlsx")
                .obtieneDatosXLSX("Datos Clientes");
        new ProcesadorExcel_ReporteFugaFS_LlamadoCC(System.getProperty("user.home")+"/Desktop/"+"llamadosCC.xls")
                .obtieneDatosXLS("Datos");
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
