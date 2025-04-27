package Lecturer.GUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class viewFinalMarks {
    private JPanel panel;
    private JButton backButton;
    private JTable table1;
    private JTextField stuId;
    private JTextField courseId;
    private JButton VIEWButton;
    private JButton VIEWButton1;
    private JButton CLEARButton;
    private JFrame frame;

    public viewFinalMarks() {
        frame = new JFrame("View Final Marks");
        frame.setContentPane(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // Setup table columns (as you want)
        String[] columns = {"Student ID", "Course Code", "Final Mark", "SGPA", "CGPA"};
        table1.setModel(new DefaultTableModel(columns, 0));

        VIEWButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewIndividualFinalMarks();
            }
        });

        VIEWButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewBatchFinalMarks();
            }
        });

        CLEARButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stuId.setText("");
                courseId.setText("");
                ((DefaultTableModel) table1.getModel()).setRowCount(0);
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

    private void viewIndividualFinalMarks() {
        String studentId = stuId.getText().trim();
        String courseCode = courseId.getText().trim();

        if (studentId.isEmpty() || courseCode.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please enter both Student ID and Course Code.");
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT m.*, c.Credits FROM Marks m JOIN Course_unit c ON m.Course_code = c.Course_code WHERE m.Student_Username = ? AND m.Course_code = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, studentId);
            pstmt.setString(2, courseCode);
            ResultSet rs = pstmt.executeQuery();

            DefaultTableModel model = (DefaultTableModel) table1.getModel();
            model.setRowCount(0);

            float totalCredits = 0;
            float totalWeightedPoints = 0;
            float totalMarks = 0;

            if (rs.next()) {
                float finalMark = calculateTotalMarks(rs);
                String grade = calculateGrade(finalMark);
                int credits = rs.getInt("Credits");

                float gradePoint = gradeToPoint(grade);
                totalCredits += credits;
                totalWeightedPoints += (gradePoint * credits);
                totalMarks = finalMark;

                float sgpa = (totalCredits > 0) ? (totalWeightedPoints / totalCredits) : 0;
                float cgpa = sgpa; // same unless you have semesters

                model.addRow(new Object[]{
                        rs.getString("Student_Username"),
                        rs.getString("Course_code"),
                        String.format("%.2f", totalMarks),
                        String.format("%.2f", sgpa),
                        String.format("%.2f", cgpa)
                });
            } else {
                JOptionPane.showMessageDialog(frame, "No marks found for this student and course.");
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(frame, "Database error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void viewBatchFinalMarks() {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT m.*, c.Credits FROM Marks m JOIN Course_unit c ON m.Course_code = c.Course_code";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            DefaultTableModel model = (DefaultTableModel) table1.getModel();
            model.setRowCount(0);

            while (rs.next()) {
                float finalMark = calculateTotalMarks(rs);
                String grade = calculateGrade(finalMark);
                int credits = rs.getInt("Credits");

                float sgpa = (credits > 0) ? (gradeToPoint(grade)) : 0;
                float cgpa = sgpa;

                model.addRow(new Object[]{
                        rs.getString("Student_Username"),
                        rs.getString("Course_code"),
                        String.format("%.2f", finalMark),
                        String.format("%.2f", sgpa),
                        String.format("%.2f", cgpa)
                });
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(frame, "Database error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private float calculateTotalMarks(ResultSet rs) throws SQLException {
        // CA and Final Exam Marks:
        float quizzes = averageBestQuizzes(rs.getFloat("Quiz1"), rs.getFloat("Quiz2"), rs.getFloat("Quiz3"), rs.getFloat("Quiz4"));
        float assessment1 = rs.getFloat("Assessment_01");
        float assessment2 = rs.getFloat("Assessment_02");
        float midTheory = rs.getFloat("Mid_Theory");
        float midPractical = rs.getFloat("Mid_Practical");
        float endTheory = rs.getFloat("End_Theory");
        float endPractical = rs.getFloat("End_Practical");

        float caMarks = (quizzes * 0.10f) + (assessment1 * 0.10f) + (assessment2 * 0.10f) + (midTheory * 0.10f) + (midPractical * 0.10f);
        float finalExamMarks = (endTheory * 0.30f) + (endPractical * 0.20f);

        return caMarks + finalExamMarks;
    }

    private float averageBestQuizzes(float q1, float q2, float q3, float q4) {
        float[] quizzes = {q1, q2, q3, q4};
        java.util.Arrays.sort(quizzes);
        return (quizzes[1] + quizzes[2] + quizzes[3]) / 3.0f; // Best 3 quizzes average
    }

    private String calculateGrade(float marks) {
        if (marks >= 85) return "A+";
        else if (marks >= 75) return "A";
        else if (marks >= 70) return "A-";
        else if (marks >= 65) return "B+";
        else if (marks >= 60) return "B";
        else if (marks >= 55) return "B-";
        else if (marks >= 50) return "C+";
        else if (marks >= 45) return "C";
        else if (marks >= 40) return "C-";
        else return "F";
    }

    private float gradeToPoint(String grade) {
        Map<String, Float> map = new HashMap<>();
        map.put("A+", 4.0f);
        map.put("A", 4.0f);
        map.put("A-", 3.7f);
        map.put("B+", 3.3f);
        map.put("B", 3.0f);
        map.put("B-", 2.7f);
        map.put("C+", 2.3f);
        map.put("C", 2.0f);
        map.put("C-", 1.7f);
        map.put("F", 0.0f);

        return map.getOrDefault(grade, 0.0f);
    }

    public static void main(String[] args) {
        new viewFinalMarks();
    }
}
