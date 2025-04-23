import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class UsereditPage {
    private JButton button1;
    private JPanel edituserpage;
    private JTextField firstname;
    private JTextField lastname;
    private JTextField email;
    private JTextField phonenumber;
    private JPasswordField passwordField1;
    private JPasswordField passwordField2;
    private JTextField image;
    private JButton UPDATEButton;
    private JButton RESETButton;

    public UsereditPage() {
        JFrame frame = new JFrame("Edit User");
        frame.setContentPane(edituserpage);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 500);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);

        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                  frame.dispose();
                  new adminPage();
            }
        });
        RESETButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                firstname.setText("");
                lastname.setText("");
                email.setText("");
                phonenumber.setText("");
                passwordField1.setText("");
                passwordField2.setText("");
                image.setText("");

            }
        });
        UPDATEButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String fname = firstname.getText();
                String lname = lastname.getText();
                String mail = email.getText();
                String phone = phonenumber.getText();
                String pass1 = new String(passwordField1.getPassword());
                String pass2 = new String(passwordField2.getPassword());
                String img = image.getText(); // If needed
                String username = "AD0001"; // You can dynamically set this
                String role = "Admin"; // Can also be dynamic

                if (!pass1.equals(pass2)) {
                    JOptionPane.showMessageDialog(null, "Passwords do not match!");
                    return;
                }

                try {
                    // Load JDBC driver (example for MySQL)
                    Class.forName("com.mysql.cj.jdbc.Driver");

                    // Connect to the database
                    Connection conn = DBConnection.getConnection();

                    // Insert SQL statement
                    String sql = "UPDATE User SET First_Name = ?, Last_Name = ?, Role = ?, Phone_Number = ?, Email = ?, Password = ? " +
                            "WHERE Username = ?";


                    PreparedStatement pstmt = conn.prepareStatement(sql);

                    pstmt.setString(1, fname);
                    pstmt.setString(2, lname);
                    pstmt.setString(3, role);
                    pstmt.setString(4, phone);
                    pstmt.setString(5, mail);
                    pstmt.setString(6, pass1);
                    pstmt.setString(7, username);

                    int rowsInserted = pstmt.executeUpdate();
                    if (rowsInserted > 0) {
                        JOptionPane.showMessageDialog(null, "User updated successfully!");
                    }

                    conn.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
                }

            }
        });
    }
}
