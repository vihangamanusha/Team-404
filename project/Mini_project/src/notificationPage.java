import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.time.LocalDate;

public class notificationPage {
    private JButton button1;
    private JPanel notificationPanel;
    private JTextField title;
    private JTextArea content;
    private JComboBox<String> categary;  // Specify generic type
    private JTextField attachment;
    private JButton RESETButton;
    private JButton UPLOADButton;

    public notificationPage() {
        // Initialize category dropdown
        categary.addItem("General");
        categary.addItem("Academic");
        categary.addItem("Event");
        categary.addItem("Urgent");

        JFrame frame = new JFrame("Notification Page");
        frame.setContentPane(notificationPanel);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(1000, 500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        button1.addActionListener(e -> {
            frame.dispose();
            new adminPage();
        });

        RESETButton.addActionListener(e -> resetForm());

        UPLOADButton.addActionListener(e -> uploadNotice());
    }

    private void uploadNotice() {
        // 1. Validate inputs
        if(title.getText().trim().isEmpty() || content.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null,
                    "Title and content are required!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 2. Generate notice ID (modify as needed)
        String noticeId = "N" + System.currentTimeMillis() % 1000000;

        try (Connection conn = DBConnection.getConnection()) {
            // 3. Create SQL with all columns
            String sql = "INSERT INTO Notice (Notice_id, Title, Publish_date, Admin_Username, content, category) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";

            // 4. Set parameters
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, noticeId);
                pstmt.setString(2, title.getText());
                pstmt.setDate(3, Date.valueOf(LocalDate.now()));
                pstmt.setString(4, "AD0001"); // Replace with actual admin username
                pstmt.setString(5, content.getText());
                pstmt.setString(6, (String)categary.getSelectedItem());

                // 5. Execute update
                int rowsAffected = pstmt.executeUpdate();

                if(rowsAffected > 0) {
                    JOptionPane.showMessageDialog(null, "Notice published successfully!");
                    resetForm();
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
}
