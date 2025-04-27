import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class newTOEdit {
    private JPanel edituserpage;
    private JButton button1; // Assuming this is the "Back" button
    private JTextField firstname;
    private JTextField lastname;
    private JTextField email;
    private JTextField phonenumber;
    private JTextField image;
    private JButton UPDATEButton;
    private JButton RESETButton;

    private JFrame frame;

    public newTOEdit() {
        // Check if UserSession is properly initialized
        if (ToUserSession.getInstance() == null || ToUserSession.getInstance().getUsername() == null) {
            JOptionPane.showMessageDialog(null, "User session is not properly initialized. Please log in.");
            System.exit(1); // Exit the application if no user is logged in
        }

        frame = new JFrame("Edit Profile");
        frame.setContentPane(edituserpage);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // Load the current profile information
        loadCurrentProfile();

        // Add action listener for Update button
        UPDATEButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateProfile();
            }
        });

        // Add action listener for Reset button
        RESETButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                firstname.setText("");
                lastname.setText("");
                email.setText("");
                phonenumber.setText("");
                image.setText("");
            }
        });

        // Add action listener for Back button (assuming button1 is for navigating back)
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ToofficerPage(); // Navigate back to the lecturer's dashboard
                frame.dispose(); // Close the current frame
            }
        });
    }

    // Method to load the current logged-in user's profile details into the form
    private void loadCurrentProfile() {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT First_Name, Last_Name, Phone_Number, Email, Profile_Pic_Path FROM USER WHERE Username = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, ToUserSession.getInstance().getUsername()); // Get the logged-in user's username
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                firstname.setText(rs.getString("First_Name"));
                lastname.setText(rs.getString("Last_Name"));
                phonenumber.setText(rs.getString("Phone_Number"));
                email.setText(rs.getString("Email"));
                image.setText(rs.getString("Profile_Pic_Path"));
            } else {
                JOptionPane.showMessageDialog(frame, "User details not found.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Database error: " + ex.getMessage());
        }
    }

    // Method to update the logged-in user's profile with new information
    private void updateProfile() {
        String fname = firstname.getText().trim();
        String lname = lastname.getText().trim();
        String phone = phonenumber.getText().trim();
        String mail = email.getText().trim();
        String img = image.getText().trim();

        // Check if all required fields are filled out
        if (fname.isEmpty() || lname.isEmpty() || phone.isEmpty() || mail.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please fill all required fields.");
            return;
        }

        // Validate First Name (only letters allowed)
        if (!fname.matches("[A-Za-z]+")) {
            JOptionPane.showMessageDialog(frame, "First Name should only contain letters.");
            return;
        }

        // Validate Last Name (only letters allowed)
        if (!lname.matches("[A-Za-z]+")) {
            JOptionPane.showMessageDialog(frame, "Last Name should only contain letters.");
            return;
        }

        // Validate Phone Number (should be digits only and match a phone pattern)
        if (!phone.matches("\\d{10}")) {
            JOptionPane.showMessageDialog(frame, "Phone number should be exactly 10 digits.");
            return;
        }

        // Validate Email Format
        String emailPattern = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        if (!mail.matches(emailPattern)) {
            JOptionPane.showMessageDialog(frame, "Please enter a valid email address.");
            return;
        }

        // Validate Profile Picture Path (if required, can leave empty if not necessary)
        if (img.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please provide a valid profile picture path.");
            return;
        }

        // Proceed to update profile in the database
        try (Connection conn = DBConnection.getConnection()) {
            // SQL query to update the current logged-in user's profile (only update for the logged-in user)
            String sql = "UPDATE USER SET First_Name = ?, Last_Name = ?, Phone_Number = ?, Email = ?, Profile_Pic_Path = ? WHERE Username = ? AND Role = 'Technical Officer'";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, fname);
            pstmt.setString(2, lname);
            pstmt.setString(3, phone);
            pstmt.setString(4, mail);
            pstmt.setString(5, img);
            pstmt.setString(6, ToUserSession.getInstance().getUsername()); // Update based on logged-in user

            int rowsUpdated = pstmt.executeUpdate();

            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(frame, "Profile updated successfully!");
            } else {
                JOptionPane.showMessageDialog(frame, "Update failed. Ensure you're logged in as a TO.");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Database error: " + ex.getMessage());
        }
    }

    // Main method to launch the Lecturer Edit profile window
    public static void main(String[] args) {
        // Initialize UserSession and set a test username if not already set
        ToUserSession.getInstance().setUsername("testUsername"); // Set the username for testing

        new ToofficerPage();
    }
}

