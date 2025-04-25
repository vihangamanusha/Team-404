import javax.swing.*;

public class ViewAttendance {
    private JButton button1;
    private JTextField textField1;
    private JTextField textField2;
    private JButton viewAttendanceButton;
    private JTextArea textArea1;
    private JPanel mainPanel;

    public ViewAttendance() {
        // Initialize components
        mainPanel = new JPanel();
        textField1 = new JTextField(15); // Student ID
        textField2 = new JTextField(15); // Course Code
        viewAttendanceButton = new JButton("View Attendance");
        button1 = new JButton("Back");
        textArea1 = new JTextArea(10, 30); // For attendance display
        textArea1.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea1);


        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(new JLabel("Student ID:"));
        mainPanel.add(textField1);
        mainPanel.add(new JLabel("Course Code:"));
        mainPanel.add(textField2);
        mainPanel.add(viewAttendanceButton);
        mainPanel.add(scrollPane);
        mainPanel.add(button1);


        viewAttendanceButton.addActionListener(e -> {
            String studentID = textField1.getText();
            String courseCode = textField2.getText();

            if (studentID.isEmpty() || courseCode.isEmpty()) {
                JOptionPane.showMessageDialog(mainPanel, "Please enter both Student ID and Course Code.");
                return;
            }

            // Example dummy attendance data
            String attendanceInfo = "Attendance for Student ID: " + studentID + "\n";
            attendanceInfo += "Course Code: " + courseCode + "\n\n";
            attendanceInfo += "Date       Status\n";
            attendanceInfo += "----------------------\n";
            attendanceInfo += "2025-04-20   Present\n";
            attendanceInfo += "2025-04-21   Absent\n";

            textArea1.setText(attendanceInfo);
        });

        button1.addActionListener(e -> JOptionPane.showMessageDialog(mainPanel, "Going back..."));

            JFrame frame = new JFrame("View Attendance");
            frame.setContentPane(mainPanel);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setSize(400, 300);
            frame.setVisible(true);



    }



    /*public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ViewAttendance view = new ViewAttendance();
            JFrame frame = new JFrame("View Attendance");
            frame.setContentPane(view.mainPanel);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(500, 400);
            frame.setVisible(true);
        });
    }*/
}

