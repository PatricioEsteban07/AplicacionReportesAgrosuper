/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import static Controller.ImportarCSVController.CSV_NON_SELECTED;
import static Controller.ImportarCSVController.STATUS_DEFAULT;
import static Controller.ImportarCSVController.STATUS_READY;
import Controller.filtroPeriodo.FiltroPeriodoController;
import Model.CommandNames;
import Model.DBConfig;
import Model.Filtros.Filtro;
import Model.LocalDB;
import Model.Reportes.Reporte;
import Model.Reportes.Reporte_ArbolPerdidas;
import Model.Reportes.Reporte_Disponibilidad;
import Model.Reportes.Reporte_FugaFS;
import java.io.File;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
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
public class Main_BetaController implements Initializable
{
    @FXML
    private Pane panel_estadoSistema;
    @FXML
    private Text text_estadoSistema;
    @FXML
    private ChoiceBox choiceBox_periodo;
    @FXML
    private AnchorPane pane_Disponibilidad;
    @FXML
    private AnchorPane pane_ArbolPerdidas;
    @FXML
    private AnchorPane pane_FugaFS;
    @FXML
    private VBox vBox_paso2;
    @FXML
    private VBox vBox_paso3;
    @FXML
    private TextField textField_archivoDestino;
    @FXML
    private Button button_generarReporte;
    
    

    private CheckComboBox checkComboBox_fechaSemana;
    private CheckComboBox checkComboBox_fechaMes;
    private CheckComboBox checkComboBox_fechaAnio;

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
                    this.pane_Disponibilidad.setStyle("-fx-background-color: orange;");
                    this.pane_ArbolPerdidas.setStyle("-fx-background-color: white;");
                    this.pane_FugaFS.setStyle("-fx-background-color: white;");
                    break;
                case 1://reporte árbol pérdidas
                    this.reporteBase = new Reporte_ArbolPerdidas(this.db);
                    this.pane_Disponibilidad.setStyle("-fx-background-color: white;");
                    this.pane_ArbolPerdidas.setStyle("-fx-background-color: orange;");
                    this.pane_FugaFS.setStyle("-fx-background-color: white;");
                    break;
                case 2://reporte árbol pérdidas
                    this.reporteBase = new Reporte_FugaFS(this.db);
                    this.pane_Disponibilidad.setStyle("-fx-background-color: white;");
                    this.pane_ArbolPerdidas.setStyle("-fx-background-color: white;");
                    this.pane_FugaFS.setStyle("-fx-background-color: orange;");
                    break;
            }
            this.opcion = opcion;
            buttonVaciarFiltroFecha();
            this.vBox_paso2.setDisable(false);
            this.vBox_paso3.setDisable(true);
            this.actualizarEstadoProceso(CommandNames.ESTADO_INFO, this.reporteBase.nombre + " seleccionado para trabajar.");
            return true;
        }
        catch (InterruptedException e)
        {
            CommandNames.generaMensaje("Error de Sistema", AlertType.ERROR, "Problemas con reporte base", "Hubo un problema al momento de preparar"
                    + " las opciones para la generación de un reporte. Seleccione el reporte a generar nuevamente y vuelva a intentarlo.");
            System.out.println("ERROR: no se geenra reporte base como corresponde :c");
            this.vBox_paso2.setDisable(true);
            this.vBox_paso3.setDisable(true);
            return false;
        }
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
    public void buttonVaciarFiltroFecha()
    {
        Iterator it = this.reporteBase.filtros.entrySet().iterator();
        while (it.hasNext())
        {
            Map.Entry pair = (Map.Entry) it.next();
            it.remove(); // avoids a ConcurrentModificationException
        }
        this.reporteBase.generarFiltrosBase();
        this.choiceBox_periodo.getSelectionModel().clearSelection();
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
        listadoAux = FXCollections.observableArrayList();
        for (int i = 1; i < 13; i++)
        {
            listadoAux.add(i + "");
        }
        this.checkComboBox_fechaMes = new CheckComboBox<String>(listadoAux);
        this.checkComboBox_fechaMes.setPrefWidth(300);
        this.checkComboBox_fechaMes.setStyle("-fx-padding: 0 10 0 0");
        listadoAux = FXCollections.observableArrayList();
        for (int i = 2015; i < 2031; i++)
        {
            listadoAux.add(i + "");
        }
        this.checkComboBox_fechaAnio = new CheckComboBox<String>(listadoAux);
        this.checkComboBox_fechaAnio.setPrefWidth(300);
        this.checkComboBox_fechaAnio.setStyle("-fx-padding: 0 10 0 0");
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
    }

    @FXML
    public void infoApp()
    {
        CommandNames.generaMensaje("Información de Aplicación", AlertType.INFORMATION, "Sistema de Generación de Reportes",
                "Aplicación en proceso de desarrollo :)");
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
        
        Alert alertAux=CommandNames.generaMensaje("Generación de reporte en proceso", 
                Alert.AlertType.NONE, null,"Estamos generando su reporte, esta operación puede demorar dependiendo del "
                        + "detalle y cantidad de datos seleccionados...",false);
        alertAux.show();
        
        actualizarEstadoProceso(CommandNames.ESTADO_INFO, CommandNames.MSG_INFO_GEN_REPORTE);
        /*
            compactar filtros
            generar recursos en base a filtros
         */
        ArrayList<String> columnasTabla = this.reporteBase.columnasExcel;
        if (columnasTabla == null)
        {
            actualizarEstadoProceso(CommandNames.ESTADO_ERROR, CommandNames.MSG_ERROR_GEN_REPORTE);
            alertAux.getDialogPane().getButtonTypes().add(ButtonType.OK);
            alertAux.close();
            return false;
        }
        if (!generarReporte(this.reporteBase, columnasTabla))
        {
            actualizarEstadoProceso(CommandNames.ESTADO_ERROR, CommandNames.MSG_ERROR_GEN_REPORTE);
            alertAux.getDialogPane().getButtonTypes().add(ButtonType.OK);
            alertAux.close();
            return false;
        }
        this.generarReporteBase(this.opcion);
        buttonVaciarFiltroFecha();
        actualizarEstadoProceso(CommandNames.ESTADO_SUCCESS, CommandNames.MSG_SUCCESS_GEN_REPORTE);
        alertAux.getDialogPane().getButtonTypes().add(ButtonType.OK);
        alertAux.close();
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

    @FXML
    public void buttonReporteFugaFS() throws InterruptedException
    {
        System.out.println("REPORTE FUGA FS - EN CONSTRUCCION");
        CommandNames.generaMensaje("Aviso de Aplicación", AlertType.INFORMATION, "Sistema de Generación de Reportes",
                "Reporte en construcción...");
        //generarReporteBase(2);
    }
    
    @FXML
    public void buttonSeleccionarDestino()
    {
        //validar datos
        //no cerrar la ventana
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Selección de archivo CSV para importación de datos");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos CSV", "*.csv"));
        File selectedFile = fileChooser.showOpenDialog(null);
        
        if (selectedFile != null)
        {
            setArchivoSeleccionado(selectedFile.getPath());
        }
        else
        {
            setArchivoSeleccionado(null);
        }
        validarSeleccionArchivo();
    }

    private void setArchivoSeleccionado(String dirAux)
    {
        if (dirAux == null)
        {
            this.textField_archivoDestino.setText(System.getProperty("user.home")+"/Desktop/"+"resultado.xlsx");
        }
        else
        {
            this.textField_archivoDestino.setText(dirAux);
        }
    }
    
    public boolean validarSeleccionArchivo()
    {
        if (this.textField_archivoDestino.getText().equals(System.getProperty("user.home")+"/Desktop/"+"resultado.xlsx"))
        {
            this.button_generarReporte.setDisable(true);
            return false;
        }
        this.button_generarReporte.setDisable(false);
        return true;
    }
    
    public boolean generarReporte(Reporte reporte, ArrayList<String> columnsGeneral) throws InterruptedException
    {
        System.out.println("obteniendo reporte...");
        /*
        if (!reporte.generarReporte())
        {
            System.out.println("ERROR: generar reporte MainController :C");
            return false;
        }*/
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
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/img/Agrosuper.png")));
        stage.initModality(Modality.APPLICATION_MODAL);
        FiltroPeriodoController controller = loader.getController();
        controller.setFiltro(reporteBase.filtros.get("Filtro_Fecha"));
        stage.setResizable(false);
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
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/img/Agrosuper.png")));
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
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/img/Agrosuper.png")));
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
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/img/Agrosuper.png")));
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
