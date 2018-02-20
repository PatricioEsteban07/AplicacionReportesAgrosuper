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
        launch(args);      
        
        //código para implementar exportación CSV->DB
        //IMPLEMENTAR
        //lecturaArchivoPrueba();
    /*    
        new CSVImport_Region(new LocalDB(new DBConfig()), System.getProperty("user.home") + "/Desktop/regiones.csv",
                "regiones").procesarArchivo();
        new CSVImport_Centro(new LocalDB(new DBConfig()), System.getProperty("user.home") + "/Desktop/centros.csv",
                "centros").procesarArchivo();
        new CSVImport_ZonaVentas(new LocalDB(new DBConfig()), System.getProperty("user.home") + "/Desktop/zonaVentas.csv",
                "zonaVentas").procesarArchivo();
        new CSVImport_Sector(new LocalDB(new DBConfig()), System.getProperty("user.home") + "/Desktop/sectores.csv",
                "sectores").procesarArchivo();
        new CSVImport_N2(new LocalDB(new DBConfig()), System.getProperty("user.home") + "/Desktop/n2.csv",
                "n2").procesarArchivo();
        new CSVImport_N3(new LocalDB(new DBConfig()), System.getProperty("user.home") + "/Desktop/n3.csv",
                "n3").procesarArchivo();
        new CSVImport_N4(new LocalDB(new DBConfig()), System.getProperty("user.home") + "/Desktop/n4.csv",
                "n4").procesarArchivo();
        new CSVImport_TipoCliente(new LocalDB(new DBConfig()), System.getProperty("user.home") + "/Desktop/tipoCliente.csv",
                "tipoCliente").procesarArchivo();
        new CSVImport_Marca(new LocalDB(new DBConfig()), System.getProperty("user.home") + "/Desktop/marcas.csv",
                "marcas").procesarArchivo();
        new CSVImport_Agrupado(new LocalDB(new DBConfig()), System.getProperty("user.home") + "/Desktop/agrupados.csv",
                "agrupados").procesarArchivo();
        new CSVImport_TipoEnvasado(new LocalDB(new DBConfig()), System.getProperty("user.home") + "/Desktop/tipoEnvasados.csv",
                "tipoEnvasados").procesarArchivo();
        new CSVImport_EstadoRefrigerado(new LocalDB(new DBConfig()), System.getProperty("user.home") + "/Desktop/estadoRefrigerados.csv",
                "estadoRefrigerados").procesarArchivo();
        new CSVImport_OficinaVentas(new LocalDB(new DBConfig()), System.getProperty("user.home") + "/Desktop/oficinaVentas.csv",
                "oficinaVentas").procesarArchivo();
        new CSVImport_Material(new LocalDB(new DBConfig()), System.getProperty("user.home") + "/Desktop/materiales.csv",
                "materiales").procesarArchivo();
        new CSVImport_Cliente(new LocalDB(new DBConfig()), System.getProperty("user.home") + "/Desktop/clientes.csv",
                "clientes").procesarArchivo();
        new CSVImport_ClienteLocal(new LocalDB(new DBConfig()), System.getProperty("user.home") + "/Desktop/clienteLocal.csv",
                "clienteLocal").procesarArchivo();
        new CSVImport_Pedido(new LocalDB(new DBConfig()), System.getProperty("user.home") + "/Desktop/pedidos.csv",
                "pedidos").procesarArchivo();
        new CSVImport_PedidoMaterial(new LocalDB(new DBConfig()), System.getProperty("user.home") + "/Desktop/pedidos_material.csv",
                "pedidos_material").procesarArchivo();
        new CSVImport_Stock(new LocalDB(new DBConfig()), System.getProperty("user.home") + "/Desktop/stock.csv",
                "stock").procesarArchivo();
        new CSVImport_Despacho(new LocalDB(new DBConfig()), System.getProperty("user.home") + "/Desktop/despachos.csv",
                "despachos").procesarArchivo();
        new CSVImport_DespachoMaterial(new LocalDB(new DBConfig()), System.getProperty("user.home") + "/Desktop/despachos_material.csv",
                "despachos_material").procesarArchivo();

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
