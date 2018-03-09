/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.CSVImport.*;
import Model.CommandNames;
import Model.LocalDB;
import Model.ProcesadoresExcel.*;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * [CLASE EN OCNSTRUCCION] FXML Controller class: 
 * Controlador encargado de la vista para realizar tratamiento de archivos Excel, generación de archivos CSV de éste y 
 * la posterior carga de datos a la base de datos (En construcción).
 * @author Patricio
 */
public class ImportarExcelController implements Initializable
{

    public static final String STATUS_READY = "READY";
    public static final String STATUS_SUCCESS = "SUCCESS";
    public static final String STATUS_RUNNING = "RUNNING";
    public static final String STATUS_ERROR_CSV = "CSV ERROR";
    public static final String STATUS_ERROR_EXCEL = "EXCEL ERROR";
    public static final String STATUS_DEFAULT = "DEFAULT";
    public static final String EXCEL_NON_SELECTED = "No ha seleccionado archivo Excel";

    @FXML
    private Button button_Import;
    @FXML
    private Button button_Cancelar;
    @FXML
    private Button button_selectFile;
    @FXML
    private Text text_status;
    @FXML
    private Pane pane_status;
    @FXML
    private ChoiceBox choiceBox_TablasDestino;
    @FXML
    private TextField textField_fileDir;
    @FXML
    private GridPane gp;

    private LocalDB db;
    private String fileDir;
    private String fileName;
    private CSVImport csvImport;
    private ProcesadorExcel excelProcesador;
    private MainController parent;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        this.excelProcesador = null;
        this.csvImport = null;
        inicializarTablasDestino();
        this.button_Import.setDisable(true);
        setEstadoOperacion(STATUS_DEFAULT);
        setFileSeleccionado(null, null);
        validarFormulario();
    }
    
    /**
     * Método para modificar variables que contienen la ubicación del archivo Excel seleccionado, 
     * o NULL en caso que no se haya definido.
     * @param dirAux contiene la dirección del archivo Excel seleccionado
     * @param fileName contiene el nombre del archivo Excel seleccionado
     */
    private void setFileSeleccionado(String dirAux, String fileName)
    {
        if (dirAux == null)
        {
            this.fileDir = "";
            this.textField_fileDir.setText(EXCEL_NON_SELECTED);
        }
        else
        {
            this.fileDir = dirAux;
            this.textField_fileDir.setText(fileName);
        }
    }

    /**
     * [METODO EN CONSTRUCCION] Método para actualizar el mensaje de estado del sistema en el proceso de 
     * procesamiento de un archivo Excel y carga de datos a la base de datos.
     * @param info contiene una cadena de texto que, dependiendo de su contenido, actualiza el mensaje de estado de 
     * la ventana asociada: STATUS_READY para definir que se puede iniciar la importación, STSTUS_SUCCESS para definir que 
     * la importación fué exitosa, STATUS_RUNNING para definir que el proceso de importación está en ejecución, y 
     * STATUS_ERROR para definir que hubo un problema al momento de realizar la importación y que se ha cancelado.
     */
    public boolean setEstadoOperacion(String info)
    {
        //STATUS_DEFAULT
        String texto = "Seleccione un archivo CSV y la tabla a la cuál importar los datos.";
        String style = "-fx-background-color: white;";
        switch (info)
        {
            case STATUS_READY:
                texto = "Archivo y tabla seleccionada para importación.";
                style = "-fx-background-color: cornflowerblue;";
                break;
            case STATUS_SUCCESS:
                texto = "Carga de archivo CSV realizado exitosamente.";
                style = "-fx-background-color: white;";
                break;
            case STATUS_RUNNING:
                texto = "Ejecutando importación de archivo CSV...";
                style = "-fx-background-color: cornflowerblue;";
                break;
            case STATUS_ERROR_EXCEL:
                texto = "No se ha procesado el archivo Excel debido a un error con el archivo.";
                style = "-fx-background-color: orange;";
                break;
            case STATUS_ERROR_CSV:
                texto = "No se ha cargado el archivo CSV debido a un error con el archivo.";
                style = "-fx-background-color: orange;";
                break;
        }
        this.pane_status.setStyle(style);
        this.text_status.setText(texto);
        this.pane_status.applyCss();
        return true;
    }

    /**
     * [METODO EN CONSTRUCCION] Método para inicializar listado de tablas de la base de datos.
     */
    public void inicializarTablasDestino()
    {
        ArrayList<String> nombreTablas = new ArrayList<>();
        nombreTablas.add("Agrupado");
        nombreTablas.add("Categoría Cliente");
        nombreTablas.add("clubCliente");
        ObservableList<String> items = FXCollections.observableArrayList();
        items.addAll(nombreTablas);
        this.choiceBox_TablasDestino = new ChoiceBox();
        this.choiceBox_TablasDestino.setItems(items);
        this.choiceBox_TablasDestino.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>()
        {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue)
            {
                if (newValue.intValue() != -1)
                {
                    System.out.println("TS: " + newValue);
                    setSelectionTable(newValue.intValue());
                }
            }
        });
        this.choiceBox_TablasDestino.setPrefWidth(300);
        this.gp.add(this.choiceBox_TablasDestino, 3, 1);
    }

    /**
     * [METODO EN CONSTRUCCION] Método para actuar en conjunto con el listado de archivos Excel dispuestos a procesar. Cuando se seleccione 
     * un archivo Excel, la aplicación generará una instancia del objeto ProcesadorExcel acorde a la opción seleciconada 
     * para el procesamiento e importación de datos.
     */
    private void setSelectionTable(int value)
    {
        switch (value)
        {
            case 0://agrupado
                this.csvImport = new CSVImport_Agrupado(this.db, "", "");
                break;
            case 1://categoriaCliente
                this.csvImport = new CSVImport_CategoriaCliente(this.db, "", "");
                break;
            case 2://clubCliente
                //    this.csvImport = new CSVImport_ClubCliente(this.db, "", "");
                System.out.println("OJO, OPCION NO IMPLEMENTADA");
                this.csvImport = null;
                break;
            default://no reconocido o -1
                System.out.println("OPCION NO RECONOCIDA O -1");
                this.csvImport = null;
                break;
        }
        if (this.csvImport == null)
        {
            this.choiceBox_TablasDestino.getSelectionModel().select(-1);
            CommandNames.generaMensaje("Problema de Selección", Alert.AlertType.INFORMATION, "Problemas con selección de tabla",
                    "Lo lamentamos, esta tabla no está disponible para poblarla con datos.");
        }
        validarFormulario();
    }

    /**
     * Método para verificar que se haya seleccionado un archivo Excel.
     * @return true cuando las condiciones previamente descritas se cumplen, o false en caso contrario.
     */
    public boolean validarFormulario()
    {
        if (this.textField_fileDir.getText().equals(EXCEL_NON_SELECTED)
                || this.choiceBox_TablasDestino.getSelectionModel().getSelectedIndex() == -1
                || this.fileDir == null || this.excelProcesador == null)
        {
            setEstadoOperacion(STATUS_DEFAULT);
            this.button_Import.setDisable(true);
            return false;
        }
        setEstadoOperacion(STATUS_READY);
        this.button_Import.setDisable(false);
        return true;
    }

    /**
     * Método para vaciar variables que contengan dirección del archivo Excel.
     */
    public void limpiarTablero()
    {
        this.csvImport = null;
        this.fileDir = null;
        this.fileName = null;
        this.choiceBox_TablasDestino.getSelectionModel().select(-1);
        this.textField_fileDir.setText(EXCEL_NON_SELECTED);
        this.button_Import.setDisable(true);
    }

    
    /**
     * [METODO EN CONSTRUCCION] Método que en base a un archivo Excel genera archivos CSV para su posterior carga a la base de datos. 
     * Se despliega un Alert al usuario para confirmar la acción y si el usuario confirma, se inicia el procesamiento del archivo Excel, 
     * éste genera archivos CSV los cuáles e cargan a las tablas adecuadas de la base de datos.
     */
    @FXML
    public void procesarExcel()
    {
        Alert alert=CommandNames.generaMensaje("Aviso de confirmación", Alert.AlertType.CONFIRMATION, "¿Está seguro de la acción a realizar?", 
            "Considere verificar el archivo Excel seleccionado y la tabla tal que, los datos del archivo posean el formato y estructura"
            + "adecuados para que la aplicación logre extraer lo necesario de manera exitosa. Cualquier diferencia con el formato"
            + " definido podría provocar extracción errónea de información o la detención de dicha operación.",false);
             
        alert.showAndWait().ifPresent(response ->
        {  
            if (response == ButtonType.OK)
            {
                //realizar procesamiento de excel
                
                if(excelProcesador.generarCSV())
                    setEstadoOperacion(STATUS_SUCCESS);
                else
                    setEstadoOperacion(STATUS_ERROR_EXCEL);
                //realizar importación de CSV una vez el paso anterior resulte en éxito
            }
        });
    
    }
    
    /**
     * [METODO EN CONSTRUCCION] Método que se encarga de iniciar la importación del archivo CSV. Se despliega un Alert al usuario para confirmar la acción. 
     * Si el usuario confirma, se formatea la dirección del archivo CSV para posteriormente llamar al método procesarArchivo() 
     * ubicado dentro de la instancia CSVImport para que realice la carga de información a la base de datos en la tabla correspondiente.
     * Una vez finalizado el proceso se llama al método limpiarTablero() para inicializar la ventana. Dentro de todo este proceso 
     * se ejecuta el método setEstadoOperación() para actualizar el mensaje de estado en todo momento.
     * @return true si la importación fué exitosa, o false en caso contrario.
     */
    public boolean importarCSV()
    {       
        System.out.println("paso en buena");
        setEstadoOperacion(STATUS_RUNNING);

        this.csvImport.fileName = this.fileName;
        this.csvImport.fileDir = this.fileDir.replaceAll("\\\\", "/");

        Alert alertAux=CommandNames.generaMensaje("Importación en proceso", 
                Alert.AlertType.NONE, null,"Importando archivo CSV en tabla, espere un momento...",false);

        alertAux.show();

        if(csvImport.procesarArchivo())
            setEstadoOperacion(STATUS_SUCCESS);
        else
            setEstadoOperacion(STATUS_ERROR_CSV);

        alertAux.getDialogPane().getButtonTypes().add(ButtonType.OK);
        alertAux.close();

        limpiarTablero();
        
        return true;
    }

    /**
     * Método que despliega un FileChooser para seleccionar la ubicación del archivo Excel a tratar.
     */
    @FXML
    public void seleccionarArchivoExcel()
    {
        //validar datos
        //no cerrar la ventana
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Selección de archivo Excel para importación de datos");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos Excel", "*.xlsx"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos Excel 97-2003", "*.xls"));
        File selectedFile = fileChooser.showOpenDialog(null);
        
        if (selectedFile != null)
        {
            setFileSeleccionado(selectedFile.getPath(), selectedFile.getName());
        }
        else
        {
            setFileSeleccionado(null, null);
        }
        validarFormulario();
    }

    /**
     * Método que cierra la ventana asociada al controlador.
     */
    @FXML
    public void buttonCancelar()
    {
        Stage s = (Stage) this.button_Cancelar.getScene().getWindow();
        s.close();
    }

    /**
     * Método que guarda en una variable la instancia de la configuracion de la base de datos del sistema.
     * @param db contiene la información de la conexión a la base de datos.
     */
    public void setDB(LocalDB db)
    {
        this.db = db;
    }
    
    /**
     * Método que define en una variable el controlador padre de tal ventana
     * @param parent contiene el controlador de la ventana principal
     */
    public void setParent(MainController parent) {
        this.parent = parent ;
    }
}
