package Database;

import java.sql.*;

public class dbCountry {

    public static Integer getCountryId(String country) {

        try {
            Connection conn = db.getConnection();

            PreparedStatement ps = conn.prepareStatement(
                    "SELECT countryId, country FROM country WHERE country = ?");

            ps.setString(1, country);

            ResultSet rs = ps.executeQuery();

            Integer id = null;

            if(rs.next()) {
                id = rs.getInt("countryId");
            }

            rs.close();
            return id;

        } catch (SQLException e) {
            System.out.println("SQLException->" + e.getMessage());
            return null;
        }
    }

    public static Integer insertCountry(String country) {

        try {
            Connection conn = db.getConnection();

            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO country ("
                            +"country, "
                            +"createDate, "
                            +"createdBy, "
                            +"lastUpdate, "
                            +"lastUpdateBy) "
                            +"VALUES (?, ?, ?, ?, ?); ",
                    Statement.RETURN_GENERATED_KEYS);

               ps.setString(1, country);
            ps.setTimestamp(2, Utility.Time.now());
               ps.setString(3, "admin");
            ps.setTimestamp(4, Utility.Time.now());
               ps.setString(5, "admin");

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
