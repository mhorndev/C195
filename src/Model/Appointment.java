package Model;

import java.sql.Timestamp;

public class Appointment {

    private Integer appointmentId;
    private Integer customerId;
    private Integer userId;
    private String title;
    private String description;
    private String location;
    private String contact;
    private String type;
    private String url;

    private String date;
    private String start;
    private String end;

    private String startLocal;
    private String endLocal;

    private String timeUntil;

    private String userName;
    private String customerName;

    private Timestamp tsStart;
    private Timestamp tsEnd;

    public Appointment() {}

    public Integer getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Integer appointmentId) {
        this.appointmentId = appointmentId;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStartLocal() {
        return startLocal;
    }

    public void setStartLocal(String startLocal) {
        this.startLocal = startLocal;
    }

    public String getEndLocal() {
        return endLocal;
    }

    public void setEndLocal(String endLocal) {
        this.endLocal = endLocal;
    }

    public String getTimeUntil() {
        return timeUntil;
    }

    public void setTimeUntil(String timeUntil) {
        this.timeUntil = timeUntil;
    }

    public Timestamp getTsStart() {
        return tsStart;
    }

    public void setTsStart(Timestamp tsStart) {
        this.tsStart = tsStart;
    }

    public Timestamp getTsEnd() {
        return tsEnd;
    }

    public void setTsEnd(Timestamp tsEnd) {
        this.tsEnd = tsEnd;
    }
}
