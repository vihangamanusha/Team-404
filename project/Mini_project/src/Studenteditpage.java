import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Studenteditpage{
    private JPanel editUserPanel;
    private JButton backButton;
    private JTextField emailField;
    private JTextField phoneNumberField;
    private JTextField imageField;
    private JButton updateButton;
    private JButton resetButton;
    private JFrame frame;

    public Studenteditpage(){
        // Check if UserSession is properly initialized
        if (stUserSession.getInstance() == null || stUserSession.getInstance().getUsername() == null) {
            JOptionPane.showMessageDialog(null, "User session not initialized. Please login first.");
            System.exit(1);
        }

        frame = new JFrame("Edit Student Profile");
        frame.setContentPane(editUserPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // Load student details
        loadStudentDetails();

        // Button actions
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
                new testStudent(); // Go back to Student Dashboard
                frame.dispose();
            }
        });
    }

    private void loadStudentDetails() {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT First_Name, Last_Name, Phone_Number, Email, Profile_Pic_Path FROM USER WHERE Username = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1,stUserSession.getInstance().getUsername());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {

                phoneNumberField.setText(rs.getString("Phone_Number"));
                emailField.setText(rs.getString("Email"));
                imageField.setText(rs.getString("Profile_Pic_Path"));
            } else {
                JOptionPane.showMessageDialog(frame, "Student details not found!");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Database error: " + ex.getMessage());
        }
    }

    private void updateProfile() {

        String phone = phoneNumberField.getText().trim();
        String email = emailField.getText().trim();
        String imagePath = imageField.getText().trim();

        if ( phone.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please fill all required fields.");
            return;
        }

        // Validations

        if (!phone.matches("\\d{10}")) {
            JOptionPane.showMessageDialog(frame, "Phone number must be exactly 10 digits.");
            return;
        }
        String emailPattern = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        if (!email.matches(emailPattern)) {
            JOptionPane.showMessageDialog(frame, "Please enter a valid email address.");
            return;
        }
        if (imagePath.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please enter profile picture path.");
            return;
        }

        // Update in database
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "UPDATE USER SET  Phone_Number = ?, Email = ?, Profile_Pic_Path = ? WHERE Username = ? AND Role = 'Student'";
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, phone);
            stmt.setString(2, email);
            stmt.setString(3, imagePath);
            stmt.setString(4, stUserSession.getInstance().getUsername());

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(frame, "Profile updated successfully!");
            } else {
                JOptionPane.showMessageDialog(frame, "Update failed. Are you logged in as a student?");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Database error: " + ex.getMessage());
        }
    }

    private void clearFields() {

        emailField.setText("");
        phoneNumberField.setText("");
        imageField.setText("");
    }

}

