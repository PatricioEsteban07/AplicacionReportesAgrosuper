/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.CommandNames;
import Model.DataExtract;
import TablasReporte.Empresa_Cliente;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TitledPane;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author Patricio
 */
public class MainController implements Initializable
{
    private final DataExtract db = new DataExtract();
    
    @FXML
    private MenuItem menu_close;
    @FXML
    private TitledPane titledPane_areaEstrategica;
    @FXML
    private TitledPane titledPane_areaVentas;
    @FXML
    private TitledPane titledPane_areaServicios;
    @FXML
    private AnchorPane panelGrafico;
    @FXML
    private AnchorPane panelTabla;
    
    @FXML 
    private TableView<Empresa_Cliente> ReportesTableView;
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        this.menu_close.setOnAction(new EventHandler()
        {
            @Override
            public void handle(Event event)
            {
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

    @FXML
    public void changeActiveTitled()
    {
        this.titledPane_areaEstrategica.setExpanded(false);
        this.titledPane_areaVentas.setExpanded(false);
        this.titledPane_areaServicios.setExpanded(false);
        if (this.titledPane_areaEstrategica.isFocused())
        {
            this.titledPane_areaEstrategica.setExpanded(true);
        }
        else if (this.titledPane_areaVentas.isFocused())
        {
            this.titledPane_areaVentas.setExpanded(true);
        }
        else if (this.titledPane_areaServicios.isFocused())
        {
            this.titledPane_areaServicios.setExpanded(true);
        }
    }
    
    @FXML
    public void generarDatosExcel() throws IOException, SQLException, ClassNotFoundException
    {
        System.out.println("generando excel...");
        
        ArrayList<String> columns=new ArrayList<>();
        columns.add("cliente_id");
        columns.add("cliente_nombre");
        columns.add("cliente_apellido");
        columns.add("cliente_edad");
        columns.add("cliente_sexo");
        columns.add("cliente_descripcion");
        columns.add("empresa_id");
        columns.add("empresa_nombre");
        columns.add("empresa_direccion");
        columns.add("empresa_descripcion");
               
        db.ejecutarPeticion("cliente", CommandNames.queryClientes);
        db.generarExcel("cliente", columns);
        System.out.println("finalizado :)");
    }

    @FXML
    public void buttonReporteEjemplo() throws SQLException, ClassNotFoundException, IOException
    {
        System.out.println("obteniendo clientes...");
        ArrayList<String> columns=new ArrayList<>();
        ArrayList<String> columnsGeneral=new ArrayList<>();
        
        columns.add("cliente_id");
        columnsGeneral.add("cliente_id");
        columns.add("cliente_nombre");
        columnsGeneral.add("cliente_nombre");
        columns.add("cliente_apellido");
        columnsGeneral.add("cliente_apellido");
        columns.add("cliente_edad");
        columnsGeneral.add("cliente_edad");
        columns.add("cliente_sexo");
        columnsGeneral.add("cliente_sexo");
        columns.add("cliente_descripcion");
        columnsGeneral.add("cliente_descripcion");
        
        this.db.ejecutarPeticion(CommandNames.clientes, CommandNames.queryClientes);
        this.db.generarExcel(CommandNames.clientes, columns);
        
        for (int i = 0; i < this.db.clientes.size(); i++)
        {
            System.out.println("c: "+this.db.clientes.get(i));
        }
        
        System.out.println("obteniendo empresas...");
        columns=new ArrayList<>();
        columns.add("empresa_id");
        columnsGeneral.add("empresa_id");
        columns.add("empresa_nombre");
        columnsGeneral.add("empresa_nombre");
        columns.add("empresa_direccion");
        columnsGeneral.add("empresa_direccion");
        columns.add("empresa_descripcion");
        columnsGeneral.add("empresa_descripcion");
        
        this.db.ejecutarPeticion(CommandNames.empresas, CommandNames.queryEmpresas);
        this.db.generarExcel(CommandNames.empresas, columns);
        
        for (int i = 0; i < this.db.empresas.size(); i++)
        {
            System.out.println("e: "+this.db.empresas.get(i));
        }
        
        System.out.println("obteniendo relacion cliente-empresa...");
        columns=new ArrayList<>();
        columns.add("empresa_id");
        columns.add("cliente_id");
        
        this.db.ejecutarPeticionRelacion(CommandNames.cliente_empresa, CommandNames.queryClienteEmpresa, 
                new ArrayList<String>(Arrays.asList(CommandNames.empresa, CommandNames.cliente)));
        
        for (int i = 0; i < this.db.empresas_clientes.size(); i++)
        {
            System.out.println("c-e: "+this.db.empresas_clientes.get(i));
        }
        
        //generar tabla con reporte
        this.ReportesTableView=new TableView<>();
        
        for (int i = 0; i < columnsGeneral.size(); i++)
        {
            TableColumn<Empresa_Cliente, String> tc = new TableColumn(columnsGeneral.get(i));
            tc.setCellValueFactory(new PropertyValueFactory<Empresa_Cliente,String>(columnsGeneral.get(i)));
            
            this.ReportesTableView.getColumns().add(tc);
        }
        
        ObservableList<Empresa_Cliente> list = FXCollections.observableArrayList();
        //mostrar tabla en app
        for (int i = 0; i < this.db.empresas_clientes.size(); i++)
        {
            this.db.empresas_clientes.get(i).completaClase();
            list.add(this.db.empresas_clientes.get(i));
        }

       // ReportesTableView.getSelectionModel().setCellSelectionEnabled(true);
        ReportesTableView.setItems(list);
        
        ReportesTableView.setPrefSize(panelTabla.getWidth(), panelTabla.getHeight());
        
        panelTabla.setTopAnchor(ReportesTableView, 0.0);
        panelTabla.setLeftAnchor(ReportesTableView, 0.0);
        panelTabla.setRightAnchor(ReportesTableView, 0.0);

        this.panelTabla.getChildren().clear();
        this.panelTabla.getChildren().add(ReportesTableView);
    }    
    
    @FXML
    public void generarGraficoPrueba() throws SQLException, ClassNotFoundException
    {
        System.out.println("Inicio generación grafico prueba");
        
        String s0="Norte", s1="Santiago", s2="Centro", s3="Sur";
        
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        BarChart chart_ejemplo=new BarChart(xAxis, yAxis);
        
        chart_ejemplo.setTitle("Gráfico Clientes por Sucursal");
        xAxis.setLabel("Empresas");       
        yAxis.setLabel("Cantidad Clientes");
 
        XYChart.Series series1 = new XYChart.Series();
        series1.setName("2003");       
        series1.getData().add(new XYChart.Data(s0, 25601.34));
        series1.getData().add(new XYChart.Data(s1, 20148.82));
        series1.getData().add(new XYChart.Data(s2, 10000));
        series1.getData().add(new XYChart.Data(s3, 35407.15)); 
        
        XYChart.Series series2 = new XYChart.Series();
        series2.setName("2004");
        series2.getData().add(new XYChart.Data(s0, 57401.85));
        series2.getData().add(new XYChart.Data(s1, 41941.19));
        series2.getData().add(new XYChart.Data(s2, 45263.37));
        series2.getData().add(new XYChart.Data(s3, 117320.16)); 
        
        XYChart.Series series3 = new XYChart.Series();
        series3.setName("2005");
        series3.getData().add(new XYChart.Data(s0, 45000.65));
        series3.getData().add(new XYChart.Data(s1, 44835.76));
        series3.getData().add(new XYChart.Data(s2, 18722.18));
        series3.getData().add(new XYChart.Data(s3, 17557.31));
        
        chart_ejemplo.getData().addAll(series1, series2, series3);
        this.panelGrafico.getChildren().add(chart_ejemplo);
        
        System.out.println("finalizado :)");
    }
}
