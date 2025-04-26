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
        // Action listener for the View Attendance button
        viewAttendance.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(mainPanel);
                // Set the ViewAttendance panel as the current frame's content
                currentFrame.setContentPane(new ViewAttendance(currentFrame).getMainPanel());
                currentFrame.revalidate();
                currentFrame.repaint();
            }
        });

        // Action listener for the View Course Details button
        viewCourseDetails.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(mainPanel);
                // Set the viewCourseDetails panel as the current frame's content
                currentFrame.setContentPane(new viewCourseDetails(currentFrame).getMainPanel());
                currentFrame.revalidate();
                currentFrame.repaint();
            }
        });
        viewGrade.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(mainPanel);
                // Set the viewCourseDetails panel as the current frame's content
                currentFrame.setContentPane(new ViewGradeDetails(currentFrame).getMainPanel());
                currentFrame.revalidate();
                currentFrame.repaint();
            }
        });


        viewMedical.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(mainPanel);
                // Set the viewCourseDetails panel as the current frame's content
                currentFrame.setContentPane(new  Medical(currentFrame).getMainPanel());
                currentFrame.revalidate();
                currentFrame.repaint();
            }
        });
        viewTimeTableButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(mainPanel);
                // Set the viewCourseDetails panel as the current frame's content
                currentFrame.setContentPane(new  ViewTimeTable(currentFrame).getMainPanel());
                currentFrame.revalidate();
                currentFrame.repaint();
            }
        });
        viewNotice.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(mainPanel);
                // Set the viewCourseDetails panel as the current frame's content
                currentFrame.setContentPane(new  viewNotice(currentFrame).getMainPanel());
                currentFrame.revalidate();
                currentFrame.repaint();
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
        frame.setSize(1000, 500);
        frame.setVisible(true);
    }
}


