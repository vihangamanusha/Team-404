import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Attendance {
    private JButton button1;
    private JTextField attendanceDetailsTextField;
    private JTextField universityOfRuhunaFacultyTextField;
    private JTextField studentIDTextField;
    private JButton viewAttendanceButton;
    private JPanel GattendancePanel;

    public Attendance() {
        viewAttendanceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                  String studid ;
                  studid.getText('studentIDTextField');
            }
        });
    }
}
