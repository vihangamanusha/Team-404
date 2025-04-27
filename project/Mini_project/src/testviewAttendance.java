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
    private JFrame frame;

    public testviewAttendance() {
        frame = new JFrame("Student Dashboard");
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

                if (courseCode.isEmpty() || sessionType.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please enter both Course Code and Session Type.");
                    return;
                }

                try {
                    Connection conn = DBConnection.getConnection(); // Use your DBConnection class

                    String sql = "SELECT Session_date, Status FROM Attendance WHERE Course_code = ? AND Session_Type = ?";
                    PreparedStatement pst = conn.prepareStatement(sql);
                    pst.setString(1, courseCode);
                    pst.setString(2, sessionType);

                    ResultSet rs = pst.executeQuery();

                    // Create table model manually
                    DefaultTableModel model = new DefaultTableModel();
                    ResultSetMetaData rsmd = rs.getMetaData();
                    int columnCount = rsmd.getColumnCount();

                    // Add column names
                    for (int i = 1; i <= columnCount; i++) {
                        model.addColumn(rsmd.getColumnName(i));
                    }

                    // Add rows
                    while (rs.next()) {
                        Object[] rowData = new Object[columnCount];
                        for (int i = 1; i <= columnCount; i++) {
                            rowData[i - 1] = rs.getObject(i);
                        }
                        model.addRow(rowData);
                    }

                    attendanceTable.setModel(model);

                    // Close
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
                new testStudent();
            }
        });
    }
}
