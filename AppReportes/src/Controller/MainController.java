/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Controller.filtroPeriodo.FiltroPeriodoController;
import Model.CommandNames;
import Model.Filtros.Filtro;
import Model.Filtros.Filtro_Canal;
import Model.Filtros.Filtro_Cliente;
import Model.Filtros.Filtro_Sucursal;
import Model.Filtros.Filtro_Zona;
import Model.Reportes.Reporte;
import Model.Reportes.Reporte_ClienteEmpresas;
import com.sun.javafx.property.adapter.PropertyDescriptor;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Accordion;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
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
    private Text text_nombreReporte;
    @FXML
    private Text text_areaReporte;
    @FXML
    private Text text_filtroReporte;
    @FXML
    private GridPane gridPane_Filtros;
    @FXML
    private GridPane gridPane_Filtros2;
    @FXML
    private GridPane gridPane_otrosFiltros;
    @FXML
    private ChoiceBox choiceBox_periodo;
    
    private CheckComboBox checkComboBox_fechaSemana;
    private CheckComboBox checkComboBox_fechaMes;
    private CheckComboBox checkComboBox_fechaAnio;
    private CheckComboBox checkComboBox_zonas;
    private CheckComboBox checkComboBox_canales;
    private CheckComboBox checkComboBox_sucursales;
    private CheckComboBox checkComboBox_clientes;
    
    @FXML 
    private TableView<ArrayList<String>> ReportesTableView;
    
    private int opcion;
    private Reporte reporteBase;
    
    private HashMap<String, Reporte> reportesGenerados;
    
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
        this.reportesGenerados=new HashMap<>();
        
        try
        {
            this.generarReporteBase(0);
        }
        catch (InterruptedException ex)
        {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
        inicializarPeriodo();
        inicializarFiltros();
        try
        {
            this.buttonReporteEjemplo();
        }
        catch (InterruptedException ex)
        {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public boolean generarReporteBase(int opcion) throws InterruptedException
    {
        this.actualizarEstadoProceso(CommandNames.ESTADO_INFO, "Generando elementos de configuración de reporte base para procesamiento...");
        try
        {
            switch(opcion)
            {
                case 0:
                    this.reporteBase=new Reporte_ClienteEmpresas();
                    this.opcion=opcion;
                    this.text_areaReporte.setText("Área Estratégica");
            }
            this.text_nombreReporte.setText(this.reporteBase.nombre);
            this.text_filtroReporte.setText("pendiente...");
            this.actualizarEstadoProceso(CommandNames.ESTADO_SUCCESS, "Elementos de configuración de reporte generado y listo para ser trabajado.");
            return true;
        }
        catch (InterruptedException e)
        {
        }
        this.actualizarEstadoProceso(CommandNames.ESTADO_ERROR, "Ups, hubo un problema para generar los elementos base para tu reporte. Intente nuevamente o reinicie el sistema.");
        return false;
    }
    
    public void inicializarPeriodo()
    {
        this.choiceBox_periodo.setItems(FXCollections.observableArrayList("Año", "Mes", "Semana", 
                "Fecha"));
        this.choiceBox_periodo.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>()
        {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue)
            {
                System.out.println("Filtro fecha Seleccionado! "+newValue);
                setFiltroPeriodo(newValue.intValue());
            }
        });
    }
    
    @FXML
    public void buttonVaciarFiltro()
    {
            Iterator it = this.reporteBase.filtros.entrySet().iterator();
            while (it.hasNext()) 
            {
                Map.Entry pair = (Map.Entry)it.next();
                System.out.println(pair.getKey() + " = " + ((Filtro)(pair.getValue())).generarEtiquetaInfo());
                it.remove(); // avoids a ConcurrentModificationException
            }
        this.reporteBase.generarFiltrosBase();
        this.choiceBox_periodo.getSelectionModel().clearSelection();
        inicializarFiltros();
    }
    
    public void inicializarFiltros()
    {
        ObservableList<String> listadoAux = FXCollections.observableArrayList();
        listadoAux.addAll(((Filtro_Zona)(this.reporteBase.filtros.get("Filtro_Zona"))).zonas);
        this.checkComboBox_zonas = new CheckComboBox<String>(listadoAux);
        this.checkComboBox_zonas.setPrefWidth(300);
        this.gridPane_Filtros2.add(checkComboBox_zonas, 3, 1);
        
        this.checkComboBox_zonas.getCheckModel().getCheckedItems().addListener(new ListChangeListener<String>() {
            public void onChanged(ListChangeListener.Change<? extends String> c) {
                System.out.println(checkComboBox_zonas.getCheckModel().getCheckedItems());
                
                ArrayList<String> aux=new ArrayList<>();
                for(int i =0 ; i < checkComboBox_zonas.getCheckModel().getCheckedItems().size(); i++)
                {
                    aux.add(checkComboBox_zonas.getCheckModel().getCheckedItems().get(i).toString());   
                }
                ((Filtro_Zona)(reporteBase.filtros.get("Filtro_Zona"))).setZonasSeleccionadas(aux);
               // generarEtiqueta(reporteBase.filtros.get("Filtro_Zona"),checkComboBox_zonas);
            }
        });
        
        listadoAux = FXCollections.observableArrayList();
        listadoAux.addAll(((Filtro_Canal)(this.reporteBase.filtros.get("Filtro_Canal"))).canales);
        this.checkComboBox_canales = new CheckComboBox<String>(listadoAux);
        this.checkComboBox_canales.setPrefWidth(300);
        this.gridPane_Filtros2.add(checkComboBox_canales, 3, 2);
        
        this.checkComboBox_canales.getCheckModel().getCheckedItems().addListener(new ListChangeListener<String>() {
            public void onChanged(ListChangeListener.Change<? extends String> c) {
                System.out.println(checkComboBox_canales.getCheckModel().getCheckedItems());
                
                ArrayList<String> aux=new ArrayList<>();
                for(int i =0 ; i < checkComboBox_canales.getCheckModel().getCheckedItems().size(); i++)
                {
                    aux.add(checkComboBox_canales.getCheckModel().getCheckedItems().get(i).toString());   
                }
                ((Filtro_Canal)(reporteBase.filtros.get("Filtro_Canal"))).setCanalesSeleccionados(aux);
                generarEtiqueta(reporteBase.filtros.get("Filtro_Canal"),checkComboBox_canales);
            }
        });
        
        listadoAux = FXCollections.observableArrayList();
        listadoAux.addAll(((Filtro_Sucursal)(this.reporteBase.filtros.get("Filtro_Sucursal"))).sucursales);
        this.checkComboBox_sucursales = new CheckComboBox<String>(listadoAux);
        this.checkComboBox_sucursales.setPrefWidth(300);
        this.gridPane_Filtros2.add(checkComboBox_sucursales, 7, 0);
        
        this.checkComboBox_sucursales.getCheckModel().getCheckedItems().addListener(new ListChangeListener<String>() {
            public void onChanged(ListChangeListener.Change<? extends String> c) {
                System.out.println(checkComboBox_sucursales.getCheckModel().getCheckedItems());
                
                ArrayList<String> aux=new ArrayList<>();
                for(int i =0 ; i < checkComboBox_sucursales.getCheckModel().getCheckedItems().size(); i++)
                {
                    aux.add(checkComboBox_sucursales.getCheckModel().getCheckedItems().get(i).toString());   
                }
                ((Filtro_Sucursal)(reporteBase.filtros.get("Filtro_Sucursal"))).setSucursalesSeleccionadas(aux);
                generarEtiqueta(reporteBase.filtros.get("Filtro_Sucursal"),checkComboBox_sucursales);
            }
        });
        
        listadoAux = FXCollections.observableArrayList();
        listadoAux.addAll(((Filtro_Cliente)(this.reporteBase.filtros.get("Filtro_Cliente"))).clientes);
        this.checkComboBox_clientes = new CheckComboBox<String>(listadoAux);
        this.checkComboBox_clientes.setPrefWidth(300);
        this.gridPane_Filtros2.add(checkComboBox_clientes, 7, 1);
        
        this.checkComboBox_clientes.getCheckModel().getCheckedItems().addListener(new ListChangeListener<String>() {
            public void onChanged(ListChangeListener.Change<? extends String> c) {
                System.out.println(checkComboBox_clientes.getCheckModel().getCheckedItems());
                
                ArrayList<String> aux=new ArrayList<>();
                for(int i =0 ; i < checkComboBox_clientes.getCheckModel().getCheckedItems().size(); i++)
                {
                    aux.add(checkComboBox_clientes.getCheckModel().getCheckedItems().get(i).toString());   
                }
                ((Filtro_Cliente)(reporteBase.filtros.get("Filtro_Cliente"))).setClientesSeleccionados(aux);
                generarEtiqueta(reporteBase.filtros.get("Filtro_Cliente"),checkComboBox_clientes);
            }
        });
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
    
    public void setFiltroPeriodo(int opcion)
    {
        switch(opcion)
        {
            case 0://año
                this.cargarModalFiltroFecha("/Views/filtroPeriodo/filtroPeriodo_Anio.fxml","Filtrado por año");
                break;
            case 1://mes
                this.cargarModalFiltroFecha("/Views/filtroPeriodo/filtroPeriodo_Mes.fxml","Filtrado por mes");
                break;
            case 2://semana
                this.cargarModalFiltroFecha("/Views/filtroPeriodo/filtroPeriodo_Semana.fxml","Filtrado por semana");
                break;
            case 3://fecha
                this.cargarModalFiltroFecha("/Views/filtroPeriodo/filtroPeriodo_Dia.fxml","Filtrado por dia");
                break;
            case 4://rango fecha
                //this.AbrirModal("/vistas/toolbar/Cursos.fxml", "Titulo");
                break;
        }
        generarEtiqueta(this.reporteBase.filtros.get("Filtro_Fecha"),this.choiceBox_periodo);
    }

    @FXML
    public void infoApp()
    {
        CommandNames.generaMensaje("Información de Aplicación", AlertType.INFORMATION, "Sistema de Generación de Reportes", 
                "Aplicación en proceso de desarrollo :)");
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
               // CommandNames.generaMensaje("Mensaje del Sistema", Alert.AlertType.INFORMATION, "Operación Exitosa", mensaje);
                style="-fx-background-color: lightgreen;";
                break;
            case CommandNames.ESTADO_INFO:
                style="-fx-background-color: yellow;";
                break;
            case CommandNames.ESTADO_ERROR:
               // CommandNames.generaMensaje("Mensaje del Sistema", Alert.AlertType.ERROR, "Error del sistema", mensaje);
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
    public boolean buttonGenerarReporte() throws InterruptedException
    {
        actualizarEstadoProceso(CommandNames.ESTADO_INFO,CommandNames.MSG_INFO_GEN_REPORTE);
        
        /*
            compactar filtros
            generar recursos en base a filtros
            
        
        */
        
        ArrayList<String> columnasTabla=this.reporteBase.columnasExcel;
        if(columnasTabla==null)
        {
            actualizarEstadoProceso(CommandNames.ESTADO_ERROR,CommandNames.MSG_ERROR_GEN_REPORTE);
            return false;
        }
        if(!generarReporte(this.reporteBase, columnasTabla))
        {
            actualizarEstadoProceso(CommandNames.ESTADO_ERROR,CommandNames.MSG_ERROR_GEN_REPORTE);
            return false;
        }
        this.reportesGenerados.put(this.reporteBase.nombre,this.reporteBase);
        this.generarReporteBase(this.opcion);
        actualizarEstadoProceso(CommandNames.ESTADO_SUCCESS,CommandNames.MSG_SUCCESS_GEN_REPORTE);
        return true;
    }
    
    public boolean generarReporte(Reporte reporte, ArrayList<String> columnsGeneral) throws InterruptedException
    {
        System.out.println("obteniendo reporte...");
        if(!reporte.generarRecursos())
        {
            System.out.println("ERROR: generar recursos :C");
        }
        if(!reporte.generarExcel())
        {
            System.out.println("ERROR: generar excel :C");
            return false;
        }        
        //generar tabla con reporte
        this.ReportesTableView=new TableView<>();
        
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
        return true;
    }
    
    @FXML
    public void buttonReporteEjemplo() throws InterruptedException
    {
        generarReporteBase(0);
    }  
    
    private void cargarModalFiltroFecha(String resource, String title)
    {
        Parent root = cargarModal(resource,title);
        FXMLLoader loader = null;
    
        try {
            loader = new FXMLLoader(getClass().getResource(resource));
            root = loader.load();
        } catch (IOException ex) {
            System.out.println("Error al abrir el modal "+title+" ("+resource+")");
            ex.printStackTrace();
            System.exit(0);
        }
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle(title);
        stage.initModality(Modality.APPLICATION_MODAL);
        FiltroPeriodoController controller = loader.getController();
        controller.setFiltro(reporteBase.filtros.get("Filtro_Fecha"));
        stage.setResizable(false);
        //stage.initOwner();
        //stage.getIcons().add(new Image("img/icon.png"));
        stage.showAndWait();
    }
    
    private Parent cargarModal(String recurso, String titulo){
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource(recurso));
        } catch (IOException ex) {
            System.out.println("Error al abrir el modal "+titulo+" ("+recurso+")");
            ex.printStackTrace();
            
            CommandNames.generaMensaje("Error de Aplicación", AlertType.ERROR, "Sistema de Generación de Reportes", 
                "Ha ocurrido un problema abriendo una nueva ventana. Contáctese con un informático :c."
                        + "\n La aplicación se cerrará...");
            System.exit(0);
        }      
        return root;
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

    private void generarEtiqueta(Filtro filtroAux, javafx.scene.control.Control component)
    {
        component.setTooltip(new Tooltip(filtroAux.generarEtiquetaInfo()));
    }
}