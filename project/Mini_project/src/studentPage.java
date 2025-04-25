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
    private JPanel mainPanel;

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public studentPage() {
        viewAttendance.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(mainPanel);
                new ViewAttendance(currentFrame); // navigate in same frame
            }
        });
    }

    public void openDashboard(JFrame currentFrame) {
        currentFrame.setTitle("Student Dashboard");
        currentFrame.setContentPane(this.getMainPanel());
        currentFrame.revalidate();
        currentFrame.repaint();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Student Dashboard");
        studentPage page = new studentPage();
        page.openDashboard(frame);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);
        frame.setVisible(true);
    }
}
