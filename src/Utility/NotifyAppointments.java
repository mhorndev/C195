package Utility;

import Model.Appointment;
import Model.User;
import Database.dbAppointment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.io.IOException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static Utility.Time.dateTimeDifference;
import static Utility.Time.toUTC;


public class NotifyAppointments {

    public static void check(User user) throws IOException {

        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        appointments = dbAppointment.getAppointments(user);

        ObservableList<Appointment> upcomingAppointments = FXCollections.observableArrayList();
        ObservableList<Appointment> upcomingIn15Appointments = FXCollections.observableArrayList();

        /**
         * Lambda
         * prefer to use Lambda to iterate over objects without including for or while loops
         * especially when an index is not needed.
         * lambda is much less taxing on cpu cycles than standard index style loops
         */

        appointments.forEach(appointment -> {

            //Get the time now (UTC)
            LocalDateTime now = Utility.Time.toUTC(
                    LocalDateTime.now(),
                    ZoneId.systemDefault());

            //Get the appointment time (string to LocalDateTime)
            String dateTime = appointment.getDate() + " " + appointment.getStart();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy h:mm a");
            LocalDateTime apptTime = LocalDateTime.parse(dateTime, formatter);

            //Get the appointment time (UTC)
            LocalDateTime apt = Utility.Time.toUTC(
                    apptTime,
                    Utility.Time.getZoneId(appointment.getLocation()));

            long countdown = Utility.Time.dateTimeDifference(
                    now,apt,ChronoUnit.MINUTES);

            System.out.println(appointment.getUserName() + " has an appointment " +
                    "with " + appointment.getCustomerName() + " " +
                    "on " + appointment.getDate() + " " +
                    "at " + appointment.getStart() + " " +
                    "in the " + appointment.getLocation() + " ( " +
                    countdown + " minutes )"
            );

            if (countdown >= 0) {
                appointment.setDescription(Long.toString(countdown));
                upcomingAppointments.add(appointment);
            }

            if (countdown >= 0 && countdown <= 15) {
                appointment.setDescription(Long.toString(countdown));
                upcomingIn15Appointments.add(appointment);
            }

        });

        if (upcomingIn15Appointments.size() > 0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Appointment Notify");
            alert.setHeaderText(user.getUserName());
            StringBuilder sb = new StringBuilder();
            sb.append("\n");
            sb.append("You have upcoming appointment(s) in the next 15 minutes\n\n");

            upcomingIn15Appointments.forEach(appointment -> {
                sb.append("Customer : " + appointment.getCustomerName()+"\n");
                sb.append("Type : " + appointment.getType()+"\n");
                sb.append("Date : " + appointment.getDate()+"\n");
                sb.append("Time : " + appointment.getStart()+"\n");
                sb.append("Location : " + appointment.getLocation()+"\n");
                sb.append("In : " + appointment.getDescription()+" minutes\n\n");
            });

            alert.setContentText(sb.toString());
            alert.showAndWait();
        } else
        if (upcomingAppointments.size() > 0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Appointment Notify");
            alert.setHeaderText(user.getUserName());
            StringBuilder sb = new StringBuilder();
            sb.append("\n");
            sb.append("You have no upcoming appointment(s) in the next 15 minutes\n\n");
            sb.append("Your next appointment:\n\n");

            Appointment appointment = upcomingAppointments.get(0);
            sb.append("Customer : " + appointment.getCustomerName()+"\n");
            sb.append("Type : " + appointment.getType()+"\n");
            sb.append("Date : " + appointment.getDate()+"\n");
            sb.append("Time : " + appointment.getStart()+"\n");
            sb.append("Location : " + appointment.getLocation()+"\n");
            sb.append("In : " + appointment.getDescription()+" minutes\n");
            alert.setContentText(sb.toString());
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Appointment Notify");
            alert.setHeaderText(user.getUserName());
            StringBuilder sb = new StringBuilder();
            sb.append("\n");
            sb.append("You have no upcoming appointment(s)");
            alert.setContentText(sb.toString());
            alert.showAndWait();
        }


        /*
        if (filteredAppointments.size() > 0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Upcoming Appointments");
            alert.setHeaderText("The following appointment(s) occur in the next 15 minutes:");
            StringBuilder sb = new StringBuilder();
            sb.append("\n");
            sb.append("User: " + user.getUserName() + "\n\n");
            filteredAppointments.forEach(appointment -> {
                sb.append("Customer: " + appointment.getCustomerName() + "\n");
                sb.append("Time(UTC): " + appointment.getStart() + "\n");
                sb.append("Time(Local): " + appointment.getFriendlyStart() + "\n");
                sb.append("Location: " + appointment.getLocation() + "\n");
                sb.append("\n");
            });
            alert.setContentText(sb.toString());
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Appointments");
            alert.setHeaderText("No appointment(s)");
            StringBuilder sb = new StringBuilder();
            sb.append("\n");
            sb.append(user.getUserName() + ", you have no upcoming appointment(s) in the next 15 minutes\n");
            alert.setContentText(sb.toString());
            alert.showAndWait();
        }

         */
    }
}