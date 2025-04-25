import javax.swing.*;
import javax.swing.table.DefaultTableModel;
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
    private JButton GETTITLESButton;

    public notificationPage() {
        // Initialize category dropdown
        categary.addItem("General");
        categary.addItem("Academic");
        categary.addItem("Event");
        categary.addItem("Urgent");

        // Set up the JTable and JScrollPane
        notificationtable = new JTable();
        jscrollpanenotice.setViewportView(notificationtable); // ✅ Attach JTable to JScrollPane

        JFrame frame = new JFrame("Notification Page");
        frame.setContentPane(notificationPanel);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(1100, 500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        button1.addActionListener(e -> {
            frame.dispose();
            new adminPage();
        });

        RESETButton.addActionListener(e -> resetForm());
        UPLOADButton.addActionListener(e -> uploadNotice());
        REMOVEButton.addActionListener(e -> removeSelectedNotice());
        GETTITLESButton.addActionListener(e -> getAllNoticeTitles());

        loadNoticesIntoTable(); // Load on start
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
                    loadNoticesIntoTable(); // Refresh table
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
        model.setColumnIdentifiers(new String[] {
                "Notice ID", "Title", "Date", "Admin", "Content", "Category", "Attachment"
        });

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT Notice_id, Title, Publish_date, Admin_Username, content, category, attachment FROM Notice";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                model.addRow(new Object[] {
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

    private void getAllNoticeTitles() {
        DefaultTableModel model = (DefaultTableModel) notificationtable.getModel();
        int rowCount = model.getRowCount();

        System.out.println("All Notice Titles:");
        for (int i = 0; i < rowCount; i++) {
            String title = model.getValueAt(i, 1).toString(); // Column 1 = Title
            System.out.println("- " + title);
        }
    }

    private void getSelectedNoticeTitle() {
        int selectedRow = notificationtable.getSelectedRow();
        if (selectedRow != -1) {
            String selectedTitle = notificationtable.getValueAt(selectedRow, 1).toString();
            JOptionPane.showMessageDialog(null, "Selected Title: " + selectedTitle);
        } else {
            JOptionPane.showMessageDialog(null, "No row selected.");
        }
    }
}
