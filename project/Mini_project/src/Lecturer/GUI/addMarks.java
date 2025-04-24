package Lecturer.GUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

    public addMarks() {
        JFrame frame = new JFrame("Add Marks");
        frame.setContentPane(panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // Add action listeners for each addButton
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

        if (studentUsername.isEmpty() || courseCode.isEmpty() || value.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please fill all fields properly.");
            return;
        }

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/tecmis", "root", "");
             PreparedStatement ps = conn.prepareStatement("UPDATE Marks SET " + column + " = ? WHERE Student_Username = ? AND Course_code = ?")) {

            ps.setFloat(1, Float.parseFloat(value));
            ps.setString(2, studentUsername);
            ps.setString(3, courseCode);

            int updated = ps.executeUpdate();
            if (updated > 0) {
                JOptionPane.showMessageDialog(null, column + " updated successfully!");
            } else {
                JOptionPane.showMessageDialog(null, "No record found to update.");
            }

        } catch (SQLException | NumberFormatException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
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
    }

    public static void main(String[] args) {
        new addMarks();
    }
}
