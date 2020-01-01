package Controller;

import Database.dbAppointment;
import Model.Appointment;
import Model.CalendarPane;
import com.sun.org.apache.xerces.internal.dom.ChildNode;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Locale;
import java.util.ResourceBundle;

public class CalendarMain implements Initializable {
    @FXML
    GridPane calendarGridPane;

    @FXML
    Button btnWeekly;

    @FXML
    Button btnPrev;

    @FXML
    Button btnNext;

    @FXML
    Text txtCurrent;

    private YearMonth currentYearMonth;

    private ObservableList<CalendarPane> calendarPanes = FXCollections.observableArrayList();

    private ObservableList<Appointment> appointments = FXCollections.observableArrayList();

    private ObservableList<String> daysOfTheWeek = FXCollections.observableArrayList(
            "SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT");

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        appointments = dbAppointment.getAppointments();

        for (int j = 0; j < 7; j++) {
            addHeader(j, 0);
        }

        for (int i = 1; i < 7; i++) { //Rows
            for (int j = 0; j < 7; j++) { //Columns
                addCell(j, i);
            }
        }

        currentYearMonth = YearMonth.now();

        populateDate(currentYearMonth);

    }

    private void addHeader(Integer column, Integer row) {
        AnchorPane pane = new AnchorPane();
        pane.setStyle("-fx-background-color:#380200; -fx-border-color:black;");
        Label label = new Label();
        label.setAlignment(Pos.CENTER);
        label.setStyle("-fx-text-fill: #fb0006;");
        label.setText(daysOfTheWeek.get(column));
        pane.setTopAnchor(label, 0.0);
        pane.setLeftAnchor(label, 0.0);
        pane.setBottomAnchor(label, 0.0);
        pane.setRightAnchor(label, 0.0);
        pane.getChildren().add(label);
        calendarGridPane.add(pane, column, row);
    }

    private void addCell(Integer column, Integer row) {
        CalendarPane pane = new CalendarPane();

        if (isWeekend(column)) {
            setWeekendStyle(pane);
        } else {
            setWeekdayStyle(pane);
        }

        calendarGridPane.add(pane, column, row);
        calendarPanes.add(pane);
    }

    private boolean isWeekend(Integer column) {
        return (column == 0 || column == 6);
    }

    private void setWeekdayStyle(CalendarPane pane) {
        pane.setStyle("-fx-border-color:black; -fx-background-color:white;");
    }

    private void setWeekendStyle(CalendarPane pane) {
        pane.setStyle("-fx-border-color:black; -fx-background-color:lightgrey;");
    }

    private String appointmentStyle() {
        return "-fx-border-color:black; -fx-background-color:lightblue;";
    }

    private String appointmentStyleHover() {
        return "-fx-border-color:black; -fx-background-color:lightblue;";
    }

    private void populateDate(YearMonth yearMonth){

        LocalDate date = LocalDate.of(
                yearMonth.getYear(), yearMonth.getMonthValue(), 1);

        if (date.getDayOfWeek() != (DayOfWeek.SUNDAY)) {
            do { date = date.minusDays(1); } while (
                    date.getDayOfWeek() != (DayOfWeek.SUNDAY)
            );
        }

        txtCurrent.setText(currentYearMonth.getMonth().toString() + " " + currentYearMonth.getYear());

        // for each pane
        for (CalendarPane calendarPane : calendarPanes) {

            // clear appointments
            calendarPane.clearAppointments();

            // clear children
            calendarPane.getChildren().clear();

            // set date
            calendarPane.setDate(date);

            // add appointments
            LocalDate finalCalendarDate = date;
            appointments.forEach(appointment -> {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
                LocalDate appointmentDate = LocalDate.parse(appointment.getDate(), formatter);
                if(appointmentDate.equals(finalCalendarDate)) {
                    calendarPane.addAppointment(appointment);
                }
            });

            //add children
            VBox vb = new VBox();
            vb.getStyleClass().add("calendar-box");

            Label dateNumber = new Label();
            Label dateContent = new Label();
            dateNumber.getStyleClass().add("dateNumber");
            dateContent.getStyleClass().add("dateContent");
            dateNumber.setText(String.valueOf(date.getDayOfMonth()));

            if (calendarPane.hasAppointments()) {
                dateContent.setText(
                        calendarPane.getNumAppointments().toString()
                        + " appointments"
                );
            }

            dateContent.setAlignment(Pos.CENTER);
            dateContent.setMaxHeight(Double.MAX_VALUE);
            VBox.setVgrow(dateContent, Priority.ALWAYS);
            vb.getChildren().add(dateNumber);
            vb.getChildren().add(dateContent);

            calendarPane.setTopAnchor(vb, 0.0);
            calendarPane.setLeftAnchor(vb, 0.0);
            calendarPane.setBottomAnchor(vb, 0.0);
            calendarPane.setRightAnchor(vb, 0.0);
            calendarPane.getChildren().add(vb);

            if (date.getDayOfWeek().equals(DayOfWeek.SATURDAY)
                || date.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
                calendarPane.setDisable(true);
            }

            if (calendarPane.hasAppointments()) {
                calendarPane.setCursor(Cursor.HAND);
            }

            calendarPane.setOnMouseClicked(e -> {
                try {
                    calendarPaneOnMouseClicked(calendarPane);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });

            date = date.plusDays(1);
        }
    }

    private void calendarPaneOnMouseClicked(CalendarPane calendarPane) throws IOException {
        if (!calendarPane.hasAppointments())
            return;

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/View/CalendarDetail.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.initModality(Modality.APPLICATION_MODAL);
        CalendarDetail controller = (CalendarDetail)loader.getController();
        controller.bindAppointments(calendarPane.getAppointments());
        stage.show();
    }

    @FXML
    private void btnWeeklyAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/View/CalendarWeekly.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.initModality(Modality.APPLICATION_MODAL);
        CalendarWeekly controller = (CalendarWeekly)loader.getController();
        controller.bindAppointments(appointments);
        stage.show();
    }

    @FXML
    private void btnPrevAction(ActionEvent event) {
        currentYearMonth = currentYearMonth.minusMonths(1);
        populateDate(currentYearMonth);
    }

    @FXML
    private void btnNextAction(ActionEvent event) {
        currentYearMonth = currentYearMonth.plusMonths(1);
        populateDate(currentYearMonth);
    }
}