package Admin;

import DBConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.sql.*;

public class timeTablePage {
    private JButton button1;
    private JTextField PDF;
    private JButton INSERTButton;
    private JButton RESETButton;
    private JButton REMOVEButton;
    private JPanel timetablepage;
    private JTextField timetableid;
    private JTable table1;
    private JButton VIEWTIMETABLEButton;
    private JScrollPane Jscrollpane;

    public timeTablePage() {
        JFrame frame = new JFrame("Time Table");
        frame.setContentPane(timetablepage);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 500);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

        // Load data on start
        loadTableData();

        // Action Listeners
        INSERTButton.addActionListener(e -> insertTimeTable());
        RESETButton.addActionListener(e -> resetForm());
        REMOVEButton.addActionListener(e -> deleteSelectedTimetable());
        VIEWTIMETABLEButton.addActionListener(e -> viewSelectedTimetable());

        button1.addActionListener(e -> {
            frame.dispose();
            new adminPage();
        });

        frame.setVisible(true);
    }

    // Insert timetable into database
    private void insertTimeTable() {
        String id = timetableid.getText().trim();
        String pdfPath = PDF.getText().trim();

        if (id.isEmpty() || pdfPath.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please fill in all fields!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "INSERT INTO TIMETABLE (timetable_id, Content) VALUES (?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, id);
                pstmt.setString(2, pdfPath);
                int rows = pstmt.executeUpdate();

                if (rows > 0) {
                    JOptionPane.showMessageDialog(null, "Timetable inserted successfully!");
                    resetForm();
                    loadTableData(); // Refresh table
                } else {
                    JOptionPane.showMessageDialog(null, "Insertion failed!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Delete selected timetable from the database
    private void deleteSelectedTimetable() {
        int selectedRow = table1.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Please select a row to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String timetableId = table1.getValueAt(selectedRow, 0).toString();

        int confirm = JOptionPane.showConfirmDialog(null,
                "Are you sure you want to delete the selected timetable?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);

        if (confirm != JOptionPane.YES_OPTION) return;

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "DELETE FROM TIMETABLE WHERE timetable_id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, timetableId);
                int rows = pstmt.executeUpdate();

                if (rows > 0) {
                    JOptionPane.showMessageDialog(null, "Timetable deleted successfully!");
                    loadTableData(); // Refresh table
                } else {
                    JOptionPane.showMessageDialog(null, "Deletion failed!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // View the selected timetable (open the file)
    private void viewSelectedTimetable() {
        int selectedRow = table1.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Please select a row to view.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String contentPath = table1.getValueAt(selectedRow, 1).toString();

        File file = new File(contentPath);
        if (!file.exists()) {
            JOptionPane.showMessageDialog(null, "File not found: " + contentPath, "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Desktop.getDesktop().open(file);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Unable to open file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Load data into the table
    private void loadTableData() {
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[]{"Timetable ID", "Content"});

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM TIMETABLE";
            try (PreparedStatement pstmt = conn.prepareStatement(sql);
                 ResultSet rs = pstmt.executeQuery()) {

                while (rs.next()) {
                    model.addRow(new Object[]{
                            rs.getString("timetable_id"),
                            rs.getString("Content")
                    });
                }

                table1.setModel(model);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Reset form fields and table selection
    private void resetForm() {
        timetableid.setText("");
        PDF.setText("");
        table1.clearSelection();
    }

    // Create custom components (called by the GUI designer)
    private void createUIComponents() {

    }
}
