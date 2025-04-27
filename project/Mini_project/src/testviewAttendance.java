import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

public class testviewAttendance {
    private JPanel mainPanel;
    private JButton backBtn;
    private JTextField viewAttendanceCourseCode;
    private JButton viewAttendanceButton;
    private JTextField viewAttendanceSessionType;
    private JTable attendanceTable;
    private JScrollPane attendanceScroller;
    private JFrame frame;

    public testviewAttendance() {
        frame = new JFrame("View Attendance");
        frame.setContentPane(mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        viewAttendanceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String courseCode = viewAttendanceCourseCode.getText().trim();
                String sessionType = viewAttendanceSessionType.getText().trim();
                String loggedInUsername = "TG1301"; // 🔥 Replace with the actual logged-in student's username

                if (courseCode.isEmpty() || sessionType.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please enter both Course Code and Session Type.");
                    return;
                }

                try {
                    Connection conn = DBConnection.getConnection();

                    String sql = "SELECT Session_date, Status " +
                            "FROM Attendance " +
                            "WHERE Course_code = ? AND Session_Type = ? AND Student_Username = ?";
                    PreparedStatement pst = conn.prepareStatement(sql);
                    pst.setString(1, courseCode);
                    pst.setString(2, sessionType);
                    pst.setString(3, loggedInUsername);

                    ResultSet rs = pst.executeQuery();

                    // Create table model manually
                    DefaultTableModel model = new DefaultTableModel();
                    model.addColumn("Session Date");
                    model.addColumn("Status");

                    while (rs.next()) {
                        String sessionDate = rs.getString("Session_date");
                        String status = rs.getString("Status");
                        model.addRow(new Object[]{sessionDate, status});
                    }

                    attendanceTable.setModel(model);

                    rs.close();
                    pst.close();
                    conn.close();

                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error retrieving attendance data.");
                }
            }
        });

        backBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                new testStudent(); // 👈 Go back to the student dashboard
            }
        });
    }
}
