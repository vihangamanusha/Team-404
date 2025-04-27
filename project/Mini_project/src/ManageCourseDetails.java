import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.*;

public class ManageCourseDetails {
    private JPanel panel1;
    private JButton backButton;
    private JTable table1;
    private JTextField filePath;
    private JTextField couresCode;
    private JButton ADDButton;
    private JButton UPDATEButton;
    private JButton REMOVEButton;
    private JTextField lectureNum;

    private JFrame frame;

    public ManageCourseDetails() {
        frame = new JFrame("Manage Course Details");
        frame.setContentPane(panel1);
        frame.setSize(800, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        String[] columnNames = { "Course Code", "Lecturer No", "File Path" };
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        table1.setModel(tableModel);

        // ADD button action
        ADDButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String course = couresCode.getText().trim();
                String lecturer = lectureNum.getText().trim();
                String path = filePath.getText().trim();

                if (course.isEmpty() || lecturer.isEmpty() || path.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Please fill all fields.");
                    return;
                }

                File file = new File(path);
                if (!file.exists() || !file.isFile()) {
                    JOptionPane.showMessageDialog(frame, "File path is invalid or does not exist.");
                    return;
                }

                // Add data to database
                try (Connection conn = DBConnection.getConnection()) {
                    String sql = "INSERT INTO Course_Materials (course_code, lecturer_no, file_path) VALUES (?, ?, ?)";
                    PreparedStatement pstmt = conn.prepareStatement(sql);
                    pstmt.setString(1, course);
                    pstmt.setString(2, lecturer);
                    pstmt.setString(3, path);
                    pstmt.executeUpdate();
                    JOptionPane.showMessageDialog(frame, "Course material added successfully.");

                    // Add row to table (to update the GUI)
                    tableModel.addRow(new Object[]{ course, lecturer, path });

                    // Clear the text fields
                    couresCode.setText("");
                    lectureNum.setText("");
                    filePath.setText("");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(frame, "Error adding course material: " + ex.getMessage());
                }
            }
        });

        // REMOVE button action
        REMOVEButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table1.getSelectedRow();

                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(frame, "Please select a row to remove.");
                    return;
                }

                String fileToDelete = (String) tableModel.getValueAt(selectedRow, 2);
                File file = new File(fileToDelete);

                int confirm = JOptionPane.showConfirmDialog(frame, "Do you want to delete the file as well?", "Delete File", JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    if (file.exists() && file.delete()) {
                        JOptionPane.showMessageDialog(frame, "File deleted from system.");
                    } else {
                        JOptionPane.showMessageDialog(frame, "Failed to delete the file or it doesn't exist.");
                    }
                }

                // Remove from database
                String courseCode = (String) tableModel.getValueAt(selectedRow, 0);
                try (Connection conn = DBConnection.getConnection()) {
                    String sql = "DELETE FROM Course_Materials WHERE course_code = ?";
                    PreparedStatement pstmt = conn.prepareStatement(sql);
                    pstmt.setString(1, courseCode);
                    pstmt.executeUpdate();
                    JOptionPane.showMessageDialog(frame, "Course material removed from database.");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(frame, "Error removing course material from database: " + ex.getMessage());
                }

                tableModel.removeRow(selectedRow);
            }
        });

        // UPDATE button action (you can implement as needed)
        UPDATEButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(frame, "Update logic not implemented yet.");
            }
        });

        // BACK button action
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                new LectureDashboard(); // Assuming you have this class
            }
        });
    }

    public static void main(String[] args) {
        new ManageCourseDetails();
    }
}
