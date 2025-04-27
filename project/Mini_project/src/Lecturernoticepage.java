import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.sql.*;

public class Lecturernoticepage {
    private JPanel panel1;
    private JPanel noticeviewPanel;
    private JButton button1;
    private JScrollPane jscoralpanenotice;
    private JTable table1;
    private JButton VIEWNOTICEButton;

    public Lecturernoticepage() {
        JFrame frame = new JFrame("Notification Page");
        frame.setContentPane(noticeviewPanel);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(1000, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // Initialize the table
        table1 = new JTable();
        jscoralpanenotice.setViewportView(table1);
        loadNoticeData();

        // Go back button
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                // Add logic to go back to the lecture dashboard or main page
                new LectureDashboard();
            }
        });

        // View notice (attachment) button
        VIEWNOTICEButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table1.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(frame, "Please select a notice first.");
                    return;
                }

                String attachmentPath = (String) table1.getModel().getValueAt(selectedRow, 4);

                if (attachmentPath == null || attachmentPath.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "No attachment available for this notice.");
                    return;
                }

                File file = new File(attachmentPath);
                if (!file.exists()) {
                    JOptionPane.showMessageDialog(frame, "Attachment file not found:\n" + attachmentPath);
                    return;
                }

                try {
                    Desktop.getDesktop().open(file);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Unable to open the file:\n" + ex.getMessage());
                    ex.printStackTrace();
                }
            }
        });
    }

    private void loadNoticeData() {
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[]{"Title", "Date", "Content", "Category", "Attachment"});

        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT Title, Publish_date, Content, Category, Attachment FROM Notice";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getString("Title"),
                        rs.getTimestamp("Publish_date"),
                        rs.getString("Content"),
                        rs.getString("Category"),
                        rs.getString("Attachment")
                });
            }

            table1.setModel(model);

            // Optional: Hide attachment column
            table1.getColumnModel().getColumn(4).setMinWidth(0);
            table1.getColumnModel().getColumn(4).setMaxWidth(0);
            table1.getColumnModel().getColumn(4).setWidth(0);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error loading data:\n" + e.getMessage());
            e.printStackTrace();
        }
    }
}
