package Admin;

import DatabaseConnection.DBConnection;

import javax.swing.*;
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
        JFrame frame = new JFrame("Login Page");
        frame.setContentPane(LoginPage);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        RESETButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                username.setText("");
                passwordField.setText("");
            }
        });

        LOGINButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String user = username.getText();
                String pass = new String(passwordField.getPassword());

                if (user.isEmpty() || pass.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Please enter a valid username/password");
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
                                frame.dispose();
                                new adminPage();
                                break;
                            case "student":
                                frame.dispose();
                                JOptionPane.showMessageDialog(null, "Welcome Student!");
                                // new studentPage();
                                break;
                            case "lecture":
                                frame.dispose();
                                JOptionPane.showMessageDialog(null, "Welcome Lecturer!");
                                // new lecturerPage();
                                break;
                            case "t/o":
                                frame.dispose();
                                JOptionPane.showMessageDialog(null, "Welcome Technical Officer!");
                                // new officerPage();
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

    public void showLoginPage() {
        new loginPage();
    }
}
