package Lecturer.GUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.sql.*;

public class viewAttendence {

    private JPanel mainPanel;
    private JTextArea studentID;
    private JComboBox<String> courseCode;
    private JComboBox<String> TP;
    private JButton viewButton;
    private JButton backButton;
    private JTable attendanceTable;  // Add JTable to display results
    private JScrollPane scrollPane;

    public viewAttendence() {
        JFrame frame = new JFrame("Student Attendance");
        frame.setContentPane(mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // Add table to panel
        attendanceTable = new JTable();
        scrollPane = new JScrollPane(attendanceTable);
        mainPanel.add(scrollPane);

        // Load Course Codes
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/tecmis", "root", "1234");
            PreparedStatement ps = conn.prepareStatement("SELECT DISTINCT Course_code FROM Attendance");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                courseCode.addItem(rs.getString("Course_code"));
            }
            rs.close();
            ps.close();

            // Load T/P
            ps = conn.prepareStatement("SELECT DISTINCT Session_Type FROM Attendance");
            rs = ps.executeQuery();
            while (rs.next()) {
                TP.addItem(rs.getString("Session_Type"));
            }

            rs.close();
            ps.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            e.printStackTrace();
        }

        viewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadAttendanceData();
            }
        });
    }

    private void loadAttendanceData() {
        String student = studentID.getText().trim();
        String course = (String) courseCode.getSelectedItem();
        String type = (String) TP.getSelectedItem();

        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[]{"Date", "Status"});

        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/tecmis", "root", "");
            PreparedStatement ps = conn.prepareStatement("SELECT Session_date, Status FROM Attendance WHERE Student_Username = ? AND Course_code = ? AND Session_Type = ?");
            ps.setString(1, student);
            ps.setString(2, course);
            ps.setString(3, type);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{rs.getString("Session_date"), rs.getString("Status")});
            }

            attendanceTable.setModel(model);
            rs.close();
            ps.close();
            conn.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
