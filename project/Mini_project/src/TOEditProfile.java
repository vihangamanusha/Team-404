/*
import javax.swing.*;

public class TOEditProfile extends JFrame {
    private JButton backButton;
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JTextField textField4;
    private JTextField textField5;
    private JButton UPDATEButton;
    private JButton RESETButton;

    private JPanel editProfilePanel;
    public TOEditProfile() {
        setContentPane(editProfilePanel);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 500);
        setLocationRelativeTo(null);
        setVisible(true);
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(TOEditProfile::new);
    }
}
*/
import javax.swing.*;
import java.awt.event.*;
import java.sql.*;

public class TOEditProfile extends JFrame {
    private JButton backButton;
    private JTextField textField1; // First Name
    private JTextField textField2; // Last Name
    private JTextField textField3; // Phone Number
    private JTextField textField4; // Email
    private JTextField textField5; // Password
    private JButton UPDATEButton;
    private JButton RESETButton;
    private JPanel editProfilePanel;

    private String loggedInUsername; // Username of the logged-in TO

    public TOEditProfile(String username) {
        this.loggedInUsername = username;

        setContentPane(editProfilePanel);
        setTitle("Edit Profile");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 500);
        setLocationRelativeTo(null);
        setVisible(true);

        loadProfileData();

        UPDATEButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateProfileData();
            }
        });

        RESETButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadProfileData(); // Reset fields
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close window
            }
        });
    }

    private void loadProfileData() {
        try {
            Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/your_database_name",
                    "your_username",
                    "your_password"
            );
            String sql = "SELECT First_Name, Last_Name, Phone_Number, Email, Password FROM USER WHERE Username = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, loggedInUsername);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                textField1.setText(rs.getString("First_Name"));
                textField2.setText(rs.getString("Last_Name"));
                textField3.setText(rs.getString("Phone_Number"));
                textField4.setText(rs.getString("Email"));
                textField5.setText(rs.getString("Password"));
            }
            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading profile data.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateProfileData() {
        try {
           /* Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/your_database_name",
                    "your_username",
                    "your_password"
            );

            */
            String sql = "UPDATE USER SET First_Name=?, Last_Name=?, Phone_Number=?, Email=?, Password=? WHERE Username=?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, textField1.getText());
            pstmt.setString(2, textField2.getText());
            pstmt.setString(3, textField3.getText());
            pstmt.setString(4, textField4.getText());
            pstmt.setString(5, textField5.getText());
            pstmt.setString(6, loggedInUsername);

            int updatedRows = pstmt.executeUpdate();
            if (updatedRows > 0) {
                JOptionPane.showMessageDialog(this, "Profile updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Update failed.", "Error", JOptionPane.ERROR_MESSAGE);
            }
            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating profile.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
