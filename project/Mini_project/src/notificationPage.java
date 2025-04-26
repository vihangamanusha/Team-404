import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.sql.*;
import java.time.LocalDate;

public class notificationPage {
    private JButton button1;
    private JPanel notificationPanel;
    private JTextField title;
    private JTextArea content;
    private JComboBox<String> categary;
    private JTextField attachment;
    private JButton RESETButton;
    private JButton UPLOADButton;
    private JTable notificationtable;
    private JButton REMOVEButton;
    private JScrollPane jscrollpanenotice;
    private JButton updatebutton;

    public notificationPage() {
        // Initialize category dropdown
        categary.addItem("General");
        categary.addItem("Academic");
        categary.addItem("Event");
        categary.addItem("Urgent");

        // Set up JTable and JScrollPane
        notificationtable = new JTable();
        notificationtable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        notificationtable.setFillsViewportHeight(true);
        jscrollpanenotice.setViewportView(notificationtable);


        JFrame frame = new JFrame("Notification Page");
        frame.setContentPane(notificationPanel);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(1000, 500);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);


        button1.addActionListener(e -> {
            frame.dispose();
            new adminPage(); // Redirect to admin page
        });

        RESETButton.addActionListener(e -> resetForm());
        UPLOADButton.addActionListener(e -> uploadNotice());
        REMOVEButton.addActionListener(e -> removeSelectedNotice());
        updatebutton.addActionListener(e -> updateSelectedNotice());

        // Load notices on startup
        loadNoticesIntoTable();

        // Add MouseListener to JTable to handle row selection and populate form fields
        notificationtable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = notificationtable.getSelectedRow();
                if (selectedRow != -1) {
                    // Populate the form fields with the data from the selected row
                    title.setText(notificationtable.getValueAt(selectedRow, 1).toString()); // Title
                    content.setText(notificationtable.getValueAt(selectedRow, 4).toString()); // Content
                    attachment.setText(notificationtable.getValueAt(selectedRow, 6).toString()); // Attachment
                    categary.setSelectedItem(notificationtable.getValueAt(selectedRow, 5).toString()); // Category
                }
            }
        });
    }

    private void uploadNotice() {
        if (title.getText().trim().isEmpty() || content.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null,
                    "Title and content are required!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        String noticeId = "N" + System.currentTimeMillis() % 1000000;

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "INSERT INTO Notice (Notice_id, Title, Publish_date, Admin_Username, content, category, attachment) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, noticeId);
                pstmt.setString(2, title.getText());
                pstmt.setDate(3, Date.valueOf(LocalDate.now()));
                pstmt.setString(4, "AD0001");
                pstmt.setString(5, content.getText());
                pstmt.setString(6, (String) categary.getSelectedItem());

                String attachmentText = attachment.getText().trim();
                if (attachmentText.isEmpty()) {
                    pstmt.setNull(7, Types.VARCHAR);
                } else {
                    pstmt.setString(7, attachmentText);
                }

                int rowsAffected = pstmt.executeUpdate();

                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(null, "Notice published successfully!");
                    resetForm();
                    loadNoticesIntoTable();
                } else {
                    JOptionPane.showMessageDialog(null,
                            "Failed to publish notice",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,
                    "Database Error: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void resetForm() {
        title.setText("");
        content.setText("");
        categary.setSelectedIndex(0);
        attachment.setText("");
    }

    private void loadNoticesIntoTable() {
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[]{
                "Notice ID", "Title", "Date", "Admin", "Content", "Category", "Attachment"
        });

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT Notice_id, Title, Publish_date, Admin_Username, content, category, attachment FROM Notice";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getString("Notice_id"),
                        rs.getString("Title"),
                        rs.getDate("Publish_date"),
                        rs.getString("Admin_Username"),
                        rs.getString("content"),
                        rs.getString("category"),
                        rs.getString("attachment")
                });
            }

            notificationtable.setModel(model);

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Error loading notices: " + ex.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void removeSelectedNotice() {
        int selectedRow = notificationtable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null,
                    "Please select a notice to remove.",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String noticeId = notificationtable.getValueAt(selectedRow, 0).toString();

        int confirm = JOptionPane.showConfirmDialog(null,
                "Are you sure you want to delete Notice ID: " + noticeId + "?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "DELETE FROM Notice WHERE Notice_id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, noticeId);
                int rowsAffected = pstmt.executeUpdate();

                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(null, "Notice deleted successfully!");
                    loadNoticesIntoTable();
                } else {
                    JOptionPane.showMessageDialog(null,
                            "Failed to delete notice.",
                            "Error",
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

    private void updateSelectedNotice() {
        int selectedRow = notificationtable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null,
                    "Please select a notice to update.",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String noticeId = notificationtable.getValueAt(selectedRow, 0).toString();

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "UPDATE Notice SET Title=?, content=?, category=?, attachment=? WHERE Notice_id=?";

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, title.getText());
                pstmt.setString(2, content.getText());
                pstmt.setString(3, (String) categary.getSelectedItem());

                String attachmentText = attachment.getText().trim();
                if (attachmentText.isEmpty()) {
                    pstmt.setNull(4, Types.VARCHAR);
                } else {
                    pstmt.setString(4, attachmentText);
                }

                pstmt.setString(5, noticeId);

                int rowsAffected = pstmt.executeUpdate();

                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(null, "Notice updated successfully!");
                    loadNoticesIntoTable();
                    resetForm();// Refresh the JTable to reflect the updated data
                } else {
                    JOptionPane.showMessageDialog(null,
                            "Failed to update notice.",
                            "Error",
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
