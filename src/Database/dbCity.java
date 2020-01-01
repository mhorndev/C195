package Database;

import java.sql.*;

public class dbCity {

    public static Integer getCityId(String city, Integer countryId) {

        try {
            Connection conn = db.getConnection();

            PreparedStatement ps = conn.prepareStatement(
                    "SELECT cityId FROM city WHERE city = ? AND countryId = ?");
            ps.setString(1, city);
            ps.setInt(2, countryId);

            ResultSet rs = ps.executeQuery();

            Integer id = null;

            if(rs.next()) {
                id = rs.getInt("cityId");
            }

            rs.close();
            return id;

        } catch (SQLException e) {
            System.out.println("SQLException->" + e.getMessage());
            return null;
        }
    }

    public static Integer insertCity(String city, Integer countryId) {

        try {
            Connection conn = db.getConnection();

            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO city ("
                            +"city, "
                            +"countryId, "
                            +"createDate, "
                            +"createdBy, "
                            +"lastUpdate, "
                            +"lastUpdateBy) "
                            +"VALUES (?, ?, ?, ?, ?, ?); ",
                    Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, city);
            ps.setInt(2, countryId);
            ps.setTimestamp(3, Utility.Time.now());
            ps.setString(4, "admin");
            ps.setTimestamp(5, Utility.Time.now());
            ps.setString(6, "admin");

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

