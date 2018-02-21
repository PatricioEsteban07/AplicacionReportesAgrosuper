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
        new CSVImport_Centro(new LocalDB(new DBConfig()), System.getProperty("user.home") + "/Desktop/centro.csv",
                "centro").procesarArchivo();
        new CSVImport_ZonaVentas(new LocalDB(new DBConfig()), System.getProperty("user.home") + "/Desktop/zonaventas.csv",
                "zonaventas").procesarArchivo();
        new CSVImport_Sector(new LocalDB(new DBConfig()), System.getProperty("user.home") + "/Desktop/sector.csv",
                "sector").procesarArchivo();
        new CSVImport_N2(new LocalDB(new DBConfig()), System.getProperty("user.home") + "/Desktop/n2.csv",
                "n2").procesarArchivo();
        new CSVImport_N3(new LocalDB(new DBConfig()), System.getProperty("user.home") + "/Desktop/n3.csv",
                "n3").procesarArchivo();
        new CSVImport_N4(new LocalDB(new DBConfig()), System.getProperty("user.home") + "/Desktop/n4.csv",
                "n4").procesarArchivo();
        new CSVImport_TipoCliente(new LocalDB(new DBConfig()), System.getProperty("user.home") + "/Desktop/tipocliente.csv",
                "tipocliente").procesarArchivo();
        new CSVImport_CategoriaCliente(new LocalDB(new DBConfig()), System.getProperty("user.home") + "/Desktop/categoriacliente.csv",
                "categoriacliente").procesarArchivo();
        new CSVImport_SubcategoriaCliente(new LocalDB(new DBConfig()), System.getProperty("user.home") + "/Desktop/subcategoriacliente.csv",
                "subcategoriacliente").procesarArchivo();
        new CSVImport_Marca(new LocalDB(new DBConfig()), System.getProperty("user.home") + "/Desktop/marca.csv",
                "marca").procesarArchivo();
        new CSVImport_Agrupado(new LocalDB(new DBConfig()), System.getProperty("user.home") + "/Desktop/agrupado.csv",
                "agrupado").procesarArchivo();
        new CSVImport_TipoEnvasado(new LocalDB(new DBConfig()), System.getProperty("user.home") + "/Desktop/tipoenvasado.csv",
                "tipoenvasado").procesarArchivo();
        new CSVImport_EstadoRefrigerado(new LocalDB(new DBConfig()), System.getProperty("user.home") + "/Desktop/estadorefrigerado.csv",
                "estadorefrigerado").procesarArchivo();
        new CSVImport_OficinaVentas(new LocalDB(new DBConfig()), System.getProperty("user.home") + "/Desktop/oficinaventas.csv",
                "oficinaventas").procesarArchivo();
        new CSVImport_Material(new LocalDB(new DBConfig()), System.getProperty("user.home") + "/Desktop/material.csv",
                "material").procesarArchivo();
        new CSVImport_Cliente(new LocalDB(new DBConfig()), System.getProperty("user.home") + "/Desktop/cliente.csv",
                "cliente").procesarArchivo();
        new CSVImport_ClienteLocal(new LocalDB(new DBConfig()), System.getProperty("user.home") + "/Desktop/clientelocal.csv",
                "clientelocal").procesarArchivo();
        new CSVImport_Pedido(new LocalDB(new DBConfig()), System.getProperty("user.home") + "/Desktop/pedido.csv",
                "pedido").procesarArchivo();
        new CSVImport_PedidoMaterial(new LocalDB(new DBConfig()), System.getProperty("user.home") + "/Desktop/pedido_material.csv",
                "pedido_material").procesarArchivo();
        new CSVImport_Stock(new LocalDB(new DBConfig()), System.getProperty("user.home") + "/Desktop/stock.csv",
                "stock").procesarArchivo();
        new CSVImport_Despacho(new LocalDB(new DBConfig()), System.getProperty("user.home") + "/Desktop/despacho.csv",
                "despacho").procesarArchivo();
        new CSVImport_DespachoMaterial(new LocalDB(new DBConfig()), System.getProperty("user.home") + "/Desktop/despacho_material.csv",
                "despacho_material").procesarArchivo();
        new CSVImport_Faltante(new LocalDB(new DBConfig()), System.getProperty("user.home") + "/Desktop/faltante.csv",
                "faltante").procesarArchivo();
        new CSVImport_NSCliente(new LocalDB(new DBConfig()), System.getProperty("user.home") + "/Desktop/nscliente.csv",
                "nscliente").procesarArchivo();

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
