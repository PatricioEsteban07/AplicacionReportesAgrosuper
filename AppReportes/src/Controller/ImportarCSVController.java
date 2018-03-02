/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.CSVImport.CSVImport_Cliente;
import Model.CSVImport.CSVImport_Region;
import Model.CSVImport.CSVImport_N2;
import Model.CSVImport.CSVImport_ZonaVentas;
import Model.CSVImport.CSVImport_N3;
import Model.CSVImport.CSVImport_Sector;
import Model.CSVImport.CSVImport_Marca;
import Model.CSVImport.CSVImport_CategoriaCliente;
import Model.CSVImport.CSVImport_PedidoMaterial;
import Model.CSVImport.CSVImport_Pedido;
import Model.CSVImport.CSVImport_TipoEnvasado;
import Model.CSVImport.CSVImport_Despacho;
import Model.CSVImport.CSVImport;
import Model.CSVImport.CSVImport_SubcategoriaCliente;
import Model.CSVImport.CSVImport_N4;
import Model.CSVImport.CSVImport_Stock;
import Model.CSVImport.CSVImport_Faltante;
import Model.CSVImport.CSVImport_Material;
import Model.CSVImport.CSVImport_NSCliente;
import Model.CSVImport.CSVImport_TipoCliente;
import Model.CSVImport.CSVImport_EstadoRefrigerado;
import Model.CSVImport.CSVImport_DespachoMaterial;
import Model.CSVImport.CSVImport_Agrupado;
import Model.CSVImport.CSVImport_OficinaVentas;
import Model.CSVImport.CSVImport_Centro;
import Model.CSVImport.CSVImport_FacturaVentaMaterial;
import Model.CSVImport.CSVImport_ClienteLocal;
import Model.CSVImport.CSVImport_FacturaVenta;
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
 * FXML Controller class
 *
 * @author Patricio
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

    public void limpiarTablero()
    {
        this.csvImport = null;
        this.fileDir = null;
        this.fileName = null;
        this.choiceBox_TablasDestino.getSelectionModel().select(-1);
        this.textField_fileDir.setText(CSV_NON_SELECTED);
        this.button_Import.setDisable(true);
    }

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
                //validar datos
                //no cerrar la ventana
            }
        });
    }

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
}