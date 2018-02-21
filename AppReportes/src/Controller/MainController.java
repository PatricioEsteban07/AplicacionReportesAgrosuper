/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Controller.filtroPeriodo.FiltroPeriodoController;
import Model.CommandNames;
import Model.DBConfig;
import Model.Filtros.Filtro;
import Model.LocalDB;
import Model.Recurso;
import Model.Reportes.Reporte;
import Model.Reportes.Reporte_ArbolPerdidas;
import Model.Reportes.Reporte_Disponibilidad;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
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
    private TableView<Recurso> ReportesTableView;
    
    private FileChooser fileChooser;

    private int opcion;
    private Reporte reporteBase;
    private LocalDB db;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        this.db = new LocalDB(new DBConfig());    
        this.accordion_Listado.setExpandedPane(titledPane_areaEstrategica);

        try
        {
            this.generarReporteBase(0);
        }
        catch (InterruptedException ex)
        {
            CommandNames.generaMensaje("Error del Sistema", AlertType.ERROR, "Prolemas para seleccionar reporte",
                "Hubo un problema para generar la instancia necesaria para la selección del reporte. El error es el siguiente: "+ex);
        }
        inicializarPeriodo();
        inicializarFiltros();
        try
        {
            this.buttonReporteDisponibilidad();
        }
        catch (InterruptedException ex)
        {
            CommandNames.generaMensaje("Error del Sistema", AlertType.ERROR, "Prolemas para seleccionar reporte",
                "Hubo un problema para generar la instancia necesaria para la selección del reporte. El error es el siguiente: "+ex);
        }
        this.db.probarDBConection();
    }

    public boolean generarReporteBase(int opcion) throws InterruptedException
    {
        this.actualizarEstadoProceso(CommandNames.ESTADO_INFO, "Generando elementos de configuración de reporte base para procesamiento...");
        try
        {
            switch (opcion)
            {
                case 0://reporte disponibilidad
                    this.reporteBase = new Reporte_Disponibilidad(this.db);
                    this.text_areaReporte.setText("Área Servicios");
                    break;
                case 1://reporte árbol pérdidas
                    this.reporteBase = new Reporte_ArbolPerdidas(this.db);
                    this.text_areaReporte.setText("Área Servicios");
                    break;
            }
            this.opcion = opcion;
            this.text_nombreReporte.setText(this.reporteBase.nombre);
            this.text_filtroReporte.setText("pendiente...");
            buttonVaciarFiltro();
            this.actualizarEstadoProceso(CommandNames.ESTADO_INFO, this.reporteBase.nombre + " seleccionado para trabajar.");
            return true;
        }
        catch (InterruptedException e)
        {
            CommandNames.generaMensaje("Error de Sistema", AlertType.ERROR, "Problemas con reporte base", "Hubo un problema al momento de preparar"
                    + " las opciones para la generación de un reporte. Seleccione el reporte a generar nuevamente y vuelva a intentarlo.");
            System.out.println("ERROR: no se geenra reporte base como corresponde :c");
        }
        this.actualizarEstadoProceso(CommandNames.ESTADO_ERROR, "Ups, hubo un problema para generar los elementos base para tu reporte. "
                + "Intente nuevamente o reinicie el sistema.");
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
            Map.Entry pair = (Map.Entry) it.next();
            it.remove(); // avoids a ConcurrentModificationException
        }
        this.reporteBase.generarFiltrosBase();
        this.choiceBox_periodo.getSelectionModel().clearSelection();
        inicializarFiltros();
    }

    public void inicializarFiltros()
    {
        /*
        ObservableList<String> listadoAux = FXCollections.observableArrayList();
        
        if(this.reporteBase.filtros.containsKey("Filtro_Zona"))
        {
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
        }

        if(this.reporteBase.filtros.containsKey("Filtro_Canal"))
        {
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
        }
        
        if(this.reporteBase.filtros.containsKey("Filtro_Sucursal"))
        {
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
        }
        
        if(this.reporteBase.filtros.containsKey("Filtro_Cliente"))
        {
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
         */
        this.text_filtroReporte.setText("Sin filtros aplicados");
    }

    public void inicializarFechas()
    {
        ObservableList<String> listadoAux = FXCollections.observableArrayList();
        for (int i = 1; i < 53; i++)
        {
            listadoAux.add(i + "");
        }
        this.checkComboBox_fechaSemana = new CheckComboBox<String>(listadoAux);
        this.checkComboBox_fechaSemana.setPrefWidth(300);
        this.checkComboBox_fechaSemana.setStyle("-fx-padding: 0 10 0 0");
        this.gridPane_Filtros.add(checkComboBox_fechaSemana, 1, 1);
        listadoAux = FXCollections.observableArrayList();
        for (int i = 1; i < 13; i++)
        {
            listadoAux.add(i + "");
        }
        this.checkComboBox_fechaMes = new CheckComboBox<String>(listadoAux);
        this.checkComboBox_fechaMes.setPrefWidth(300);
        this.checkComboBox_fechaMes.setStyle("-fx-padding: 0 10 0 0");
        this.gridPane_Filtros.add(checkComboBox_fechaMes, 3, 0);
        listadoAux = FXCollections.observableArrayList();
        for (int i = 2015; i < 2031; i++)
        {
            listadoAux.add(i + "");
        }
        this.checkComboBox_fechaAnio = new CheckComboBox<String>(listadoAux);
        this.checkComboBox_fechaAnio.setPrefWidth(300);
        this.checkComboBox_fechaAnio.setStyle("-fx-padding: 0 10 0 0");
        this.gridPane_Filtros.add(checkComboBox_fechaAnio, 3, 1);
    }

    public void setFiltroPeriodo(int opcion)
    {
        switch (opcion)
        {
            case 0://año
                this.cargarModalFiltroFecha("/Views/filtroPeriodo/filtroPeriodo_Anio.fxml", "Filtrado por año");
                break;
            case 1://mes
                this.cargarModalFiltroFecha("/Views/filtroPeriodo/filtroPeriodo_Mes.fxml", "Filtrado por mes");
                break;
            case 2://semana
                CommandNames.generaMensaje("Información de Aplicación", AlertType.INFORMATION, "Sistema de Generación de Reportes",
                        "Opción en proceso de desarrollo :)");
                // this.cargarModalFiltroFecha("/Views/filtroPeriodo/filtroPeriodo_Semana.fxml","Filtrado por semana");
                break;
            case 3://fecha
                this.cargarModalFiltroFecha("/Views/filtroPeriodo/filtroPeriodo_Dia.fxml", "Filtrado por dia");
                break;
            case 4://rango fecha
                //this.AbrirModal("/vistas/toolbar/Cursos.fxml", "Titulo");
                break;
        }
        generarEtiqueta(this.reporteBase.filtros.get("Filtro_Fecha"), this.choiceBox_periodo);
        this.text_filtroReporte.setText(this.reporteBase.filtros.get("Filtro_Fecha").generarEtiquetaInfo());
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

    public boolean actualizarEstadoProceso(String estado, String mensaje) throws InterruptedException
    {
        String style;
        switch (estado)
        {
            case CommandNames.ESTADO_SUCCESS:
                // CommandNames.generaMensaje("Mensaje del Sistema", Alert.AlertType.INFORMATION, "Operación Exitosa", mensaje);
                style = "-fx-background-color: lightgreen;";
                break;
            case CommandNames.ESTADO_INFO:
                style = "-fx-background-color: yellow;";
                break;
            case CommandNames.ESTADO_ERROR:
                // CommandNames.generaMensaje("Mensaje del Sistema", Alert.AlertType.ERROR, "Error del sistema", mensaje);
                style = "-fx-background-color: orange;";
                break;
            default:
                style = "-fx-background-color: white;";
                break;
        }
        this.panel_estadoSistema.setStyle(style);
        this.text_estadoSistema.setText(estado + ": " + mensaje);
        return true;
    }

    @FXML
    public boolean buttonGenerarReporte() throws InterruptedException
    {
        if (this.choiceBox_periodo.getSelectionModel().getSelectedIndex() == -1)
        {
            actualizarEstadoProceso(CommandNames.ESTADO_ERROR, "Debe seleccionar un rango de fechas para generar tal reporte.");
            return false;
        }
        actualizarEstadoProceso(CommandNames.ESTADO_INFO, CommandNames.MSG_INFO_GEN_REPORTE);
        /*
            compactar filtros
            generar recursos en base a filtros
         */
        ArrayList<String> columnasTabla = this.reporteBase.columnasExcel;
        if (columnasTabla == null)
        {
            actualizarEstadoProceso(CommandNames.ESTADO_ERROR, CommandNames.MSG_ERROR_GEN_REPORTE);
            return false;
        }
        if (!generarReporte(this.reporteBase, columnasTabla))
        {
            actualizarEstadoProceso(CommandNames.ESTADO_ERROR, CommandNames.MSG_ERROR_GEN_REPORTE);
            return false;
        }
        this.generarReporteBase(this.opcion);
        buttonVaciarFiltro();
        this.inicializarFiltros();
        actualizarEstadoProceso(CommandNames.ESTADO_SUCCESS, CommandNames.MSG_SUCCESS_GEN_REPORTE);
        return true;
    }

    @FXML
    public void buttonReporteDisponibilidad() throws InterruptedException
    {
        generarReporteBase(0);
    }

    @FXML
    public void buttonReporteArbolPerdidas() throws InterruptedException
    {
        generarReporteBase(1);
    }

    public boolean generarReporte(Reporte reporte, ArrayList<String> columnsGeneral) throws InterruptedException
    {
        System.out.println("obteniendo reporte...");
        if (!reporte.generarReporte())
        {
            CommandNames.generaMensaje("Error del Sistema", AlertType.ERROR, "Error al generar reporte",
                    "Hubo un problema para la generación del reporte. Como sugerencia verifique los datos de conexión"
                    + " a la Base de Datos y/o la integridad de éste e intente mas tarde.");
            System.out.println("ERROR: generar reporte MainController :C");
            return false;
        }
        //generar tabla con reporte
        //this.ReportesTableView=new TableView<>();
        //this.reporteBase.desplegarInfoExcelApp(this.ReportesTableView, panelTabla);
        return true;
    }

    private void cargarModalFiltroFecha(String resource, String title)
    {
        Parent root = cargarModal(resource, title);
        FXMLLoader loader = null;

        try
        {
            loader = new FXMLLoader(getClass().getResource(resource));
            root = loader.load();
        }
        catch (IOException ex)
        {
            CommandNames.generaMensaje("Error de Aplicación", AlertType.ERROR, "Sistema de Generación de Reportes",
                    "Ha ocurrido un problema abriendo una nueva ventana. Contáctese con un informático :c."
                    + "\n La aplicación se cerrará...");
            System.out.println("Error al abrir el modal " + title + " (" + resource + ")");
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

    private Parent cargarModal(String recurso, String titulo)
    {
        Parent root = null;
        try
        {
            root = FXMLLoader.load(getClass().getResource(recurso));
        }
        catch (IOException ex)
        { 
            CommandNames.generaMensaje("Error de Aplicación", AlertType.ERROR, "Sistema de Generación de Reportes",
                    "Ha ocurrido un problema abriendo una nueva ventana. Contáctese con un informático :c."
                    + "\n La aplicación se cerrará..."+ex);
            System.out.println("Error al abrir el modal " + titulo + " (" + recurso + ")"+ex);
            System.exit(0);
        }
        return root;
    }

    private void generarEtiqueta(Filtro filtroAux, javafx.scene.control.Control component)
    {
        component.setTooltip(new Tooltip(filtroAux.generarEtiquetaInfo()));
    }
    
    @FXML
    public void poblarDB()
    {
        String resource = "/Views/ImportarCSV.fxml";
        String title = "Importar datos a Base de Datos";
        Parent root = cargarModal(resource, title);
        FXMLLoader loader = null;

        try
        {
            loader = new FXMLLoader(getClass().getResource(resource));
            root = loader.load();
        }
        catch (IOException ex)
        {
            CommandNames.generaMensaje("Error de Aplicación", AlertType.ERROR, "Sistema de Generación de Reportes",
                    "Ha ocurrido un problema abriendo una nueva ventana. Contáctese con un informático :c."
                    + "\n La aplicación se cerrará...");
            System.out.println("Error al abrir el modal " + title + " (" + resource + ")");
            System.exit(0);
        }
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle(title);
        stage.initModality(Modality.APPLICATION_MODAL);
        ImportarCSVController controller = loader.getController();
        //setea valores base
        controller.setDB(db);
        stage.setResizable(false);
        //stage.initOwner();
        //stage.getIcons().add(new Image("img/icon.png"));
        stage.showAndWait();
    }
    
    @FXML
    public void poblarDBMultiple()
    {
        String resource = "/Views/ImportarCSVMultiple.fxml";
        String title = "Importación múltiple de datos a Base de Datos";
        Parent root = cargarModal(resource, title);
        FXMLLoader loader = null;

        try
        {
            loader = new FXMLLoader(getClass().getResource(resource));
            root = loader.load();
        }
        catch (IOException ex)
        {
            CommandNames.generaMensaje("Error de Aplicación", AlertType.ERROR, "Sistema de Generación de Reportes",
                    "Ha ocurrido un problema abriendo una nueva ventana. Contáctese con un informático :c."
                    + "\n La aplicación se cerrará...");
            System.out.println("Error al abrir el modal " + title + " (" + resource + ")");
            System.exit(0);
        }
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle(title);
        stage.initModality(Modality.APPLICATION_MODAL);
        ImportarCSVMultipleController controller = loader.getController();
        //setea valores base
        controller.setDB(db);
        stage.setResizable(false);
        //stage.initOwner();
        //stage.getIcons().add(new Image("img/icon.png"));
        stage.showAndWait();
    }
    
    @FXML
    private void setConfigDB()
    {
        String resource = "/Views/configDBInfo.fxml";
        String title = "Configuración de conexión a DB";
        Parent root = cargarModal(resource, title);
        FXMLLoader loader = null;

        try
        {
            loader = new FXMLLoader(getClass().getResource(resource));
            root = loader.load();
        }
        catch (IOException ex)
        {
            CommandNames.generaMensaje("Error de Aplicación", AlertType.ERROR, "Sistema de Generación de Reportes",
                    "Ha ocurrido un problema abriendo una nueva ventana. Contáctese con un informático :c."
                    + "\n La aplicación se cerrará...");
            System.out.println("Error al abrir el modal " + title + " (" + resource + ")");
            System.exit(0);
        }
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle(title);
        stage.initModality(Modality.APPLICATION_MODAL);
        configDBInfoController controller = loader.getController();
        //setea valores base
        controller.completaInfo(db);
        stage.setResizable(false);
        //stage.initOwner();
        //stage.getIcons().add(new Image("img/icon.png"));
        stage.showAndWait();
    }
    

    @FXML
    private void closeApp()
    {
        Platform.exit();
    }

}
