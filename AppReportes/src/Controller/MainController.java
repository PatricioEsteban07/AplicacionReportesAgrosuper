/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.Cliente;
import Model.CommandNames;
import Model.RecursosDB.RecursoDB;
import Model.Reportes.Reporte_ClienteEmpresas;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
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
import javafx.scene.control.Accordion;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TitledPane;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import org.controlsfx.control.CheckComboBox;

/**
 * FXML Controller class
 *
 * @author Patricio
 */
public class MainController implements Initializable
{    
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
    private Accordion accordion_Listado;
    @FXML
    private Pane panel_estadoSistema;
    @FXML
    private Text text_estadoSistema;
    @FXML
    private GridPane gridPane_Filtros;
    @FXML
    private GridPane gridPane_otrosFiltros;
    private CheckComboBox checkComboBox_fechaSemana;
    private CheckComboBox checkComboBox_fechaMes;
    private CheckComboBox checkComboBox_fechaAnio;
    private CheckComboBox checkComboBox_zonas;
    private CheckComboBox checkComboBox_canales;
    private CheckComboBox checkComboBox_sucursales;
    private CheckComboBox checkComboBox_clientes;
    
    @FXML 
    private TableView<ArrayList<String>> ReportesTableView;
    

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
        this.accordion_Listado.setExpandedPane(titledPane_areaEstrategica);
        
        inicializarFiltros();
    }
    
    public void inicializarFiltros()
    {
        inicializarFechas();
        ObservableList<String> listadoAux = FXCollections.observableArrayList();
        listadoAux.add("Norte");
        listadoAux.add("Centro Norte");
        listadoAux.add("Santiago");
        listadoAux.add("Centro Sur");
        listadoAux.add("Sur");
        this.checkComboBox_zonas = new CheckComboBox<String>(listadoAux);
        this.checkComboBox_zonas.setPrefWidth(300);
        this.checkComboBox_zonas.setStyle("-fx-padding: 0 10 0 0");
        this.gridPane_otrosFiltros.add(checkComboBox_zonas, 1, 0);
        
        listadoAux = FXCollections.observableArrayList();
        listadoAux.add("Supermercado");
        listadoAux.add("Food Service");
        listadoAux.add("Call Center");
        listadoAux.add("Tradicional");
        listadoAux.add("Cliente Importante");
        this.checkComboBox_canales = new CheckComboBox<String>(listadoAux);
        this.checkComboBox_canales.setPrefWidth(300);
        this.checkComboBox_canales.setStyle("-fx-padding: 0 10 0 0");
        this.gridPane_otrosFiltros.add(checkComboBox_canales, 1, 1);
        
        listadoAux = FXCollections.observableArrayList();
        listadoAux.add("Sucursal 1");
        listadoAux.add("Sucursal 2");
        listadoAux.add("Sucursal 3");
        this.checkComboBox_sucursales = new CheckComboBox<String>(listadoAux);
        this.checkComboBox_sucursales.setPrefWidth(300);
        this.checkComboBox_sucursales.setStyle("-fx-padding: 0 10 0 0");
        this.gridPane_otrosFiltros.add(checkComboBox_sucursales, 1, 2);
        
        listadoAux = FXCollections.observableArrayList();
        listadoAux.add("Cliente 1");
        listadoAux.add("Cliente 2");
        listadoAux.add("Cliente 3");
        this.checkComboBox_clientes = new CheckComboBox<String>(listadoAux);
        this.checkComboBox_clientes.setPrefWidth(300);
        this.checkComboBox_clientes.setStyle("-fx-padding: 0 10 0 0");
        this.gridPane_otrosFiltros.add(checkComboBox_clientes, 1, 3);
    }
    
    public void inicializarFechas()
    {
        ObservableList<String> listadoAux = FXCollections.observableArrayList();
        for (int i = 1; i < 53; i++)
        {
            listadoAux.add(i+"");
        }
        this.checkComboBox_fechaSemana = new CheckComboBox<String>(listadoAux);
        this.checkComboBox_fechaSemana.setPrefWidth(300);
        this.checkComboBox_fechaSemana.setStyle("-fx-padding: 0 10 0 0");
        this.gridPane_Filtros.add(checkComboBox_fechaSemana, 1, 1);
        listadoAux = FXCollections.observableArrayList();
        for (int i = 1; i < 13; i++)
        {
            listadoAux.add(i+"");
        }
        this.checkComboBox_fechaMes = new CheckComboBox<String>(listadoAux);
        this.checkComboBox_fechaMes.setPrefWidth(300);
        this.checkComboBox_fechaMes.setStyle("-fx-padding: 0 10 0 0");
        this.gridPane_Filtros.add(checkComboBox_fechaMes, 3, 0);
        listadoAux = FXCollections.observableArrayList();
        for (int i = 2015; i < 2031; i++)
        {
            listadoAux.add(i+"");
        }
        this.checkComboBox_fechaAnio = new CheckComboBox<String>(listadoAux);
        this.checkComboBox_fechaAnio.setPrefWidth(300);
        this.checkComboBox_fechaAnio.setStyle("-fx-padding: 0 10 0 0");
        this.gridPane_Filtros.add(checkComboBox_fechaAnio, 3, 1);
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
    
    public void actualizarEstadoProceso(String estado, String mensaje) throws InterruptedException
    {
        String style;
        switch(estado)
        {
            case CommandNames.ESTADO_SUCCESS:
                style="-fx-background-color: lightgreen;";
                break;
            case CommandNames.ESTADO_INFO:
                style="-fx-background-color: yellow;";
                break;
            case CommandNames.ESTADO_ERROR:
                style="-fx-background-color: orange;";
                break;
            default:
                style="-fx-background-color: white;";
                break;
        }
        this.panel_estadoSistema.setStyle(style);
        this.text_estadoSistema.setText(estado+": "+mensaje);
    }
    
    @FXML
    public void buttonReporteEjemplo() throws SQLException, ClassNotFoundException, IOException, InterruptedException
    {
        actualizarEstadoProceso(CommandNames.ESTADO_INFO,CommandNames.MSG_INFO_GEN_REPORTE);
        System.out.println("obteniendo reporte...");
        Reporte_ClienteEmpresas reporte=new Reporte_ClienteEmpresas();
        if(!reporte.generarRecursos())
        {
            System.out.println("ERROR: generar recursos :C");
            actualizarEstadoProceso(CommandNames.ESTADO_ERROR,CommandNames.MSG_ERROR_GEN_REPORTE);
        }
        else
        {
            RecursoDB r = reporte.recursos.get("Clientes");
            System.out.println("Recursos: "+r.nombre);
            for (int i = 0; i < r.getAll().size(); i++)
            {
                System.out.println("Rec: "+((Cliente)(r.getAll().get(i))).id);
            }
        }
        if(!reporte.generarExcel())
        {
            System.out.println("ERROR: generar excel :C");
            actualizarEstadoProceso(CommandNames.ESTADO_ERROR,CommandNames.MSG_ERROR_GEN_REPORTE);
        }        
        //generar tabla con reporte
        this.ReportesTableView=new TableView<>();
        
        ArrayList<String> columnsGeneral=new ArrayList<>();
        columnsGeneral.add("cliente_id");
        columnsGeneral.add("cliente_nombre");
        columnsGeneral.add("cliente_apellido");
        columnsGeneral.add("cliente_edad");
        columnsGeneral.add("cliente_sexo");
        columnsGeneral.add("cliente_descripcion");
        columnsGeneral.add("empresa_id");
        columnsGeneral.add("empresa_nombre");
        columnsGeneral.add("empresa_direccion");
        columnsGeneral.add("empresa_descripcion");
        
        for (int i = 0; i < columnsGeneral.size(); i++)
        {
            TableColumn<ArrayList<String>, String> tc = new TableColumn(columnsGeneral.get(i));
            tc.setCellValueFactory(new PropertyValueFactory<ArrayList<String>,String>(columnsGeneral.get(i)));
            this.ReportesTableView.getColumns().add(tc);
        }
        
        ObservableList<ArrayList<String>> list = FXCollections.observableArrayList();
        //mostrar tabla en app
        for (int i = 0; i < 5; i++)
        {
            ArrayList<String> aux=new ArrayList<>();
            aux.add("hola"); aux.add("hola"); aux.add("hola");
            aux.add("hola"); aux.add("hola"); aux.add("hola");
            aux.add("hola"); aux.add("hola"); aux.add("hola");
            aux.add("hola");
            list.add(aux);
        }

        ReportesTableView.getSelectionModel().setCellSelectionEnabled(true);
        ReportesTableView.setItems(list);
        
        ReportesTableView.setPrefSize(panelTabla.getWidth(), panelTabla.getHeight());
        
        panelTabla.setTopAnchor(ReportesTableView, 0.0);
        panelTabla.setLeftAnchor(ReportesTableView, 0.0);
        panelTabla.setRightAnchor(ReportesTableView, 0.0);

        this.panelTabla.getChildren().clear();
        this.panelTabla.getChildren().add(ReportesTableView);
        actualizarEstadoProceso(CommandNames.ESTADO_SUCCESS,CommandNames.MSG_SUCCESS_GEN_REPORTE);
    }   
    
    @FXML
    public void generarGraficoPrueba() throws SQLException, ClassNotFoundException, InterruptedException
    {
        actualizarEstadoProceso(CommandNames.ESTADO_INFO,CommandNames.MSG_INFO_GEN_GRAPHICS);
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
        actualizarEstadoProceso(CommandNames.ESTADO_SUCCESS,CommandNames.MSG_SUCCESS_GEN_GRAPHICS);
    }
}
