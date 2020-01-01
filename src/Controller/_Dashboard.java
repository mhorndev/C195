package Controller;

import Database.dbUser;
import Model.User;
import Utility.Log;
import Utility.NotifyAppointments;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class _Dashboard implements Initializable {
    @FXML
    private Button navCustomers;

    @FXML
    private Button navAppointments;

    @FXML
    private Button navCalendars;

    @FXML
    private Button navReports;

    @FXML
    private Button navLogout;

    @FXML
    private AnchorPane appPane;

    @FXML
    private AnchorPane loginPane;

    @FXML
    private AnchorPane contentPane;

    @FXML
    private Label lblLogin;

    @FXML
    private TextField fieldUsername;

    @FXML
    private TextField fieldTextPassword;

    @FXML
    private PasswordField fieldPassword;

    @FXML
    private CheckBox cbShowPassword;

    @FXML
    private Button btnLogin;

    @FXML
    private Label lblError;

    @FXML
    private Label lblLanguage;

    @FXML
    private ImageView flagIcon;

    private Button selectedNav;

    private User user;

    private ResourceBundle language;

    @FXML
    private void navCustomersAction(ActionEvent event) throws IOException {
        selectedNav = navCustomers;
        navLoadContent("/View/CustomerMain.fxml");
    }

    @FXML
    private void navAppointmentsAction(ActionEvent event) throws IOException {
        selectedNav = navAppointments;
        navLoadContent("/View/AppointmentMain.fxml");
    }

    @FXML
    private void navCalendarsAction(ActionEvent event) throws IOException {
        selectedNav = navCalendars;
        navLoadContent("/View/CalendarMain.fxml");
    }

    @FXML
    private void navReportsAction(ActionEvent event) throws IOException {
        selectedNav = navReports;
        navLoadContent("/View/ReportMain.fxml");
    }

    @FXML
    private void navLogoutAction(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Confirm Logout");
        alert.setContentText("Do you really want to logout?");

        Optional<ButtonType> result = alert.showAndWait();

        if(result.isPresent() && result.get() == ButtonType.OK) {
            user = null;
            activateLoginUI();
        }
    }

    @FXML
    private void navLoadContent(String location) throws IOException {
        navUpdateUI();
        Parent root = FXMLLoader.load(getClass().getResource(location));
        contentPane.getChildren().clear();
        contentPane.getStylesheets().add("/Style/Style.css");
        contentPane.setTopAnchor(root, 0.0);
        contentPane.setLeftAnchor(root, 0.0);
        contentPane.setBottomAnchor(root, 0.0);
        contentPane.setRightAnchor(root, 0.0);
        contentPane.getChildren().add(root);
    }

    private void navUpdateUI() {
        navCustomers.setStyle("");
        navAppointments.setStyle("");
        navCalendars.setStyle("");
        navReports.setStyle("");
        selectedNav.setStyle("-fx-background-color:#bc0004;");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        language = ResourceBundle.getBundle("Language.login", Locale.getDefault());
        //language = ResourceBundle.getBundle("Language.login", Locale.GERMAN);

        //System.out.println(Locale.getDefault());
        //System.out.println(Locale.GERMAN);

        if (language.getLocale() == Locale.GERMAN) {
            flagIcon.setImage(new Image("/Style/flag_de.png"));
        } else {
            flagIcon.setImage(new Image("/Style/flag_en.png"));
        }

        lblLogin.setText(language.getString("login"));
        fieldUsername.setPromptText(language.getString("username"));
        fieldPassword.setPromptText(language.getString("password"));
        cbShowPassword.setText(language.getString("showpassword"));
        btnLogin.setText(language.getString("login"));
        lblLanguage.setText(language.getString("language"));

        activateLoginUI();
    }

    public void activateLoginUI() {

        Platform.runLater(new Runnable() {
            @Override
            public void run() { btnLogin.requestFocus(); }
        });

        user = null;
        fieldUsername.setText("");
        fieldPassword.setText("");
        fieldTextPassword.setText("");
        lblError.setVisible(false);
        cbShowPassword.setSelected(false);
        fieldPassword.visibleProperty().bind(cbShowPassword.selectedProperty().not());
        fieldTextPassword.visibleProperty().bind(cbShowPassword.selectedProperty());
        fieldTextPassword.textProperty().bindBidirectional(fieldPassword.textProperty());
        appPane.setVisible(false);
        loginPane.setVisible(true);
    }

    public void activateDashboardUI() {
        navLogout.setText("Logout ( " + user.getUserName() + " )");
        navAppointments.fire();
        appPane.setVisible(true);
        loginPane.setVisible(false);
    }

    @FXML
    public void btnLoginAction(ActionEvent event) throws IOException {

        if (fieldUsername.getText().isEmpty()) {
            lblError.setText(language.getString("usernamerequired")); lblError.setVisible(true);
        } else
        if (fieldPassword.getText().isEmpty()) {
            lblError.setText(language.getString("passwordrequired")); lblError.setVisible(true);
        } else {
            User newUser = dbUser.login(fieldUsername.getText(),fieldPassword.getText());

            if (newUser.getUserName() == null) {
                lblError.setText(language.getString("usernameinvalid")); lblError.setVisible(true);
            } else
            if (newUser.getPassword() == null) {
                lblError.setText(language.getString("passwordinvalid")); lblError.setVisible(true);
            } else {
                user = newUser;
                activateDashboardUI();
                Log.write(user);
                NotifyAppointments.check(user);
            }
        }
    }
}
