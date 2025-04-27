//start of working code
/*import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ViewAttendance {
    private JPanel mainPanel;
    private JButton backBtn;
    private JTextField viewAttendanceSessionType;
    private JTextField viewAttendanceCourseCode;
    private JButton viewAttendanceButton;
    private JTable attendanceTable;

    String viewaccode;
    String viewactype;



    public JPanel getMainPanel() {
        return mainPanel;
    }

    public ViewAttendance(JFrame currentFrame) {
        mainPanel = new JPanel();
        viewAttendanceSessionType = new JTextField(15);
        viewAttendanceCourseCode = new JTextField(15);
        viewAttendanceButton = new JButton("View Attendance");
        backBtn = new JButton("Back");

        String[] columnNames = {"Date", "Attendance ID", "Type", "Status"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        attendanceTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(attendanceTable);

        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(new JLabel("Student ID:"));
        mainPanel.add(viewAttendanceSessionType);
        mainPanel.add(new JLabel("Course Code:"));
        mainPanel.add(viewAttendanceCourseCode);
        mainPanel.add(viewAttendanceButton);
        mainPanel.add(tableScrollPane);
        mainPanel.add(backBtn);
        mainPanel.setSize(1000,500);

        viewAttendanceButton.addActionListener(e -> {
            String studentID = viewAttendanceSessionType.getText();
            String courseCode = viewAttendanceCourseCode.getText();

            if (studentID.isEmpty() || courseCode.isEmpty()) {
                JOptionPane.showMessageDialog(mainPanel, "Please enter both Student ID and Course Code.");
                return;
            }

            tableModel.setRowCount(0); // Clear previous rows
            tableModel.addRow(new Object[]{"2025-04-20", "A001", "Lecture", "Present"});
            tableModel.addRow(new Object[]{"2025-04-21", "A002", "Lecture", "Absent"});
        });

        backBtn.addActionListener(e -> {
            studentPage studentDashboard = new studentPage();
            studentDashboard.openDashboard(currentFrame); // Switch back to dashboard in same window
        });

        // Set this panel to the current frame
        currentFrame.setTitle("View Attendance");
        currentFrame.setContentPane(mainPanel);
        currentFrame.revalidate();
        currentFrame.repaint();
    }
}

//end of working code*/

/*import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ViewAttendance {
    private JPanel mainPanel;
    private JButton backBtn;
    private JTextField viewAttendanceSessionType;
    private JTextField viewAttendanceCourseCode;
    private JButton viewAttendanceButton;
    private JTable attendanceTable;

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public ViewAttendance(JFrame currentFrame) {
        mainPanel = new JPanel();
        viewAttendanceSessionType = new JTextField(15);
        viewAttendanceCourseCode = new JTextField(15);
        viewAttendanceButton = new JButton("View Attendance");
        backBtn = new JButton("Back");

        String[] columnNames = {"Date", "Attendance ID", "Type", "Status"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        attendanceTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(attendanceTable);

        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(new JLabel("Student ID:"));
        mainPanel.add(viewAttendanceSessionType);
        mainPanel.add(new JLabel("Course Code:"));
        mainPanel.add(viewAttendanceCourseCode);
        mainPanel.add(viewAttendanceButton);
        mainPanel.add(tableScrollPane);
        mainPanel.add(backBtn);

        viewAttendanceButton.addActionListener(e -> {
            String studentID = viewAttendanceSessionType.getText();
            String courseCode = viewAttendanceCourseCode.getText();

            if (studentID.isEmpty() || courseCode.isEmpty()) {
                JOptionPane.showMessageDialog(mainPanel, "Please enter both Student ID and Course Code.");
                return;
            }

            tableModel.setRowCount(0); // Clear previous rows
            tableModel.addRow(new Object[]{"2025-04-20", "A001", "Lecture", "Present"});
            tableModel.addRow(new Object[]{"2025-04-21", "A002", "Lecture", "Absent"});
        });

        backBtn.addActionListener(e -> {
            studentPage studentDashboard = new studentPage(currentFrame); // Pass currentFrame here
            studentDashboard.openDashboard(currentFrame); // Switch back to dashboard in same window
        });

        currentFrame.setTitle("View Attendance");
        currentFrame.setContentPane(mainPanel);
        currentFrame.revalidate();
        currentFrame.repaint();
    }
}*/
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class ViewAttendance {
    private JPanel mainPanel;
    private JButton backBtn;
    private JTextField sessionTypeField;
    private JTextField courseCodeField;
    private JButton viewAttendanceButton;
    private JTable attendanceTable;

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public ViewAttendance(JFrame currentFrame) {
        // Initialize UI components
        mainPanel = new JPanel();
        sessionTypeField = new JTextField(15);
        courseCodeField = new JTextField(15);
        viewAttendanceButton = new JButton("View Attendance");
        backBtn = new JButton("Back");

        // Table model
        String[] columnNames = {"Date", "Type", "Status"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        attendanceTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(attendanceTable);

        // Layout
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(new JLabel("Session Type:"));
        mainPanel.add(sessionTypeField);
        mainPanel.add(new JLabel("Course Code:"));
        mainPanel.add(courseCodeField);
        mainPanel.add(viewAttendanceButton);
        mainPanel.add(tableScrollPane);
        mainPanel.add(backBtn);
        mainPanel.setSize(1000, 500);

        // View Attendance button action
        viewAttendanceButton.addActionListener(e -> {
            String sessionType = sessionTypeField.getText().trim();
            String courseCode = courseCodeField.getText().trim();

            if (sessionType.isEmpty() || courseCode.isEmpty()) {
                JOptionPane.showMessageDialog(mainPanel, "Please enter both Session Type and Course Code.");
                return;
            }

            tableModel.setRowCount(0); // Clear table

            try {
                Connection conn = DBConnection.getConnection();

                String sql = "SELECT date, session_type, status FROM attendance WHERE course_code = ? AND session_type = ?";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, courseCode);
                pstmt.setString(2, sessionType);

                ResultSet rs = pstmt.executeQuery();

                boolean hasData = false;
                while (rs.next()) {
                    hasData = true;
                    String date = rs.getString("date");
                    String type = rs.getString("session_type");
                    String status = rs.getString("status");

                    tableModel.addRow(new Object[]{date, type, status});
                }

                if (!hasData) {
                    JOptionPane.showMessageDialog(mainPanel, "No attendance records found.");
                }

                rs.close();
                pstmt.close();
                conn.close();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(mainPanel, "Database error: " + ex.getMessage());
            }
        });

        // Back button (stub, you can customize)
        backBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(mainPanel, "Back to Dashboard");
            // Example: new StudentPage(currentFrame).openDashboard(currentFrame);
        });

        // Attach to frame
        currentFrame.setTitle("View Attendance");
        currentFrame.setContentPane(mainPanel);
        currentFrame.revalidate();
        currentFrame.repaint();
    }

    // For testing
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        new ViewAttendance(frame);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setVisible(true);
    }
}
