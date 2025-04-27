import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class viewStdMedicales {
    private JPanel panel1;
    private JButton backButton;
    private JLabel studentID;
    private JTextField stuID;
    private JButton viewButton;
    private JTable table1;
    private JButton clearButton;
    private DefaultTableModel tableModel;

    public viewStdMedicales() {
        initializeTable();
        JFrame frame = new JFrame("Student Medical Records");
        frame.setContentPane(panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        viewButton.addActionListener(e -> loadMedicalRecords());
        backButton.addActionListener(e -> {
            frame.dispose();
            new LectureDashboard();
        });
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stuID.setText("");          // Clear input field
                tableModel.setRowCount(0);  // Clear table data
            }
        });
    }

    private void initializeTable() {
        // Configure table model with the required columns
        String[] columns = {"Medical ID", "Submission Date", "Status", "Start Date", "End Date"};
        tableModel = new DefaultTableModel(columns, 0);
        table1.setModel(tableModel);

        // Set column widths
        table1.getColumnModel().getColumn(0).setPreferredWidth(100); // Medical ID
        table1.getColumnModel().getColumn(1).setPreferredWidth(120); // Submission Date
        table1.getColumnModel().getColumn(2).setPreferredWidth(100); // Status
        table1.getColumnModel().getColumn(3).setPreferredWidth(120); // Start Date
        table1.getColumnModel().getColumn(4).setPreferredWidth(120); // End Date
    }

    private void loadMedicalRecords() {
        String studentId = stuID.getText().trim();
        if (studentId.isEmpty()) {
            JOptionPane.showMessageDialog(panel1, "Please enter a Student ID");
            return;
        }

        try (Connection conn = new DBConnection().getConnection()) {
            String sql = "SELECT Medical_id, Submission_date, Status, Start_date, End_date " +
                    "FROM Medical WHERE Student_Username = ?";

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, studentId);
                ResultSet rs = pstmt.executeQuery();

                tableModel.setRowCount(0); // Clear existing data

                while (rs.next()) {
                    String medicalId = rs.getString("Medical_id");
                    String submissionDate = rs.getString("Submission_date");
                    String status = rs.getString("Status");
                    String startDate = rs.getString("Start_date");
                    String endDate = rs.getString("End_date");

                    tableModel.addRow(new Object[]{
                            medicalId,
                            submissionDate,
                            status,
                            startDate,
                            endDate
                    });
                }

                if (tableModel.getRowCount() == 0) {
                    JOptionPane.showMessageDialog(panel1, "No medical records found for student: " + studentId);
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(panel1, "Database error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }


}