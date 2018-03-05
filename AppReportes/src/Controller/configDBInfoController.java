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
 *
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

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        this.location=location;
    }

    public void completaInfo(LocalDB db)
    {
        this.db = db;
        this.fieldText_nameDB.setText(this.db.dbConfig.dbName);
        this.fieldText_ip.setText(this.db.dbConfig.host);
        this.fieldText_port.setText(this.db.dbConfig.port);
        this.fieldText_user.setText(this.db.dbConfig.user);
        this.passField_pass.setText(this.db.dbConfig.pass);
    }

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

    @FXML
    public void buttonCancelar()
    {
        Stage s = (Stage) this.button_Cancelar.getScene().getWindow();
        s.close();
    }

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

    private boolean validarPuerto()
    {
        if (!(isNumeric(this.fieldText_port.getText())) || !(Integer.parseInt(this.fieldText_port.getText()) > 1023 
            && Integer.parseInt(this.fieldText_port.getText()) < 65536) )
            return false;
        return true;
    }

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
    
    public void setParent(MainController parent) {
        this.parent = parent ;
    }

}
