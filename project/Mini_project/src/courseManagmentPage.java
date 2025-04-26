import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionListener;
import java.sql.*;

public class courseManagmentPage {
    private JButton button1;
    private JComboBox<String> status;
    private JComboBox<String> lecturerid;
    private JTextField username;
    private JButton INSERTButton;
    private JButton UPDATEButton;
    private JButton REMOVEButton;
    private JButton RESETButton;
    private JTextField search;
    private JPanel coureManagementPage;
    private JTextField coursename;
    private JTextField theoryhours;
    private JTextField pactricalhours;
    private JTextField coursecode;
    private JTextField Credit;
    private JButton SEARCHButton;
    private JTable table1;
    private JScrollPane jscrollpanecourse;

    public courseManagmentPage() {
        JFrame frame = new JFrame("Course Management Page");
        frame.setContentPane(coureManagementPage);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        showCourses();

        INSERTButton.addActionListener(e -> insertCourse());
        RESETButton.addActionListener(e -> resetForm());
        button1.addActionListener(e -> {
            frame.dispose();
            new adminPage();
        });
        SEARCHButton.addActionListener(e -> searchCourse());
        UPDATEButton.addActionListener(e -> updateCourse());
        REMOVEButton.addActionListener(e -> removeCourse());

        table1.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table1.getSelectedRow() != -1) {
                int row = table1.getSelectedRow();
                coursecode.setText(table1.getValueAt(row, 0).toString());
                coursename.setText(table1.getValueAt(row, 1).toString());
                status.setSelectedItem(table1.getValueAt(row, 2).toString());
                theoryhours.setText(table1.getValueAt(row, 3).toString());
                pactricalhours.setText(table1.getValueAt(row, 4).toString());
                Credit.setText(table1.getValueAt(row, 5).toString());
                lecturerid.setSelectedItem(table1.getValueAt(row, 6).toString());
                username.setText(table1.getValueAt(row, 7).toString());

            }
        });
    }

    private void insertCourse() {
        String code = coursecode.getText().trim();
        String name = coursename.getText().trim();
        String type = (String) status.getSelectedItem();
        String theory = theoryhours.getText().trim();
        String practical = pactricalhours.getText().trim();
        String creditVal = Credit.getText().trim();
        String lecturer = (String) lecturerid.getSelectedItem();
        String admin = username.getText().trim();


        if (code.isEmpty() || name.isEmpty() || type == null || theory.isEmpty() ||
                practical.isEmpty() || creditVal.isEmpty() || lecturer == null || admin.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please fill in all fields!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int theoryInt = Integer.parseInt(theory);
            int practicalInt = Integer.parseInt(practical);
            int creditsInt = Integer.parseInt(creditVal);

            try (Connection conn = DBConnection.getConnection()) {
                String sql = "INSERT INTO Course_unit (Course_code, CourseName, Course_type, Theory_hours, Practical_hours, Credits, Lecturer_Username, Admin_Username, Lecture_Note) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, code);
                    pstmt.setString(2, name);
                    pstmt.setString(3, type);
                    pstmt.setInt(4, theoryInt);
                    pstmt.setInt(5, practicalInt);
                    pstmt.setInt(6, creditsInt);
                    pstmt.setString(7, lecturer);
                    pstmt.setString(8, admin);

                    int rows = pstmt.executeUpdate();
                    if (rows > 0) {
                        JOptionPane.showMessageDialog(null, "Course inserted successfully!");
                        showCourses();
                        resetForm();
                    }
                }
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Numeric fields must be valid numbers.", "Validation Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void searchCourse() {
        String keyword = search.getText().trim();
        if (keyword.isEmpty()) {
            showCourses();
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM Course_unit WHERE Course_code = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, keyword);
                try (ResultSet rs = pstmt.executeQuery()) {
                    DefaultTableModel model = new DefaultTableModel();
                    model.setColumnIdentifiers(new String[]{
                            "Course_code", "CourseName", "Course_type", "Theory_hours", "Practical_hours", "Credits", "Lecturer_Username", "Admin_Username", "Lecture_Note"
                    });

                    while (rs.next()) {
                        model.addRow(new Object[]{
                                rs.getString("Course_code"),
                                rs.getString("CourseName"),
                                rs.getString("Course_type"),
                                rs.getInt("Theory_hours"),
                                rs.getInt("Practical_hours"),
                                rs.getInt("Credits"),
                                rs.getString("Lecturer_Username"),
                                rs.getString("Admin_Username"),

                        });
                    }

                    table1.setModel(model);
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Search Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showCourses() {
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM Course_unit")) {

            DefaultTableModel model = new DefaultTableModel();
            model.setColumnIdentifiers(new String[]{
                    "Course_code", "CourseName", "Course_type", "Theory_hours", "Practical_hours", "Credits", "Lecturer_Username", "Admin_Username"
            });

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getString("Course_code"),
                        rs.getString("CourseName"),
                        rs.getString("Course_type"),
                        rs.getInt("Theory_hours"),
                        rs.getInt("Practical_hours"),
                        rs.getInt("Credits"),
                        rs.getString("Lecturer_Username"),
                        rs.getString("Admin_Username")

                });
            }

            table1.setModel(model);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error loading table: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateCourse() {
        String code = coursecode.getText().trim();
        if (code.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Enter Course Code to update.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "UPDATE Course_unit SET CourseName=?, Course_type=?, Theory_hours=?, Practical_hours=?, Credits=?, Lecturer_Username=?, Admin_Username=?, Lecture_Note=? WHERE Course_code=?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, coursename.getText().trim());
                pstmt.setString(2, (String) status.getSelectedItem());
                pstmt.setInt(3, Integer.parseInt(theoryhours.getText().trim()));
                pstmt.setInt(4, Integer.parseInt(pactricalhours.getText().trim()));
                pstmt.setInt(5, Integer.parseInt(Credit.getText().trim()));
                pstmt.setString(6, (String) lecturerid.getSelectedItem());
                pstmt.setString(7, username.getText().trim());
                pstmt.setString(9, code);

                int rows = pstmt.executeUpdate();
                if (rows > 0) {
                    JOptionPane.showMessageDialog(null, "Course updated successfully.");
                    showCourses();
                    resetForm();
                } else {
                    JOptionPane.showMessageDialog(null, "Course not found.");
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Update Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void removeCourse() {
        String code = coursecode.getText().trim();
        if (code.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Enter Course Code to remove.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this course?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "DELETE FROM Course_unit WHERE Course_code=?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, code);
                int rows = pstmt.executeUpdate();
                if (rows > 0) {
                    JOptionPane.showMessageDialog(null, "Course deleted.");
                    showCourses();
                    resetForm();
                } else {
                    JOptionPane.showMessageDialog(null, "Course not found.");
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Delete Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void resetForm() {
        coursecode.setText("");
        coursename.setText("");
        status.setSelectedIndex(0);
        theoryhours.setText("");
        pactricalhours.setText("");
        Credit.setText("");
        lecturerid.setSelectedIndex(0);
        username.setText("");
        search.setText("");
    }
}
