package Controller;

import Database.*;
import java.net.URL;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.scene.Scene;
import java.util.Optional;
import javafx.scene.Parent;
import java.io.IOException;
import javafx.stage.Modality;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.beans.binding.Bindings;
import javafx.scene.control.cell.PropertyValueFactory;

public class CustomerMain implements Initializable {
    @FXML
    private Button btnAdd;

    @FXML
    private Button btnEdit;

    @FXML
    private Button btnDelete;

    @FXML
    private TableView<Model.Customer> tblCustomers;

    @FXML
    private TableColumn<?, ?> columnId;

    @FXML
    private TableColumn<?, ?> columnName;

    @FXML
    private TableColumn<?, ?> columnPhone;

    @FXML
    private TableColumn<?, ?> columnAddress;

    @FXML
    private TableColumn<?, ?> columnCity;

    @FXML
    private TableColumn<?, ?> columnCountry;

    @FXML
    private TableColumn<?, ?> columnPostalCode;

    @FXML
    private void btnAddAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/View/CustomerAdd.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.initModality(Modality.APPLICATION_MODAL);
        CustomerAdd controller = (CustomerAdd)loader.getController();
        controller.bind(this);
        stage.show();
    }

    @FXML
    private void btnEditAction(ActionEvent event) throws IOException {
        if (tblCustomers.getSelectionModel().getSelectedItem() == null)
            return;
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/View/CustomerEdit.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.initModality(Modality.APPLICATION_MODAL);
        CustomerEdit controller = (CustomerEdit)loader.getController();
        controller.bind(this);
        controller.bindCustomer(tblCustomers.getSelectionModel().getSelectedItem());
        stage.show();
    }

    @FXML
    private void btnDeleteAction(ActionEvent event) {
        if (tblCustomers.getSelectionModel().getSelectedItem() == null)
            return;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Confirm Delete");
        alert.setContentText("Do you really want to delete '" +
                tblCustomers.getSelectionModel().getSelectedItem().getCustomerName() + "' ?\n\n" +
                "WARNING: This will also delete all corresponding appointments");

        Optional<ButtonType> result = alert.showAndWait();

        if(result.isPresent() && result.get() == ButtonType.OK) {
            dbCustomer.deleteCustomer(tblCustomers.getSelectionModel().getSelectedItem().getCustomerId());
            refreshTable();
        }
    }

    public void refreshTable() {
        tblCustomers.setItems(dbCustomer.getCustomers());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        columnId.setCellValueFactory(new PropertyValueFactory<>("customerId"));         //customer table
        columnName.setCellValueFactory(new PropertyValueFactory<>("customerName"));     //customer table
        columnAddress.setCellValueFactory(new PropertyValueFactory<>("address"));       //address table
        columnCity.setCellValueFactory(new PropertyValueFactory<>("city"));             //address table
        columnPostalCode.setCellValueFactory(new PropertyValueFactory<>("postalCode")); //address table
        columnPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));           //address table
        columnCountry.setCellValueFactory(new PropertyValueFactory<>("country"));       //country table
        btnEdit.disableProperty().bind(Bindings.isEmpty(tblCustomers.getSelectionModel().getSelectedItems()));
        btnDelete.disableProperty().bind(Bindings.isEmpty(tblCustomers.getSelectionModel().getSelectedItems()));
        refreshTable();
    }

}
