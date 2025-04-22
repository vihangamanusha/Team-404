import javax.swing.*;

public class Student {
    private JPanel mainPanel;
    private JButton btnUpdateContact;
    private JButton btnViewAttendance;
    private JButton btnViewMedicals;
    private JButton btnViewCourses;
    private JButton btnViewGrades;
    private JButton btnViewTimetable;
    private JButton btnViewNotices;
    private JLabel lblWelcome;

    public Student(String studentName) {
        lblWelcome.setText("Welcome, " + studentName);

        // Example Action Listeners
        btnUpdateContact.addActionListener(e -> {
            JOptionPane.showMessageDialog(mainPanel, "Open Update Contact Window");
        });

        btnViewAttendance.addActionListener(e -> {
            JOptionPane.showMessageDialog(mainPanel, "Open Attendance Window");
        });

        btnViewGrades.addActionListener(e -> {
            JOptionPane.showMessageDialog(mainPanel, "Open Grades & GPA Window");
        });

        // You can define similar actions for the other buttons
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}
