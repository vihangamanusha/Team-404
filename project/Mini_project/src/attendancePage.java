import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import com.github.lgooddatepicker.components.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.time.format.DateTimeFormatter;
import javax.swing.event.TableModelEvent;

public class attendancePage extends JFrame {
    private JPanel panel1;
    private JPanel insertAttendanse;
    private JComboBox comboBox1; // Course code
    private JComboBox comboBox2; // Session type
    private JComboBox comboBox3; // TO Username
    private JPanel datePickerPanel;
    private JButton showStudentsButton;
    private JButton UPDATEButton;
    private JButton CLEARButton;
    private JButton DELETEButton;
    private JTable table1;
    private JButton button1;
    private JButton SELECTALLButton;
    private JButton ADDButton;
    private JButton CLEARALLButton;
    private DatePicker datePicker1;
    private JButton calculateEligibilityButton; // New button for calculating eligibility

    public attendancePage() {
        // Set up date picker
        DatePickerSettings settings = new DatePickerSettings();
        settings.setFormatForDatesCommonEra("yyyy-MM-dd");
        settings.setFormatForTodayButton(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        datePicker1 = new DatePicker(settings);
        datePickerPanel.setLayout(new BorderLayout());
        datePickerPanel.add(datePicker1, BorderLayout.CENTER);

        setTitle("Attendance Dashboard");
        setSize(1000, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setContentPane(panel1);
        setLocationRelativeTo(null);
        setVisible(true);

        loadComboBoxData();

        // Button listeners
        showStudentsButton.addActionListener(e -> loadStudentsFromDatabase());
        SELECTALLButton.addActionListener(e -> selectAllPresent());
        CLEARALLButton.addActionListener(e -> clearCheckboxesOnly());
        CLEARButton.addActionListener(e -> clearEntireForm());
        ADDButton.addActionListener(e -> addAttendanceToDatabase());
        UPDATEButton.addActionListener(e -> updateAttendanceInDatabase());
        DELETEButton.addActionListener(e -> deleteAttendanceFromDatabase());
        button1.addActionListener(e -> {
            new ToofficerPage();
            dispose();
        });

        // Add calculateEligibility button (optional)
        calculateEligibilityButton = new JButton("Calculate Eligibility");
        panel1.add(calculateEligibilityButton);
        calculateEligibilityButton.addActionListener(e -> calculateAttendanceEligibility());
    }

    private void loadComboBoxData() {
        try (Connection con = getConnection()) {
            // Load course codes from Enrollment
            comboBox1.removeAllItems();
            PreparedStatement ps1 = con.prepareStatement("SELECT DISTINCT Course_code FROM Enrollment");
            ResultSet rs1 = ps1.executeQuery();
            while (rs1.next()) comboBox1.addItem(rs1.getString("Course_code"));

            // Load session types from Attendance table
            comboBox2.removeAllItems();
            PreparedStatement ps2 = con.prepareStatement("SELECT DISTINCT Session_Type FROM Attendance");
            ResultSet rs2 = ps2.executeQuery();
            while (rs2.next()) comboBox2.addItem(rs2.getString("Session_Type"));

            // Load TO usernames from users table
            comboBox3.removeAllItems();
            PreparedStatement ps3 = con.prepareStatement("SELECT Username FROM users WHERE Role = 'Technical Officer'");
            ResultSet rs3 = ps3.executeQuery();
            while (rs3.next()) comboBox3.addItem(rs3.getString("Username"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadStudentsFromDatabase() {
        DefaultTableModel model = new DefaultTableModel(new Object[]{"Student ID", "Present", "Absent", "Medical"}, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 0 ? String.class : Boolean.class;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column > 0;
            }
        };

        try (Connection con = getConnection()) {
            String courseCode = (String) comboBox1.getSelectedItem();
            if (courseCode == null) return;

            PreparedStatement ps = con.prepareStatement("SELECT Student_Username FROM Enrollment WHERE Course_code = ?");
            ps.setString(1, courseCode);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{rs.getString("Student_Username"), false, false, false});
            }

            table1.setModel(model);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        table1.getModel().addTableModelListener((TableModelEvent e) -> {
            int row = e.getFirstRow();
            int col = e.getColumn();
            if (col >= 1 && col <= 3 && Boolean.TRUE.equals(table1.getValueAt(row, col))) {
                for (int i = 1; i <= 3; i++) {
                    if (i != col) table1.setValueAt(false, row, i);
                }
            }
        });
    }

    private void addAttendanceToDatabase() {
        try (Connection con = getConnection()) {
            String course = (String) comboBox1.getSelectedItem();
            String session = (String) comboBox2.getSelectedItem();
            String toUsername = (String) comboBox3.getSelectedItem();
            String date = datePicker1.getDate() != null ? datePicker1.getDate().toString() : null;

            if (course == null || session == null || toUsername == null || date == null) {
                JOptionPane.showMessageDialog(this, "Please complete all fields.");
                return;
            }

            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO Attendance (Course_code, Student_Username, Session_Type, Session_date, Status, TO_Username, Medical_id) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE Status = VALUES(Status)");

            for (int i = 0; i < table1.getRowCount(); i++) {
                String student = (String) table1.getValueAt(i, 0);
                String status = getStatus(i);
                if (status != null) {
                    ps.setString(1, course);
                    ps.setString(2, student);
                    ps.setString(3, session);
                    ps.setString(4, date);
                    ps.setString(5, status);
                    ps.setString(6, toUsername);
                    ps.setString(7, null);
                    ps.addBatch();
                }
            }

            ps.executeBatch();
            JOptionPane.showMessageDialog(this, "Attendance added/updated.");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void updateAttendanceInDatabase() {
        addAttendanceToDatabase(); // Same logic as add
    }

    private void deleteAttendanceFromDatabase() {
        try (Connection con = getConnection()) {
            String course = (String) comboBox1.getSelectedItem();
            String date = datePicker1.getDate() != null ? datePicker1.getDate().toString() : null;

            if (course == null || date == null) {
                JOptionPane.showMessageDialog(this, "Please select Course and Date.");
                return;
            }

            PreparedStatement ps = con.prepareStatement("DELETE FROM Attendance WHERE Course_code = ? AND Session_date = ?");
            ps.setString(1, course);
            ps.setString(2, date);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Attendance records deleted.");
            clearEntireForm();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private String getStatus(int row) {
        if ((Boolean) table1.getValueAt(row, 1)) return "Present";
        if ((Boolean) table1.getValueAt(row, 2)) return "Absent";
        if ((Boolean) table1.getValueAt(row, 3)) return "Medical";
        return null;
    }

    private void selectAllPresent() {
        for (int i = 0; i < table1.getRowCount(); i++) {
            table1.setValueAt(true, i, 1);
            table1.setValueAt(false, i, 2);
            table1.setValueAt(false, i, 3);
        }
    }

    private void clearCheckboxesOnly() {
        for (int i = 0; i < table1.getRowCount(); i++) {
            table1.setValueAt(false, i, 1);
            table1.setValueAt(false, i, 2);
            table1.setValueAt(false, i, 3);
        }
    }

    private void clearEntireForm() {
        comboBox1.setSelectedIndex(-1);
        comboBox2.setSelectedIndex(-1);
        comboBox3.setSelectedIndex(-1);
        datePicker1.clear();
        table1.setModel(new DefaultTableModel(new Object[]{"Student ID", "Present", "Absent", "Medical"}, 0));
    }

    public Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection("jdbc:mysql://localhost:3306/techmis", "root", "password");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // New method: Read eligibility from View (not Table!)
    private void calculateAttendanceEligibility() {
        try (Connection con = getConnection()) {
            String query = "SELECT * FROM attendance_eligibility_view"; // from VIEW not table
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String studentUsername = rs.getString("Student_Username");
                String courseCode = rs.getString("Course_code");
                String eligibility = rs.getString("Eligibility");

                System.out.println("Student: " + studentUsername + ", Course: " + courseCode + ", Eligibility: " + eligibility);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new attendancePage());
    }
}
