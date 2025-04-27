import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class ViewGradeDetails {
    private JButton viewGradeDetailsButton;
    private JTextField CGPAfield;
    private JTextField SGPAfield;
    private JTable viewGradeTable;
    private JButton gradeBackBtnButton;
    private JTextField gradeCourseIDField;
    private JPanel mainPanel;
    private JFrame frame;

    public ViewGradeDetails() {
        frame = new JFrame("View Grade Details");
        frame.setContentPane(mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        gradeBackBtnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                new testStudent();
            }
        });

        viewGradeDetailsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String courseId = gradeCourseIDField.getText().trim();

                if (courseId.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Please enter a Course Code.");
                    return;
                }

                try {
                    Connection conn = DBConnection.getConnection();
                    String sql = "SELECT m.*, c.Credits FROM Marks m JOIN Course_unit c ON m.Course_code = c.Course_code WHERE m.Course_code = ?";
                    PreparedStatement pst = conn.prepareStatement(sql);
                    pst.setString(1, courseId);
                    ResultSet rs = pst.executeQuery();

                    DefaultTableModel model = new DefaultTableModel();
                    model.addColumn("Student Username");
                    model.addColumn("Lecturer Username");
                    model.addColumn("Course Code");
                    model.addColumn("Medical ID");
                    model.addColumn("Quiz1");
                    model.addColumn("Quiz2");
                    model.addColumn("Quiz3");
                    model.addColumn("Quiz4");
                    model.addColumn("Assessment 01");
                    model.addColumn("Assessment 02");
                    model.addColumn("Mid Practical");
                    model.addColumn("Mid Theory");
                    model.addColumn("End Practical");
                    model.addColumn("End Theory");

                    float totalCredits = 0;
                    float totalWeightedPoints = 0;

                    while (rs.next()) {
                        model.addRow(new Object[]{
                                rs.getString("Student_Username"),
                                rs.getString("Lecturer_Username"),
                                rs.getString("Course_code"),
                                rs.getString("Medical_id"),
                                rs.getFloat("Quiz1"),
                                rs.getFloat("Quiz2"),
                                rs.getFloat("Quiz3"),
                                rs.getFloat("Quiz4"),
                                rs.getFloat("Assessment_01"),
                                rs.getFloat("Assessment_02"),
                                rs.getFloat("Mid_Practical"),
                                rs.getFloat("Mid_Theory"),
                                rs.getFloat("End_Practical"),
                                rs.getFloat("End_Theory")
                        });

                        // Calculate final marks
                        float finalMark = calculateTotalMarks(rs);
                        String grade = calculateGrade(finalMark);
                        int credits = rs.getInt("Credits");
                        float gradePoint = gradeToPoint(grade);

                        totalCredits += credits;
                        totalWeightedPoints += (gradePoint * credits);
                    }

                    viewGradeTable.setModel(model);

                    if (totalCredits > 0) {
                        float sgpa = totalWeightedPoints / totalCredits;
                        float cgpa = sgpa; // Assuming single semester for now
                        SGPAfield.setText(String.format("%.2f", sgpa));
                        CGPAfield.setText(String.format("%.2f", cgpa));
                    } else {
                        SGPAfield.setText("0.00");
                        CGPAfield.setText("0.00");
                    }

                    rs.close();
                    pst.close();
                    conn.close();

                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(frame, "Error retrieving grade details.");
                }
            }
        });
    }

    private float calculateTotalMarks(ResultSet rs) throws SQLException {
        float quizzes = averageBestQuizzes(
                rs.getFloat("Quiz1"),
                rs.getFloat("Quiz2"),
                rs.getFloat("Quiz3"),
                rs.getFloat("Quiz4")
        );
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
        return (quizzes[1] + quizzes[2] + quizzes[3]) / 3.0f;
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
}
