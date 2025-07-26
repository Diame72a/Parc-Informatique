package ui;

import com.formdev.flatlaf.FlatLightLaf;
import dao.MachineDAO;
import model.Machine;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class MachineUI extends JFrame {

    private JTable table;
    private DefaultTableModel model;

    private JButton addBtn, editBtn, deleteBtn, refreshBtn;

    public MachineUI() {
        super("Gestion des Machines");
        FlatLightLaf.setup();
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Table
        model = new DefaultTableModel(new String[]{"ID", "Nom", "Type", "État"}, 0);
        table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(table);

        // Buttons
        JPanel buttonPanel = new JPanel();
        addBtn = new JButton("Ajouter");
        editBtn = new JButton("Modifier");
        deleteBtn = new JButton("Supprimer");
        refreshBtn = new JButton("Actualiser");

        buttonPanel.add(addBtn);
        buttonPanel.add(editBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(refreshBtn);

        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        refreshMachines();

        // Events
        addBtn.addActionListener(e -> openMachineForm(null));
        editBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                int id = Integer.parseInt(model.getValueAt(row, 0).toString());
                String name = model.getValueAt(row, 1).toString();
                String type = model.getValueAt(row, 2).toString();
                String state = model.getValueAt(row, 3).toString();
                openMachineForm(new Machine(id, name, type, state));
            } else {
                JOptionPane.showMessageDialog(this, "Sélectionnez une machine.");
            }
        });

        deleteBtn.addActionListener(this::deleteMachine);
        refreshBtn.addActionListener(e -> refreshMachines());

        setVisible(true);
    }

    private void refreshMachines() {
        try {
            List<Machine> machines = MachineDAO.getAllMachines();
            model.setRowCount(0);
            for (Machine m : machines) {
                model.addRow(new Object[]{m.getId(), m.getName(), m.getType(), m.getStatus()});
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement des machines.");
        }
    }

    private void deleteMachine(ActionEvent e) {
        int row = table.getSelectedRow();
        if (row != -1) {
            int confirm = JOptionPane.showConfirmDialog(this, "Confirmer la suppression ?", "Supprimer", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                int id = Integer.parseInt(model.getValueAt(row, 0).toString());
                try {
                    MachineDAO.deleteMachine(id);
                    refreshMachines();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Erreur lors de la suppression.");
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Sélectionnez une machine.");
        }
    }

    private void openMachineForm(Machine machine) {
        JDialog dialog = new JDialog(this, "Formulaire Machine", true);
        dialog.setLayout(new GridBagLayout());
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);

        JTextField nameField = new JTextField(20);
        JTextField typeField = new JTextField(20);
        JComboBox<String> statusBox = new JComboBox<>(new String[]{"active", "inactive", "en maintenance"});

        JButton saveBtn = new JButton("Enregistrer");

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0; dialog.add(new JLabel("Nom de la machine :"), gbc);
        gbc.gridx = 1; dialog.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; dialog.add(new JLabel("Type :"), gbc);
        gbc.gridx = 1; dialog.add(typeField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; dialog.add(new JLabel("État :"), gbc);
        gbc.gridx = 1; dialog.add(statusBox, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        dialog.add(saveBtn, gbc);

        if (machine != null) {
            nameField.setText(machine.getName());
            typeField.setText(machine.getType());
            statusBox.setSelectedItem(machine.getStatus());
        }

        saveBtn.addActionListener(ev -> {
            String name = nameField.getText().trim();
            String type = typeField.getText().trim();
            String status = statusBox.getSelectedItem().toString();

            if (name.isEmpty() || type.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Champs requis manquants.");
                return;
            }

            try {
                if (machine == null) {
                    MachineDAO.insertMachine(new Machine(0, name, type, status));
                } else {
                    machine.setName(name);
                    machine.setType(type);
                    machine.setStatus(status);
                    MachineDAO.updateMachine(machine);
                }
                dialog.dispose();
                refreshMachines();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Erreur lors de l'enregistrement.");
            }
        });

        dialog.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MachineUI::new);
    }
}
