package Controller;

import Model.*;
import Database.*;

import java.net.URL;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import java.util.Optional;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.application.Platform;
import javafx.beans.binding.BooleanBinding;

public class CustomerAdd implements Initializable {
    @FXML
    private Button btnCancel;

    @FXML
    private Button btnSave;

    @FXML
    private TextField fieldId;

    @FXML
    private TextField fieldName;

    @FXML
    private TextField fieldPhone;

    @FXML
    private TextField fieldAddress;

    @FXML
    private TextField fieldPostalCode;

    @FXML
    private TextField fieldCity;

    @FXML
    private TextField fieldCountry;

    @FXML
    private CheckBox cbActive;

    private CustomerMain parentController;

    @FXML
    void btnSaveAction(ActionEvent event) {
        String name = fieldName.getText();
        String phone = fieldPhone.getText();
        String address = fieldAddress.getText();
        String postalCode = fieldPostalCode.getText();
        String city = fieldCity.getText();
        String country = fieldCountry.getText();
        Integer active = cbActive.isSelected()?1:0;

        Integer countryId = dbCountry.getCountryId(country);

        if (countryId == null) {
            countryId = dbCountry.insertCountry(country);
        }

        Integer cityId = dbCity.getCityId(city, countryId);

        if (cityId == null) {
            cityId = dbCity.insertCity(city, countryId);
        }

        Integer addressId = dbAddress.getAddressId(address, cityId);

        if (addressId == null) {
            addressId = dbAddress.insertAddress(address, cityId, postalCode, phone);
        }

        Integer customerId = dbCustomer.getCustomerId(name, addressId);

        if (customerId == null) {
            customerId = dbCustomer.insertCustomer(name, addressId, active);
        }

        parentController.refreshTable();

        Stage stage = (Stage) btnSave.getScene().getWindow();
        stage.close();
    }

    @FXML
    void btnCancelAction(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Confirm Cancel");
        alert.setContentText("Do you really want to cancel adding this customer?");

        Optional<ButtonType> result = alert.showAndWait();

        if(result.isPresent() && result.get() == ButtonType.OK) {
            Stage stage = (Stage) btnCancel.getScene().getWindow();
            stage.close();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                btnCancel.requestFocus();
            }
        });

        BooleanBinding validInput = new BooleanBinding() {
            {
                super.bind(
                        fieldName.textProperty(),
                        fieldPhone.textProperty(),
                        fieldAddress.textProperty(),
                        fieldCity.textProperty(),
                        fieldCountry.textProperty(),
                        fieldPostalCode.textProperty());
            }

            @Override
            protected boolean computeValue() {
                return (fieldName.getText().isEmpty()
                        || fieldPhone.getText().isEmpty()
                        || fieldAddress.getText().isEmpty()
                        || fieldCity.getText().isEmpty()
                        || fieldCountry.getText().isEmpty()
                        || fieldPostalCode.getText().isEmpty());
            }
        };

        btnSave.disableProperty().bind(validInput);
    }

    public void bind(CustomerMain parentController) {
        this.parentController = parentController;
    }
}
