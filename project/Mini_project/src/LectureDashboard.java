import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

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
    private JLabel lecName;

    public LectureDashboard() {
        JFrame frame = new JFrame("Lecturer Dashboard");
        frame.setContentPane(mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 500);
        frame.setLocationRelativeTo(null); // center
        frame.setVisible(true);

        // Load the profile picture and name for the logged-in user
        loadProfileDetails();

        // Add action listeners for buttons
        viewDetailes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new StdDetails();
                frame.dispose();
            }
        });

        attendence.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ViewAttndnce();
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
                new ViewCAMarks();
                frame.dispose();
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

    // Method to load the profile picture and name for the logged-in lecturer
    private void loadProfileDetails() {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT First_Name, Last_Name, Profile_Pic_Path FROM USER WHERE Username = ? AND Role = 'Lecturer'";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, UserSession.getInstance().getUsername()); // Get logged-in user's username
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String firstName = rs.getString("First_Name");
                String lastName = rs.getString("Last_Name");
                String profilePicPath = rs.getString("Profile_Pic_Path");

                // Set the lecturer's name
                lecName.setText(firstName + " " + lastName); // Display full name

                // If profile picture path is not null, load the image
                if (profilePicPath != null && !profilePicPath.isEmpty()) {
                    ImageIcon profileImage = new ImageIcon(profilePicPath); // Load image from path
                    Image img = profileImage.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH); // Scale image
                    profilePic.setIcon(new ImageIcon(img)); // Set the image as the icon of the JLabel
                } else {
                    profilePic.setIcon(null); // Set to null if no profile picture is set
                    JOptionPane.showMessageDialog(null, "No profile picture found.");
                }
            } else {
                JOptionPane.showMessageDialog(null, "No user details found.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error loading profile details: " + ex.getMessage());
        }
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

}
