package Controller;

import Database.dbAppointment;
import Database.dbCustomer;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;


public class AppointmentMain implements Initializable {
    @FXML
    private Button btnAdd;

    @FXML
    private Button btnEdit;

    @FXML
    private Button btnDelete;

    @FXML
    private TableView<Model.Appointment> tblAppointments;

    @FXML
    private TableColumn<?, ?> columnConsultant;

    @FXML
    private TableColumn<?, ?> columnCustomer;

    @FXML
    private TableColumn<?, ?> columnType;

    @FXML
    private TableColumn<?, ?> columnLocation;

    @FXML
    private TableColumn<?, ?> columnDate;

    @FXML
    private TableColumn<?, ?> columnStart;

    @FXML
    private TableColumn<?, ?> columnEnd;

    @FXML
    private TableColumn<?, ?> columnStartLocal;

    @FXML
    private TableColumn<?, ?> columnEndLocal;

    @FXML
    private TableColumn<?, ?> columnTimeUntil;

    @FXML
    void btnAddAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/View/AppointmentAdd.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.initModality(Modality.APPLICATION_MODAL);
        AppointmentAdd controller = (AppointmentAdd)loader.getController();
        controller.bindController(this);
        controller.bindAppointment(null);
        stage.show();
    }

    @FXML
    void btnEditAction(ActionEvent event) throws IOException {
        if (tblAppointments.getSelectionModel().getSelectedItem() == null)
            return;
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/View/AppointmentEdit.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.initModality(Modality.APPLICATION_MODAL);
        AppointmentEdit controller = (AppointmentEdit)loader.getController();
        controller.bindController(this);
        controller.bindAppointment(tblAppointments.getSelectionModel().getSelectedItem());
        stage.show();
    }

    @FXML
    void btnDeleteAction(ActionEvent event) {
        if (tblAppointments.getSelectionModel().getSelectedItem() == null)
            return;
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Confirm Delete");
        alert.setContentText("Do you really want to delete this appointment?");

        Optional<ButtonType> result = alert.showAndWait();

        if(result.isPresent() && result.get() == ButtonType.OK) {
            dbAppointment.deleteAppointment(tblAppointments.getSelectionModel().getSelectedItem());
            tblAppointments.setItems(dbAppointment.getAppointments());
        }
    }

    public void refreshTable() {
        tblAppointments.setItems(dbAppointment.getAppointments());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        columnConsultant.setCellValueFactory(new PropertyValueFactory<>("userName"));
        columnCustomer.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        columnType.setCellValueFactory(new PropertyValueFactory<>("type"));
        columnLocation.setCellValueFactory(new PropertyValueFactory<>("location"));
        columnDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        columnStart.setCellValueFactory(new PropertyValueFactory<>("start"));
        columnEnd.setCellValueFactory(new PropertyValueFactory<>("end"));
        columnStartLocal.setCellValueFactory(new PropertyValueFactory<>("startLocal"));
        columnEndLocal.setCellValueFactory(new PropertyValueFactory<>("endLocal"));
        columnTimeUntil.setCellValueFactory(new PropertyValueFactory<>("timeUntil"));
        btnEdit.disableProperty().bind(Bindings.isEmpty(tblAppointments.getSelectionModel().getSelectedItems()));
        btnDelete.disableProperty().bind(Bindings.isEmpty(tblAppointments.getSelectionModel().getSelectedItems()));
        tblAppointments.setItems(dbAppointment.getAppointments());
    }


}
