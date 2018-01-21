/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller.filtroPeriodo;

import Model.CommandNames;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Patricio
 */
public class FiltroPeriodo_DiaController extends FiltroPeriodoController
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
    private DatePicker datePicker_fechaInicio;
    @FXML
    private DatePicker datePicker_fechaFin;
    @FXML
    private Text text_FechaInicio;
    @FXML
    private Text text_FechaFin;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        this.radioButtonA.setSelected(true);
        this.opcion = 1;
       
        this.datePicker_fechaInicio.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(date.isBefore(LocalDate.parse(MIN_DATE, DateTimeFormatter.ofPattern("dd-MM-yyyy")))
                        || date.isAfter(LocalDate.parse(MAX_DATE, DateTimeFormatter.ofPattern("dd-MM-yyyy"))));
            }
        });
        this.datePicker_fechaFin.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(date.isBefore(LocalDate.parse(MIN_DATE, DateTimeFormatter.ofPattern("dd-MM-yyyy")))
                        || date.isAfter(LocalDate.parse(MAX_DATE, DateTimeFormatter.ofPattern("dd-MM-yyyy"))));
            }
        });
        this.datePicker_fechaInicio.setValue(LocalDate.parse(MIN_DATE, DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        this.datePicker_fechaFin.setValue(LocalDate.parse(MIN_DATE, DateTimeFormatter.ofPattern("dd-MM-yyyy")));
    }
    
    @FXML
    public void updateOptionCB2()
    {
        this.datePicker_fechaFin.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(date.isBefore(datePicker_fechaInicio.getValue().plusDays(1))
                        || date.isAfter(LocalDate.parse(MAX_DATE, DateTimeFormatter.ofPattern("dd-MM-yyyy"))));
            }
        });
        this.datePicker_fechaFin.setValue(this.datePicker_fechaInicio.getValue().plusDays(1));
    
    }

    @FXML
    public void setRadioButtonOption()
    {
        if (this.opcion == 2 && this.radioButtonA.isSelected())//por especifico
        {
            System.out.println("caso a");
            this.text_FechaInicio.setText("Dia:");
            this.text_FechaFin.setVisible(false);
            this.datePicker_fechaFin.setVisible(false);

            this.radioButtonB.setSelected(false);
            this.opcion = 1;
        }
        else if (this.opcion == 1 && this.radioButtonB.isSelected())//por rango
        {
            System.out.println("caso b");
            this.text_FechaInicio.setText("Dia Inicio:");
            this.text_FechaFin.setVisible(true);
            this.datePicker_fechaFin.setVisible(true);

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
        this.datePicker_fechaInicio.setValue(LocalDate.parse(MIN_DATE, DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        this.datePicker_fechaFin.setValue(LocalDate.parse(MIN_DATE, DateTimeFormatter.ofPattern("dd-MM-yyyy")));
    }

    @FXML
    public void buttonAceptar()
    {
        //implementar!
        LocalDate inicio = this.datePicker_fechaInicio.getValue();
        LocalDate fin = this.datePicker_fechaFin.getValue();
        switch(this.opcion)
        {
            case 1:
                this.filtro.vaciarFiltro();
                this.filtro.setFechaInicio(new Date(inicio.getYear(),inicio.getMonthValue(),inicio.getDayOfMonth()));
                this.filtro.setOpcion(7);
                this.buttonCancelar();
                break;
            case 2:
                if(inicio.isBefore(fin))
                {
                    this.filtro.vaciarFiltro();
                    this.filtro.setFechaInicio(new Date(inicio.getYear(),inicio.getMonthValue(),inicio.getDayOfMonth()));
                    this.filtro.setFechaFin(new Date(fin.getYear(),fin.getMonthValue(),fin.getDayOfMonth()));
                    this.filtro.setOpcion(8);
                    this.buttonCancelar();
                }
                else
                {
                    CommandNames.generaMensaje("Mensaje del Sistema", Alert.AlertType.INFORMATION, 
                            "Años seleccionados erróneos", "Debe seleccionar el un rango de fechas tal que el inicio sea menor al final. "
                                    + "En caso que sólo necesite filtrar por un año cambie su opción y reintente nuevamente.");
                }
                break;
        }
    }

    @FXML
    public void buttonCancelar()
    {
        Stage s = (Stage) this.button_Cancelar.getScene().getWindow();
        s.close();
    }

}
