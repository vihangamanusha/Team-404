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
    }




   /* public studentPage() {
        viewAttendance.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(mainPanel);
                new ViewAttendance(currentFrame); // navigate in same frame
            }
        });
        viewCourseDetails.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame currentFrame1 = (JFrame) SwingUtilities.getWindowAncestor(mainPanel);
                new viewCourseDetails(currentFrame1);
            }
        });
    }*/

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

/*------------------------------------------------------------/*


/*import javax.swing.*;
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

    public studentPage(JFrame currentFrame) {
        // Reused the same JFrame in the constructor to keep the reference for navigation
        viewAttendance.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ViewAttendance(currentFrame); // navigate to ViewAttendance
            }
        });

        // Similar actions for other buttons
    }

    public void openDashboard(JFrame currentFrame) {
        currentFrame.setTitle("Student Dashboard");
        currentFrame.setContentPane(this.getMainPanel());
        currentFrame.revalidate();
        currentFrame.repaint();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Student Dashboard");
        studentPage page = new studentPage(frame);
        page.openDashboard(frame);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);
        frame.setVisible(true);
    }
}*/



/*import javax.swing.*;
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

    public studentPage() {
        // Initialize components (only needed if not using .form GUI designer)
        mainPanel = new JPanel();
        LOGOUTButton = new JButton("Logout");
        EDITUSER = new JButton("Edit User");
        viewAttendance = new JButton("View Attendance");
        viewMedical = new JButton("View Medical");
        viewCourseDetails = new JButton("View Course Details");
        viewGrade = new JButton("View Grade");
        viewNotice = new JButton("View Notice");
        viewTimeTableButton = new JButton("View Time Table");

        // Add buttons to the panel
        mainPanel.add(viewAttendance);
        mainPanel.add(viewMedical);
        mainPanel.add(viewCourseDetails);
        mainPanel.add(viewGrade);
        mainPanel.add(viewNotice);
        mainPanel.add(viewTimeTableButton);
        mainPanel.add(EDITUSER);
        mainPanel.add(LOGOUTButton);

        // View Attendance
        viewAttendance.addActionListener(e -> {
            JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(mainPanel);
            new ViewAttendance(currentFrame);
        });

        // View Course Details
     /*   viewCourseDetails.addActionListener(e -> {
            JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(mainPanel);
           // new ViewCourseDetails(currentFrame);
        });*/

        // TODO: Add other button actions if needed
  //  }

    /*public JPanel getMainPanel() {
        return mainPanel;
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
        frame.setSize(600, 400);
        frame.setVisible(true);
    }
}*/


