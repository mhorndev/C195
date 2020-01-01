package Controller;

import java.net.URL;

import Database.db;
import Database.dbAppointment;
import Database.dbUser;
import Model.Appointment;
import Model.User;
import Utility.Time;
import javafx.fxml.FXML;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;

public class ReportMain implements Initializable {
    @FXML
    private ComboBox<String> cbReport;

    @FXML
    private ComboBox<User> cbConsultant;

    @FXML
    private Button btnExecute;

    @FXML
    private TableView tblResults;

    private ObservableList<String> reportNames = FXCollections.observableArrayList(
            "Number of Appointment Types by Month", "Consultant Appointment Schedule", "Number of Appointments by Office");

    private ObservableList<Function> reportFunctions = FXCollections.observableArrayList();

    private ObservableList<Appointment> appointments = FXCollections.observableArrayList();

    @FXML
    private void btnExecuteAction(ActionEvent event) {
        Integer index = cbReport.getSelectionModel().getSelectedIndex();
        reportFunctions.get(index).run(null);
    }

    @FXML
    private void cbReportAction(ActionEvent event) {
        if (cbReport.getSelectionModel().getSelectedItem().equals("Consultant Appointment Schedule")) {
            cbConsultant.getSelectionModel().select(0);
            cbConsultant.setVisible(true);
        } else {
            cbConsultant.getSelectionModel().select(0);
            cbConsultant.setVisible(false);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cbReport.setItems(reportNames);
        btnExecute.disableProperty().bind(Bindings.isNull(cbReport.getSelectionModel().selectedItemProperty()));

        reportFunctions.add(new Function() {
            @Override
            public void run(Object arg) {
                reportNumberOfAppointmentTypesByMonth();
            }
        });

        reportFunctions.add(new Function() {
            @Override
            public void run(Object arg) {
                reportConsultantAppointmentSchedule();
            }
        });

        reportFunctions.add(new Function() {
            @Override
            public void run(Object arg) {
                NumberOfAppointmentsByOffice();
            }
        });

        cbConsultant.setItems(
                dbUser.getUsers()
        );

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
    }

    public interface Function {
        public void run(Object arg);
    }

    /** ***************************************************************************************************************
     * Report displays the month, and total of each type of appointment in the given month
     ******************************************************************************************************************/

    public void reportNumberOfAppointmentTypesByMonth() {

        ObservableList<Appointment> types = dbAppointment.getAppointmentTypesByMonth();

        tblResults.getColumns().clear();

        TableColumn<String, Appointment> monthColumn = new TableColumn<>();
        monthColumn.setText("MONTH");
        monthColumn.setCellValueFactory(new PropertyValueFactory<>("date"));

        TableColumn<String, Appointment> typeColumn = new TableColumn<>();
        typeColumn.setText("TYPE");
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));

        TableColumn<String, Appointment> totalColumn = new TableColumn<>();
        totalColumn.setText("TOTAL");
        totalColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));

        tblResults.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        tblResults.getColumns().addAll(
                monthColumn,typeColumn,totalColumn);

        tblResults.setItems(types);

    }

    /** ***************************************************************************************************************
     * Report displays all appointments for selected consultant
     ******************************************************************************************************************/

    public void reportConsultantAppointmentSchedule() {

        User user = cbConsultant.getSelectionModel().getSelectedItem();
        ObservableList<Appointment> appointments = dbAppointment.getAppointments(user);

        tblResults.getColumns().clear();

        TableColumn<String, Appointment> consultantColumn = new TableColumn<>();
        consultantColumn.setText("CONSULTANT");
        consultantColumn.setCellValueFactory(new PropertyValueFactory<>("userName"));

        TableColumn<String, Appointment> customerColumn = new TableColumn<>();
        customerColumn.setText("CUSTOMER");
        customerColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));

        TableColumn<String, Appointment> locationColumn = new TableColumn<>();
        locationColumn.setText("LOCATION");
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));

        TableColumn<String, Appointment> dateColumn = new TableColumn<>();
        dateColumn.setText("DATE");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));

        TableColumn<String, Appointment> startColumn = new TableColumn<>();
        startColumn.setText("START");
        startColumn.setCellValueFactory(new PropertyValueFactory<>("start"));

        TableColumn<String, Appointment> endColumn = new TableColumn<>();
        endColumn.setText("END");
        endColumn.setCellValueFactory(new PropertyValueFactory<>("end"));

        tblResults.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        tblResults.getColumns().addAll(
                consultantColumn,customerColumn,locationColumn,dateColumn,startColumn,endColumn);

        tblResults.setItems(appointments);

    }

    /** ***************************************************************************************************************
     * Report displays the number of appointments in each office
     * got LAZY and wrote the SQL here instead of dbAppointment, you suck
     ******************************************************************************************************************/

    public void NumberOfAppointmentsByOffice() {
        ObservableList<Record> results = FXCollections.observableArrayList();

        try {
            Connection conn = db.getConnection();

            PreparedStatement ps = conn.prepareStatement(
                    "SELECT location AS LOCATION, " +
                            "COUNT(location) AS TOTAL " +
                            "FROM appointment " +
                            "GROUP BY location;");

            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                Record record = new Record();
                //record.setMonth(rs.getString("MONTH"));
                record.setType(rs.getString("LOCATION"));
                record.setCount(rs.getInt("TOTAL"));
                results.add(record);
            }
            ps.close();

        } catch (SQLException e) {
            System.out.println("SQLException->" + e.getMessage());
        } finally {
            tblResults.getColumns().clear();

            TableColumn col = new TableColumn();
            col.setText("LOCATION");
            col.setCellValueFactory(new PropertyValueFactory<>("type"));

            TableColumn col2 = new TableColumn();
            col2.setText("COUNT");
            col2.setCellValueFactory(new PropertyValueFactory<>("count"));

            tblResults.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
            tblResults.getColumns().addAll(col,col2);
            tblResults.setItems(results);
        }
    }

    public class Record {
        String month;
        String type;
        Integer count;

        public Record() {}

        public String getMonth() {
            return month;
        }

        public void setMonth(String month) {
            this.month = month;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public Integer getCount() {
            return count;
        }

        public void setCount(Integer count) {
            this.count = count;
        }
    }

}
