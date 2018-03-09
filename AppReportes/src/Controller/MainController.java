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
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.controlsfx.control.CheckComboBox;

/**
 * FXML Controller class:
 * Controlador de la ventana principal del sistema
 *
 * @author Patricio
 */
public class MainController implements Initializable
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
    private TextField textField_archivoDestino;
    
    @FXML
    private VBox vBox_paso1;
    @FXML
    private VBox vBox_paso2;
    @FXML
    private VBox vBox_paso3;
    @FXML
    private Button button_generarReporte;    
    @FXML
    private HBox hBoxEstado_paso1;
    @FXML
    private HBox hBoxEstado_paso2;
    @FXML
    private HBox hBoxEstado_paso3;
    
    private CheckComboBox checkComboBox_fechaSemana;
    private CheckComboBox checkComboBox_fechaMes;
    private CheckComboBox checkComboBox_fechaAnio;

    private int pasoActual;
    private int reporteSeleccionado;
    private Reporte reporteBase;
    private LocalDB db;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        this.db = new LocalDB(new DBConfig());
        this.reporteBase=null;
        this.reporteSeleccionado=-1;
        inicializarPeriodo();
        actualizarEstadoPasos(this.db.probarDBConection() ? 1 : 0);
    }

    /**
     * Método que, dado el reporte seleccionado por el usuario para ser generado, prepara los objetos base necesarios 
     * para modificar filtros y llamar a los métodos adecuados para la generación del reporte.
     * @param opcion recibe la opcion que identifica al tipo de reporte que el usuario solicita generar
     * @return true en caso que la operación sea exitosa y false en caso que algo ocurra mal.
     * @exception InterruptedException
     */
    public boolean generarReporteBase(int opcion) throws InterruptedException
    {
        if(this.reporteSeleccionado==opcion)
        {
            this.reporteSeleccionado=-1;
            actualizarEstadoPasos(1);
            return false;
        }
        ArrayList<String> paneStyles = new ArrayList<>();
        switch (opcion)
        {
            case 0://reporte disponibilidad
                this.reporteBase = new Reporte_Disponibilidad(this.db);
                break;
            case 1://reporte árbol pérdidas
                this.reporteBase = new Reporte_ArbolPerdidas(this.db);
                break;
            case 2://reporte fugas FS
                this.reporteBase = new Reporte_FugaFS(this.db);
                break;
        }
        this.pane_Disponibilidad.setStyle((opcion==0) ? "-fx-background-color: orange;" : "");
        this.pane_ArbolPerdidas.setStyle((opcion==1) ? "-fx-background-color: orange;" : "");
        this.pane_FugaFS.setStyle((opcion==2) ? "-fx-background-color: orange;" : "");
        this.reporteSeleccionado = opcion;
        actualizarEstadoPasos(2);
        return true;
    }

    /**
     * Método para inicializar el filtro de fechas, configurando tramos de fechas válidos y un Listener que responde 
     * a cambios dentro del elemento.
     */
    public void inicializarPeriodo()
    {
        this.choiceBox_periodo.setItems(FXCollections.observableArrayList("Año", "Mes", "Semana",
                "Fecha"));
        this.choiceBox_periodo.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>()
        {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue)
            {
                if(newValue.intValue()!=-1)
                {
                    setFiltroPeriodo(newValue.intValue());
                }
            }
        });
    }

    /**
     * Método para vaciar la selección del usuario en el filtro fecha.
     */
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
        actualizarEstadoPasos(2);
    }

    /**
     * Método para inicializar los tramos de fechas para los objetos que dependan de tal información.
     */
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

    /**
     * Método que se activa en base al Listener agregado en el objeto del filtro de fechas. Modifica la opción correspondiente 
     * al tipo de filtro fecha aplicado por el usuario.
     * @param opcion contiene la opción correspondiente al tipo de filtro fecha aplicado para que el sistema despliegue la 
     * ventana asociada a tal opción.
     */
    public void setFiltroPeriodo(int opcion)
    {
        int opcionAux=this.reporteBase.filtros.get("Filtro_Fecha").getOpcion();
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
        if(opcionAux!=this.reporteBase.filtros.get("Filtro_Fecha").getOpcion())
        {
            actualizarEstadoPasos(3);
        }
    }

    /**
     * Método que despliega un Alert con información del sistema.
     */
    @FXML
    public void infoApp()
    {
        CommandNames.generaMensaje("Información de Aplicación", AlertType.INFORMATION, "Sistema de Generación de Reportes",
            "Proyecto de práctica profesional - Aplicación en fase inicial. \n El código fuente de dicha aplicación está disponible "
            + "en manos de área de Gestión Ventas de Agrosuper. Cualquier uso, reproducción y modificación "
            + "debe ser aprobada por el área propietaria.");
    }
    
    /**
     * Método que actualiza el paso en el cuál el usuario debe completar para continuar con la generación del reporte. 
     * El sistema necesita de información previa antes de generar un informe, es por esto que el usuario debe realizar ciertos 
     * pasos para llegar a generar el informe. Este método controla tales pasos. Dependiendo del paso en el que se encuentre, 
     * el sistema bloqueará opciones y liberará otras. 
     * @param op contiene la opción que define el estado del proceso de generación de reporte antes de ser generado.
     * @return true cuando la actualización del paso actual es exitoso, y false si ocurre algo inusual.
     */
    public boolean actualizarEstadoPasos(int op)
    {
        if(op>this.pasoActual+1)
        {
            System.out.println("ERROR! ESTA PEGANDO UN SALTO EN LOS PASOS QUE NO DEBERÍA!");
            return false;
        }
        this.pasoActual=op;
        String style_alert="-fx-background-color: red;";
        String style_success="-fx-background-color: cornflowerblue;";
        String style_actual="-fx-background-color: blue;";
        String style_block="-fx-background-color: gray;";
        switch(this.pasoActual)
        {
            case 0://deshabilitar todo por no conexión a DB
                this.pane_Disponibilidad.setStyle("");
                this.pane_ArbolPerdidas.setStyle("");
                this.pane_FugaFS.setStyle("");
                this.choiceBox_periodo.getSelectionModel().select(-1);//solo cuando opcion < 3
                this.reporteSeleccionado=-1;
                actualizarEstadoProceso("AVISO: No es posible una conexión a la Base de Datos. Verifique información para la conexión.");
                break;
            case 1:
                //eliminar selecion reporte / dejar en null variable
                this.pane_Disponibilidad.setStyle("");
                this.pane_ArbolPerdidas.setStyle("");
                this.pane_FugaFS.setStyle("");
                //bloquear p2 / bloquear p3 / bloquear button reporte
                this.choiceBox_periodo.getSelectionModel().select(-1);//solo cuando opcion < 3
                this.reporteSeleccionado=-1;
                actualizarEstadoProceso("Paso 1: Seleccione el reporte que necesite generar.");
                break;
            case 2:
                //liberar p2 / bloquear p3 / bloquear button reporte
                this.choiceBox_periodo.getSelectionModel().select(-1);//solo cuando opcion < 3
                actualizarEstadoProceso("Paso 2: Seleccione los parámetros del "
                        + "filtro de acuerdo a las necesidades del reporte a generar.");
                break;
            case 3:
                //liberar p3 / bloquear button reporte
                actualizarEstadoProceso("Paso 3: Ahora puede generar su reporte. "
                        + "Puede modificar la dirección de creación de éste.");
                break;
        }
        
        this.vBox_paso1.setDisable((this.pasoActual < 1));
        this.vBox_paso2.setDisable((this.pasoActual < 2));
        this.vBox_paso3.setDisable((this.pasoActual < 3));
        this.hBoxEstado_paso1.setStyle((this.pasoActual==0) ? style_alert : ((this.pasoActual==1) ? style_actual : ((this.pasoActual<1) ? style_block : style_success)));
        this.hBoxEstado_paso2.setStyle((this.pasoActual==0) ? style_alert : ((this.pasoActual==2) ? style_actual : ((this.pasoActual<2) ? style_block : style_success)));
        this.hBoxEstado_paso3.setStyle((this.pasoActual==0) ? style_alert : ((this.pasoActual==3) ? style_actual : ((this.pasoActual<3) ? style_block : style_success)));
        this.button_generarReporte.setDisable((this.pasoActual != 3));
        setArchivoSeleccionado(null);
        return true;
    }
    
    /**
     * Método que actualiza el panel de estado del sistema.
     * @param mensaje contiene el enunciado a desplegar como información de estado del sistema.
     */
    public void actualizarEstadoProceso(String mensaje)
    {
        this.panel_estadoSistema.setStyle("-fx-background-color: white;");
        this.text_estadoSistema.setText(mensaje);
    }

    /**
     * Método que genera el reporte en base al filtro previamente definido. Se realiza inicialmente validaciones de sistema 
     * (saber si el llamado a este método ocurre una vez completados los pasos previos), cuando tal validación pasa se inicia 
     * el llamado del método generarReporte() y se espera a que finalice. Mientras ocurre este proceso habrá un Alert que 
     * bloqueará el sistema hasta que la operación finalice tanto exitosamente o falle. En cualquiera de los casos se le notificará 
     * adecuadamente al usuario con un Alert adicional.
     * @return true si el reporte ha sido generado sin problemas, y false en caso contrario.
     * @exception InterruptedException
     */
    @FXML
    public boolean buttonGenerarReporte() throws InterruptedException
    {
        if(this.pasoActual==0 || this.choiceBox_periodo.getSelectionModel().getSelectedIndex() == -1)
        {
            CommandNames.generaMensaje("Acceso Denegado", AlertType.ERROR, "No tiene permisos para accesar a esta opción",
                    "Reconfigure la conexión a la base de datos y luego intente acceder a esta opción.");
            this.actualizarEstadoPasos(0);
            return false;
        }
        Alert alertAux=CommandNames.generaMensaje("Generación de reporte en proceso", 
        Alert.AlertType.NONE, null,"Estamos generando su reporte, esta operación puede demorar dependiendo del "
            + "detalle y cantidad de datos seleccionados...",false);
        alertAux.show();
        
        ArrayList<String> columnasTabla = this.reporteBase.columnasExcel;
        if (columnasTabla == null || !generarReporte(this.reporteBase))
        {
            alertAux.getDialogPane().getButtonTypes().add(ButtonType.OK);
            alertAux.close();
            actualizarEstadoPasos(1);
            return false;
        }
        alertAux.getDialogPane().getButtonTypes().add(ButtonType.OK);
        alertAux.close();
        actualizarEstadoPasos(1);
        return true;
    }

    /**
     * Método que llama al método generarReporteBase() con la opción acorde al reporte Disponibilidad.
     * @exception InterruptedException
     */
    @FXML
    public void buttonReporteDisponibilidad() throws InterruptedException
    {
        generarReporteBase(0);
    }

    /**
     * Método que llama al método generarReporteBase() con la opción acorde al reporte Árbol Pérdidas.
     * @exception InterruptedException
     */
    @FXML
    public void buttonReporteArbolPerdidas() throws InterruptedException
    {
        generarReporteBase(1);
    }

    /**
     * Método que llama al método generarReporteBase() con la opción acorde al reporte Fuga FS.
     * @exception InterruptedException
     */
    @FXML
    public void buttonReporteFugaFS() throws InterruptedException
    {
        System.out.println("REPORTE FUGA FS - EN CONSTRUCCION");
        CommandNames.generaMensaje("Aviso de Aplicación", AlertType.INFORMATION, "Sistema de Generación de Reportes",
                "Reporte en construcción...");
        //generarReporteBase(2);
    }
    
    /**
     * Método que despliega un DirectoryChooser para seleccionar la ubicación en donde se almacenará el reporte 
     * a generar.
     */
    @FXML
    public void buttonSeleccionarDestino()
    {
        //validar datos
        //no cerrar la ventana
        DirectoryChooser dirChooser = new DirectoryChooser();
        dirChooser.setTitle("Selección de carpeta para guardar reporte");
        File selectedDir = dirChooser.showDialog(null);
        if (selectedDir != null)
        {
            setArchivoSeleccionado(selectedDir.getPath());
        }
        else
        {
            setArchivoSeleccionado(null);
        }
        this.reporteBase.setFileDir(this.textField_archivoDestino.getText());
    }

    /**
     * Método que modifica la ubicación destino del reporte a generar.
     * @param dirAux la nueva dirección destino para el reporte a generar.
     */
    private void setArchivoSeleccionado(String dirAux)
    {
        if (dirAux == null)
        {
            this.textField_archivoDestino.setText(System.getProperty("user.home")+"/Desktop/");
        }
        else
        {
            this.textField_archivoDestino.setText(dirAux);
        }
    }
    
    /**
     * Método que llama al método generarReporte() del objeto Reporte adecuado al reporte que el usuario necesite.
     * @param reporte contiene el objeto Reporte adecuado al que necesite el usuario
     * @return true si el reporte fué generado de manera exitosa, o false en caso contrario.
     * @exception InterruptedException
     */
    public boolean generarReporte(Reporte reporte) throws InterruptedException
    {
        System.out.println("obteniendo reporte...");
        
        if (!reporte.generarReporte())
        {
            System.out.println("ERROR: generar reporte MainController :C");
            return false;
        }
        return true;
    }

    /**
     * Método que invoca el despliegue de la ventana para configurar el filtro fecha para un reporte.
     * @param resource contiene el directorio que contiene el archivo FXML que genera la ventana.
     * @param title contiene el título a definir para la ventana a desplegar.
     * @exception IOException
     */
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

    /**
     * Método que invoca al despliegue de una ventana a través de un archivo FXML.
     * @param resource contiene el directorio que contiene el archivo FXML que genera la ventana.
     * @param title contiene el título a definir para la ventana a desplegar.
     * @exception IOException
     */
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

    /**
     * Método que crea una etiqueta de información para un componente en particular.
     * @param filtroAux contiene el filtro del cuál generar el texto de la etiqueta.
     * @param component contiene el componente al cuál agregar la etiqueta.
     */
    private void generarEtiqueta(Filtro filtroAux, javafx.scene.control.Control component)
    {
        component.setTooltip(new Tooltip(filtroAux.generarEtiquetaInfo()));
    }
    
    /**
     * Método que invoca el despliegue de la ventana para la importación de un archivo CSV a la base de datos.
     */
    @FXML
    public void poblarDB()
    {
        if(this.pasoActual==0)
        {
            CommandNames.generaMensaje("Acceso Denegado", AlertType.ERROR, "No tiene permisos para accesar a esta opción",
                    "Reconfigure la conexión a la base de datos y luego intente acceder a esta opción.");
            return;
        }
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
        controller.setParent(this);
        stage.setResizable(false);
        stage.showAndWait();
    }
    
    /**
     * Método que invoca el despliegue de la ventana para la importación múltiple de archivos CSV a la base de datos.
     */
    @FXML
    public void poblarDBMultiple()
    {
        if(this.pasoActual==0)
        {
            CommandNames.generaMensaje("Acceso Denegado", AlertType.ERROR, "No tiene permisos para accesar a esta opción",
                    "Reconfigure la conexión a la base de datos y luego intente acceder a esta opción.");
            return;
        }
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
        controller.setParent(this);
        stage.setResizable(false);
        stage.showAndWait();
    }
    
    /**
     * Método que invoca el despliegue de la ventana para la configuración de conexión a la base de datos.
     */
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
        controller.setParent(this);
        stage.setResizable(false);
        stage.showAndWait();
    }
    
    /**
     * Método que cierra la aplicación.
     */
    @FXML
    private void closeApp()
    {
        Platform.exit();
    }

}
