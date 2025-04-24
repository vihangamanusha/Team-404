package Lecturer.GUI;


import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class StdDetails {
    private JTextField StudentID;
    private JTextField FirstName;
    private JTextField LastName;
    private JTextField contactNum;
    private JTextField emailAddress;
    private JButton backButton;
    private JButton searchButton;
    private JButton clearButton;
    private JPanel mainPanel;

    public StdDetails() {
        JFrame frame = new JFrame("Student Details");
        frame.setContentPane(mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null); // Center the frame
        frame.setVisible(true);

        // Search Button Logic
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
                        String query = "SELECT u.First_Name, u.Last_Name, u.Phone_Number, u.Email " +
                                "FROM USER u " +
                                "JOIN Student s ON u.NIC = s.NIC " +
                                "WHERE s.Student_id = ?";

                        PreparedStatement stmt = conn.prepareStatement(query);
                        stmt.setString(1, studentId);

                        ResultSet rs = stmt.executeQuery();

                        if (rs.next()) {
                            FirstName.setText(rs.getString("First_Name"));
                            LastName.setText(rs.getString("Last_Name"));
                            contactNum.setText(rs.getString("Phone_Number"));
                            emailAddress.setText(rs.getString("Email"));
                        } else {
                            JOptionPane.showMessageDialog(frame, "No student found with ID: " + studentId);
                            clearFields();
                        }

                        rs.close();
                        stmt.close();
                    } else {
                        JOptionPane.showMessageDialog(frame, "Database connection failed!");
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(frame, "An error occurred while fetching data.");
                }
            }
        });

        // Clear Button Logic
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearFields();
            }
        });

        // Back Button Logic
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose(); // Close current window
                new LectureDashboard();// Add navigation to previous form if needed
            }
        });
    }

    // Method to clear text fields
    private void clearFields() {
        StudentID.setText("");
        FirstName.setText("");
        LastName.setText("");
        contactNum.setText("");
        emailAddress.setText("");
    }

    public static void main(String[] args) {
        new StdDetails();
    }
}
