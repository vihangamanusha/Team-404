import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class studentPage {
    private JButton LOGOUTButton;
    private JButton EDITUSER;
    private JButton viewAttendance;
    private JButton viewMedical;
    private JButton viewCourseDetails;
    private JButton viewGrade;
    private JButton viewNotice;
    private JButton viewTimeTableButton;

    public void viewattendance() {

    }


    public studentPage() {
        viewAttendance.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new viewattendance();
            }
        });
    }
}
