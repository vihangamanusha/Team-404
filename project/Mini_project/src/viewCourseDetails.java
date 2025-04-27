import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class viewCourseDetails {
    private JComboBox<String> comboBox1;
    private JButton viewCourseDetailsButton;
    private JTable table1;
    private JButton backBtn;
    private JPanel mainPanel;
    private JFrame frame;

    public viewCourseDetails() {
        // Initialize the frame and main panel
        frame = new JFrame("View Course Details");
        frame.setContentPane(mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // Load course codes into comboBox
        loadCourseCodes();

        // Back button action
        backBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                new testStudent(); // Go back
            }
        });

        // View course details button action
        viewCourseDetailsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedCourseCode = (String) comboBox1.getSelectedItem();
                if (selectedCourseCode != null) {
                    loadCourseDetails(selectedCourseCode);
                } else {
                    JOptionPane.showMessageDialog(frame, "Please select a course code.");
                }
            }
        });
    }

    private void loadCourseCodes() {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT Course_code FROM course_unit";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            comboBox1.removeAllItems();
            while (rs.next()) {
                comboBox1.addItem(rs.getString("Course_code"));
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Error loading course codes: " + e.getMessage());
        }
    }

    private void loadCourseDetails(String courseCode) {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT Course_code, CourseName, Course_type, Theory_hours, Practical_hours, Credits FROM course_unit WHERE Course_code = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, courseCode);
            ResultSet rs = stmt.executeQuery();

            // Create a model for the JTable
            DefaultTableModel model = new DefaultTableModel();
            model.setColumnIdentifiers(new String[]{"Course Code", "Course Name", "Course Type", "Theory Hours", "Practical Hours", "Credits"});

            if (rs.next()) {
                // Add row to the table
                model.addRow(new Object[]{
                        rs.getString("Course_code"),
                        rs.getString("CourseName"),
                        rs.getString("Course_type"),
                        rs.getInt("Theory_hours"),
                        rs.getInt("Practical_hours"),
                        rs.getInt("Credits")
                });
                table1.setModel(model);
            } else {
                JOptionPane.showMessageDialog(frame, "No details found for the selected course.");
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Error loading course details: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new viewCourseDetails();
        });
    }
}
