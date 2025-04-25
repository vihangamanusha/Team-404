import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ViewAttendance {
    private JPanel mainPanel;
    private JButton backBtn;
    private JTextField textField1;
    private JTextField textField2;
    private JButton viewAttendanceButton;
    private JTable attendanceTable;

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public ViewAttendance(JFrame currentFrame) {
        mainPanel = new JPanel();
        textField1 = new JTextField(15);
        textField2 = new JTextField(15);
        viewAttendanceButton = new JButton("View Attendance");
        backBtn = new JButton("Back");

        String[] columnNames = {"Date", "Attendance ID", "Type", "Status"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        attendanceTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(attendanceTable);

        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(new JLabel("Student ID:"));
        mainPanel.add(textField1);
        mainPanel.add(new JLabel("Course Code:"));
        mainPanel.add(textField2);
        mainPanel.add(viewAttendanceButton);
        mainPanel.add(tableScrollPane);
        mainPanel.add(backBtn);
        mainPanel.setSize(1000,500);

        viewAttendanceButton.addActionListener(e -> {
            String studentID = textField1.getText();
            String courseCode = textField2.getText();

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



/*import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ViewAttendance {
    private JPanel mainPanel;
    private JButton backBtn;
    private JTextField textField1;
    private JTextField textField2;
    private JButton viewAttendanceButton;
    private JTable attendanceTable;

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public ViewAttendance(JFrame currentFrame) {
        mainPanel = new JPanel();
        textField1 = new JTextField(15);
        textField2 = new JTextField(15);
        viewAttendanceButton = new JButton("View Attendance");
        backBtn = new JButton("Back");

        String[] columnNames = {"Date", "Attendance ID", "Type", "Status"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        attendanceTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(attendanceTable);

        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(new JLabel("Student ID:"));
        mainPanel.add(textField1);
        mainPanel.add(new JLabel("Course Code:"));
        mainPanel.add(textField2);
        mainPanel.add(viewAttendanceButton);
        mainPanel.add(tableScrollPane);
        mainPanel.add(backBtn);

        viewAttendanceButton.addActionListener(e -> {
            String studentID = textField1.getText();
            String courseCode = textField2.getText();

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




