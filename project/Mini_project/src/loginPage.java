import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class loginPage {
    private JTextField username;
    private JButton LOGINButton;
    private JButton RESETButton;
    private JPanel LoginPage;
    private JPasswordField passwordField;

    public loginPage() {
        RESETButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                username.setText("");
                passwordField.setText("");
            }
        });
        LOGINButton.addActionListener(new ActionListener() {
            private Component loginPage;

            @Override
            public void actionPerformed(ActionEvent e) {
                String user = username.getText();
                String pass = new String(passwordField.getPassword());

                if (user.isEmpty() || pass.isEmpty()) {
                    JOptionPane.showMessageDialog(loginPage, "Please enter a valid username/password");
                    return;
                }

                try {
                    Connection conn = DBConnection.getConnection();

                    String sql = "SELECT * FROM User WHERE Username = ? AND Password = ?";
                    PreparedStatement pst = conn.prepareStatement(sql);
                    pst.setString(1, user);
                    pst.setString(2, pass);

                    ResultSet rs = pst.executeQuery();

                    if (rs.next()) {
                        String role = rs.getString("Role").toLowerCase();
                        conn.close();

                        switch (role) {
                            case "admin":
                                new adminPage();  // Replace with actual admin page
                                username.setText("");
                                passwordField.setText("");
                                break;
                            case "student":
                                JOptionPane.showMessageDialog(null, "Welcome Student!");
                                //new studentPage();  // Replace with actual student page
                                break;
                            case "lecture":
                                JOptionPane.showMessageDialog(null, "Welcome Lecturer!");
                                //new lecturerPage();  // Replace with actual lecturer page
                                break;
                            case "t/o":
                                JOptionPane.showMessageDialog(null, "Welcome Technical Officer!");
                                //new officerPage();  // Replace with actual officer page
                                break;
                            default:
                                JOptionPane.showMessageDialog(null, "Unknown role: " + role);
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Invalid username or password.");
                    }

                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Database error: " + ex.getMessage());
                }



        }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("loginPage");
        frame.setContentPane(new loginPage().LoginPage);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void showLoginFrame() {
    }
}
