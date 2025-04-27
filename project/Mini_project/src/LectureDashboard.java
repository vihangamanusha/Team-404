import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LectureDashboard {



    private JButton viewDetailes;
    private JButton attendence;
    private JButton medicalRecords;
    private JButton manage;
    private JButton viewMarks;
    private JButton notification;
    private JButton editProfile;
    private JButton logout;
    private JPanel mainPanel;
    private JButton ADDMARKSButton;
    private JButton FINALMARKSButton;
    private JButton CAELIGIBILITYButton;
    private JLabel profilePic;


    public LectureDashboard() {

        JFrame frame = new JFrame("Lecturer Dashboard");
        //frame.setContentPane(new LectureDashboard().getMainPanel());
        frame.setContentPane(mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 500);
        frame.setLocationRelativeTo(null); // center
        frame.setVisible(true);

        viewDetailes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //frame.dispose();
                 new StdDetails();
                frame.dispose();

            }
        });

        attendence.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               new ViewAttendance();
                frame.dispose();
            }
        });
        medicalRecords.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new viewStdMedicales();
                frame.dispose();
            }
        });
        manage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ManageCourseDetails();
                frame.dispose();
            }
        });
        viewMarks.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //new ViewMarks();
            }
        });


        notification.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               new Lecturernoticepage();
                frame.dispose();
            }
        });
        editProfile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new LectuerEdit();
                frame.dispose();
            }
        });
        logout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                new loginPage();

            }
        });


        ADDMARKSButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new addMarks();
                frame.dispose();
            }
        });
        CAELIGIBILITYButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        viewMarks.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               new ViewCAMarks();
                frame.dispose();
            }
        });
        FINALMARKSButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new viewFinalMarks();
                frame.dispose();
            }
        });
        CAELIGIBILITYButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new CaEligibility();
                frame.dispose();
            }
        });

    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public static void main(String[] args) {
        new LectureDashboard();
    }

}
