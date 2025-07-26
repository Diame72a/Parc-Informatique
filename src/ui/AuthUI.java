package ui;

import com.formdev.flatlaf.FlatLightLaf;
import dao.UserDAO;
import model.User;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.SQLException;

public class AuthUI extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginBtn, switchToRegisterBtn;

    private JTextField regUsernameField, regFullNameField, regEmailField;
    private JPasswordField regPasswordField, regConfirmField;
    private JButton registerBtn, switchToLoginBtn;

    private JPanel mainPanel;
    private CardLayout cardLayout;

    private final Color blueProfessional = new Color(10, 65, 130); // bleu pro

    public AuthUI() {
        super("Connexion / Inscription");
        FlatLightLaf.setup();

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(createLoginPanel(), "login");
        mainPanel.add(createRegisterPanel(), "register");

        add(mainPanel);

        setSize(450, 380);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Fond bleu pro pour la fenêtre
        getContentPane().setBackground(blueProfessional);
        mainPanel.setBackground(blueProfessional);

        setVisible(true);
    }

    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(blueProfessional);
        GridBagConstraints gbc = new GridBagConstraints();

        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        loginBtn = new JButton("Connexion");
        switchToRegisterBtn = new JButton("S'inscrire");

        styleButton(loginBtn);
        styleButton(switchToRegisterBtn);

        styleTextField(usernameField);
        styleTextField(passwordField);

        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        JLabel lblUser = new JLabel("Nom d'utilisateur");
        lblUser.setForeground(Color.WHITE);
        panel.add(lblUser, gbc);

        gbc.gridx = 1; panel.add(usernameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        JLabel lblPass = new JLabel("Mot de passe");
        lblPass.setForeground(Color.WHITE);
        panel.add(lblPass, gbc);

        gbc.gridx = 1; panel.add(passwordField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(loginBtn, gbc);

        gbc.gridy = 3;
        panel.add(switchToRegisterBtn, gbc);

        loginBtn.addActionListener(this::handleLogin);
        switchToRegisterBtn.addActionListener(e -> cardLayout.show(mainPanel, "register"));

        return panel;
    }

    private JPanel createRegisterPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(blueProfessional);
        GridBagConstraints gbc = new GridBagConstraints();

        regUsernameField = new JTextField(20);
        regFullNameField = new JTextField(20);
        regEmailField = new JTextField(20);
        regPasswordField = new JPasswordField(20);
        regConfirmField = new JPasswordField(20);
        registerBtn = new JButton("Créer un compte");
        switchToLoginBtn = new JButton("Déjà inscrit ?");

        styleButton(registerBtn);
        styleButton(switchToLoginBtn);

        styleTextField(regUsernameField);
        styleTextField(regFullNameField);
        styleTextField(regEmailField);
        styleTextField(regPasswordField);
        styleTextField(regConfirmField);

        gbc.insets = new Insets(8, 8, 8, 8);
        int y = 0;

        JLabel lbl;

        lbl = new JLabel("Nom d'utilisateur");
        lbl.setForeground(Color.WHITE);
        gbc.gridx = 0; gbc.gridy = y; gbc.anchor = GridBagConstraints.WEST;
        panel.add(lbl, gbc);
        gbc.gridx = 1; panel.add(regUsernameField, gbc);

        lbl = new JLabel("Nom complet");
        lbl.setForeground(Color.WHITE);
        gbc.gridx = 0; gbc.gridy = ++y;
        panel.add(lbl, gbc);
        gbc.gridx = 1; panel.add(regFullNameField, gbc);

        lbl = new JLabel("Email");
        lbl.setForeground(Color.WHITE);
        gbc.gridx = 0; gbc.gridy = ++y;
        panel.add(lbl, gbc);
        gbc.gridx = 1; panel.add(regEmailField, gbc);

        lbl = new JLabel("Mot de passe");
        lbl.setForeground(Color.WHITE);
        gbc.gridx = 0; gbc.gridy = ++y;
        panel.add(lbl, gbc);
        gbc.gridx = 1; panel.add(regPasswordField, gbc);

        lbl = new JLabel("Confirmation");
        lbl.setForeground(Color.WHITE);
        gbc.gridx = 0; gbc.gridy = ++y;
        panel.add(lbl, gbc);
        gbc.gridx = 1; panel.add(regConfirmField, gbc);

        gbc.gridx = 0; gbc.gridy = ++y; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(registerBtn, gbc);
        gbc.gridy++;
        panel.add(switchToLoginBtn, gbc);

        registerBtn.addActionListener(this::handleRegister);
        switchToLoginBtn.addActionListener(e -> cardLayout.show(mainPanel, "login"));

        return panel;
    }

    private void styleButton(JButton btn) {
        btn.setBackground(new Color(30, 90, 180));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        btn.setOpaque(true);
        btn.setBorderPainted(false);
    }

    private void styleTextField(JTextComponent field) {
        field.setBackground(new Color(220, 230, 250));
        field.setForeground(Color.BLACK);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setCaretColor(Color.BLACK);
    }

    private void handleLogin(ActionEvent e) {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            User user = UserDAO.login(username, password);
            if (user != null) {
                JOptionPane.showMessageDialog(this, "Bienvenue " + user.getFullName() + " !");
                new MenuUI(user);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Identifiants invalides", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur base de données", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleRegister(ActionEvent e) {
        String username = regUsernameField.getText().trim();
        String fullname = regFullNameField.getText().trim();
        String email = regEmailField.getText().trim();
        String pass = new String(regPasswordField.getPassword());
        String confirm = new String(regConfirmField.getPassword());

        if (username.isEmpty() || fullname.isEmpty() || email.isEmpty() || pass.isEmpty() || confirm.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!pass.equals(confirm)) {
            JOptionPane.showMessageDialog(this, "Les mots de passe ne correspondent pas", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            User newUser = new User(username, fullname, email, "user", pass);
            if (UserDAO.insertUser(newUser)) {
                JOptionPane.showMessageDialog(this, "Compte créé !");
                cardLayout.show(mainPanel, "login");
            } else {
                JOptionPane.showMessageDialog(this, "Échec de l'inscription", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur base de données", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AuthUI::new);
    }
}
