import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import com.github.lgooddatepicker.components.DatePicker;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class MedicalPage extends JFrame {

    private JTextField textField1; // Student ID
    private JComboBox<String> comboBox1; // Course Code
    private JTextField textField2; // Medical ID
    private JComboBox<String> comboBox2; // TO ID
    private JComboBox<String> comboBox3; // Status
    private JTextArea textArea1; // Reason
    private JButton ADDButton;
    private JButton UPDATEButton;
    private JButton DELETEButton;
    private JButton CLEARButton;
    private JButton BACKButton;

    private JPanel mainpanel;
    private JPanel SubmissionDatePanel;
    private JPanel StartDatePanel;
    private JPanel EndDatePanel;
    private JPanel formpanel;
    private JPanel ButtonPanel;
    private JTable table1;

    private DatePicker submissionDatePicker;
    private DatePicker startDatePicker;
    private DatePicker endDatePicker;

    private DefaultTableModel tableModel;

    public MedicalPage() {
        setTitle("Medical Page");

        // Initialize DatePickers
        submissionDatePicker = new DatePicker();
        startDatePicker = new DatePicker();
        endDatePicker = new DatePicker();

        // Add DatePickers to their respective panels
        SubmissionDatePanel.setLayout(new BorderLayout());
        SubmissionDatePanel.add(submissionDatePicker, BorderLayout.CENTER);

        StartDatePanel.setLayout(new BorderLayout());
        StartDatePanel.add(startDatePicker, BorderLayout.CENTER);

        EndDatePanel.setLayout(new BorderLayout());
        EndDatePanel.add(endDatePicker, BorderLayout.CENTER);

        // Initialize combo boxes with sample values
        comboBox1.setModel(new DefaultComboBoxModel<>(new String[]{"ICT2152", "ICT2220", "ICT2243"})); // Course codes
        comboBox2.setModel(new DefaultComboBoxModel<>(new String[]{"T001", "T002", "T003"})); // TO IDs
        comboBox3.setModel(new DefaultComboBoxModel<>(new String[]{"Pending", "Approved", "Rejected"})); // Status options

        // Table setup
        String[] columnNames = {"Medical ID", "Student ID", "Course Code", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table1.setModel(tableModel);

        // Set content pane
        setContentPane(mainpanel);

        // Frame settings
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1000, 500);
        setLocationRelativeTo(null);
        setVisible(true);

        // Load existing records from database
        loadMedicalRecords();

        // ADD button
        ADDButton.addActionListener(e -> {
            String medicalId = textField2.getText();
            String courseCode = (String) comboBox1.getSelectedItem();
            String studentId = textField1.getText();
            String toId = (String) comboBox2.getSelectedItem();
            String submissionDate = submissionDatePicker.getDate() != null ? submissionDatePicker.getDate().toString() : null;
            String startDate = startDatePicker.getDate() != null ? startDatePicker.getDate().toString() : null;
            String endDate = endDatePicker.getDate() != null ? endDatePicker.getDate().toString() : null;
            String status = (String) comboBox3.getSelectedItem();
            String reason = textArea1.getText();

            try (Connection conn = getConnection()) {
                String sql = "INSERT INTO medical (Medical_id, Course_code, Student_id, TO_id, Submission_date, Status, Start_date, End_date, Reason) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, medicalId);
                ps.setString(2, courseCode);
                ps.setString(3, studentId);
                ps.setString(4, toId);
                ps.setString(5, submissionDate);
                ps.setString(6, status);
                ps.setString(7, startDate);
                ps.setString(8, endDate);
                ps.setString(9, reason);
                ps.executeUpdate();

                tableModel.addRow(new Object[]{medicalId, studentId, courseCode, status});
                JOptionPane.showMessageDialog(null, "Medical record added.");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
            }
        });

        // UPDATE button
        UPDATEButton.addActionListener(e -> {
            int selectedRow = table1.getSelectedRow();
            if (selectedRow != -1) {
                String medicalId = textField2.getText();
                String courseCode = (String) comboBox1.getSelectedItem();
                String studentId = textField1.getText();
                String toId = (String) comboBox2.getSelectedItem();
                String submissionDate = submissionDatePicker.getDate() != null ? submissionDatePicker.getDate().toString() : null;
                String startDate = startDatePicker.getDate() != null ? startDatePicker.getDate().toString() : null;
                String endDate = endDatePicker.getDate() != null ? endDatePicker.getDate().toString() : null;
                String status = (String) comboBox3.getSelectedItem();
                String reason = textArea1.getText();

                try (Connection conn = getConnection()) {
                    String sql = "UPDATE medical SET Course_code=?, Student_id=?, TO_id=?, Submission_date=?, Status=?, Start_date=?, End_date=?, Reason=? WHERE Medical_id=?";
                    PreparedStatement ps = conn.prepareStatement(sql);
                    ps.setString(1, courseCode);
                    ps.setString(2, studentId);
                    ps.setString(3, toId);
                    ps.setString(4, submissionDate);
                    ps.setString(5, status);
                    ps.setString(6, startDate);
                    ps.setString(7, endDate);
                    ps.setString(8, reason);
                    ps.setString(9, medicalId);
                    ps.executeUpdate();

                    tableModel.setValueAt(medicalId, selectedRow, 0);
                    tableModel.setValueAt(studentId, selectedRow, 1);
                    tableModel.setValueAt(courseCode, selectedRow, 2);
                    tableModel.setValueAt(status, selectedRow, 3);
                    JOptionPane.showMessageDialog(null, "Record updated.");
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
                }
            } else {
                JOptionPane.showMessageDialog(null, "Please select a row to update.");
            }
        });

        // DELETE button
        DELETEButton.addActionListener(e -> {
            int selectedRow = table1.getSelectedRow();
            if (selectedRow != -1) {
                String medicalId = (String) tableModel.getValueAt(selectedRow, 0);
                try (Connection conn = getConnection()) {
                    String sql = "DELETE FROM medical WHERE Medical_id=?";
                    PreparedStatement ps = conn.prepareStatement(sql);
                    ps.setString(1, medicalId);
                    ps.executeUpdate();

                    tableModel.removeRow(selectedRow);
                    JOptionPane.showMessageDialog(null, "Record deleted.");
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
                }
            } else {
                JOptionPane.showMessageDialog(null, "Please select a row to delete.");
            }
        });

        // CLEAR button
        CLEARButton.addActionListener(e -> {
            textField1.setText("");
            textField2.setText("");
            comboBox1.setSelectedIndex(0);
            comboBox2.setSelectedIndex(0);
            comboBox3.setSelectedIndex(0);
            textArea1.setText("");
            submissionDatePicker.clear();
            startDatePicker.clear();
            endDatePicker.clear();
        });
    }

    private void loadMedicalRecords() {
        try (Connection conn = getConnection()) {
            String query = "SELECT Medical_id, Student_id, Course_code, Status FROM medical";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                String medicalId = rs.getString("Medical_id");
                String studentId = rs.getString("Student_id");
                String courseCode = rs.getString("Course_code");
                String status = rs.getString("Status");
                tableModel.addRow(new Object[]{medicalId, studentId, courseCode, status});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to load data: " + e.getMessage());
        }
    }

    private Connection getConnection() {
        try {
            return DriverManager.getConnection("jdbc:mysql://localhost:3306/techmis", "your_username", "your_password");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database connection failed: " + e.getMessage());
            return null;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MedicalPage::new);
    }
}
