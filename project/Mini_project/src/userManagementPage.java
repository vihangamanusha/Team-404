import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class userManagementPage {
    private JPanel UsermanagementPage;
    private JButton button1;
    private JTextField id;
    private JTextField username;
    private JTextField fname;
    private JTextField lname;
    private JTextField phoneno;
    private JTextField email;
    private JComboBox<String> role;
    private JPasswordField passwordField1;
    private JButton RESETButton;
    private JButton UPDATEButton;
    private JButton INSERTButton;
    private JTextField search;

    public userManagementPage() {
        JFrame frame = new JFrame("User Management Page");
        frame.setContentPane(UsermanagementPage);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(1000, 500);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);

        // Insert button logic
        INSERTButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                insertUser();
            }
        });



        // Back button logic
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
                id.setText("");
                username.setText("");
                fname.setText("");
                lname.setText("");
                phoneno.setText("");
                email.setText("");
                role.setSelectedIndex(0);
                passwordField1.setText("");
            }
        });
    }

    private void insertUser() {
        String userName = username.getText().trim();
        String firstName = fname.getText().trim();
        String lastName = lname.getText().trim();
        String phone = phoneno.getText().trim();
        String userEmail = email.getText().trim();
        String selectedRole = (String) role.getSelectedItem();
        String password = new String(passwordField1.getPassword()).trim();

        // Validation
        if (userName.isEmpty() || firstName.isEmpty() || lastName.isEmpty() ||
                phone.isEmpty() || userEmail.isEmpty() || selectedRole == null || password.isEmpty()) {
            JOptionPane.showMessageDialog(null,
                    "Please fill in all fields!",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Insert into database
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "INSERT INTO USER (Username, First_name, Last_name, Phone_Number, Email, Role, Password) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, userName);
                pstmt.setString(2, firstName);
                pstmt.setString(3, lastName);
                pstmt.setString(4, phone);
                pstmt.setString(5, userEmail);
                pstmt.setString(6, selectedRole);
                pstmt.setString(7, password);

                int rowsAffected = pstmt.executeUpdate();

                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(null, "User inserted successfully!");
                    id.setText("");
                    username.setText("");
                    fname.setText("");
                    lname.setText("");
                    phoneno.setText("");
                    email.setText("");
                    role.setSelectedIndex(0);
                    passwordField1.setText(""); // Reset all fields after successful insert
                } else {
                    JOptionPane.showMessageDialog(null,
                            "Failed to insert user.",
                            "Insert Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Database Error: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }


}
