package Database;

import java.sql.*;

public class dbAddress {

    public static Integer getAddressId(String address, Integer cityId) {

        try {
            Connection conn = db.getConnection();

            PreparedStatement ps = conn.prepareStatement(
                    "SELECT addressId FROM address WHERE address = ? AND cityId = ?");
            ps.setString(1, address);
            ps.setInt(2, cityId);

            ResultSet rs = ps.executeQuery();

            Integer id = null;

            if(rs.next()) {
                id = rs.getInt("addressId");
            }

            rs.close();
            return id;

        } catch (SQLException e) {
            System.out.println("SQLException->" + e.getMessage());
            return null;
        }
    }

    public static Integer insertAddress(String address, Integer cityId, String postalCode, String phone) {

        try {
            Connection conn = db.getConnection();

            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO address ("
                            +"address, "
                            +"address2, "
                            +"cityId, "
                            +"postalCode, "
                            +"phone, "
                            +"createDate, "
                            +"createdBy, "
                            +"lastUpdate, "
                            +"lastUpdateBy) "
                            +"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?); ",
                    Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, address);
            ps.setString(2, "");
            ps.setInt(3, cityId);
            ps.setString(4, postalCode);
            ps.setString(5, phone);
            ps.setTimestamp(6, Utility.Time.now());
            ps.setString(7, "admin");
            ps.setTimestamp(8, Utility.Time.now());
            ps.setString(9, "admin");

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
}
