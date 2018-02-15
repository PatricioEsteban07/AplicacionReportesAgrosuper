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
    private TextField fieldText_ip1;
    @FXML
    private TextField fieldText_ip2;
    @FXML
    private TextField fieldText_ip3;
    @FXML
    private TextField fieldText_ip4;
    @FXML
    private TextField fieldText_port;
    @FXML
    private TextField fieldText_user;
    @FXML
    private PasswordField passField_pass;

    public LocalDB db;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
    }

    public void completaInfo(LocalDB db)
    {
        this.db = db;
        this.fieldText_nameDB.setText(this.db.dbConfig.dbName);
        String[] ips = this.db.dbConfig.host.split("\\.");
        this.fieldText_ip1.setText(ips[0]);
        this.fieldText_ip2.setText(ips[1]);
        this.fieldText_ip3.setText(ips[2]);
        this.fieldText_ip4.setText(ips[3]);
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
            String host=Integer.parseInt(this.fieldText_ip1.getText())+"."+Integer.parseInt(this.fieldText_ip2.getText())+"."
                    +Integer.parseInt(this.fieldText_ip3.getText())+"."+Integer.parseInt(this.fieldText_ip4.getText());
            String port=Integer.parseInt(this.fieldText_ip1.getText())+"";
            String user=this.fieldText_user.getText();
            String pass=this.passField_pass.getText();
            if(new LocalDB(new DBConfig(host, port, name, user, pass)).probarDBConection())
            {
                //copiar datos
                this.buttonCancelar();
            }
        }

        //probar datos nuevo
        //guardar datos
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
        if (!validarIP())
            errores.add("El formato de ip es en base a números entre 0 y 255 (Ej: 192.168.2.1)");
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

    private boolean validarIP()
    {
        if (!(isNumeric(this.fieldText_ip1.getText())) || !(isNumeric(this.fieldText_ip2.getText()))
                || !(isNumeric(this.fieldText_ip3.getText())) || !(isNumeric(this.fieldText_ip4.getText())) 
                || !(Integer.parseInt(this.fieldText_ip1.getText()) > 0 && Integer.parseInt(this.fieldText_ip1.getText()) < 256)
                || !(Integer.parseInt(this.fieldText_ip2.getText()) > 0 && Integer.parseInt(this.fieldText_ip2.getText()) < 256)
                || !(Integer.parseInt(this.fieldText_ip3.getText()) > 0 && Integer.parseInt(this.fieldText_ip3.getText()) < 256)
                || !(Integer.parseInt(this.fieldText_ip4.getText()) > 0 && Integer.parseInt(this.fieldText_ip4.getText()) < 256))
        {
            return false;
        }
        return true;
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

}
