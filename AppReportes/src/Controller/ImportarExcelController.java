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
 * FXML Controller class
 *
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

    public void inicializarTablasDestino()
    {
        ArrayList<String> nombreTablas = new ArrayList<>();
        nombreTablas.add("Agrupado");
        nombreTablas.add("Categoría Cliente");
        nombreTablas.add("Centro");
        nombreTablas.add("Cliente");
        nombreTablas.add("Cliente-Local");
        nombreTablas.add("Club Cliente");
        nombreTablas.add("Despacho");
        nombreTablas.add("Despacho-Material");
        nombreTablas.add("Estado Refrigerado");
        nombreTablas.add("FacturaVenta");
        nombreTablas.add("FacturaVentas-Material");
        nombreTablas.add("Faltante");
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
                    System.out.println("TS: " + newValue);
                    setSelectionTable(newValue.intValue());
                }
            }
        });
        this.choiceBox_TablasDestino.setPrefWidth(300);
        this.gp.add(this.choiceBox_TablasDestino, 3, 1);
    }

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
            case 2://centro
                this.csvImport = new CSVImport_Centro(this.db, "", "");
                break;
            case 3://cliente
                this.csvImport = new CSVImport_Cliente(this.db, "", "");
                break;
            case 4://cliente-local
                this.csvImport = new CSVImport_ClienteLocal(this.db, "", "");
                break;
            case 5://clubCliente
                //    this.csvImport = new CSVImport_ClubCliente(this.db, "", "");
                System.out.println("OJO, OPCION NO IMPLEMENTADA");
                this.csvImport = null;
                break;
            case 6://despacho
                this.csvImport = new CSVImport_Despacho(this.db, "", "");
                break;
            case 7://despacho-material
                this.csvImport = new CSVImport_DespachoMaterial(this.db, "", "");
                break;
            case 8://estadoRefrigerado
                this.csvImport = new CSVImport_EstadoRefrigerado(this.db, "", "");
                break;
            case 9://facturaVenta
                this.csvImport = new CSVImport_FacturaVenta(this.db, "", "");
                break;
            case 10://facturaVenta-material
                this.csvImport = new CSVImport_FacturaVentaMaterial(this.db, "", "");
                break;
            case 11://faltante
                this.csvImport = new CSVImport_Faltante(this.db, "", "");
                break;
            case 12://marca
                this.csvImport = new CSVImport_Marca(this.db, "", "");
                break;
            case 13://material
                this.csvImport = new CSVImport_Material(this.db, "", "");
                break;
            case 14://n2
                this.csvImport = new CSVImport_N2(this.db, "", "");
                break;
            case 15://n3
                this.csvImport = new CSVImport_N3(this.db, "", "");
                break;
            case 16://n4
                this.csvImport = new CSVImport_N4(this.db, "", "");
                break;
            case 17://ns cliente
                this.csvImport = new CSVImport_NSCliente(this.db, "", "");
                break;
            case 18://oficinaVentas
                this.csvImport = new CSVImport_OficinaVentas(this.db, "", "");
                break;
            case 19://pedido
                this.csvImport = new CSVImport_Pedido(this.db, "", "");
                break;
            case 20://pedido-material
                this.csvImport = new CSVImport_PedidoMaterial(this.db, "", "");
                break;
            case 21://region
                this.csvImport = new CSVImport_Region(this.db, "", "");
                break;
            case 22://sector
                this.csvImport = new CSVImport_Sector(this.db, "", "");
                break;
            case 23://stock
                this.csvImport = new CSVImport_Stock(this.db, "", "");
                break;
            case 24://subcategoriaCliente
                this.csvImport = new CSVImport_SubcategoriaCliente(this.db, "", "");
                break;
            case 25://tipo cliente
                this.csvImport = new CSVImport_TipoCliente(this.db, "", "");
                break;
            case 26://tipoEnvasado
                this.csvImport = new CSVImport_TipoEnvasado(this.db, "", "");
                break;
            case 27://zonaVentas
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

    public void limpiarTablero()
    {
        this.csvImport = null;
        this.fileDir = null;
        this.fileName = null;
        this.choiceBox_TablasDestino.getSelectionModel().select(-1);
        this.textField_fileDir.setText(EXCEL_NON_SELECTED);
        this.button_Import.setDisable(true);
    }

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

    @FXML
    public void seleccionarArchivoCSV()
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

    @FXML
    public void buttonCancelar()
    {
        Stage s = (Stage) this.button_Cancelar.getScene().getWindow();
        s.close();
    }

    public void setDB(LocalDB db)
    {
        this.db = db;
    }
    
    public void setParent(MainController parent) {
        this.parent = parent ;
    }
}