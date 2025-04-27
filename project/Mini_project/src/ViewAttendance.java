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
    private JTextField viewAttendanceSessionType; // Session Type field (previously used for Student ID)
    private JTextField viewAttendanceCourseCode;   // Course Code field
    private JButton viewAttendanceButton;
    private JTable attendanceTable;

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public ViewAttendance(JFrame currentFrame) {
        // Initialize components
        mainPanel = new JPanel();
        viewAttendanceSessionType = new JTextField(15); // TextField for Session Type
        viewAttendanceCourseCode = new JTextField(15);   // TextField for Course Code
        viewAttendanceButton = new JButton("View Attendance");
        backBtn = new JButton("Back");

        // Column names for the JTable (Removed "Attendance ID" column)
        String[] columnNames = {"Date", "Type", "Status"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        attendanceTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(attendanceTable);

        // Layout configuration for the main panel
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(new JLabel("Session Type:"));
        mainPanel.add(viewAttendanceSessionType);
        mainPanel.add(new JLabel("Course Code:"));
        mainPanel.add(viewAttendanceCourseCode);
        mainPanel.add(viewAttendanceButton);
        mainPanel.add(tableScrollPane);
        mainPanel.add(backBtn);
        mainPanel.setSize(1000, 500);

        // ActionListener for the View Attendance button
        viewAttendanceButton.addActionListener(e -> {
            String sessionType = viewAttendanceSessionType.getText(); // Session Type field
            String courseCode = viewAttendanceCourseCode.getText();

            // Check if the fields are empty
            if (sessionType.isEmpty() || courseCode.isEmpty()) {
                JOptionPane.showMessageDialog(mainPanel, "Please enter both Session Type and Course Code.");
                return;
            }

            // Clear previous table rows
            tableModel.setRowCount(0);

            // Connect to the database and fetch the attendance data
            try {
                MyDbConnector dbConnector = new MyDbConnector();
                Connection conn = dbConnector.getMyConnection();

                // SQL query to get attendance based on course code and session type
                String sql = "SELECT date, session_type, status FROM attendance WHERE course_code = ? AND session_type = ?";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, courseCode); // Set the Course Code
                pstmt.setString(2, sessionType); // Set the Session Type

                // Execute the query and process the results
                ResultSet rs = pstmt.executeQuery();
                boolean hasRecords = false;
                while (rs.next()) {
                    hasRecords = true;
                    String date = rs.getString("date");
                    String type = rs.getString("session_type");
                    String status = rs.getString("status");

                    // Add rows to the table
                    tableModel.addRow(new Object[]{date, type, status});
                }
                if (!hasRecords) {
                    JOptionPane.showMessageDialog(mainPanel, "No attendance records found for the given Course Code and Session Type.");
                }

                // Close the resources
                rs.close();
                pstmt.close();
                conn.close();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(mainPanel, "Error retrieving attendance: " + ex.getMessage());
            }
        });

        // Back button action
        backBtn.addActionListener(e -> {
            studentPage studentDashboard = new studentPage();
            studentDashboard.openDashboard(currentFrame); // Switch back to the student dashboard in the same window
        });

        // Set this panel to the current frame
        currentFrame.setTitle("View Attendance");
        currentFrame.setContentPane(mainPanel);
        currentFrame.revalidate();
        currentFrame.repaint();
    }
}


