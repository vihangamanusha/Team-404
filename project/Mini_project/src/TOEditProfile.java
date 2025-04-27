/*import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class TOEditProfile {
    private JPanel panel;
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField phoneField;
    private JTextField emailField;
    private JTextField profilePicture;
    private JTextField passwordField;
    private JButton updateButton;
    //private String username; // Logged-in TO's username
    private JButton backButton;

    public TOEditProfile(String username) {
        this.username = username;

        panel = new JPanel();
        panel.setLayout(new GridLayout(6, 2, 10, 10));

        panel.add(new JLabel("First Name:"));
        firstNameField = new JTextField();
        panel.add(firstNameField);

        panel.add(new JLabel("Last Name:"));
        lastNameField = new JTextField();
        panel.add(lastNameField);

        panel.add(new JLabel("Phone Number:"));
        phoneField = new JTextField();
        panel.add(phoneField);

        panel.add(new JLabel("Email:"));
        emailField = new JTextField();
        panel.add(emailField);

        panel.add(new JLabel("Password:"));
        passwordField = new JTextField();
        panel.add(passwordField);

        updateButton = new JButton("Update Profile");
        panel.add(updateButton);

        loadUserData();

        updateButton.addActionListener(e -> updateUserData());

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ToofficerPage();
                //frame.dispose();
            }
        });
    }

    private void loadUserData() {
        try (Connection con = DBConnection.getConnection()) {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM User WHERE Username = ?");
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                firstNameField.setText(rs.getString("First_Name"));
                lastNameField.setText(rs.getString("Last_Name"));
                phoneField.setText(rs.getString("Phone_Number"));
                emailField.setText(rs.getString("Email"));
                passwordField.setText(rs.getString("Password"));
            } else {
                JOptionPane.showMessageDialog(panel, "User not found!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void updateUserData() {
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String phone = phoneField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();

        if (firstName.isEmpty() || lastName.isEmpty() || phone.isEmpty() || email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(panel, "Please fill all fields!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try (Connection con = DBConnection.getConnection()) {
            PreparedStatement ps = con.prepareStatement(
                    "UPDATE User SET First_Name = ?, Last_Name = ?, Phone_Number = ?, Email = ?, Password = ? WHERE Username = ?"
            );
            ps.setString(1, firstName);
            ps.setString(2, lastName);
            ps.setString(3, phone);
            ps.setString(4, email);
            ps.setString(5, password);
            ps.setString(6, username);

            int updated = ps.executeUpdate();
            if (updated > 0) {
                JOptionPane.showMessageDialog(panel, "Profile updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(panel, "Update failed!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public JPanel getPanel() {
        return panel;
    }

    public static void main(String[] args) {

    }
}
*/
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class TOEditProfile {
    private JPanel panel;
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField phoneField;
    private JTextField emailField;
    private JTextField profilePicture;
    private JPasswordField passwordField;
    private JButton updateButton;
    private JButton resetButton;
    private JButton backButton;

    private String username;

    public TOEditProfile(String username) {
        this.username = username;

        JFrame frame = new JFrame("Edit Profile - Technical Officer");
        frame.setContentPane(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        loadUserData();

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateProfile();
            }
        });

        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearFields();
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });
    }

    private void loadUserData() {
        try {
            Connection conn = DBConnection.getConnection();
            String sql = "SELECT * FROM User WHERE Username = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, username);

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                firstNameField.setText(rs.getString("First_Name"));
                lastNameField.setText(rs.getString("Last_Name"));
                phoneField.setText(rs.getString("Phone_Number"));
                emailField.setText(rs.getString("Email"));
                profilePicture.setText(rs.getString("Profile_Pic_Path"));
                passwordField.setText(rs.getString("Password")); // show password, but not editable
                passwordField.setEditable(false);
            }
            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error loading profile: " + ex.getMessage());
        }
    }

    private void updateProfile() {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String phone = phoneField.getText();
        String email = emailField.getText();
        String profilePic = profilePicture.getText();

        if (firstName.isEmpty() || lastName.isEmpty() || phone.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please fill all required fields!");
            return;
        }

        try {
            Connection conn = DBConnection.getConnection();
            String sql = "UPDATE User SET First_Name = ?, Last_Name = ?, Phone_Number = ?, Email = ?, Profile_Pic_Path = ? WHERE Username = ?";
            PreparedStatement pst = conn.prepareStatement(sql);

            pst.setString(1, firstName);
            pst.setString(2, lastName);
            pst.setString(3, phone);
            pst.setString(4, email);
            pst.setString(5, profilePic);
            pst.setString(6, username);

            int updated = pst.executeUpdate();

            if (updated > 0) {
                JOptionPane.showMessageDialog(null, "Profile updated successfully.");
            } else {
                JOptionPane.showMessageDialog(null, "Update failed.");
            }

            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database error: " + ex.getMessage());
        }
    }

    private void clearFields() {
        firstNameField.setText("");
        lastNameField.setText("");
        phoneField.setText("");
        emailField.setText("");
        profilePicture.setText("");
        // Do not clear password field
    }
}


