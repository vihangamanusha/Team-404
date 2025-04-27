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

            }
        });

        attendence.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               new ViewAttendance();
            }
        });
        medicalRecords.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new viewStdMedicales();
            }
        });
        manage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ManageCourseDetails();
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
            }
        });
        editProfile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

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
            }
        });
        FINALMARKSButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new viewFinalMarks();
            }
        });
        CAELIGIBILITYButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new CaEligibility();
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
