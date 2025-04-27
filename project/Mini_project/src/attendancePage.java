/*
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import com.github.lgooddatepicker.components.*;
import java.awt.*;
import java.sql.*;
import java.time.format.DateTimeFormatter;
import javax.swing.event.TableModelEvent;

public class attendancePage extends JFrame {
    private JPanel panel1;
    private JPanel insertAttendanse;
    private JComboBox comboBox1; // TO Username
    private JComboBox comboBox2; // Course code
    private JComboBox comboBox3; // Course type
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
    private JButton SEARCHButton;
    private DatePicker datePicker1;
    private JButton calculateEligibilityButton;

    public attendancePage() {
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

        calculateEligibilityButton = new JButton("Calculate Eligibility");
        panel1.add(calculateEligibilityButton);
        calculateEligibilityButton.addActionListener(e -> calculateAttendanceEligibility());
    }

    private void loadComboBoxData() {
        try (Connection con = DBConnection.getConnection()) {
            // ComboBox 1 - TO Username
            comboBox1.removeAllItems();
            PreparedStatement ps1 = con.prepareStatement("SELECT Username FROM Technical_officer");
            ResultSet rs1 = ps1.executeQuery();
            while (rs1.next()) {
                comboBox1.addItem(rs1.getString("Username"));
            }

            // ComboBox 2 - Course Code
            comboBox2.removeAllItems();
            PreparedStatement ps2 = con.prepareStatement("SELECT DISTINCT Course_code FROM Course_unit");
            ResultSet rs2 = ps2.executeQuery();
            while (rs2.next()) {
                comboBox2.addItem(rs2.getString("Course_code"));
            }

            // ComboBox 3 - Course Type (Session Type) - Modified again here
            comboBox2.addActionListener(e -> {
                comboBox3.removeAllItems();
                if (comboBox2.getSelectedItem() == null) return;
                String selectedCourse = (String) comboBox2.getSelectedItem();
                try (Connection con2 = DBConnection.getConnection()) {
                    boolean hasP = false;
                    boolean hasT = false;

                    // Check for 'P'
                    PreparedStatement psCheckP = con2.prepareStatement(
                            "SELECT DISTINCT Session_Type FROM Attendance WHERE Course_code = ? AND Session_Type = 'P'");
                    psCheckP.setString(1, selectedCourse);
                    ResultSet rsP = psCheckP.executeQuery();
                    hasP = rsP.next();

                    // Check for 'T'
                    PreparedStatement psCheckT = con2.prepareStatement(
                            "SELECT DISTINCT Session_Type FROM Attendance WHERE Course_code = ? AND Session_Type = 'T'");
                    psCheckT.setString(1, selectedCourse);
                    ResultSet rsT = psCheckT.executeQuery();
                    hasT = rsT.next();

                    if (hasP) comboBox3.addItem("P");
                    if (hasT) comboBox3.addItem("T");

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });

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

        try (Connection con = DBConnection.getConnection()) {
            if (comboBox2.getSelectedItem() == null) return;
            String courseCode = (String) comboBox2.getSelectedItem();

            PreparedStatement ps = con.prepareStatement("SELECT Username FROM Student_course_unit WHERE Course_code = ?");
            ps.setString(1, courseCode);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{rs.getString("Username"), false, false, false});
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
        try (Connection con = DBConnection.getConnection()) {
            if (comboBox1.getSelectedItem() == null || comboBox2.getSelectedItem() == null || comboBox3.getSelectedItem() == null || datePicker1.getDate() == null) {
                JOptionPane.showMessageDialog(this, "Please complete all fields.");
                return;
            }

            String toUsername = (String) comboBox1.getSelectedItem();
            String course = (String) comboBox2.getSelectedItem();
            String session = (String) comboBox3.getSelectedItem();
            String date = datePicker1.getDate().toString();

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
        addAttendanceToDatabase();
    }

    private void deleteAttendanceFromDatabase() {
        try (Connection con = DBConnection.getConnection()) {
            if (comboBox2.getSelectedItem() == null || datePicker1.getDate() == null) {
                JOptionPane.showMessageDialog(this, "Please select Course and Date.");
                return;
            }

            String course = (String) comboBox2.getSelectedItem();
            String date = datePicker1.getDate().toString();

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

    private void calculateAttendanceEligibility() {
        try (Connection con = DBConnection.getConnection()) {
            String query = "SELECT Student_Username, Course_code, " +
                    "SUM(CASE WHEN Status='Present' THEN 1 ELSE 0 END) AS Presents, " +
                    "SUM(CASE WHEN Status='Absent' THEN 1 ELSE 0 END) AS Absents, " +
                    "SUM(CASE WHEN Status='Medical' THEN 1 ELSE 0 END) AS Medicals, " +
                    "COUNT(*) AS Total " +
                    "FROM Attendance GROUP BY Student_Username, Course_code";
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String studentUsername = rs.getString("Student_Username");
                String courseCode = rs.getString("Course_code");
                int presents = rs.getInt("Presents");
                int absents = rs.getInt("Absents");
                int medicals = rs.getInt("Medicals");
                int total = rs.getInt("Total");

                double attendancePercentage = ((double) (presents + medicals) / total) * 100.0;

                System.out.println("Student: " + studentUsername + ", Course: " + courseCode +
                        ", Attendance: " + String.format("%.2f", attendancePercentage) + "%");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new attendancePage());
    }
}

 */

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import com.github.lgooddatepicker.components.*;
import java.awt.*;
import java.sql.*;
import java.time.format.DateTimeFormatter;
import javax.swing.event.TableModelEvent;

public class attendancePage extends JFrame {
    private JPanel panel1;
    private JPanel insertAttendanse;
    private JComboBox comboBox1; // TO Username
    private JComboBox comboBox2; // Course code
    private JComboBox comboBox3; // Course type
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
    private JButton SEARCHButton;
    private DatePicker datePicker1;
    private JButton calculateEligibilityButton;

    public attendancePage() {
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

        SEARCHButton.addActionListener(e -> searchAttendanceData());

        calculateEligibilityButton = new JButton("Calculate Eligibility");
        panel1.add(calculateEligibilityButton);
        calculateEligibilityButton.addActionListener(e -> calculateAttendanceEligibility());
    }

    private void loadComboBoxData() {
        try (Connection con = DBConnection.getConnection()) {
            // ComboBox 1 - TO Username
            comboBox1.removeAllItems();
            PreparedStatement ps1 = con.prepareStatement("SELECT Username FROM Technical_officer");
            ResultSet rs1 = ps1.executeQuery();
            while (rs1.next()) {
                comboBox1.addItem(rs1.getString("Username"));
            }

            // ComboBox 2 - Course Code
            comboBox2.removeAllItems();
            PreparedStatement ps2 = con.prepareStatement("SELECT DISTINCT Course_code FROM Course_unit");
            ResultSet rs2 = ps2.executeQuery();
            while (rs2.next()) {
                comboBox2.addItem(rs2.getString("Course_code"));
            }

            // ComboBox 3 - Course Type (Session Type) - Modified again here
            comboBox2.addActionListener(e -> {
                comboBox3.removeAllItems();
                if (comboBox2.getSelectedItem() == null) return;
                String selectedCourse = (String) comboBox2.getSelectedItem();
                try (Connection con2 = DBConnection.getConnection()) {
                    boolean hasP = false;
                    boolean hasT = false;

                    // Check for 'P'
                    PreparedStatement psCheckP = con2.prepareStatement(
                            "SELECT DISTINCT Session_Type FROM Attendance WHERE Course_code = ? AND Session_Type = 'P'");
                    psCheckP.setString(1, selectedCourse);
                    ResultSet rsP = psCheckP.executeQuery();
                    hasP = rsP.next();

                    // Check for 'T'
                    PreparedStatement psCheckT = con2.prepareStatement(
                            "SELECT DISTINCT Session_Type FROM Attendance WHERE Course_code = ? AND Session_Type = 'T'");
                    psCheckT.setString(1, selectedCourse);
                    ResultSet rsT = psCheckT.executeQuery();
                    hasT = rsT.next();

                    if (hasP) comboBox3.addItem("P");
                    if (hasT) comboBox3.addItem("T");

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });

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

        try (Connection con = DBConnection.getConnection()) {
            if (comboBox2.getSelectedItem() == null) return;
            String courseCode = (String) comboBox2.getSelectedItem();

            PreparedStatement ps = con.prepareStatement("SELECT Username FROM Student_course_unit WHERE Course_code = ?");
            ps.setString(1, courseCode);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{rs.getString("Username"), false, false, false});
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

    private void searchAttendanceData() {
        String courseCode = (String) comboBox2.getSelectedItem();
        String date = datePicker1.getDate().toString();

        if (courseCode == null || date == null) {
            JOptionPane.showMessageDialog(this, "Please select both Course and Date.");
            return;
        }

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

        try (Connection con = DBConnection.getConnection()) {
            PreparedStatement ps = con.prepareStatement(
                    "SELECT Student_Username, Status FROM Attendance WHERE Course_code = ? AND Session_date = ?");
            ps.setString(1, courseCode);
            ps.setString(2, date);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String student = rs.getString("Student_Username");
                String status = rs.getString("Status");
                model.addRow(new Object[]{student, "Present".equals(status), "Absent".equals(status), "Medical".equals(status)});
            }

            table1.setModel(model);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void addAttendanceToDatabase() {
        try (Connection con = DBConnection.getConnection()) {
            if (comboBox1.getSelectedItem() == null || comboBox2.getSelectedItem() == null || comboBox3.getSelectedItem() == null || datePicker1.getDate() == null) {
                JOptionPane.showMessageDialog(this, "Please complete all fields.");
                return;
            }

            String toUsername = (String) comboBox1.getSelectedItem();
            String course = (String) comboBox2.getSelectedItem();
            String session = (String) comboBox3.getSelectedItem();
            String date = datePicker1.getDate().toString();

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
        addAttendanceToDatabase();
    }

    private void deleteAttendanceFromDatabase() {
        try (Connection con = DBConnection.getConnection()) {
            if (comboBox2.getSelectedItem() == null || datePicker1.getDate() == null) {
                JOptionPane.showMessageDialog(this, "Please select Course and Date.");
                return;
            }

            String course = (String) comboBox2.getSelectedItem();
            String date = datePicker1.getDate().toString();

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

    private void calculateAttendanceEligibility() {
        try (Connection con = DBConnection.getConnection()) {
            String query = "SELECT Student_Username, Course_code, " +
                    "SUM(CASE WHEN Status='Present' THEN 1 ELSE 0 END) AS Presents, " +
                    "SUM(CASE WHEN Status='Absent' THEN 1 ELSE 0 END) AS Absents, " +
                    "SUM(CASE WHEN Status='Medical' THEN 1 ELSE 0 END) AS Medicals, " +
                    "COUNT(*) AS Total " +
                    "FROM Attendance GROUP BY Student_Username, Course_code";
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String studentUsername = rs.getString("Student_Username");
                String courseCode = rs.getString("Course_code");
                int presents = rs.getInt("Presents");
                int absents = rs.getInt("Absents");
                int medicals = rs.getInt("Medicals");
                int total = rs.getInt("Total");

                double attendancePercentage = ((double) (presents + medicals) / total) * 100.0;

                System.out.println("Student: " + studentUsername + ", Course: " + courseCode +
                        ", Attendance: " + String.format("%.2f", attendancePercentage) + "%");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new attendancePage());
    }
}

