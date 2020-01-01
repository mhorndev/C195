package Controller;

import Model.Appointment;

import java.net.URL;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;

public class CalendarDetail implements Initializable {
    @FXML
    private TableView<Appointment> tblAppointments;

    @FXML
    private TableColumn<?, ?> columnDate;

    @FXML
    private TableColumn<?, ?> columnStart;

    @FXML
    private TableColumn<?, ?> columnEnd;

    @FXML
    private TableColumn<?, ?> columnLocation;

    @FXML
    private TableColumn<?, ?> columnConsultant;

    @FXML
    private TableColumn<?, ?> columnCustomer;

    @FXML
    private TableColumn<?, ?> columnType;

    @FXML
    private Label lblDate;

    @FXML
    private Button btnClose;

    private ObservableList<Appointment> appointments = FXCollections.observableArrayList();

    @FXML
    void btnCloseAction(ActionEvent event) {
        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();
    }

    public void bindAppointments(ObservableList<Appointment> appointments) {
        this.appointments = appointments;
        tblAppointments.setItems(appointments);
        lblDate.setText(appointments.get(0).getDate());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                btnClose.requestFocus();
            }
        });

        columnConsultant.setCellValueFactory(new PropertyValueFactory<>("userName"));
        columnCustomer.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        columnType.setCellValueFactory(new PropertyValueFactory<>("type"));
        columnLocation.setCellValueFactory(new PropertyValueFactory<>("location"));
        columnDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        columnStart.setCellValueFactory(new PropertyValueFactory<>("start"));
        columnEnd.setCellValueFactory(new PropertyValueFactory<>("end"));
    }
}
