package Admin;

import DatabaseConnection.DBConnection;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class userManagementPage {
    private JPanel UsermanagementPage;
    private JButton button1;
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
    private JButton SEARCHButton;
    private JTable userdatatable;
    private JButton DELETEButton;
    private JScrollPane jscrollpaneuser;

    public userManagementPage() {
        JFrame frame = new JFrame("User Management Page");
        frame.setContentPane(UsermanagementPage);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(1000, 600);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        jscrollpaneuser.setViewportView(userdatatable);
        frame.setVisible(true);

        showUsers();

        INSERTButton.addActionListener(e -> insertUser());
        RESETButton.addActionListener(e -> resetForm());
        DELETEButton.addActionListener(e -> deleteUser());
        SEARCHButton.addActionListener(e -> searchUser());
        UPDATEButton.addActionListener(e -> updateUser());
        button1.addActionListener(e -> {
            frame.dispose();
            new adminPage(); // Assuming adminPage exists
        });

        userdatatable.getSelectionModel().addListSelectionListener(e -> populateFieldsFromTable(e));
    }

    private void insertUser() {
        String userName = username.getText().trim();
        String firstName = fname.getText().trim();
        String lastName = lname.getText().trim();
        String phone = phoneno.getText().trim();
        String userEmail = email.getText().trim();
        String selectedRole = (String) role.getSelectedItem();
        String password = new String(passwordField1.getPassword()).trim();

        if (userName.isEmpty() || firstName.isEmpty() || lastName.isEmpty() ||
                phone.isEmpty() || userEmail.isEmpty() || selectedRole == null || password.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please fill in all fields!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "INSERT INTO USER (Username, First_name, Last_name, Phone_Number, Email, Role, Password) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, userName);
                pstmt.setString(2, firstName);
                pstmt.setString(3, lastName);
                pstmt.setString(4, phone);
                pstmt.setString(5, userEmail);
                pstmt.setString(6, selectedRole);
                pstmt.setString(7, password);
                int rows = pstmt.executeUpdate();

                if (rows > 0) {
                    JOptionPane.showMessageDialog(null, "User inserted successfully!");
                    showUsers();
                    resetForm();
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void searchUser() {
        String searchTerm = search.getText().trim();
        if (searchTerm.isEmpty()) {
            showUsers();
            return;
        }

        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[]{"Username", "First Name", "Last Name", "Phone", "Email", "Role"});

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM USER WHERE Username LIKE ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, "%" + searchTerm + "%");
                ResultSet rs = pstmt.executeQuery();

                while (rs.next()) {
                    model.addRow(new Object[]{
                            rs.getString("Username"),
                            rs.getString("First_name"),
                            rs.getString("Last_name"),
                            rs.getString("Phone_Number"),
                            rs.getString("Email"),
                            rs.getString("Role")
                    });
                }
                userdatatable.setModel(model);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteUser() {
        String userName = username.getText().trim();
        if (userName.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Select a user to delete!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(null,
                "Are you sure you want to delete the user: " + userName + "?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);

        if (confirm != JOptionPane.YES_OPTION) return;

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "DELETE FROM USER WHERE Username = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, userName);
                int rowsAffected = pstmt.executeUpdate();

                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(null, "User deleted successfully!");
                    showUsers();
                    resetForm();
                } else {
                    JOptionPane.showMessageDialog(null, "No user found with that username.", "Delete Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateUser() {
        String userName = username.getText().trim();
        String firstName = fname.getText().trim();
        String lastName = lname.getText().trim();
        String phone = phoneno.getText().trim();
        String userEmail = email.getText().trim();
        String selectedRole = (String) role.getSelectedItem();
        String password = new String(passwordField1.getPassword()).trim();

        if (userName.isEmpty() || firstName.isEmpty() || lastName.isEmpty() ||
                phone.isEmpty() || userEmail.isEmpty() || selectedRole == null || password.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please fill in all fields!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "UPDATE USER SET First_name = ?, Last_name = ?, Phone_Number = ?, Email = ?, Role = ?, Password = ? WHERE Username = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, firstName);
                pstmt.setString(2, lastName);
                pstmt.setString(3, phone);
                pstmt.setString(4, userEmail);
                pstmt.setString(5, selectedRole);
                pstmt.setString(6, password);
                pstmt.setString(7, userName);

                int rows = pstmt.executeUpdate();
                if (rows > 0) {
                    JOptionPane.showMessageDialog(null, "User updated successfully!");
                    showUsers();
                    resetForm();
                } else {
                    JOptionPane.showMessageDialog(null, "Update failed!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showUsers() {
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[]{"Username", "First Name", "Last Name", "Phone", "Email", "Role"});

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM USER")) {

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getString("Username"),
                        rs.getString("First_name"),
                        rs.getString("Last_name"),
                        rs.getString("Phone_Number"),
                        rs.getString("Email"),
                        rs.getString("Role")
                });
            }

            userdatatable.setModel(model);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error loading user table: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void populateFieldsFromTable(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting() && userdatatable.getSelectedRow() != -1) {
            int row = userdatatable.getSelectedRow();
            username.setText(userdatatable.getValueAt(row, 0).toString());
            fname.setText(userdatatable.getValueAt(row, 1).toString());
            lname.setText(userdatatable.getValueAt(row, 2).toString());
            phoneno.setText(userdatatable.getValueAt(row, 3).toString());
            email.setText(userdatatable.getValueAt(row, 4).toString());
            role.setSelectedItem(userdatatable.getValueAt(row, 5).toString());
        }
    }

    private void resetForm() {
        username.setText("");
        fname.setText("");
        lname.setText("");
        phoneno.setText("");
        email.setText("");
        role.setSelectedIndex(0);
        passwordField1.setText("");
        search.setText("");
        showUsers();
    }
}
