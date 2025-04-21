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
                    //JOptionPane.showMessageDialog(loginpage, "Please enter a valid username/password");
                    return;
                }

                try {
                    Connection conn = DriverManager.getConnection(
                            "jdbc:mysql://localhost:3306/vihang", "root", "801@Vihanga");

                    String sql = "SELECT * FROM data WHERE username = ? AND password = ?";
                    PreparedStatement pst = conn.prepareStatement(sql);
                    pst.setString(1, user);
                    pst.setString(2, pass);

                    ResultSet rs = pst.executeQuery();

                    if (rs.next()) {
                        JOptionPane.showMessageDialog(null, "Login successful!");
                        conn.close();

                        // Open next page
                        //new adminPage();
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
}
