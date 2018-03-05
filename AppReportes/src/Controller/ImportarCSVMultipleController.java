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
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Patricio
 */
public class ImportarCSVMultipleController implements Initializable
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
    private TextField textField_fileDir;

    private LocalDB db;
    private String directorio;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        this.button_Import.setDisable(true);
        setEstadoOperacion(STATUS_DEFAULT);
        setDirectorioSeleccionado(null);
        validarFormulario();
    }

    private void setEstadoOperacion(String info)
    {
        //STATUS_DEFAULT
        String texto = "Seleccione el directorio que contiene los archivos CSV para la importación de datos.";
        String style = "-fx-background-color: white;";
        switch (info)
        {
            case STATUS_READY:
                texto = "Directorio seleccionado para importación múltiple.";
                style = "-fx-background-color: cornflowerblue;";
                break;
            case STATUS_SUCCESS:
                texto = "Carga múltiple de archivos CSV realizado exitosamente.";
                style = "-fx-background-color: white;";
                break;
            case STATUS_RUNNING:
                texto = "Ejecutando importación múltiple de archivos CSV...";
                style = "-fx-background-color: cornflowerblue;";
                break;
            case STATUS_ERROR:
                texto = "Hubo un problema en la carga simultánea de archivos CSV.";
                style = "-fx-background-color: orange;";
                break;
        }
        this.pane_status.setStyle(style);
        this.text_status.setText(texto);
    }

    public boolean validarFormulario()
    {
        if (this.textField_fileDir.getText().equals(CSV_NON_SELECTED) || this.directorio == null)
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
        this.directorio=null;
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
                
                Alert alertAux=CommandNames.generaMensaje("Importación múltiple en proceso", 
                        Alert.AlertType.NONE, null,"Importación múltiple en proceso, esta operación puede demorar mucho...",false);
                
                alertAux.show();
                if(procesarMultiplesArchivos(this.directorio))
                {
                    setEstadoOperacion(STATUS_SUCCESS);
                }
                else
                {
                    setEstadoOperacion(STATUS_ERROR);
                }
                alertAux.getDialogPane().getButtonTypes().add(ButtonType.OK);
                alertAux.close();
                limpiarTablero();
                //validar datos
                //no cerrar la ventana
            }
        });
        
    }
    
    private void setDirectorioSeleccionado(String dirAux)
    {
        if (dirAux == null)
        {
            this.directorio = "";
            this.textField_fileDir.setText(CSV_NON_SELECTED);
        }
        else
        {
            this.directorio = dirAux;
            this.textField_fileDir.setText(dirAux);
        }
    }
    
    @FXML
    public void seleccionarDirectorioCSV()
    {
        //validar datos
        //no cerrar la ventana
        DirectoryChooser dirChooser = new DirectoryChooser();
        dirChooser.setTitle("Selección de carpeta para importación múltiple de datos");
        File selectedDir = dirChooser.showDialog(null);

        if (selectedDir != null)
        {
            setDirectorioSeleccionado(selectedDir.getPath());
        }
        else
        {
            setDirectorioSeleccionado(null);
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
    
    public boolean procesarMultiplesArchivos(String directorio)
    {
        HashMap<String,CSVImport> csvEncontrados = new HashMap<>();
        
        String[] aux = new File(directorio).list();
        HashMap<String,String> files=new HashMap<>();
        for (int i = 0; aux!=null && i < aux.length; i++)
        {
            System.out.println("posible: "+aux[i].substring(aux[i].length()-4));
            if(aux[i].contains(".csv"))
            {
                System.out.println("F:"+directorio+"/"+aux[i]);
                files.put(aux[i].replace(".csv", ""),aux[i]);
            }
        }
        if(files==null || files.isEmpty())
        {
            CommandNames.generaMensaje("Problemas con directorio seleccionado", Alert.AlertType.ERROR, "No existen archivos", 
                "El directorio seleccionado no contiene archivos válidos para la importación.");
            return false;
        }
        System.out.println("keys: "+files.keySet());
        
        /*
        if(files.containsKey("region"))
            new CSVImport_Region(this.db, directorio+"/"+files.get("region"),
                "regiones").procesarArchivo();
        if(files.containsKey("centro"))
            new CSVImport_Centro(this.db, directorio+"/"+files.get("centro"),
                "centro").procesarArchivo();
        if(files.containsKey("zonaventas"))
            new CSVImport_ZonaVentas(this.db, directorio+"/"+files.get("zonaventas"),
                "zonaventas").procesarArchivo();
        if(files.containsKey("sector"))
            new CSVImport_Sector(this.db, directorio+"/"+files.get("sector"),
                "sector").procesarArchivo();
        if(files.containsKey("n2"))
            new CSVImport_N2(this.db, directorio+"/"+files.get("n2"),
                "n2").procesarArchivo();
        if(files.containsKey("n3"))
            new CSVImport_N3(this.db, directorio+"/"+files.get("n3"),
                "n3").procesarArchivo();
        if(files.containsKey("n4"))
            new CSVImport_N4(this.db, directorio+"/"+files.get("n4"),
                "n4").procesarArchivo();
        if(files.containsKey("tipocliente"))
            new CSVImport_TipoCliente(this.db, directorio+"/"+files.get("tipocliente"),
                "tipocliente").procesarArchivo();
        if(files.containsKey("categoriacliente"))
            new CSVImport_CategoriaCliente(this.db, directorio+"/"+files.get("categoriacliente"),
                "categoriacliente").procesarArchivo();
        if(files.containsKey("subcategoriacliente"))
            new CSVImport_SubcategoriaCliente(this.db, directorio+"/"+files.get("subcategoriacliente"),
                "subcategoriacliente").procesarArchivo();
        if(files.containsKey("marca"))
            new CSVImport_Marca(this.db, directorio+"/"+files.get("marca"),
                "marca").procesarArchivo();
        if(files.containsKey("agrupado"))
            new CSVImport_Agrupado(this.db, directorio+"/"+files.get("agrupado"),
                "agrupado").procesarArchivo();
        if(files.containsKey("tipoenvasado"))
            new CSVImport_TipoEnvasado(this.db, directorio+"/"+files.get("tipoenvasado"),
                "tipoenvasado").procesarArchivo();
        if(files.containsKey("estadorefrigerado"))
            new CSVImport_EstadoRefrigerado(this.db, directorio+"/"+files.get("estadorefrigerado"),
                "estadorefrigerado").procesarArchivo();
        if(files.containsKey("oficinaventas"))
            new CSVImport_OficinaVentas(this.db, directorio+"/"+files.get("oficinaventas"),
                "oficinaventas").procesarArchivo();
        if(files.containsKey("material"))
            new CSVImport_Material(this.db, directorio+"/"+files.get("material"),
                "material").procesarArchivo();
        if(files.containsKey("cliente"))
            new CSVImport_Cliente(this.db, directorio+"/"+files.get("cliente"),
                "cliente").procesarArchivo();
        if(files.containsKey("clientelocal"))
            new CSVImport_ClienteLocal(this.db, directorio+"/"+files.get("clientelocal"),
                "clientelocal").procesarArchivo();
        if(files.containsKey("pedido"))
            new CSVImport_Pedido(this.db, directorio+"/"+files.get("pedido"),
                "pedido").procesarArchivo();
        if(files.containsKey("pedido_material"))
            new CSVImport_PedidoMaterial(this.db, directorio+"/"+files.get("pedido_material"),
                "pedido_material").procesarArchivo();
        if(files.containsKey("stock"))
            new CSVImport_Stock(this.db, directorio+"/"+files.get("stock"),
                "stock").procesarArchivo();
        if(files.containsKey("despacho_faltante"))
            new CSVImport_DespachoFaltante(this.db, directorio+"/"+files.get("despacho_faltante"),
                "despacho_faltante").procesarArchivo();
        if(files.containsKey("ns_cliente"))
            new CSVImport_NSCliente(this.db, directorio+"/"+files.get("ns_cliente"),
                "ns_cliente").procesarArchivo();
        if(files.containsKey("facturaVentas"))
            new CSVImport_FacturaVenta(this.db, directorio+"/"+files.get("facturaVentas"),
                "facturaVentas").procesarArchivo();
        if(files.containsKey("facturaVentas_material"))
            new CSVImport_FacturaVentaMaterial(this.db, directorio+"/"+files.get("facturaVentas_material"),
                "facturaVentas_material").procesarArchivo();
        */
        return true;
    }
}
