import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class StdDetails {
    private JTextField StudentID;
    private JButton backButton;
    private JButton searchButton;
    private JButton clearButton;
    private JPanel mainPanel;
    private JTable table1;

    public StdDetails() {
        JFrame frame = new JFrame("Student Details");
        frame.setContentPane(mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        String[] columnNames = {"Student ID", "First Name", "Last Name", "Contact", "Email"};
        table1.setModel(new DefaultTableModel(null, columnNames));

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String studentId = StudentID.getText().trim();

                if (studentId.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Please enter a Student ID.");
                    return;
                }

                try (Connection conn = DBConnection.getConnection()) {
                    if (conn != null) {
                        String query = "SELECT Username AS StudentID, First_Name, Last_Name, Phone_Number, Email " +
                                "FROM USER WHERE Username = ? AND Role = 'Student'";

                        PreparedStatement stmt = conn.prepareStatement(query);
                        stmt.setString(1, studentId);
                        ResultSet rs = stmt.executeQuery();

                        if (rs.next()) {
                            String studentID = rs.getString("StudentID");
                            String firstName = rs.getString("First_Name");
                            String lastName = rs.getString("Last_Name");
                            String contact = rs.getString("Phone_Number");
                            String email = rs.getString("Email");

                            DefaultTableModel model = new DefaultTableModel(null, columnNames);
                            model.addRow(new Object[]{studentID, firstName, lastName, contact, email});
                            table1.setModel(model);
                        } else {
                            JOptionPane.showMessageDialog(frame, "No student found with ID: " + studentId);
                            table1.setModel(new DefaultTableModel(null, columnNames));
                        }

                        rs.close();
                        stmt.close();
                    } else {
                        JOptionPane.showMessageDialog(frame, "Database connection failed!");
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(frame, "An error occurred: " + ex.getMessage());
                }
            }
        });

        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StudentID.setText("");
                table1.setModel(new DefaultTableModel(null, columnNames)); // Reset table with column headers
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

}