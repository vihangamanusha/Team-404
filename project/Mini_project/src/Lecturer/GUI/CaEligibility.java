package Lecturer.GUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Arrays;

public class CaEligibility {
    private JPanel panel1;
    private JButton BACKButton;
    private JTable table1;
    private JTextField stuId;
    private JTextField courseCode;
    private JButton VIEWButton;
    private JButton BATCHVIEWButton;
    private JFrame frame;

    public CaEligibility() {
        frame = new JFrame("CA Eligibility Checker");
        frame.setContentPane(panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);


        String[] columns = {"Student ID", "Course Code", "Course Type", "CA Total", "Attendance %", "Eligibility Status"};
        table1.setModel(new DefaultTableModel(columns, 0));

        VIEWButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkIndividualEligibility();
            }
        });

        BATCHVIEWButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkBatchEligibility();
            }
        });

        BACKButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                new LectureDashboard();
            }
        });
    }

    private void checkIndividualEligibility() {
        String studentIdText = stuId.getText().trim();
        String courseCodeText = courseCode.getText().trim();

        if (studentIdText.isEmpty() || courseCodeText.isEmpty()) {
            JOptionPane.showMessageDialog(panel1, "Please enter both Student ID and Course Code.");
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT m.*, c.Course_type, " +
                    "(SELECT COUNT(*) FROM Attendance a WHERE a.Student_Username = m.Student_Username AND a.Course_code = m.Course_code) AS total_classes, " +
                    "(SELECT COUNT(*) FROM Attendance a WHERE a.Student_Username = m.Student_Username AND a.Course_code = m.Course_code AND (a.Status = 'Present' OR a.Medical_id IS NOT NULL)) AS present_classes " +
                    "FROM Marks m " +
                    "JOIN Course_unit c ON m.Course_code = c.Course_code " +
                    "WHERE m.Student_Username = ? AND m.Course_code = ?";

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, studentIdText);
            pstmt.setString(2, courseCodeText);
            ResultSet rs = pstmt.executeQuery();

            DefaultTableModel model = (DefaultTableModel) table1.getModel();
            model.setRowCount(0);

            if (rs.next()) {
                fillEligibilityTable(model, rs);
            } else {
                JOptionPane.showMessageDialog(panel1, "No data found for given Student ID and Course Code.");
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(panel1, "Database error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void checkBatchEligibility() {
        //String lecturerUsername = UserSession.getInstance().getUsername();  // Assuming UserSession exists

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT m.*, c.Course_type, " +
                    "(SELECT COUNT(*) FROM Attendance a WHERE a.Student_Username = m.Student_Username AND a.Course_code = m.Course_code) AS total_classes, " +
                    "(SELECT COUNT(*) FROM Attendance a WHERE a.Student_Username = m.Student_Username AND a.Course_code = m.Course_code AND (a.Status = 'Present' OR a.Medical_id IS NOT NULL)) AS present_classes " +
                    "FROM Marks m " +
                    "JOIN Course_unit c ON m.Course_code = c.Course_code " +
                    "WHERE m.Lecturer_Username = ?";

            PreparedStatement pstmt = conn.prepareStatement(sql);
            //pstmt.setString(1, lecturerUsername);
            ResultSet rs = pstmt.executeQuery();

            DefaultTableModel model = (DefaultTableModel) table1.getModel();
            model.setRowCount(0);
            boolean found = false;
            while (rs.next()) {
                fillEligibilityTable(model, rs);
                found = true;
            }

            if (!found) {
                JOptionPane.showMessageDialog(panel1, "No students found under your courses.");
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(panel1, "Database error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void fillEligibilityTable(DefaultTableModel model, ResultSet rs) throws SQLException {
        String studentId = rs.getString("Student_Username");
        String courseCode = rs.getString("Course_code");
        String courseType = rs.getString("Course_type");

        float quiz1 = rs.getFloat("Quiz1");
        float quiz2 = rs.getFloat("Quiz2");
        float quiz3 = rs.getFloat("Quiz3");
        float quiz4 = rs.getFloat("Quiz4");
        float assessment1 = rs.getFloat("Assessment_01");
        float assessment2 = rs.getFloat("Assessment_02");
        float midTheory = rs.getFloat("Mid_Theory");
        float midPractical = rs.getFloat("Mid_Practical");

        int totalClasses = rs.getInt("total_classes");
        int presentClasses = rs.getInt("present_classes");

        float caTotal = 0;
        if ("2T".equals(courseType)) {
            float[] quizzes = {quiz1, quiz2, quiz3, quiz4};
            Arrays.sort(quizzes);
            float best3QuizAvg = (quizzes[1] + quizzes[2] + quizzes[3]) / 3;
            caTotal = (best3QuizAvg * 0.1f) + (assessment1 * 0.1f) + (midTheory * 0.2f);
        } else if ("2T+1P".equals(courseType)) {
            float[] quizzes = {quiz1, quiz2, quiz3};
            Arrays.sort(quizzes);
            float best2QuizAvg = (quizzes[1] + quizzes[2]) / 2;
            caTotal = (best2QuizAvg * 0.1f) + ((assessment1 + assessment2) * 0.2f) + (midTheory * 0.2f);
        } else if ("2P".equals(courseType)) {
            caTotal = (assessment1 * 0.2f) + (midPractical * 0.2f);
        }

        caTotal = Math.round(caTotal * 100.0f) / 100.0f;

        float attendancePercentage = 0;
        if (totalClasses > 0) {
            attendancePercentage = (presentClasses * 100.0f) / totalClasses;
            attendancePercentage = Math.round(attendancePercentage * 100.0f) / 100.0f;
        }

        String eligibilityStatus = (caTotal >= 50 && attendancePercentage >= 80) ? "ELIGIBLE" : "NOT ELIGIBLE";

        model.addRow(new Object[]{
                studentId,
                courseCode,
                courseType,
                caTotal,
                attendancePercentage,
                eligibilityStatus
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CaEligibility());
    }
}
