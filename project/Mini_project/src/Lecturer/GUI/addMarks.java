package Lecturer.GUI;

import javax.swing.*;
import java.sql.*;

public class addMarks {
    private JPanel panel1;
    private JButton backButton;
    private JButton addButton;
    private JButton addButton1;
    private JButton addButton2;
    private JButton addButton3;
    private JButton addButton4;
    private JButton addButton5;
    private JButton addButton6;
    private JButton addButton7;
    private JButton addButton8;
    private JTextField quiz1;
    private JTextField quiz2;
    private JTextField quiz3;
    private JTextField quiz4;
    private JTextField assessmentMark;
    private JTextField midP;
    private JTextField midT;
    private JTextField endP;
    private JTextField endT;
    private JButton clearButton;
    private JTextField studentUsernameField;
    private JTextField courseCodeField;
    private String lecturerUsername; // Added to store lecturer's username

    // Constructor now accepts lecturerUsername
    public addMarks() {


        JFrame frame = new JFrame("Add Marks");
        frame.setContentPane(panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // Attach action listeners to buttons
        addButton.addActionListener(e -> updateMark("Quiz1", quiz1.getText()));
        addButton1.addActionListener(e -> updateMark("Quiz2", quiz2.getText()));
        addButton2.addActionListener(e -> updateMark("Quiz3", quiz3.getText()));
        addButton3.addActionListener(e -> updateMark("Quiz4", quiz4.getText()));
        addButton4.addActionListener(e -> updateMark("Assessment_Mark", assessmentMark.getText()));
        addButton5.addActionListener(e -> updateMark("Mid_Practical", midP.getText()));
        addButton6.addActionListener(e -> updateMark("Mid_Theory", midT.getText()));
        addButton7.addActionListener(e -> updateMark("End_Practical", endP.getText()));
        addButton8.addActionListener(e -> updateMark("End_Theory", endT.getText()));

        clearButton.addActionListener(e -> clearFields());
    }

    private void updateMark(String column, String value) {
        String studentUsername = studentUsernameField.getText().trim();
        String courseCode = courseCodeField.getText().trim();

        // Validate inputs
        if (studentUsername.isEmpty() || courseCode.isEmpty() || value.isEmpty()) {
            JOptionPane.showMessageDialog(null, "All fields are required.");
            return;
        }

        // Validate numeric input
        try {
            Float.parseFloat(value);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid number format for " + column);
            return;
        }

        // SQL query to insert/update marks
        String sql = "INSERT INTO Marks (Student_Username, Lecturer_Username, Course_code, " + column + ") " +
                "VALUES (?, ?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE " + column + " = VALUES(" + column + ")";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, studentUsername);
            ps.setString(2, lecturerUsername); // Lecturer from logged-in user
            ps.setString(3, courseCode);
            ps.setFloat(4, Float.parseFloat(value));

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, column + " updated successfully!");
            } else {
                JOptionPane.showMessageDialog(null, "Failed to update " + column);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database error: " + ex.getMessage());
        }
    }

    private void clearFields() {
        quiz1.setText("");
        quiz2.setText("");
        quiz3.setText("");
        quiz4.setText("");
        assessmentMark.setText("");
        midP.setText("");
        midT.setText("");
        endP.setText("");
        endT.setText("");
        studentUsernameField.setText("");
        courseCodeField.setText("");
    }

    public static void main(String[] args) {
        // Example: Pass lecturer username when opening the form
        //new addMarks("LEC0001"); // Replace with actual lecturer username
    }
}