import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class adminPage {
    private JButton button1;
    private JButton LOGOUTButton;
    private JButton EDITUSERButton;
    private JPanel adminpage;
    private JButton NOTIFICATIONButton;
    private JButton COURSEButton;
    private JButton TIMETABLEButton;
    private JButton USERButton;
    private JLabel profilepic;

    // Database connection variables
    private Connection connection;
    private PreparedStatement statement;
    private ResultSet resultSet;

    public adminPage() {

        JFrame frame = new JFrame("Admin Page");
        frame.setContentPane(adminpage);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 500);
        frame.setResizable(false); // or use frame.pack()
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // Connect to the database and load the profile picture
        loadProfilePicFromDatabase();

        // Add Action Listeners for buttons
        LOGOUTButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                new loginPage();
            }
        });

        EDITUSERButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                new UsereditPage();
            }
        });

        NOTIFICATIONButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                new notificationPage();
            }
        });

        USERButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                new userManagementPage();
            }
        });

        TIMETABLEButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                new timeTablePage();
            }
        });

        COURSEButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                new courseManagmentPage();
            }
        });

        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                new noticeviewPage();
            }
        });
    }

    // Method to load the profile picture from the database
    private void loadProfilePicFromDatabase() {
        try {

            connection = DBConnection.getConnection();

            // Query to fetch the profile picture path from the user table
            String query = "SELECT Profile_Pic_Path FROM USER WHERE Username = ?";
            statement = connection.prepareStatement(query);
            statement.setString(1, "AD0001");
            resultSet = statement.executeQuery();

            // If a record is found
            if (resultSet.next()) {
                String profilePicPath = resultSet.getString("Profile_Pic_Path");

                // Load the image from the path in the database
                ImageIcon profileImage = new ImageIcon(profilePicPath);

                // Set the image to the profilepic JLabel
                profilepic.setIcon(new ImageIcon(profileImage.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH))); // Adjust size as needed
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error loading profile picture: " + e.getMessage());
        }
    }
}
