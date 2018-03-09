/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller.filtroPeriodo;

import Model.CommandNames;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * FXML Controller class: 
 * Controlador para desplegar una ventana de filtro de fechas de meses.
 * @author Patricio
 */
public class FiltroPeriodo_MesController extends FiltroPeriodoController
{
    @FXML
    private Button button_Aceptar;
    @FXML
    private Button button_Cancelar;
    @FXML
    private RadioButton radioButtonA;
    @FXML
    private RadioButton radioButtonB;
    @FXML
    private ComboBox comboBox_anioInicio;
    @FXML
    private ComboBox comboBox_mesInicio;
    @FXML
    private ComboBox comboBox_anioFin;
    @FXML
    private ComboBox comboBox_mesFin;
    @FXML
    private Text text_periodoInicio;
    @FXML
    private Text text_periodoFin;
    @FXML
    private Text text_mark;

    private ObservableList<Integer> aniosInicio;
    private ObservableList<Integer> aniosFin;
    private ObservableList<Integer> mesesInicio;
    private ObservableList<Integer> mesesFin;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        this.radioButtonA.setSelected(true);
        this.opcion = 1;
        this.aniosInicio = FXCollections.observableArrayList();
        this.aniosFin = FXCollections.observableArrayList();
        this.mesesInicio = FXCollections.observableArrayList();
        this.mesesFin = FXCollections.observableArrayList();

        this.comboBox_anioInicio.setItems(this.aniosInicio);
        this.comboBox_anioFin.setItems(this.aniosFin);
        this.comboBox_mesInicio.setItems(this.mesesInicio);
        this.comboBox_mesFin.setItems(this.mesesFin);
        
        for (int i = MIN_YEAR; i < MAX_YEAR; i++)
        {
            this.aniosInicio.add(i);
            this.aniosFin.add(i);
        }
        for (int i = MIN_MONTH; i <= MAX_MONTH; i++)
        {
            this.mesesInicio.add(i);
            this.mesesFin.add(i);
        }
        this.comboBox_anioInicio.getSelectionModel().select(0);
        this.comboBox_anioFin.getSelectionModel().select(0);
        this.comboBox_mesInicio.getSelectionModel().select(0);
        this.comboBox_mesFin.getSelectionModel().select(1);
    }
    
    /**
     * Método que modifica los valores permitidos por el campo del mes limite para seleccionar: esto de acuerdo a lo 
     * seleccionado por el año inicial.
     */
    @FXML
    public void updateOptionCB2()
    {
        int auxAnio = Integer.parseInt(this.comboBox_anioInicio.getSelectionModel().getSelectedItem().toString());
        int auxMes = Integer.parseInt(this.comboBox_mesInicio.getSelectionModel().getSelectedItem().toString());
        this.aniosFin.clear();
        this.mesesFin.clear();
        for (int i = auxAnio ; i < MAX_YEAR+1; i++)
        {
            this.aniosFin.add(i);
        }
        for (int i = auxMes ; i < MAX_MONTH; i++)
        {
            this.mesesFin.add(i+1);
        }
        this.comboBox_anioFin.getSelectionModel().select(0);
        this.comboBox_mesFin.getSelectionModel().select(0);
    }

    /**
     * Método que realiza modificaciones de acuerdo a la opción seleccionada por el usuario: si es por un mes en específico, 
     * se permite sólo modificar el mes inicial, en caso que sea por rango de meses, se permite modificar mes inicio y término.
     */
    @FXML
    public void setRadioButtonOption()
    {
        if (this.opcion == 2 && this.radioButtonA.isSelected())//por mes especifico
        {
            System.out.println("caso a");
            this.text_periodoInicio.setText("Período (Mes/Año)");
            this.text_periodoFin.setVisible(false);
            this.text_mark.setVisible(false);
            this.comboBox_mesFin.setVisible(false);
            this.comboBox_anioFin.setVisible(false);

            this.radioButtonB.setSelected(false);
            this.opcion = 1;
        }
        else if (this.opcion == 1 && this.radioButtonB.isSelected())//por rango mes
        {
            System.out.println("caso b");
            this.text_periodoInicio.setText("Inicio (Mes/Año)");
            this.text_periodoFin.setVisible(true);
            this.text_mark.setVisible(true);
            this.comboBox_mesFin.setVisible(true);
            this.comboBox_anioFin.setVisible(true);

            this.radioButtonA.setSelected(false);
            this.opcion = 2;
        }
        else//cuando se deselecciona una opcion
        {
            switch(this.opcion)
            {
                case 1:
                    this.radioButtonA.setSelected(true);
                    break;
                case 2:
                    this.radioButtonB.setSelected(true);
                    break;
            }
            System.out.println("caso raro");
        }
        this.comboBox_anioInicio.getSelectionModel().select(0);
        this.comboBox_mesInicio.getSelectionModel().select(0);
        this.comboBox_anioFin.getSelectionModel().select(0);
        this.comboBox_mesFin.getSelectionModel().select(0);
    }

    /**
     * Método invocado por un Button. Limpia el filtro fecha del reporte base, lo modifica de acuerdo a las opciones 
     * ingresadas del usuario y cierra la ventana. Para el caso de rango de meses, si el rango es erróneo (mes inicio mayor a
     *  mes término) arroja un mensaje y no modifica el filtro ya existente.
     */
    @FXML
    public void buttonAceptar()
    {
        int mesInicio = Integer.parseInt(this.comboBox_mesInicio.getSelectionModel().getSelectedItem().toString());
        int mesFin = Integer.parseInt(this.comboBox_mesFin.getSelectionModel().getSelectedItem().toString());
        int anioInicio = Integer.parseInt(this.comboBox_anioInicio.getSelectionModel().getSelectedItem().toString());
        int anioFin = Integer.parseInt(this.comboBox_anioFin.getSelectionModel().getSelectedItem().toString());
        switch(this.opcion)
        {
            case 1:
                this.filtro.vaciarFiltro();
                this.filtro.setFechaInicio(new Date(anioInicio, mesInicio-1, 1));
                this.filtro.setOpcion(3);
                this.filtro.prepararFiltro();
                this.buttonCancelar();
                break;
            case 2:
                if(validarRango(mesInicio,anioInicio,mesFin,anioFin))
                {
                    this.filtro.vaciarFiltro();
                    this.filtro.setFechaInicio(new Date(anioInicio,mesInicio-1,1));
                    this.filtro.setFechaFin(new Date(anioFin,mesFin-1,1));
                    this.filtro.setOpcion(4);
                    this.filtro.prepararFiltro();
                    this.buttonCancelar();
                }
                else
                {
                    CommandNames.generaMensaje("Mensaje del Sistema", Alert.AlertType.INFORMATION, 
                            "Años seleccionados erróneos", "Debe seleccionar el un rango de meses tal que el tramo inicial sea menor al final. "
                                    + "En caso que sólo necesite filtrar por un mes cambie su opción y reintente nuevamente.");
                }
                break;
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
     * Método que se encarga de validar un rango de meses
     * @return true si mes inicio es menor a mes final, y false en caso contrario.
     */
    private boolean validarRango(int mesInicio, int anioInicio, int mesFin, int anioFin)
    {
        if(anioInicio<=anioFin)
        {
            //rango de año correcto
            if(anioInicio==anioFin && mesInicio>=mesFin)
                return false;
            return true;
        }
        return false;
    }

}
