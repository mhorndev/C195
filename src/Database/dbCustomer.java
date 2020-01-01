package Database;

import Model.Address;
import Model.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class dbCustomer {

    public static Integer getCustomerId(String customerName, Integer addressId) {

        try {
            Connection conn = db.getConnection();

            PreparedStatement ps = conn.prepareStatement(
                    "SELECT customerId FROM customer "
                            +"WHERE "
                            +"customerName = ? AND "
                            +"addressId = ?");
            ps.setString(1, customerName);
            ps.setInt(2, addressId);

            ResultSet rs = ps.executeQuery();

            Integer id = null;

            if(rs.next()) {
                id = rs.getInt("customerId");
            }

            rs.close();
            return id;

        } catch (SQLException e) {
            System.out.println("SQLException->" + e.getMessage());
            return null;
        }
    }

    public static Integer insertCustomer(String customerName, Integer addressId, Integer active) {

        try {
            Connection conn = db.getConnection();

            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO customer ("
                            +"customerName, "
                            +"addressId, "
                            +"active, "
                            +"createDate, "
                            +"createdBy, "
                            +"lastUpdate, "
                            +"lastUpdateBy) "
                    +"VALUES (?, ?, ?, ?, ?, ?, ?); ",
                    Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, customerName);
            ps.setInt(2, addressId);
            ps.setInt(3, active);
            ps.setTimestamp(4, Utility.Time.now());
            ps.setString(5, "admin");
            ps.setTimestamp(6, Utility.Time.now());
            ps.setString(7, "admin");

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

    public static Integer updateCustomer(Integer customerId, String customerName, Integer addressId, Integer active) {

        try {
            Connection conn = db.getConnection();

            PreparedStatement ps = conn.prepareStatement(
                    "UPDATE customer SET "
                            +"customerName = ?, "
                            +"addressId = ?, "
                            +"active = ?, "
                            +"createDate = ?, "
                            +"createdBy = ? "
                    +"WHERE "
                            +"customerId = ?");

            ps.setString(1, customerName);
            ps.setInt(2, addressId);
            ps.setInt(3, active);
            ps.setTimestamp(4, Utility.Time.now());
            ps.setString(5, "admin");
            ps.setInt(6, customerId);

            ps.executeUpdate();
            ps.close();
            return customerId;

        } catch (SQLException e) {
            System.out.println("SQLException->" + e.getMessage());
            return null;
        }
    }

    public static boolean deleteCustomer(Integer customerId) {

        try {
            Connection conn = db.getConnection();

            PreparedStatement ps = conn.prepareStatement(
                    "DELETE FROM appointment WHERE "
                            +"customerId = ?");

            ps.setInt(1, customerId);
            ps.executeUpdate();

            ps = conn.prepareStatement(
                    "DELETE FROM customer WHERE "
                            +"customerId = ?");

            ps.setInt(1, customerId);
            ps.executeUpdate();

            ps.close();
            return true;

        } catch (SQLException e) {
            System.out.println("SQLException->" + e.getMessage());
            return false;
        }
    }

    public static ObservableList<Customer> getCustomers() {
        ObservableList<Customer> customers = FXCollections.observableArrayList();

        try {
            Connection conn = db.getConnection();

            PreparedStatement ps = conn.prepareStatement(
                    "SELECT "
                            +"customer.customerId, "
                            +"customer.customerName, "
                            +"customer.active, "
                            +"address.addressId, "
                            +"address.address, "
                            +"city.cityId, "
                            +"city.city, "
                            +"address.postalCode, "
                            +"country.country, "
                            +"address.phone "
                    +"FROM "
                            +"customer "
                    +"INNER JOIN address ON customer.addressId = address.addressId "
                    +"INNER JOIN city ON address.cityId = city.cityId "
                    +"INNER JOIN country ON city.countryId = country.countryId");

            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                Customer customer = new Customer();
                customer.setCustomerId(rs.getInt("customerId"));
                customer.setCustomerName(rs.getString("customerName"));
                customer.setAddressId(rs.getInt("addressId"));
                customer.setActive(rs.getInt("active"));
                customer.setAddress(rs.getString("address"));
                customer.setCity(rs.getString("city"));
                customer.setPostalCode(rs.getString("postalCode"));
                customer.setPhone(rs.getString("phone"));
                customer.setCountry(rs.getString("country"));
                customers.add(customer);
            }

            rs.close();

        } catch (SQLException e) {
            System.out.println("SQLException->" + e.getMessage());
        }

        return customers;
    }
}
