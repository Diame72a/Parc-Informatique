package ui;

import model.Machine;
import model.User;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.*;

public class MenuUI extends JFrame {

    private User user;

    public MenuUI(User user) {
        this.user = user;

        // Setup Look and Feel
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }

        setTitle("Menu principal - " + user.getFullName());
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        initUI();

        setVisible(true);
    }

    private void initUI() {
        // Fond bleu professionnel
        getContentPane().setBackground(new Color(10, 60, 130)); // bleu foncé

        // Layout central
        setLayout(new BorderLayout());

        // Label accueil
        JLabel welcomeLabel = new JLabel("Bienvenue, " + user.getFullName() + " !");
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(welcomeLabel, BorderLayout.NORTH);

        // Panel boutons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(10, 60, 130));
        buttonPanel.setLayout(new GridLayout(2, 1, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 50, 50));

        JButton parcButton = createButton("Parc Informatique");
        JButton ticketsButton = createButton("Gestion des Tickets");

        // Actions boutons
       parcButton.addActionListener(e -> {
           new MachineUI(); // À coder si besoin
            dispose();
        });

        ticketsButton.addActionListener(e -> {
            TicketUI ticketUI = new TicketUI(user.getId());
            ticketUI.setVisible(true);  // Important !
            dispose();
        });

        buttonPanel.add(parcButton);
        buttonPanel.add(ticketsButton);

        add(buttonPanel, BorderLayout.CENTER);
    }

    private JButton createButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setBackground(new Color(45, 120, 220));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    // Main pour test rapide
    public static void main(String[] args) {
        // Dummy user pour test
        User user = new User(1, "jdoe", "John Doe", "jdoe@example.com", "user", "", null);
        SwingUtilities.invokeLater(() -> new MenuUI(user));
    }
}
