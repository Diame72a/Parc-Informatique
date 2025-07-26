package ui;

import com.formdev.flatlaf.FlatDarkLaf;
import dao.TicketDAO;
import model.Ticket;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.Timestamp;
import java.util.List;

public class TicketUI extends JFrame {

    private final int userId;
    private JTable table;
    private DefaultTableModel model;

    public TicketUI(int userId) {
        this.userId = userId;
        setTitle("Mes Tickets - Utilisateur ID " + userId);
        setSize(900, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        initComponents();
        loadTickets();
    }

    private void initComponents() {
        model = new DefaultTableModel(new String[]{"ID", "Titre", "Description", "Statut", "Créé le", "Modifié le"}, 0) {
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        table = new JTable(model);
        table.setRowHeight(30);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scroll = new JScrollPane(table);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton btnAdd = createButton("Ajouter");
        JButton btnEdit = createButton("Modifier");
        JButton btnDelete = createButton("Supprimer");

        btnPanel.add(btnAdd);
        btnPanel.add(btnEdit);
        btnPanel.add(btnDelete);

        btnEdit.setEnabled(false);
        btnDelete.setEnabled(false);

        table.getSelectionModel().addListSelectionListener(e -> {
            boolean selected = table.getSelectedRow() != -1;
            btnEdit.setEnabled(selected);
            btnDelete.setEnabled(selected);
        });

        btnAdd.addActionListener(e -> openTicketForm(null));
        btnEdit.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                int ticketId = (int) model.getValueAt(row, 0);
                Ticket ticket = TicketDAO.getTicketById(ticketId);
                if (ticket != null) openTicketForm(ticket);
            }
        });

        btnDelete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                int ticketId = (int) model.getValueAt(row, 0);
                int confirm = JOptionPane.showConfirmDialog(this, "Confirmer la suppression ?", "Confirmation", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    if (TicketDAO.deleteTicket(ticketId)) {
                        loadTickets();
                        JOptionPane.showMessageDialog(this, "Ticket supprimé !");
                    } else {
                        JOptionPane.showMessageDialog(this, "Erreur de suppression.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        add(scroll, BorderLayout.CENTER);
        add(btnPanel, BorderLayout.SOUTH);
    }

    private void loadTickets() {
        model.setRowCount(0);
        List<Ticket> tickets = TicketDAO.getTicketsByUserId(userId);
        for (Ticket t : tickets) {
            model.addRow(new Object[]{
                    t.getId(),
                    t.getTitle(),
                    t.getDescription(),
                    t.getStatus(),
                    t.getCreatedAt(),
                    t.getUpdatedAt()
            });
        }
    }

    private void openTicketForm(Ticket ticket) {
        JDialog dialog = new JDialog(this, ticket == null ? "Nouveau Ticket" : "Modifier Ticket", true);
        dialog.setSize(400, 350);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblTitle = new JLabel("Titre:");
        JTextField txtTitle = new JTextField(20);

        JLabel lblDesc = new JLabel("Description:");
        JTextArea txtDesc = new JTextArea(5, 20);
        JScrollPane spDesc = new JScrollPane(txtDesc);

        JLabel lblStatus = new JLabel("Statut:");
        JComboBox<String> cbStatus = new JComboBox<>(new String[]{"open", "in_progress", "closed"});

        if (ticket != null) {
            txtTitle.setText(ticket.getTitle());
            txtDesc.setText(ticket.getDescription());
            cbStatus.setSelectedItem(ticket.getStatus());
        }

        gbc.gridx = 0; gbc.gridy = 0;
        dialog.add(lblTitle, gbc);
        gbc.gridx = 1;
        dialog.add(txtTitle, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        dialog.add(lblDesc, gbc);
        gbc.gridx = 1;
        dialog.add(spDesc, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        dialog.add(lblStatus, gbc);
        gbc.gridx = 1;
        dialog.add(cbStatus, gbc);

        JButton btnSave = new JButton("Enregistrer");
        btnSave.setBackground(new Color(45, 120, 220));
        btnSave.setForeground(Color.WHITE);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        dialog.add(btnSave, gbc);

        btnSave.addActionListener(e -> {
            String title = txtTitle.getText().trim();
            String desc = txtDesc.getText().trim();
            String status = (String) cbStatus.getSelectedItem();

            if (title.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Le titre est obligatoire.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            boolean result;
            if (ticket == null) {
                Ticket newTicket = new Ticket();
                newTicket.setUserId(userId);
                newTicket.setTitle(title);
                newTicket.setDescription(desc);
                newTicket.setStatus(status);
                newTicket.setCreatedAt(new Timestamp(System.currentTimeMillis()));
                newTicket.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
                result = TicketDAO.insertTicket(newTicket);
            } else {
                ticket.setTitle(title);
                ticket.setDescription(desc);
                ticket.setStatus(status);
                ticket.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
                result = TicketDAO.updateTicket(ticket);
            }

            if (result) {
                loadTickets();
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Erreur lors de l'enregistrement.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.setVisible(true);
    }

    private JButton createButton(String label) {
        JButton btn = new JButton(label);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(new Color(45, 120, 220));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TicketUI(1).setVisible(true));
    }
}
