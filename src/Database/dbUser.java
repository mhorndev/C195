package Database;

import Model.Customer;
import Model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class dbUser {

    public static ObservableList<User> getUsers() {
        ObservableList<User> users = FXCollections.observableArrayList();

        try {
            Connection conn = db.getConnection();

            PreparedStatement ps = conn.prepareStatement(
                    "SELECT * FROM user");

            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                User user = new User();
                user.setUserId(rs.getInt("userId"));
                user.setUserName(rs.getString("userName"));
                user.setPassword(rs.getString("password"));
                user.setActive(rs.getInt("active"));
                users.add(user);
            }

            rs.close();

        } catch (SQLException e) {
            System.out.println("SQLException->" + e.getMessage());
        }

        return users;
    }

    public static ObservableList<String> getConsultantNames() {
        ObservableList<String> consultants = FXCollections.observableArrayList();

        try {
            Connection conn = db.getConnection();

            PreparedStatement ps = conn.prepareStatement(
                    "SELECT * FROM user");

            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                consultants.add(rs.getString("userName"));
            }

            rs.close();

        } catch (SQLException e) {
            System.out.println("SQLException->" + e.getMessage());
        }

        return consultants;
    }

    public static User login(String username, String password) {
        User user = new User();

        try {
            Connection conn = db.getConnection();

            PreparedStatement ps = conn.prepareStatement(
                    "SELECT * FROM user WHERE userName = ?");

            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if(rs.next()) {
                user.setUserId(rs.getInt("userId"));
                user.setUserName(rs.getString("userName"));
                user.setActive(rs.getInt("active"));

                if (rs.getString("password").equals(password)) {
                    user.setPassword("");
                } else {
                    user.setPassword(null);
                }
            }

            rs.close();

        } catch (SQLException e) {
            System.out.println("SQLException->" + e.getMessage());
        }

        return user;
    }
}
