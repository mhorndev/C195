package Controller;

import Model.*;
import Database.*;

import java.time.*;
import java.net.URL;
import javafx.fxml.FXML;
import java.util.Optional;
import javafx.stage.Stage;
import javafx.scene.control.*;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.application.Platform;
import javafx.util.StringConverter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.time.format.DateTimeFormatter;

public class AppointmentEdit implements Initializable {

    @FXML
    private ComboBox<User> cbConsultant;

    @FXML
    private ComboBox<Customer> cbCustomer;

    @FXML
    private ComboBox<String> cbType;

    @FXML
    private ComboBox<String> cbStartTime;

    @FXML
    private ComboBox<String> cbEndTime;

    @FXML
    private ComboBox<String> cbLocation;

    @FXML
    private DatePicker cbDate;

    @FXML
    private Button btnCancel;

    @FXML
    private Button btnSave;

    private Appointment appointment;

    private AppointmentMain parentController;

    ObservableList<String> appointmentTypes = FXCollections.observableArrayList(
            "Consultation", "Orientation", "Strategy Session", "Financial Meeting");

    ObservableList<String> appointmentTimes = FXCollections.observableArrayList(
            "8:00 AM", "9:00 AM", "10:00 AM", "11:00 AM", "12:00 PM", "1:00 PM", "2:00 PM", "3:00 PM", "4:00 PM");

    ObservableList<String> officeLocations = FXCollections.observableArrayList(
            "New York Office", "Phoenix Office", "London Office");

    /** ************************************************************************************************************* *
     * btnSaveAction
     * ************************************************************************************************************** */

    @FXML
    void btnSaveAction(ActionEvent event) {
        User consultant = cbConsultant.getSelectionModel().getSelectedItem();
        Customer customer = cbCustomer.getSelectionModel().getSelectedItem();
        String type = cbType.getSelectionModel().getSelectedItem();
        LocalDate date = cbDate.getValue();
        String startTime = cbStartTime.getSelectionModel().getSelectedItem();
        String endTime = cbEndTime.getSelectionModel().getSelectedItem();
        String location = cbLocation.getSelectionModel().getSelectedItem();

        StringBuilder validationErrors = new StringBuilder();

        if (cbConsultant.getSelectionModel().getSelectedItem() == null) {
            validationErrors.append("Field 'Consultant' is required \n");
        }

        if (cbCustomer.getSelectionModel().getSelectedItem() == null) {
            validationErrors.append("Field 'Customer' is required \n");
        }

        if (cbType.getSelectionModel().getSelectedItem() == null) {
            validationErrors.append("Field 'Type' is required \n");
        }

        if (cbDate.getValue() == null) {
            validationErrors.append("Field 'Date' is required \n");
        }

        if (cbStartTime.getSelectionModel().getSelectedItem() == null) {
            validationErrors.append("Field 'Start' is required \n");
        }

        if (cbEndTime.getSelectionModel().getSelectedItem() == null) {
            validationErrors.append("Field 'End' is required \n");
        }

        if (cbLocation.getSelectionModel().getSelectedItem() == null) {
            validationErrors.append("Field 'Location' is required \n");
        }

        if (cbStartTime.getSelectionModel().getSelectedItem() != null
           && cbEndTime.getSelectionModel().getSelectedItem() != null) {
            if (!Utility.Time.validTimeSlot(
                    cbStartTime.getSelectionModel().getSelectedItem(),
                    cbEndTime.getSelectionModel().getSelectedItem())) {
                validationErrors.append("'Start' must be less than 'End' \n");
                validationErrors.append("'End' must be greater than 'Start' \n");
            }
        }

        if (validationErrors.length() >= 1) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Validation Error");
            alert.setHeaderText("Validation Error(s)");
            alert.setContentText(validationErrors.toString());
            alert.showAndWait();
        } else {
            dbAppointment.updateAppointment(appointment.getAppointmentId(), consultant,customer,type,date.toString(),startTime,endTime,location);
            parentController.refreshTable();
            Stage stage = (Stage) btnSave.getScene().getWindow();
            stage.close();
        }
    }

    /** ************************************************************************************************************* *
     * btnCancelAction
     * ************************************************************************************************************** */

    @FXML
    void btnCancelAction(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Confirm Cancel");
        alert.setContentText("Do you really want to cancel editing this appointment?");

        Optional<ButtonType> result = alert.showAndWait();

        if(result.isPresent() && result.get() == ButtonType.OK) {
            Stage stage = (Stage) btnCancel.getScene().getWindow();
            stage.close();
        }
    }

    /** ************************************************************************************************************* *
     * bindController
     * ************************************************************************************************************** */

    public void bindController(AppointmentMain parentController) {
        this.parentController = parentController;
    }

    /** ************************************************************************************************************* *
     * bindAppointment
     * ************************************************************************************************************** */

    public void bindAppointment(Appointment appointment) {
        this.appointment = appointment;

        cbConsultant.setItems(
                dbUser.getUsers()
        );

        cbConsultant.getItems().forEach(consultant -> {
            if (consultant.getUserId() == appointment.getUserId()) {
                cbConsultant.getSelectionModel().select(consultant);
            }
        });

        cbCustomer.setItems(
                dbCustomer.getCustomers()
        );

        cbCustomer.getItems().forEach(customer -> {
            if (customer.getCustomerId() == appointment.getCustomerId()) {
                cbCustomer.getSelectionModel().select(customer);
            }
        });

        cbType.setItems(
                appointmentTypes
        );

        cbType.getItems().forEach(type -> {
            if (type.equals(appointment.getType())) {
                cbType.getSelectionModel().select(type);
            }
        });

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy");
        LocalDate localDate = LocalDate.parse(appointment.getDate(), formatter);
        cbDate.setValue(localDate);

        cbStartTime.setItems(
                appointmentTimes
        );

        cbStartTime.getItems().forEach(startTime -> {
            if (startTime.equals(appointment.getStart())) {
                cbStartTime.getSelectionModel().select(startTime);
            }
        });

        cbEndTime.setItems(
                appointmentTimes
        );

        cbEndTime.getItems().forEach(endTime -> {
            if (endTime.equals(appointment.getEnd())) {
                cbEndTime.getSelectionModel().select(endTime);
            }
        });

        cbLocation.setItems(
                officeLocations
        );

        cbLocation.getItems().forEach(location -> {
            if (location.equals(appointment.getLocation())) {
                cbLocation.getSelectionModel().select(location);
            }
        });
    }

    /** ************************************************************************************************************* *
     * initialize
     * ************************************************************************************************************** */

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                btnSave.requestFocus();
            }
        });

        cbConsultant.setConverter(new StringConverter<User>() {
            @Override
            public String toString(User object) {
                return object.getUserName();
            }

            @Override
            public User fromString(String string) {
                return null;
            }
        });

        cbCustomer.setConverter(new StringConverter<Customer>() {
            @Override
            public String toString(Customer object) {
                return object.getCustomerName();
            }

            @Override
            public Customer fromString(String string) {
                return null;
            }
        });

        cbDate.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable( empty
                        || date.isBefore(LocalDate.now())
                        || date.getDayOfWeek().equals(DayOfWeek.SATURDAY)
                        || date.getDayOfWeek().equals(DayOfWeek.SUNDAY));
                if( date.isBefore(LocalDate.now())
                        || date.getDayOfWeek().equals(DayOfWeek.SATURDAY)
                        || date.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
                    setStyle("-fx-background-color: #999999;");
                }
            }
        });
    }
}
