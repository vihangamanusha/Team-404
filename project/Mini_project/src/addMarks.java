package Lecturer.GUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class addMarks {
    private JPanel panel1;
    private JButton backButton;
    private JTextField quiz1;
    private JTextField quiz2;
    private JTextField quiz3;
    private JTextField quiz4;
    private JTextField assessmentMark;
    private JTextField assessment02;
    private JTextField midP;
    private JTextField midT;
    private JTextField endP;
    private JTextField endT;
    private JButton clearButton;
    private JButton addMarksButton;
    private JTextField studentUsernameField;
    private JTextField courseCodeField;
    private JTextField lecturerUsernameField;
    private JTextField studentId;
    private JTextField courseCode;
    private JButton deleteMarksButton;

    public addMarks() {
        JFrame frame = new JFrame("Marks Management");
        frame.setContentPane(panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        addMarksButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!allFieldsFilled()) {
                    JOptionPane.showMessageDialog(frame, "All fields must be filled!");
                    return;
                }

                String studentUsername = studentUsernameField.getText().trim();
                String courseCode = courseCodeField.getText().trim();
                String lecturerUsername = lecturerUsernameField.getText().trim();

                Float q1 = parseMark(quiz1, "Quiz 1");
                Float q2 = parseMark(quiz2, "Quiz 2");
                Float q3 = parseMark(quiz3, "Quiz 3");
                Float q4 = parseMark(quiz4, "Quiz 4");
                Float a1 = parseMark(assessmentMark, "Assessment Mark 01");
                Float a2 = parseMark(assessment02, "Assessment Mark 02");
                Float mp = parseMark(midP, "Mid Practical");
                Float mt = parseMark(midT, "Mid Theory");
                Float ep = parseMark(endP, "End Practical");
                Float et = parseMark(endT, "End Theory");

                if (q1 == null || q2 == null || q3 == null || q4 == null ||
                        a1 == null || a2 == null || mp == null ||
                        mt == null || ep == null || et == null) {
                    return;
                }

                try (Connection conn = DBConnection.getConnection()) {
                    String sql = "INSERT INTO marks (Student_Username, Lecturer_Username, Course_code, " +
                            "Quiz1, Quiz2, Quiz3, Quiz4, Assessment_01, Assessment_02, " +
                            "Mid_Practical, Mid_Theory, End_Practical, End_Theory) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

                    PreparedStatement stmt = conn.prepareStatement(sql);
                    stmt.setString(1, studentUsername);
                    stmt.setString(2, lecturerUsername);
                    stmt.setString(3, courseCode);
                    stmt.setFloat(4, q1);
                    stmt.setFloat(5, q2);
                    stmt.setFloat(6, q3);
                    stmt.setFloat(7, q4);
                    stmt.setFloat(8, a1);
                    stmt.setFloat(9, a2);
                    stmt.setFloat(10, mp);
                    stmt.setFloat(11, mt);
                    stmt.setFloat(12, ep);
                    stmt.setFloat(13, et);

                    int rows = stmt.executeUpdate();
                    if (rows > 0) {
                        JOptionPane.showMessageDialog(frame, "Marks added successfully!");
                        clearAllFields();
                    } else {
                        JOptionPane.showMessageDialog(frame, "Failed to add marks.");
                    }
                } catch (SQLException ex) {
                    if (ex.getMessage().contains("Duplicate entry")) {
                        JOptionPane.showMessageDialog(frame,
                                "Marks for this student and course already exist!");
                    } else {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(frame,
                                "Database error: " + ex.getMessage());
                    }
                }
            }
        });

        deleteMarksButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String studentIdText = studentId.getText().trim();
                String courseCodeText = courseCode.getText().trim();

                if (studentIdText.isEmpty() || courseCodeText.isEmpty()) {
                    JOptionPane.showMessageDialog(frame,
                            "Both Student ID and Course Code are required for deletion!");
                    return;
                }

                int confirm = JOptionPane.showConfirmDialog(frame,
                        "Are you sure you want to delete marks for:\n" +
                                "Student ID: " + studentIdText + "\n" +
                                "Course Code: " + courseCodeText,
                        "Confirm Deletion", JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    try (Connection conn = DBConnection.getConnection()) {
                        String sql = "DELETE FROM marks WHERE Student_Username = ? AND Course_code = ?";
                        PreparedStatement stmt = conn.prepareStatement(sql);
                        stmt.setString(1, studentIdText);
                        stmt.setString(2, courseCodeText);

                        int rowsDeleted = stmt.executeUpdate();
                        if (rowsDeleted > 0) {
                            JOptionPane.showMessageDialog(frame,
                                    "Marks deleted successfully!");
                            studentId.setText("");
                            courseCode.setText("");
                        } else {
                            JOptionPane.showMessageDialog(frame,
                                    "No marks found for this student and course!");
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(frame,
                                "Database error: " + ex.getMessage());
                    }
                }
            }
        });

        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearAllFields();
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

    private Float parseMark(JTextField field, String fieldName) {
        String input = field.getText().trim();
        if (input.isEmpty()) {
            JOptionPane.showMessageDialog(null, fieldName + " cannot be empty.");
            return null;
        }
        try {
            float mark = Float.parseFloat(input);
            if (mark < 0 || mark > 100) {
                JOptionPane.showMessageDialog(null,
                        fieldName + " must be between 0 and 100.");
                return null;
            }
            return mark;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null,
                    fieldName + " must be a valid number.");
            return null;
        }
    }

    private boolean allFieldsFilled() {
        return !studentUsernameField.getText().trim().isEmpty() &&
                !courseCodeField.getText().trim().isEmpty() &&
                !lecturerUsernameField.getText().trim().isEmpty() &&
                !quiz1.getText().trim().isEmpty() &&
                !quiz2.getText().trim().isEmpty() &&
                !quiz3.getText().trim().isEmpty() &&
                !quiz4.getText().trim().isEmpty() &&
                !assessmentMark.getText().trim().isEmpty() &&
                !assessment02.getText().trim().isEmpty() &&
                !midP.getText().trim().isEmpty() &&
                !midT.getText().trim().isEmpty() &&
                !endP.getText().trim().isEmpty() &&
                !endT.getText().trim().isEmpty();
    }

    private void clearAllFields() {
        studentUsernameField.setText("");
        courseCodeField.setText("");
        lecturerUsernameField.setText("");
        studentId.setText("");
        courseCode.setText("");
        quiz1.setText("");
        quiz2.setText("");
        quiz3.setText("");
        quiz4.setText("");
        assessmentMark.setText("");
        assessment02.setText("");
        midP.setText("");
        midT.setText("");
        endP.setText("");
        endT.setText("");
    }

    public static void main(String[] args) {
        new addMarks();
    }

}