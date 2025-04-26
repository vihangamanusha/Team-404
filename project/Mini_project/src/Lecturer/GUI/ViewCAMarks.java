package Lecturer.GUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class ViewCAMarks {
    private JButton backButton;
    private JTable table1;
    private JTextField stuId;
    private JTextField courseId;
    private JButton VIEWButton;   // View individual
    private JButton VIEWButton1;  // View batch
    private JPanel panel;          // <--- This is your main panel
    private JFrame frame;

    public ViewCAMarks() {
        frame = new JFrame("View CA Marks");
        frame.setContentPane(panel); // ✅ Correct: set the panel designed in .form
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // Setup Table Columns
        String[] columns = {"Student ID", "Course Code", "Quiz1", "Quiz2", "Quiz3", "Quiz4", "Assessment1", "Assessment2", "Mid Theory", "Mid Practical"};
        table1.setModel(new DefaultTableModel(columns, 0));

        VIEWButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewIndividualMarks();
            }
        });

        VIEWButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewBatchMarks();
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

    private void viewIndividualMarks() {
        String studentId = stuId.getText().trim();
        String courseCode = courseId.getText().trim();

        if (studentId.isEmpty() || courseCode.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please enter both Student ID and Course Code.");
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM Marks WHERE Student_Username = ? AND Course_code = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, studentId);
            pstmt.setString(2, courseCode);
            ResultSet rs = pstmt.executeQuery();

            DefaultTableModel model = (DefaultTableModel) table1.getModel();
            model.setRowCount(0);

            if (rs.next()) {
                model.addRow(new Object[]{
                        rs.getString("Student_Username"),
                        rs.getString("Course_code"),
                        rs.getFloat("Quiz1"),
                        rs.getFloat("Quiz2"),
                        rs.getFloat("Quiz3"),
                        rs.getFloat("Quiz4"),
                        rs.getFloat("Assessment_Mark"),
                        rs.getFloat("Assessment_Mark_02"),
                        rs.getFloat("Mid_Theory"),
                        rs.getFloat("Mid_Practical")
                });
            } else {
                JOptionPane.showMessageDialog(frame, "No marks found for this student and course.");
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(frame, "Database error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void viewBatchMarks() {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM Marks";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            DefaultTableModel model = (DefaultTableModel) table1.getModel();
            model.setRowCount(0);

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getString("Student_Username"),
                        rs.getString("Course_code"),
                        rs.getFloat("Quiz1"),
                        rs.getFloat("Quiz2"),
                        rs.getFloat("Quiz3"),
                        rs.getFloat("Quiz4"),
                        rs.getFloat("Assessment_Mark"),
                        rs.getFloat("Assessment_Mark_02"),
                        rs.getFloat("Mid_Theory"),
                        rs.getFloat("Mid_Practical")
                });
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(frame, "Database error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ViewCAMarks());
    }
}
