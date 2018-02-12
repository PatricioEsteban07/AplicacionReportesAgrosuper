/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 *
 * @author Patricio
 */
public final class CommandNames
{        
    public final static String URL_CONNECT_DB="jdbc:mysql://localhost:3306/db_app_agrosuper3";
    
    public final static String ESTADO_SUCCESS="Éxito";
    public final static String ESTADO_INFO="Información";
    public final static String ESTADO_ERROR="Error";
    
    public final static String MSG_SUCCESS_GEN_REPORTE="Operación de generación de reporte realizada exitosamente.";
    public final static String MSG_SUCCESS_GEN_GRAPHICS="Operación de generación de gráfico realizada exitosamente.";
    
    public final static String MSG_INFO_GEN_REPORTE="Generación de tablas para reporte seleccionado en proceso.";
    public final static String MSG_INFO_GEN_GRAPHICS="Generación de gráficos para reporte seleccionado en proceso.";
    
    public final static String MSG_ERROR_GEN_REPORTE="El sistema ha encontrado un error al momento de generar el reporte.";
    public final static String MSG_ERROR_GEN_GRAPHICS="El sistema ha encontrado un error al momento de generar el gráfico.";
    
    public final static String MSG_INFO_FILE_ALREADY_EXISTS="El archivo que desea crear ya existe. Reintente utilizando otro nombre y vuelva a intentarlo.";
    public final static String MSG_INFO_FILE_DOESNT_EXISTS="El archivo definido no existe. Reintente seleccionado otro archivo e inténtelo nuevamente.";
    
    public final static int MES_ENERO=1;
    public final static int MES_FEBRERO=2;
    public final static int MES_MARZO=3;
    public final static int MES_ABRIL=4;
    public final static int MES_MAYO=5;
    public final static int MES_JUNIO=6;
    public final static int MES_JULIO=7;
    public final static int MES_AGOSTO=8;
    public final static int MES_SEPTIEMBRE=9;
    public final static int MES_OCTUBRE=10;
    public final static int MES_NOVIEMBRE=11;
    public final static int MES_DICIEMBRE=12;
    
    public final static String CANAL_SUPERMERCADO="Supermercado";
    public final static String CANAL_FOODSERVICE="Food Service";
    public final static String CANAL_CALLCENTER="Call Center";
    public final static String CANAL_TRADICIONAL="Tradicional";
    public final static String CANAL_CLIENTE_IMPORTANTE="Cliente Importante";
    
    public static void generaMensaje(String titulo, AlertType tipo, String mensaje, String detail)
    {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(mensaje);
        alert.setContentText(detail);
        alert.showAndWait();
    }
    
}
