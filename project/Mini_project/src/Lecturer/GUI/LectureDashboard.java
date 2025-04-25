package Lecturer.GUI;


import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LectureDashboard {


    private JButton viewDetailes;
    private JButton attendence;
    private JButton medicalRecords;
    private JButton manage;
    private JButton viewMarks;
    private JButton calculateMarks;
    private JButton notification;
    private JButton editProfile;
    private JButton logout;
    private JPanel mainPanel;
    private JButton ADDMARKSButton;


    public LectureDashboard() {

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
                viewAttendence viewAttendence = new viewAttendence();
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

            }
        });
        viewMarks.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        calculateMarks.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        notification.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

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

            }
        });


        ADDMARKSButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new addMarks();
            }
        });
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public static void main(String[] args) {

            JFrame frame = new JFrame("Lecturer Dashboard");
            frame.setContentPane(new LectureDashboard().getMainPanel());
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1000, 500);
            frame.setLocationRelativeTo(null); // center
            frame.setVisible(true);

    }
}
