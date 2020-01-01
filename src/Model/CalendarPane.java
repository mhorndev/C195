package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import java.time.LocalDate;

public class CalendarPane extends AnchorPane {

    private LocalDate date;

    private ObservableList<Appointment> appointments = FXCollections.observableArrayList();

    public CalendarPane(Node... children) {
        super(children);
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void addAppointment(Appointment appointment) {
        appointments.add(appointment);
    }

    public boolean hasAppointments() {
        return appointments.size() > 0;
    }

    public Integer getNumAppointments() {
        return appointments.size();
    }

    public void clearAppointments() { this.appointments = FXCollections.observableArrayList(); }

    public ObservableList<Appointment> getAppointments() { return this.appointments; }
}