/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.CSVImport.*;
import Model.CommandNames;
import Model.LocalDB;
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
 * FXML Controller class: 
 * Controlador encargado de la vista para realizar importación de datos
 *  para una tabla de la base de datos.
 * @author Patricio
 * 
 */
public class ImportarCSVController implements Initializable
{

    public static final String STATUS_READY = "READY";
    public static final String STATUS_SUCCESS = "SUCCESS";
    public static final String STATUS_RUNNING = "RUNNING";
    public static final String STATUS_ERROR = "ERROR";
    public static final String STATUS_DEFAULT = "DEFAULT";
    public static final String CSV_NON_SELECTED = "No ha seleccionado CSV";

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
    private MainController parent;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        this.csvImport = null;
        inicializarTablasDestino();
        this.button_Import.setDisable(true);
        setEstadoOperacion(STATUS_DEFAULT);
        setFileSeleccionado(null, null);
        validarFormulario();
    }
    
    /**
     * Método para modificar variables que contienen la ubicación del archivo CSV seleccionado, 
     * o NULL en caso que no se haya definido.
     * @param dirAux contiene la dirección del archivo CSV seleccionado
     * @param fileName contiene el nombre del archivo CSV seleccionado
     */
    private void setFileSeleccionado(String dirAux, String fileName)
    {
        if (dirAux == null)
        {
            this.fileDir = "";
            this.textField_fileDir.setText(CSV_NON_SELECTED);
        }
        else
        {
            this.fileDir = dirAux;
            this.textField_fileDir.setText(fileName);
        }
    }

    /**
     * Método para actualizar el mensaje de estado del sistema en el proceso de carga de un archivo CSV 
     * a la base de datos.
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
            case STATUS_ERROR:
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
     * Método para inicializar listado de tablas de la base de datos.
     */
    public void inicializarTablasDestino()
    {
        ArrayList<String> nombreTablas = new ArrayList<>();
        nombreTablas.add("Agrupado");
        nombreTablas.add("Categoría Cliente");
        nombreTablas.add("Centro");
        nombreTablas.add("Cliente");
        nombreTablas.add("Cliente-Local");
        nombreTablas.add("Club Cliente");
        nombreTablas.add("Despacho-Faltante");
        nombreTablas.add("Estado Refrigerado");
        nombreTablas.add("FacturaVenta");
        nombreTablas.add("FacturaVentas-Material");
        nombreTablas.add("Marca");
        nombreTablas.add("Material");
        nombreTablas.add("N2");
        nombreTablas.add("N3");
        nombreTablas.add("N4");
        nombreTablas.add("NS Cliente");
        nombreTablas.add("Oficina de Ventas");
        nombreTablas.add("Pedido");
        nombreTablas.add("Pedido-Material");
        nombreTablas.add("Región");
        nombreTablas.add("Sector");
        nombreTablas.add("Stock");
        nombreTablas.add("Subcategoría Cliente");
        nombreTablas.add("Tipo de Cliente");
        nombreTablas.add("Tipo Envasado");
        nombreTablas.add("Zona de Ventas");
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
                    setSelectionTable(newValue.intValue());
                }
            }
        });
        this.choiceBox_TablasDestino.setPrefWidth(300);
        this.gp.add(this.choiceBox_TablasDestino, 3, 1);
    }

    /**
     * Método para actuar en conjunto con el listado de tablas de la base de datos. Cuando se seleccione 
     * una tabla, la aplicación generará una instancia del objeto CSVIMport acorde a la tabla seleciconada 
     * para la importación de datos.
     */
    private void setSelectionTable(int value)
    {
        switch (this.choiceBox_TablasDestino.getItems().get(value).toString())
        {
            case "Agrupado"://agrupado
                this.csvImport = new CSVImport_Agrupado(this.db, "", "");
                break;
            case "Categoría Cliente"://categoriaCliente
                this.csvImport = new CSVImport_CategoriaCliente(this.db, "", "");
                break;
            case "Centro"://centro
                this.csvImport = new CSVImport_Centro(this.db, "", "");
                break;
            case "Cliente"://cliente
                this.csvImport = new CSVImport_Cliente(this.db, "", "");
                break;
            case "Cliente-Local"://cliente-local
                this.csvImport = new CSVImport_ClienteLocal(this.db, "", "");
                break;
            case "Club Cliente"://clubCliente
                //    this.csvImport = new CSVImport_ClubCliente(this.db, "", "");
                System.out.println("OJO, OPCION NO IMPLEMENTADA");
                this.csvImport = null;
                break;
            case "Despacho-Faltante"://despacho-faltante
                this.csvImport = new CSVImport_DespachoFaltante(this.db, "", "");
                break;
            case "Estado Refrigerado"://estadoRefrigerado
                this.csvImport = new CSVImport_EstadoRefrigerado(this.db, "", "");
                break;
            case "FacturaVenta"://facturaVenta
                this.csvImport = new CSVImport_FacturaVenta(this.db, "", "");
                break;
            case "FacturaVentas-Material"://facturaVenta-material
                this.csvImport = new CSVImport_FacturaVentaMaterial(this.db, "", "");
                break;
            case "Marca"://marca
                this.csvImport = new CSVImport_Marca(this.db, "", "");
                break;
            case "Material"://material
                this.csvImport = new CSVImport_Material(this.db, "", "");
                break;
            case "N2"://n2
                this.csvImport = new CSVImport_N2(this.db, "", "");
                break;
            case "N3"://n3
                this.csvImport = new CSVImport_N3(this.db, "", "");
                break;
            case "N4"://n4
                this.csvImport = new CSVImport_N4(this.db, "", "");
                break;
            case "NS Cliente"://ns cliente
                this.csvImport = new CSVImport_NSCliente(this.db, "", "");
                break;
            case "Oficina de Ventas"://oficinaVentas
                this.csvImport = new CSVImport_OficinaVentas(this.db, "", "");
                break;
            case "Pedido"://pedido
                this.csvImport = new CSVImport_Pedido(this.db, "", "");
                break;
            case "Pedido-Material"://pedido-material
                this.csvImport = new CSVImport_PedidoMaterial(this.db, "", "");
                break;
            case "Región"://region
                this.csvImport = new CSVImport_Region(this.db, "", "");
                break;
            case "Sector"://sector
                this.csvImport = new CSVImport_Sector(this.db, "", "");
                break;
            case "Stock"://stock
                this.csvImport = new CSVImport_Stock(this.db, "", "");
                break;
            case "Subcategoría Cliente"://subcategoriaCliente
                this.csvImport = new CSVImport_SubcategoriaCliente(this.db, "", "");
                break;
            case "Tipo de Cliente"://tipo cliente
                this.csvImport = new CSVImport_TipoCliente(this.db, "", "");
                break;
            case "Tipo Envasado"://tipoEnvasado
                this.csvImport = new CSVImport_TipoEnvasado(this.db, "", "");
                break;
            case "Zona de Ventas"://zonaVentas
                this.csvImport = new CSVImport_ZonaVentas(this.db, "", "");
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
     * Método para verificar que se haya seleccionado un archivo CSV y que se haya definido la tabla destino de 
     * la importación.
     * @return true cuando las condiciones previamente descritas se cumplen, o false en caso contrario.
     */
    public boolean validarFormulario()
    {
        if (this.textField_fileDir.getText().equals(CSV_NON_SELECTED)
                || this.choiceBox_TablasDestino.getSelectionModel().getSelectedIndex() == -1
                || this.fileDir == null || this.csvImport == null)
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
     * Método para vaciar variables que contengan direcciones de archivos CSV y modificar la selección de tabla 
     * a la opción por defecto.
     */
    public void limpiarTablero()
    {
        this.csvImport = null;
        this.fileDir = null;
        this.fileName = null;
        this.choiceBox_TablasDestino.getSelectionModel().select(-1);
        this.textField_fileDir.setText(CSV_NON_SELECTED);
        this.button_Import.setDisable(true);
    }

    /**
     * Método que se encarga de iniciar la importación del archivo CSV. Se despliega un Alert al usuario para confirmar la acción. 
     * Si el usuario confirma, se formatea la dirección del archivo CSV para posteriormente llamar al método procesarArchivo() 
     * ubicado dentro de la instancia CSVImport para que realice la carga de información a la base de datos en la tabla correspondiente.
     * Una vez finalizado el proceso se llama al método limpiarTablero() para inicializar la ventana. Dentro de todo este proceso 
     * se ejecuta el método setEstadoOperación() para actualizar el mensaje de estado en todo momento.
     */
    @FXML
    public void importarCSV()
    {                
        Alert alert=CommandNames.generaMensaje("Aviso de confirmación", Alert.AlertType.CONFIRMATION, "¿Está seguro de la acción a realizar?", 
                "Considere verificar el archivo CSV seleccionado y la tabla tal que, los datos del archivo coincidan con los campos a completar en "
                + "la tabla seleccionada de la Base de Datos. Se realizará una verificación simple por lo cuál si no está seguro de su"
                + "selección, verifique y vuelva a intentarlo.",false);
        alert.showAndWait().ifPresent(response ->
        {
            if (response == ButtonType.OK)
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
                    setEstadoOperacion(STATUS_ERROR);

                alertAux.getDialogPane().getButtonTypes().add(ButtonType.OK);
                alertAux.close();
                
                limpiarTablero();
            }
        });
    }

    /**
     * Método que despliega un FileChooser para seleccionar la ubicación del archivo CSV a cargar.
     */
    @FXML
    public void seleccionarArchivoCSV()
    {
        //validar datos
        //no cerrar la ventana
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Selección de archivo CSV para importación de datos");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos CSV", "*.csv"));
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
