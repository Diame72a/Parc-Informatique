package dao;

import database.DatabaseConnection;
import model.Ticket;

import javax.swing.table.TableRowSorter;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TicketDAO {


    public static List<Ticket> getTicketsByUserId(int userId) {
        List<Ticket> tickets = new ArrayList<>();
        String sql = "SELECT * FROM tickets WHERE user_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Ticket ticket = new Ticket(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getInt("machine_id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getString("status"),
                        rs.getTimestamp("created_at"),
                        rs.getTimestamp("updated_at")
                );
                tickets.add(ticket);
            }

        } catch (SQLException e) {
            e.printStackTrace(); // En prod, utiliser un logger !
        }

        return tickets;
    }

    public static Ticket getTicketById(int id) {
        Ticket ticket = null;
        String sql = "SELECT * FROM tickets WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    ticket = new Ticket();
                    ticket.setId(rs.getInt("id"));
                    ticket.setUserId(rs.getInt("user_id"));
                    ticket.setTitle(rs.getString("title"));
                    ticket.setDescription(rs.getString("description"));
                    ticket.setStatus(rs.getString("status"));
                    ticket.setCreatedAt(rs.getTimestamp("created_at"));
                    ticket.setUpdatedAt(rs.getTimestamp("updated_at"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ticket;
    }

    public static boolean insertTicket(Ticket ticket) {
        String sql = "INSERT INTO tickets (user_id, machine_id, title, description, status,  created_at, updated_at)" + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, ticket.getUserId());
            stmt.setInt(2, ticket.getMachineId());
            stmt.setString(3, ticket.getTitle());
            stmt.setString(4, ticket.getDescription());
            stmt.setString(5, ticket.getStatus());
            stmt.setTimestamp(6, ticket.getCreatedAt());
            stmt.setTimestamp(7, ticket.getUpdatedAt());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean updateTicket(Ticket ticket) {
        String sql = "UPDATE tickets SET title = ?, description = ?, status = ?, machineId = ?, updated_at = ? WHERE user_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, ticket.getTitle());
            stmt.setString(2, ticket.getDescription());
            stmt.setString(3, ticket.getStatus());
            stmt.setInt(4, ticket.getMachineId());
            stmt.setTimestamp(5, ticket.getUpdatedAt());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean deleteTicket(int ticketId) {
        String sql = "DELETE FROM tickets WHERE user_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, ticketId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }
}
