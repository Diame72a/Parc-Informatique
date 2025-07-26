package dao;

import database.DatabaseConnection;
import model.User;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;

public class UserDAO {

    // Méthode pour insérer un utilisateur avec hash du mot de passe
    public static boolean insertUser(User user) throws SQLException {
        String sql = "INSERT INTO users (username, fullname, email, role, password, created_at) VALUES (?, ?, ?, ?, ?, NOW())";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String hashedPassword = BCrypt.hashpw(user.getPasswordHash(), BCrypt.gensalt());

            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getFullName());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getRole());
            stmt.setString(5, hashedPassword);

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;
        }
    }

    // Méthode login : retourne User si authentifié, sinon null
    public static User login(String username, String plainPassword) throws SQLException {
        String sql = "SELECT * FROM users WHERE username = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String storedHash = rs.getString("password");

                    if (BCrypt.checkpw(plainPassword, storedHash)) {
                        // Création de l'objet User avec les données
                        return new User(
                                rs.getInt("id"),
                                rs.getString("username"),
                                rs.getString("fullname"),
                                rs.getString("email"),
                                rs.getString("role"),
                                storedHash,
                                rs.getTimestamp("created_at")
                        );
                    }
                }
            }
        }
        return null; // échec login
    }
}
