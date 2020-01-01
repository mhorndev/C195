package Database;

import Controller.ReportMain;
import Model.Appointment;
import Model.Customer;
import Model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class dbAppointment {

    public static boolean insertAppointment(User consultant, Customer customer, String type, String date, String startTime, String endTime, String location) {

        try {
            Connection conn = db.getConnection();

            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO appointment ("
                            + "customerId, "
                            + "userId, "
                            + "title, "
                            + "description, "
                            + "location, "
                            + "contact, "
                            + "type, "
                            + "url, "
                            + "start, "
                            + "end, "
                            + "createDate, "
                            + "createdBy, "
                            + "lastUpdate, "
                            + "lastUpdateBy) "
                            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?); ",
                    Statement.RETURN_GENERATED_KEYS);

            ps.setInt(1, customer.getCustomerId());
            ps.setInt(2, consultant.getUserId());
            ps.setString(3, ""); //title
            ps.setString(4, ""); //description
            ps.setString(5, location);
            ps.setString(6, ""); //contact
            ps.setString(7, type);
            ps.setString(8, ""); //url

            String startDateTime = date + " " + startTime;

            LocalDateTime startLocalDateTime = LocalDateTime.parse(
                    startDateTime,
                    DateTimeFormatter.ofPattern("yyyy-M-d h:mm a"));

            ZonedDateTime startZonedDateTime = startLocalDateTime.atZone(Objects.requireNonNull(getZoneId(location)));
            Timestamp startTimestamp = Timestamp.from(startZonedDateTime.toInstant());

            String endDateTime = date + " " + endTime;

            LocalDateTime endLocalDateTime = LocalDateTime.parse(
                    endDateTime,
                    DateTimeFormatter.ofPattern("yyyy-M-d h:mm a"));

            ZonedDateTime endZonedDateTime = endLocalDateTime.atZone(Objects.requireNonNull(getZoneId(location)));
            Timestamp endTimestamp = Timestamp.from(endZonedDateTime.toInstant());

            ps.setTimestamp(9, startTimestamp);
            ps.setTimestamp(10, endTimestamp);

            ps.setTimestamp(11, Utility.Time.now());
            ps.setString(12, "admin");
            ps.setTimestamp(13, Utility.Time.now());
            ps.setString(14, "admin");

            ps.executeUpdate();
            ResultSet generatedKeys = ps.getGeneratedKeys();

            boolean success = false;

            if (generatedKeys.next()) {
                success = true;
            }

            ps.close();
            return success;

        } catch (SQLException e) {
            System.out.println("SQLException->" + e.getMessage());
            return false;
        }
    }

    public static boolean updateAppointment(Integer appointmentId, User consultant, Customer customer, String type, String date, String startTime, String endTime, String location) {

        try {
            Connection conn = db.getConnection();

            PreparedStatement ps = conn.prepareStatement(
                    "UPDATE appointment SET "
                            +"customerId = ?, "
                            +"userId = ?, "
                            +"location = ?, "
                            +"contact = ?, "
                            +"type = ?, "
                            +"url = ?, "
                            +"start = ?, "
                            +"end = ?, "
                            +"lastUpdate = ?, "
                            +"lastUpdateBy = ? "
                            +"WHERE "
                            +"appointmentId = ?");

            ps.setInt(1, customer.getCustomerId());
            ps.setInt(2, consultant.getUserId());
            ps.setString(3, location);
            ps.setString(4, ""); //contact
            ps.setString(5, type);
            ps.setString(6, ""); //url

            String startDateTime = date + " " + startTime;

            LocalDateTime startLocalDateTime = LocalDateTime.parse(
                    startDateTime,
                    DateTimeFormatter.ofPattern("yyyy-M-d h:mm a"));

            ZonedDateTime startZonedDateTime = startLocalDateTime.atZone(Objects.requireNonNull(getZoneId(location)));
            Timestamp startTimestamp = Timestamp.from(startZonedDateTime.toInstant());

            String endDateTime = date + " " + endTime;

            LocalDateTime endLocalDateTime = LocalDateTime.parse(
                    endDateTime,
                    DateTimeFormatter.ofPattern("yyyy-M-d h:mm a"));

            ZonedDateTime endZonedDateTime = endLocalDateTime.atZone(Objects.requireNonNull(getZoneId(location)));
            Timestamp endTimestamp = Timestamp.from(endZonedDateTime.toInstant());

            ps.setTimestamp(7, startTimestamp);
            ps.setTimestamp(8, endTimestamp);

            ps.setTimestamp(9, Utility.Time.now());
            ps.setString(10, "admin");
            ps.setInt(11, appointmentId);

            ps.executeUpdate();
            ps.close();
            return true;

        } catch (SQLException e) {
            System.out.println("SQLException->" + e.getMessage());
            return false;
        }
    }

    public static boolean deleteAppointment(Appointment appointment) {

        try {
            Connection conn = db.getConnection();

            PreparedStatement ps = conn.prepareStatement(
                    "DELETE FROM appointment WHERE "
                            +"appointmentId = ?");

            ps.setInt(1, appointment.getAppointmentId());

            ps.executeUpdate();
            ps.close();
            return true;

        } catch (SQLException e) {
            System.out.println("SQLException->" + e.getMessage());
            return false;
        }
    }

    public static ObservableList<Appointment> getAppointments() {
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();

        try {
            Connection conn = db.getConnection();

            PreparedStatement ps = conn.prepareStatement(
                    "SELECT "
                            +"appointment.appointmentId, "
                            +"appointment.customerId, "
                            +"appointment.userId, "
                            +"appointment.title, "
                            +"appointment.description, "
                            +"appointment.location, "
                            +"appointment.contact, "
                            +"appointment.type, "
                            +"appointment.url, "
                            +"appointment.start, "
                            +"appointment.end, "
                            +"customer.customerId, "
                            +"customer.customerName, "
                            +"user.userId, "
                            +"user.userName "
                    +"FROM "
                            +"appointment "
                    +"INNER JOIN user ON appointment.userId = user.userId "
                    +"INNER JOIN customer ON appointment.customerId = customer.customerId "
                    +"ORDER BY appointment.start");

            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                Appointment appointment = new Appointment();
                appointment.setAppointmentId(rs.getInt("appointmentId"));
                appointment.setCustomerId(rs.getInt("customerId"));
                appointment.setCustomerName(rs.getString("customerName"));
                appointment.setUserId(rs.getInt("userId"));
                appointment.setUserName(rs.getString("userName"));
                appointment.setTitle(rs.getString("title"));
                appointment.setDescription(rs.getString("description"));
                appointment.setLocation(rs.getString("location"));
                appointment.setContact(rs.getString("contact"));
                appointment.setType(rs.getString("type"));
                appointment.setUrl(rs.getString("url"));

                LocalDateTime startLocalDateTime = rs.getTimestamp("start").toInstant()
                        .atZone(Objects.requireNonNull(getZoneId(rs.getString("location"))))
                        .toLocalDateTime();

                LocalDateTime endLocalDateTime = rs.getTimestamp("end").toInstant()
                        .atZone(Objects.requireNonNull(getZoneId(rs.getString("location"))))
                        .toLocalDateTime();

                appointment.setDate(startLocalDateTime.format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));
                appointment.setStart(startLocalDateTime.format(DateTimeFormatter.ofPattern("h:mm a")));
                appointment.setEnd(endLocalDateTime.format(DateTimeFormatter.ofPattern("h:mm a")));

                appointments.add(appointment);
            }

            rs.close();

        } catch (SQLException e) {
            System.out.println("SQLException->" + e.getMessage());
        }

        return appointments;
    }

    public static ObservableList<Appointment> getAppointments(User user) {
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();

        try {
            Connection conn = db.getConnection();

            PreparedStatement ps = conn.prepareStatement(
                    "SELECT "
                            +"appointment.appointmentId, "
                            +"appointment.customerId, "
                            +"appointment.userId, "
                            +"appointment.title, "
                            +"appointment.description, "
                            +"appointment.location, "
                            +"appointment.contact, "
                            +"appointment.type, "
                            +"appointment.url, "
                            +"appointment.start, "
                            +"appointment.end, "
                            +"customer.customerId, "
                            +"customer.customerName, "
                            +"user.userId, "
                            +"user.userName "
                            +"FROM "
                            +"appointment "
                            +"INNER JOIN user ON appointment.userId = user.userId "
                            +"INNER JOIN customer ON appointment.customerId = customer.customerId "
                            +"WHERE appointment.userId = " + user.getUserId() + " "
                            +"ORDER BY appointment.start");

            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                Appointment appointment = new Appointment();
                appointment.setAppointmentId(rs.getInt("appointmentId"));
                appointment.setCustomerId(rs.getInt("customerId"));
                appointment.setCustomerName(rs.getString("customerName"));
                appointment.setUserId(rs.getInt("userId"));
                appointment.setUserName(rs.getString("userName"));
                appointment.setTitle(rs.getString("title"));
                appointment.setDescription(rs.getString("description"));
                appointment.setLocation(rs.getString("location"));
                appointment.setContact(rs.getString("contact"));
                appointment.setType(rs.getString("type"));
                appointment.setUrl(rs.getString("url"));

                LocalDateTime startLocalDateTime = rs.getTimestamp("start").toInstant()
                        .atZone(Objects.requireNonNull(getZoneId(rs.getString("location"))))
                        .toLocalDateTime();

                LocalDateTime endLocalDateTime = rs.getTimestamp("end").toInstant()
                        .atZone(Objects.requireNonNull(getZoneId(rs.getString("location"))))
                        .toLocalDateTime();

                appointment.setDate(startLocalDateTime.format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));
                appointment.setStart(startLocalDateTime.format(DateTimeFormatter.ofPattern("h:mm a")));
                appointment.setEnd(endLocalDateTime.format(DateTimeFormatter.ofPattern("h:mm a")));

                appointments.add(appointment);
            }

            rs.close();

        } catch (SQLException e) {
            System.out.println("SQLException->" + e.getMessage());
        }

        return appointments;
    }

    public static ObservableList<Appointment> getAppointmentTypesByMonth() {

        ObservableList<Appointment> appointments = FXCollections.observableArrayList();

        try {
            Connection conn = db.getConnection();

            PreparedStatement ps = conn.prepareStatement(
                    "SELECT MONTHNAME(start) AS MONTH, " +
                            "type AS TYPE, " +
                            "COUNT(type) AS TOTAL " +
                            "FROM appointment " +
                            "GROUP BY type, MONTH(start) ORDER BY MONTH(start);");

            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                Appointment appointment = new Appointment();
                appointment.setDate(rs.getString("MONTH"));
                appointment.setType(rs.getString("TYPE"));
                appointment.setAppointmentId(rs.getInt("TOTAL"));
                appointments.add(appointment);
            }
            ps.close();

        } catch (SQLException e) {
            System.out.println("SQLException->" + e.getMessage());
        }
        return appointments;
    }


    public static ZoneId getZoneId(String location) {

        switch(location)
        {
            case "New York Office" : return ZoneId.of("America/New_York");
            case "Phoenix Office"  : return ZoneId.of("America/Phoenix");
            case "London Office"   : return ZoneId.of("Europe/London");
            default : return null;
        }
    }

    ///

    public static Integer insertTestAppointment(Integer userId, Integer customerId, Timestamp timestamp) {

        try {
            Connection conn = db.getConnection();

            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO appointment ("
                            + "customerId, "
                            + "userId, "
                            + "title, "
                            + "description, "
                            + "location, "
                            + "contact, "
                            + "type, "
                            + "url, "
                            + "start, "
                            + "end, "
                            + "createDate, "
                            + "createdBy, "
                            + "lastUpdate, "
                            + "lastUpdateBy) "
                            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?); ",
                    Statement.RETURN_GENERATED_KEYS);

            ps.setInt(1, customerId); //customerId
            ps.setInt(2, userId); //userId
            ps.setString(3, ""); //title
            ps.setString(4, ""); //description
            ps.setString(5, ""); //location
            ps.setString(6, ""); //contact
            ps.setString(7, ""); //type
            ps.setString(8, ""); //url
            ps.setTimestamp(9, timestamp); //start
            ps.setTimestamp(10, timestamp); //end
            ps.setTimestamp(11, Utility.Time.now()); //createDate
            ps.setString(12, "admin"); //createdBy
            ps.setTimestamp(13, Utility.Time.now()); //lastUpdate
            ps.setString(14, "admin"); //lastUpdateBy

            ps.executeUpdate();
            ResultSet generatedKeys = ps.getGeneratedKeys();

            Integer id = null;

            if (generatedKeys.next()) {
                id = generatedKeys.getInt(1);
            }

            ps.close();
            return id;

        } catch (SQLException e) {
            System.out.println("SQLException->" + e.getMessage());
            return null;
        }
    }

    public static Timestamp getTestAppointment(Integer id) {

        try {
            Connection conn = db.getConnection();

            PreparedStatement ps = conn.prepareStatement(
                    "SELECT * FROM appointment WHERE appointmentId = ?");

            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            if(rs.next()) {
                //ps.close();
                return rs.getTimestamp("start");
            }

            ps.close();

        } catch (SQLException e) {
            System.out.println("SQLException->" + e.getMessage());
        }

        return null;
    }

}
