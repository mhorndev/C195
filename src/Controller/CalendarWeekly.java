package Controller;

import Model.Appointment;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class CalendarWeekly implements Initializable {
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
    private Label lblRange;

    @FXML
    private Button btnClose;

    private ObservableList<Appointment> appointments = FXCollections.observableArrayList();
    private FilteredList<Appointment> filteredAppointments;

    @FXML
    void btnCloseAction(ActionEvent event) {
        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();
    }

    public void bindAppointments(ObservableList<Appointment> appointments) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate now = LocalDate.parse(LocalDate.now().toString(), formatter);
        LocalDate in7 = LocalDate.parse(LocalDate.now().plusDays(7).toString(), formatter);

        /**
         * Lambda
         * a far simpler syntax for anonymous delegates, especially when dealing with filtered lists
         */

        filteredAppointments = appointments.filtered(
                appointment -> {
                    DateTimeFormatter aFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
                    LocalDate appointmentDate = LocalDate.parse(appointment.getDate(), aFormatter);
                    //System.out.println(appointmentDate);
                    return (
                            (appointmentDate.equals(now) || appointmentDate.isAfter(now))
                            && (appointmentDate.equals(in7) || appointmentDate.isBefore(in7))
                    );
                });
        tblAppointments.setItems(filteredAppointments);
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
        lblRange.setText("Next 7 Days");
    }
}
