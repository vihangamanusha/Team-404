import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class ViewAttndnce {
    private JPanel panel1;
    private JButton backButton;
    private JTextField studentIdField;
    private JButton viewButton;
    private JTable attendanceTable;
    private JScrollPane scrollPane;

    public ViewAttndnce() {
        JFrame frame = new JFrame("Attendance Viewer");
        frame.setContentPane(panel1);  // Fixed: Use current panel
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // Initialize table with column names
        String[] columns = {"Course Code", "Session Type", "Date", "Status"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table non-editable
            }
        };
        attendanceTable.setModel(model);

        viewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String studentId = studentIdField.getText().trim();

                if(studentId.isEmpty()) {
                    JOptionPane.showMessageDialog(frame,
                            "Please enter a Student ID",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try (Connection conn = DBConnection.getConnection()) {
                    String sql = "SELECT Course_code, Session_Type, Session_date, Status " +
                            "FROM Attendance WHERE Student_Username = ? " +
                            "ORDER BY Session_date DESC";

                    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                        pstmt.setString(1, studentId);
                        ResultSet rs = pstmt.executeQuery();

                        DefaultTableModel model = (DefaultTableModel) attendanceTable.getModel();
                        model.setRowCount(0); // Clear existing data

                        boolean hasRecords = false;
                        while(rs.next()) {
                            hasRecords = true;
                            model.addRow(new Object[]{
                                    rs.getString("Course_code"),
                                    rs.getString("Session_Type"),
                                    rs.getDate("Session_date"),
                                    rs.getString("Status")
                            });
                        }

                        if(!hasRecords) {
                            JOptionPane.showMessageDialog(frame,
                                    "No attendance records found for this student",
                                    "Information",
                                    JOptionPane.INFORMATION_MESSAGE);
                        }
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(frame,
                            "Database error: " + ex.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                new LectureDashboard();
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ViewAttndnce());
    }
}