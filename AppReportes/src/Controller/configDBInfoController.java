/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.CommandNames;
import Model.DBConfig;
import Model.LocalDB;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class: 
 * Controlador encargado de la vista para realizar cambios en la configuración de conexión de la base de datos.
 * @author Patricio
 */
public class configDBInfoController implements Initializable
{
    @FXML
    private Button button_Aceptar;
    @FXML
    private Button button_Cancelar;
    @FXML
    private TextField fieldText_nameDB;
    @FXML
    private TextField fieldText_ip;
    @FXML
    private TextField fieldText_port;
    @FXML
    private TextField fieldText_user;
    @FXML
    private PasswordField passField_pass;

    public LocalDB db;
    private URL location;
    
    private MainController parent;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        this.location=location;
    }

    /**
     * Método para inicializar la ventana con la información de la base de datos que maneja la aplicación.
     * @param db objeto que contiene información de conexión a base de datos.
     */
    public void completaInfo(LocalDB db)
    {
        this.db = db;
        this.fieldText_nameDB.setText(this.db.dbConfig.dbName);
        this.fieldText_ip.setText(this.db.dbConfig.host);
        this.fieldText_port.setText(this.db.dbConfig.port);
        this.fieldText_user.setText(this.db.dbConfig.user);
        this.passField_pass.setText(this.db.dbConfig.pass);
    }

    /**
     * Método invocado desde un Button. Verifica información existente en la ventana e intenta realizar una conexión 
     * a la base de datos: si es exitosa, tal información se sobreescribe en la aplicación, en caso contrario no hace nada.
     */
    @FXML
    public void buttonAceptar()
    {
        //validar datos
        if (validarInfoIngresada())
        {
            //probar conexión
            String name=this.fieldText_nameDB.getText();
            String host=this.fieldText_ip.getText();
            String port=Integer.parseInt(this.fieldText_port.getText())+"";
            String user=this.fieldText_user.getText();
            String pass=this.passField_pass.getText();
            if(new LocalDB(new DBConfig(host, port, name, user, pass)).probarDBConection())
            {
                //copiar datos
                if(this.db.dbConfig.actualizarDatos(host,port,name,user,pass))
                    parent.actualizarEstadoPasos(1);
                else
                {
                    System.out.println("ALGO PASO, REVISAR d:");
                }
                this.buttonCancelar();
            }
        }
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
     * Método que se preocupa de validar campo por campo los valores ingresados por el usuario. Dicho método muestra un Alert 
     * al usuario cuando hayan campos no válidos, especificando cuáles.
     * @return true cuando todos los campos contienen valores válidos como información de conexión a la base de datos. 
     * Esto no significa que se conecte a la base de datos, sólo que los datos ingresados son válidos. Retorna false en caso contrario.
     */
    private boolean validarInfoIngresada()
    {
        ArrayList<String> errores = new ArrayList<>();
        if (this.fieldText_nameDB.getText().isEmpty())
            errores.add("El nombre de la db no puede ser nulo o vacío");
        if (!validarIP(this.fieldText_ip.getText()))
            errores.add("El formato de ip es en base a números entre 0 y 255 (Ej: 192.168.2.1), o en su defecto 'localhost'.");
        if(!validarPuerto())
            errores.add("El formato del puerto debe ser en base a un número entre 1024 y 65535");
        if (this.fieldText_user.getText().isEmpty())
            errores.add("El nombre de usuario no puede ser nulo o vacío");
        if (this.passField_pass.getText().isEmpty())
            errores.add("La contraseña no puede ser nula");
        if(!errores.isEmpty())
        {
            String total_errores="";
            for (int i = 0; i < errores.size(); i++)
            {
                total_errores=total_errores+(errores.get(i)+" \n");
            }
            CommandNames.generaMensaje("Error con información ingresada", 
                    Alert.AlertType.ERROR, "Error con formato de información",total_errores);
            return false;
        }
        return true;

    }
    
    /**
     * Método que se encarga de validar si un String ingresado tiene el formato de ser una IP válida.
     * @param ip contiene el String que se desea validar.
     * @return true cuando el String ingresado corresponde al formato de una IP, o false en caso contrario.
     */
    public boolean validarIP(String ip) {
        if(ip.toLowerCase().equals("localhost"))
            return true;
        Pattern pattern;
        Matcher matcher;
        String IPADDRESS_PATTERN
                = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
                + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
                + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
                + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
        pattern = Pattern.compile(IPADDRESS_PATTERN);
        matcher = pattern.matcher(ip);
        return matcher.matches();
    }

    /**
     * Método que valida si el puerto ingresado es válido (valor numérico entre 1024 y 65535).
     * @return true si el puerto es válido, false en caso contrario.
     */
    private boolean validarPuerto()
    {
        if (!(isNumeric(this.fieldText_port.getText())) || !(Integer.parseInt(this.fieldText_port.getText()) > 1023 
            && Integer.parseInt(this.fieldText_port.getText()) < 65536) )
            return false;
        return true;
    }

    /**
     * Método que valida si un String puede ser transformado en un número Entero.
     * @return si el String corresponde a un número, y false en caso contrario.
     */
    public static boolean isNumeric(String cadena)
    {
        boolean resultado;
        try
        {
            Integer.parseInt(cadena);
            resultado = true;
        }
        catch (NumberFormatException excepcion)
        {
            resultado = false;
        }
        return resultado;
    }
    
    /**
     * Método que define en una variable el controlador padre de tal ventana
     * @param parent contiene el controlador de la ventana principal
     */
    public void setParent(MainController parent) {
        this.parent = parent ;
    }

}
