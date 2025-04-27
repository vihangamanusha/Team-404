import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class studenteditpage {
    private JPanel mainPanel;
    private JTextField email;
    private JTextField phonenumber;
    private JTextField image;
    private JButton UPDATEButton;
    private JButton RESETButton;
    private JPanel edituserpage;
    private JButton button1;
    private JButton BACKButton;
    private JFrame frame;

    public studenteditpage() {
        frame = new JFrame("Edit Profile");
        frame.setContentPane(mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000,500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        UPDATEButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String emailText = email.getText();
                String phoneText = phonenumber.getText();
                String imagePath = image.getText();

                if (emailText.isEmpty() || phoneText.isEmpty() || imagePath.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Please fill in all fields before updating.");
                    return;
                }

                String loggedInUsername = "AD0001"; // Replace with actual dynamic username if needed

                try {
                    Connection conn = DBConnection.getConnection();
                    String updateQuery = "UPDATE USER SET Phone_Number = ?, Email = ?, Profile_Pic_Path = ? WHERE Username = ?";
                    PreparedStatement stmt = conn.prepareStatement(updateQuery);
                    stmt.setString(1, phoneText);
                    stmt.setString(2, emailText);
                    stmt.setString(3, imagePath);
                    stmt.setString(4, loggedInUsername);

                    int rowsUpdated = stmt.executeUpdate();

                    if (rowsUpdated > 0) {
                        JOptionPane.showMessageDialog(frame, "Profile updated successfully!");
                    } else {
                        JOptionPane.showMessageDialog(frame, "Update failed. Please try again.");
                    }

                    conn.close();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(frame, "Database error: " + ex.getMessage());
                }
            }
        });

        BACKButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                new testStudent(); // Back to student dashboard
            }
        });
    }
}
