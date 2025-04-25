import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ViewAttendance {
    private JButton button1;
    private JTextField textField1;
    private JTextField textField2;
    private JButton viewAttendanceButton;
    private JPanel mainPanel;
    private JTable attendanceTable;

    public ViewAttendance() {
        // Initialize components
        mainPanel = new JPanel();
        textField1 = new JTextField(15); // Student ID
        textField2 = new JTextField(15); // Course Code
        viewAttendanceButton = new JButton("View Attendance");
        button1 = new JButton("Back");

        // Column names for the JTable
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
        mainPanel.add(button1);

        viewAttendanceButton.addActionListener(e -> {
            String studentID = textField1.getText();
            String courseCode = textField2.getText();

            if (studentID.isEmpty() || courseCode.isEmpty()) {
                JOptionPane.showMessageDialog(mainPanel, "Please enter both Student ID and Course Code.");
                return;
            }

            // Clear old data
            tableModel.setRowCount(0);

            // Example dummy data (you can replace this with real DB results)
            tableModel.addRow(new Object[]{"2025-04-20", "A001", "Lecture", "Present"});
            tableModel.addRow(new Object[]{"2025-04-21", "A002", "Lecture", "Absent"});
        });

        button1.addActionListener(e -> JOptionPane.showMessageDialog(mainPanel, "Going back..."));

        JFrame frame = new JFrame("View Attendance");
        frame.setContentPane(mainPanel);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(500, 400);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ViewAttendance::new);
    }
}
