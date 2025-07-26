package dao;

import model.Machine;
import database.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MachineDAO {

    public static List<Machine> getAllMachines() {
        List<Machine> machines = new ArrayList<>();
        String sql = "SELECT * FROM machines";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                machines.add(new Machine(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("type"),
                        rs.getString("status")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return machines;
    }

    public static void insertMachine(Machine machine) throws SQLException {
        String sql = "INSERT INTO machines (name, type, status) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, machine.getName());
            stmt.setString(2, machine.getType());
            stmt.setString(3, machine.getStatus());
            stmt.executeUpdate();
        }
    }

    public static void updateMachine(Machine machine) throws SQLException {
        String sql = "UPDATE machines SET name = ?, type = ?, status = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, machine.getName());
            stmt.setString(2, machine.getType());
            stmt.setString(3, machine.getStatus());
            stmt.setInt(4, machine.getId());
            stmt.executeUpdate();
        }
    }

    public static void deleteMachine(int id) throws SQLException {
        String sql = "DELETE FROM machines WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public static Machine getMachineById(int id) {
        String sql = "SELECT * FROM machines WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Machine(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("type"),
                            rs.getString("status")
                    );
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
