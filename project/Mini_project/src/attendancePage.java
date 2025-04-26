/*
import javax.swing.*;
import com.github.lgooddatepicker.components.DatePicker;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class attendancePage extends JFrame {
    private JPanel panel1;
    private JPanel insertAttendanse;
    private JComboBox<String> comboBox1;
    private JComboBox<String> comboBox2;
    private JComboBox<String> comboBox3;
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

    public attendancePage() {
        // 1) Create the DatePicker and add it into the placeholder panel from your .form
        datePicker1 = new DatePicker();
        datePickerPanel.setLayout(new BorderLayout());
        datePickerPanel.add(datePicker1, BorderLayout.CENTER);

        // 2) Standard JFrame setup
        setTitle("Insert Attendance");
        setSize(1000, 500);

       // setSize(1000, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setContentPane(panel1);                // panel1 is your root form panel
                                 // sizes frame to fit all components
        setLocationRelativeTo(null);           // center on screen
        setVisible(true);
        BACKButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ToofficerPage();
                dispose();
            }
        });
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ToofficerPage();
                dispose();
            }
        });
    }

    public static void main(String[] args) {
        // Launch on the Event‑Dispatch Thread
        SwingUtilities.invokeLater(() -> new attendancePage());
    }//
}


 */

// Test data for Attendance DB:
// INSERT INTO attendance (Course_code, Student_Username, Session_Type, Session_date, Status, TO_Username, Medical_id) VALUES
// ('ICT2152', 'S001', 'Lecture', '2025-04-25', 'Present', 'T001', NULL),
// ('ICT2152', 'S002', 'Lecture', '2025-04-25', 'Absent', 'T001', NULL);

/*
import javax.swing.*;
import javax.swing.table.*;
import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class attendancePage extends JFrame {
    private JPanel panel1;
    private JPanel insertAttendanse;
    private JComboBox<String> comboBox1; // Course code
    private JComboBox<String> comboBox2; // Session type
    private JComboBox<String> comboBox3; // TO Username
    private JPanel datePickerPanel;
    private JButton showStudentsButton;
    private JButton button2; // Clear
    private JButton button3; // Add to DB
    private JButton BACKButton;
    private JTable table1;
    private JButton button1; // Select All Present
    private DatePicker datePicker1;
    private DefaultTableModel tableModel;
    private JButton UPDATEButton;


    public attendancePage() {
        DatePickerSettings settings = new DatePickerSettings();
        settings.setFormatForDatesCommonEra("yyyy-MM-dd");
        settings.setFormatForTodayButton(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        datePicker1 = new DatePicker(settings);
        datePickerPanel.setLayout(new BorderLayout());
        datePickerPanel.add(datePicker1, BorderLayout.CENTER);

        setTitle("Insert Attendance");
        setSize(1000, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setContentPane(panel1);
        setLocationRelativeTo(null);

        comboBox1.setModel(new DefaultComboBoxModel<>(new String[]{"ICT2152", "ICT2220"}));
        comboBox2.setModel(new DefaultComboBoxModel<>(new String[]{"Lecture", "Practical"}));
        comboBox3.setModel(new DefaultComboBoxModel<>(new String[]{"T001", "T002"}));

        tableModel = new DefaultTableModel(new String[]{"Student ID", "Present", "Absent", "Medical"}, 0) {
            public Class<?> getColumnClass(int column) {
                return column == 0 ? String.class : Boolean.class;
            }

            public boolean isCellEditable(int row, int column) {
                return column != 0;
            }
        };

        table1.setModel(tableModel);
        table1.setRowHeight(25);

        showStudentsButton.addActionListener(e -> loadStudents());
        button1.addActionListener(e -> selectAll("Present"));
        button2.addActionListener(e -> tableModel.setRowCount(0));
        button3.addActionListener(e -> addToDatabase());

        button1.addActionListener(e -> {
            new ToofficerPage();
            dispose();
        });

        setVisible(true);
    }

    private void loadStudents() {
        tableModel.setRowCount(0);
        String course = (String) comboBox1.getSelectedItem();
        try (Connection conn = getConnection()) {
            PreparedStatement ps = conn.prepareStatement("SELECT DISTINCT Student_Username FROM student_course WHERE Course_code = ?");
            ps.setString(1, course);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                tableModel.addRow(new Object[]{rs.getString(1), false, false, false});
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to load students.");
        }
    }

    private void selectAll(String status) {
        int columnIndex = switch (status) {
            case "Present" -> 1;
            case "Absent" -> 2;
            case "Medical" -> 3;
            default -> -1;
        };
        if (columnIndex == -1) return;

        for (int i = 0; i < tableModel.getRowCount(); i++) {
            tableModel.setValueAt(true, i, columnIndex);
            for (int j = 1; j < 4; j++) {
                if (j != columnIndex) tableModel.setValueAt(false, i, j);
            }
        }
    }

    private void addToDatabase() {
        String course = (String) comboBox1.getSelectedItem();
        String sessionType = (String) comboBox2.getSelectedItem();
        String toId = (String) comboBox3.getSelectedItem();
        LocalDate date = datePicker1.getDate();

        if (date == null) {
            JOptionPane.showMessageDialog(this, "Select a session date.");
            return;
        }

        try (Connection conn = getConnection()) {
            String sql = "INSERT INTO attendance (Course_code, Student_Username, Session_Type, Session_date, Status, TO_Username, Medical_id) VALUES (?, ?, ?, ?, ?, ?, NULL)";
            PreparedStatement ps = conn.prepareStatement(sql);
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                String studentId = tableModel.getValueAt(i, 0).toString();
                String status = null;
                if ((Boolean) tableModel.getValueAt(i, 1)) status = "Present";
                else if ((Boolean) tableModel.getValueAt(i, 2)) status = "Absent";
                else if ((Boolean) tableModel.getValueAt(i, 3)) status = "Medical";
                if (status != null) {
                    ps.setString(1, course);
                    ps.setString(2, studentId);
                    ps.setString(3, sessionType);
                    ps.setString(4, date.toString());
                    ps.setString(5, status);
                    ps.setString(6, toId);
                    ps.addBatch();
                }
            }
            ps.executeBatch();
            JOptionPane.showMessageDialog(this, "Attendance added to database.");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to add attendance: " + e.getMessage());
        }
    }

    private Connection getConnection() {
        try {
            return DriverManager.getConnection("jdbc:mysql://localhost:3306/tecmis", "your_username", "1234");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "DB connection error: " + e.getMessage());
            return null;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(attendancePage::new);
    }
}

 */
/*
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import com.github.lgooddatepicker.components.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class attendancePage extends JFrame {
    private JPanel panel1;
    private JPanel insertAttendanse;
    private JComboBox<String> comboBox1; // Course Code
    private JComboBox<String> comboBox2; // Session Type
    private JComboBox<String> comboBox3; // TO Username
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

    public attendancePage() {
        DatePickerSettings settings = new DatePickerSettings();
        settings.setFormatForDatesCommonEra("yyyy-MM-dd");
        settings.setFormatForTodayButton(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        datePicker1 = new DatePicker(settings);
        datePickerPanel.setLayout(new BorderLayout());
        datePickerPanel.add(datePicker1, BorderLayout.CENTER);

        // ✅ Combo box values for testing
        comboBox1.addItem("CS101");
        comboBox1.addItem("CS102");
        comboBox1.addItem("CS103");

        comboBox2.addItem("Lecture");
        comboBox2.addItem("Lab");
        comboBox2.addItem("Tutorial");

        comboBox3.addItem("TO001");
        comboBox3.addItem("TO002");
        comboBox3.addItem("TO003");

        setTitle("Insert Attendance");
        setSize(1000, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setContentPane(panel1);
        setLocationRelativeTo(null);
        setVisible(true);

        showStudentsButton.addActionListener(e -> loadStudents());
        SELECTALLButton.addActionListener(e -> selectAllPresent());
        CLEARALLButton.addActionListener(e -> clearTable());
        ADDButton.addActionListener(e -> addAttendance());
    }

    private void loadStudents() {
        DefaultTableModel model = new DefaultTableModel(new Object[]{"student", "present", "absent", "medical"}, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 0 ? String.class : Boolean.class;
            }
        };

        try (Connection con = getConnection()) {
            String courseCode = (String) comboBox1.getSelectedItem();
            PreparedStatement ps = con.prepareStatement("SELECT Student_Username FROM Enrollment WHERE Course_code = ?");
            ps.setString(1, courseCode);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                model.addRow(new Object[]{rs.getString(1), false, false, false});
            }
            table1.setModel(model);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        table1.getModel().addTableModelListener(e -> {
            int row = e.getFirstRow();
            int col = e.getColumn();
            if (col >= 1 && col <= 3) {
                for (int i = 1; i <= 3; i++) {
                    if (i != col) {
                        table1.setValueAt(false, row, i);
                    }
                }
            }
        });
    }

    private void selectAllPresent() {
        for (int i = 0; i < table1.getRowCount(); i++) {
            table1.setValueAt(true, i, 1);
            table1.setValueAt(false, i, 2);
            table1.setValueAt(false, i, 3);
        }
    }

    private void clearTable() {
        table1.setModel(new DefaultTableModel(new Object[]{"student", "present", "absent", "medical"}, 0));
    }

    private void addAttendance() {
        try (Connection con = getConnection()) {
            String course = (String) comboBox1.getSelectedItem();
            String session = (String) comboBox2.getSelectedItem();
            String toUsername = (String) comboBox3.getSelectedItem();
            String date = datePicker1.getDate() != null ? datePicker1.getDate().toString() : null;

            PreparedStatement ps = con.prepareStatement("INSERT INTO Attendance (Course_code, Student_Username, Session_Type, Session_date, Status, TO_Username, Medical_id) VALUES (?, ?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE Status = VALUES(Status)");
            for (int i = 0; i < table1.getRowCount(); i++) {
                String student = (String) table1.getValueAt(i, 0);
                String status = null;
                if ((Boolean) table1.getValueAt(i, 1)) status = "Present";
                else if ((Boolean) table1.getValueAt(i, 2)) status = "Absent";
                else if ((Boolean) table1.getValueAt(i, 3)) status = "Medical";
                if (status != null && date != null) {
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
            JOptionPane.showMessageDialog(this, "Attendance added successfully.");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // ✅ Dummy DB connection method for testing
    public Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection("jdbc:mysql://localhost:3306/techmis", "root", "password");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new attendancePage());
    }
}


 */


/*
CORRECT ONE CHECK DB AND CHECK WORK WELL

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import com.github.lgooddatepicker.components.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.time.format.DateTimeFormatter;

public class attendancePage extends JFrame {
    private JPanel panel1;
    private JPanel insertAttendanse;
    private JComboBox<String> comboBox1; // Course Code
    private JComboBox<String> comboBox2; // Session Type
    private JComboBox<String> comboBox3; // TO Username
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

    public attendancePage() {
        // ✅ DatePicker setup with format
        DatePickerSettings settings = new DatePickerSettings();
        settings.setFormatForDatesCommonEra("yyyy-MM-dd");
        settings.setFormatForTodayButton(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        datePicker1 = new DatePicker(settings);
        datePickerPanel.setLayout(new BorderLayout());
        datePickerPanel.add(datePicker1, BorderLayout.CENTER);

        // ✅ Dummy combo box values for testing
        comboBox1.addItem("CS101");
        comboBox1.addItem("CS102");
        comboBox1.addItem("CS103");

        comboBox2.addItem("Lecture");
        comboBox2.addItem("Lab");
        comboBox2.addItem("Tutorial");

        comboBox3.addItem("TO001");
        comboBox3.addItem("TO002");
        comboBox3.addItem("TO003");

        // ✅ JFrame setup
        setTitle("Insert Attendance");
        setSize(1000, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setContentPane(panel1);
        setLocationRelativeTo(null);
        setVisible(true);

        // ✅ Event handlers
        showStudentsButton.addActionListener(e -> loadStudents());
        SELECTALLButton.addActionListener(e -> selectAllPresent());
        CLEARALLButton.addActionListener(e -> clearTable());
        ADDButton.addActionListener(e -> addAttendance());
    }

    // ✅ Load students from Enrollment table based on selected course
    private void loadStudents() {
        DefaultTableModel model = new DefaultTableModel(new Object[]{"student", "present", "absent", "medical"}, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 0 ? String.class : Boolean.class;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return true;
            }
        };

        try (Connection con = getConnection()) {
            String courseCode = (String) comboBox1.getSelectedItem();
            PreparedStatement ps = con.prepareStatement("SELECT Student_Username FROM Enrollment WHERE Course_code = ?");
            ps.setString(1, courseCode);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                model.addRow(new Object[]{rs.getString(1), false, false, false});
            }
            table1.setModel(model);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // ✅ Mutually exclusive checkbox logic
        table1.getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                int row = e.getFirstRow();
                int col = e.getColumn();
                if (col >= 1 && col <= 3) {
                    for (int i = 1; i <= 3; i++) {
                        if (i != col) {
                            table1.setValueAt(false, row, i);
                        }
                    }
                }
            }
        });
    }

    // ✅ Select all "Present"
    private void selectAllPresent() {
        for (int i = 0; i < table1.getRowCount(); i++) {
            table1.setValueAt(true, i, 1);  // Present
            table1.setValueAt(false, i, 2); // Absent
            table1.setValueAt(false, i, 3); // Medical
        }
    }

    // ✅ Clear all table content
    private void clearTable() {
        table1.setModel(new DefaultTableModel(new Object[]{"student", "present", "absent", "medical"}, 0));
    }

    // ✅ Add attendance to DB
    private void addAttendance() {
        try (Connection con = getConnection()) {
            String course = (String) comboBox1.getSelectedItem();
            String session = (String) comboBox2.getSelectedItem();
            String toUsername = (String) comboBox3.getSelectedItem();
            String date = datePicker1.getDate() != null ? datePicker1.getDate().toString() : null;

            if (date == null || course == null || session == null || toUsername == null) {
                JOptionPane.showMessageDialog(this, "All fields must be filled.");
                return;
            }

            PreparedStatement ps = con.prepareStatement("INSERT INTO Attendance (Course_code, Student_Username, Session_Type, Session_date, Status, TO_Username, Medical_id) VALUES (?, ?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE Status = VALUES(Status)");
            for (int i = 0; i < table1.getRowCount(); i++) {
                String student = (String) table1.getValueAt(i, 0);
                String status = null;
                if ((Boolean) table1.getValueAt(i, 1)) status = "Present";
                else if ((Boolean) table1.getValueAt(i, 2)) status = "Absent";
                else if ((Boolean) table1.getValueAt(i, 3)) status = "Medical";

                if (status != null) {
                    ps.setString(1, course);
                    ps.setString(2, student);
                    ps.setString(3, session);
                    ps.setString(4, date);
                    ps.setString(5, status);
                    ps.setString(6, toUsername);
                    ps.setString(7, null); // Medical_id
                    ps.addBatch();
                }
            }

            ps.executeBatch();
            JOptionPane.showMessageDialog(this, "Attendance added successfully.");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // ✅ Real DB connection method
    public Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection("jdbc:mysql://localhost:3306/techmis", "root", "password"); // <- Edit password if needed
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(attendancePage::new);
    }
}



 */
/*
GUI WRONG TEST DATA ALSO IN THIS

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import com.github.lgooddatepicker.components.*;
import java.awt.*;
import java.awt.event.*;
import java.time.format.DateTimeFormatter;

public class attendancePage extends JFrame {
    private JPanel panel1;
    private JPanel insertAttendanse;
    private JComboBox<String> comboBox1; // Course Code
    private JComboBox<String> comboBox2; // Session Type
    private JComboBox<String> comboBox3; // TO Username
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

    public attendancePage() {
        setTitle("Insert Attendance");
        setSize(1000, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        panel1 = new JPanel(new BorderLayout());
        insertAttendanse = new JPanel();
        insertAttendanse.setLayout(new FlowLayout());

        comboBox1 = new JComboBox<>();
        comboBox2 = new JComboBox<>();
        comboBox3 = new JComboBox<>();
        showStudentsButton = new JButton("SHOW STUDENTS");
        SELECTALLButton = new JButton("SELECT ALL");
        CLEARALLButton = new JButton("CLEAR ALL");
        ADDButton = new JButton("ADD");

        UPDATEButton = new JButton("UPDATE");
        CLEARButton = new JButton("CLEAR");
        DELETEButton = new JButton("DELETE");
        button1 = new JButton("Unused");

        // Add components to insertAttendanse panel
        insertAttendanse.add(new JLabel("Course:"));
        insertAttendanse.add(comboBox1);
        insertAttendanse.add(new JLabel("Session:"));
        insertAttendanse.add(comboBox2);
        insertAttendanse.add(new JLabel("TO:"));
        insertAttendanse.add(comboBox3);

        insertAttendanse.add(showStudentsButton);
        insertAttendanse.add(SELECTALLButton);
        insertAttendanse.add(CLEARALLButton);
        insertAttendanse.add(ADDButton);

        datePickerPanel = new JPanel();
        DatePickerSettings settings = new DatePickerSettings();
        settings.setFormatForDatesCommonEra("yyyy-MM-dd");
        settings.setFormatForTodayButton(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        datePicker1 = new DatePicker(settings);
        datePickerPanel.add(new JLabel("Date:"));
        datePickerPanel.add(datePicker1);

        // Table
        table1 = new JTable();
        JScrollPane tableScroll = new JScrollPane(table1);

        panel1.add(insertAttendanse, BorderLayout.NORTH);
        panel1.add(datePickerPanel, BorderLayout.CENTER);
        panel1.add(tableScroll, BorderLayout.SOUTH);

        setContentPane(panel1);
        setLocationRelativeTo(null);
        setVisible(true);

        // ✅ Dummy Combo box values
        comboBox1.addItem("CS101");
        comboBox1.addItem("CS102");
        comboBox2.addItem("Lecture");
        comboBox2.addItem("Lab");
        comboBox3.addItem("TO001");
        comboBox3.addItem("TO002");

        // ✅ Button Actions
        showStudentsButton.addActionListener(e -> loadMockStudents());
        SELECTALLButton.addActionListener(e -> selectAllPresent());
        CLEARALLButton.addActionListener(e -> clearTable());
        ADDButton.addActionListener(e -> addAttendance());
    }

    // ✅ Load dummy students for testing
    private void loadMockStudents() {
        DefaultTableModel model = new DefaultTableModel(new Object[]{"student", "present", "absent", "medical"}, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 0 ? String.class : Boolean.class;
            }
        };

        model.addRow(new Object[]{"STU001", false, false, false});
        model.addRow(new Object[]{"STU002", false, false, false});
        model.addRow(new Object[]{"STU003", false, false, false});
        table1.setModel(model);

        table1.getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                int row = e.getFirstRow();
                int col = e.getColumn();
                if (col >= 1 && col <= 3) {
                    for (int i = 1; i <= 3; i++) {
                        if (i != col) {
                            table1.setValueAt(false, row, i);
                        }
                    }
                }
            }
        });
    }

    // ✅ Select All as Present
    private void selectAllPresent() {
        for (int i = 0; i < table1.getRowCount(); i++) {
            table1.setValueAt(true, i, 1);
            table1.setValueAt(false, i, 2);
            table1.setValueAt(false, i, 3);
        }
    }

    // ✅ Clear table
    private void clearTable() {
        table1.setModel(new DefaultTableModel(new Object[]{"student", "present", "absent", "medical"}, 0));
    }

    // ✅ Simulate ADD attendance (just print)
    private void addAttendance() {
        String course = (String) comboBox1.getSelectedItem();
        String session = (String) comboBox2.getSelectedItem();
        String toUsername = (String) comboBox3.getSelectedItem();
        String date = datePicker1.getDate() != null ? datePicker1.getDate().toString() : null;

        if (date == null) {
            JOptionPane.showMessageDialog(this, "Please select a date.");
            return;
        }

        System.out.println("Adding attendance:");
        for (int i = 0; i < table1.getRowCount(); i++) {
            String student = (String) table1.getValueAt(i, 0);
            String status = null;
            if ((Boolean) table1.getValueAt(i, 1)) status = "Present";
            else if ((Boolean) table1.getValueAt(i, 2)) status = "Absent";
            else if ((Boolean) table1.getValueAt(i, 3)) status = "Medical";

            if (status != null) {
                System.out.printf("Student: %s | Course: %s | Session: %s | Date: %s | Status: %s | TO: %s%n",
                        student, course, session, date, status, toUsername);
            }
        }

        JOptionPane.showMessageDialog(this, "Mock attendance processed (check console).");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(attendancePage::new);
    }
}

 */

/*
CHECK BOX DONT WORKS CORRECTLY
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import com.github.lgooddatepicker.components.*;
import java.awt.*;
import java.awt.event.*;
import java.time.format.DateTimeFormatter;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

public class attendancePage extends JFrame {
    private JPanel panel1;
    private JPanel insertAttendanse;
    private JComboBox<String> comboBox1;
    private JComboBox<String> comboBox2;
    private JComboBox<String> comboBox3;
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

    public attendancePage() {
        // Set up date picker
        DatePickerSettings settings = new DatePickerSettings();
        settings.setFormatForDatesCommonEra("yyyy-MM-dd");
        settings.setFormatForTodayButton(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        datePicker1 = new DatePicker(settings);
        datePickerPanel.setLayout(new BorderLayout());
        datePickerPanel.add(datePicker1, BorderLayout.CENTER);

        // Add sample items to combo boxes for testing
        comboBox1.addItem("CS101");
        comboBox1.addItem("CS102");

        comboBox2.addItem("Lecture");
        comboBox2.addItem("Lab");

        comboBox3.addItem("TO001");
        comboBox3.addItem("TO002");

        // Set up frame
        setTitle("Insert Attendance");
        setSize(1000, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setContentPane(panel1);
        setLocationRelativeTo(null);
        setVisible(true);

        // Button listeners
        showStudentsButton.addActionListener(e -> loadMockStudents());
        SELECTALLButton.addActionListener(e -> selectAllPresent());
        CLEARALLButton.addActionListener(e -> clearTable());
        ADDButton.addActionListener(e -> mockAddAttendance());
    }

    // ✅ Dummy student data loader
    private void loadMockStudents() {
        DefaultTableModel model = new DefaultTableModel(new Object[]{"student", "present", "absent", "medical"}, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 0 ? String.class : Boolean.class;
            }
        };

        model.addRow(new Object[]{"STU001", false, false, false});
        model.addRow(new Object[]{"STU002", false, false, false});
        model.addRow(new Object[]{"STU003", false, false, false});
        table1.setModel(model);

        table1.getModel().addTableModelListener(new TableModelListener() {
            public void tableChanged(TableModelEvent e) {
                int row = e.getFirstRow();
                int col = e.getColumn();
                if (col >= 1 && col <= 3) {
                    for (int i = 1; i <= 3; i++) {
                        if (i != col) {
                            table1.setValueAt(false, row, i);
                        }
                    }
                }
            }
        });
    }

    // ✅ Dummy select all present
    private void selectAllPresent() {
        for (int i = 0; i < table1.getRowCount(); i++) {
            table1.setValueAt(true, i, 1);
            table1.setValueAt(false, i, 2);
            table1.setValueAt(false, i, 3);
        }
    }

    // ✅ Dummy clear
    private void clearTable() {
        table1.setModel(new DefaultTableModel(new Object[]{"student", "present", "absent", "medical"}, 0));
    }

    // ✅ Dummy add logic to simulate insert
    private void mockAddAttendance() {
        String course = (String) comboBox1.getSelectedItem();
        String session = (String) comboBox2.getSelectedItem();
        String toUsername = (String) comboBox3.getSelectedItem();
        String date = datePicker1.getDate() != null ? datePicker1.getDate().toString() : null;

        if (date == null) {
            JOptionPane.showMessageDialog(this, "Please select a date.");
            return;
        }

        System.out.println("Simulating attendance insert:");
        for (int i = 0; i < table1.getRowCount(); i++) {
            String student = (String) table1.getValueAt(i, 0);
            String status = null;
            if ((Boolean) table1.getValueAt(i, 1)) status = "Present";
            else if ((Boolean) table1.getValueAt(i, 2)) status = "Absent";
            else if ((Boolean) table1.getValueAt(i, 3)) status = "Medical";

            if (status != null) {
                System.out.printf("Student: %s | Course: %s | Session: %s | Date: %s | Status: %s | TO: %s%n",
                        student, course, session, date, status, toUsername);
            }
        }

        JOptionPane.showMessageDialog(this, "Mock attendance inserted (see console).");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new attendancePage());
    }
}


 */

/*
ALL ARE DONE PERFECLY AND CLEAR all not works

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import com.github.lgooddatepicker.components.*;
import java.awt.*;
import java.awt.event.*;
import java.time.format.DateTimeFormatter;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;



public class attendancePage extends JFrame {
    private JPanel panel1;
    private JPanel insertAttendanse;
    private JComboBox<String> comboBox1;
    private JComboBox<String> comboBox2;
    private JComboBox<String> comboBox3;
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

    public attendancePage() {
        // Set up date picker
        DatePickerSettings settings = new DatePickerSettings();
        settings.setFormatForDatesCommonEra("yyyy-MM-dd");
        settings.setFormatForTodayButton(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        datePicker1 = new DatePicker(settings);
        datePickerPanel.setLayout(new BorderLayout());
        datePickerPanel.add(datePicker1, BorderLayout.CENTER);

        // Add dummy values to combo boxes
        comboBox1.addItem("CS101");
        comboBox1.addItem("CS102");

        comboBox2.addItem("Lecture");
        comboBox2.addItem("Lab");

        comboBox3.addItem("TO001");
        comboBox3.addItem("TO002");

        // Set up frame
        setTitle("Insert Attendance");
        setSize(1000, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setContentPane(panel1);
        setLocationRelativeTo(null);
        setVisible(true);

        // Button listeners
        showStudentsButton.addActionListener(e -> loadMockStudents());
        SELECTALLButton.addActionListener(e -> selectAllPresent());
        CLEARALLButton.addActionListener(e -> clearTable());
        ADDButton.addActionListener(e -> mockAddAttendance());
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ToofficerPage();
            }
        });
    }

    // ✅ Load dummy students with column names and checkbox logic
    private void loadMockStudents() {
        DefaultTableModel model = new DefaultTableModel(new Object[]{"Student ID", "Present", "Absent", "Medical"}, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 0 ? String.class : Boolean.class;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column > 0; // Only checkboxes editable
            }
        };

        model.addRow(new Object[]{"STU001", false, false, false});
        model.addRow(new Object[]{"STU002", false, false, false});
        model.addRow(new Object[]{"STU003", false, false, false});
        table1.setModel(model);

        table1.getModel().addTableModelListener(new TableModelListener() {
            public void tableChanged(TableModelEvent e) {
                int row = e.getFirstRow();
                int col = e.getColumn();
                if (col >= 1 && col <= 3 && Boolean.TRUE.equals(table1.getValueAt(row, col))) {
                    for (int i = 1; i <= 3; i++) {
                        if (i != col) {
                            table1.setValueAt(false, row, i);
                        }
                    }
                }
            }
        });
    }

    private void selectAllPresent() {
        for (int i = 0; i < table1.getRowCount(); i++) {
            table1.setValueAt(true, i, 1);
            table1.setValueAt(false, i, 2);
            table1.setValueAt(false, i, 3);
        }
    }

   // private void clearTable() {
   //     table1.setModel(new DefaultTableModel(new Object[]{"Student ID", "Present", "Absent", "Medical"}, 0));
  //  }


    private void clearTable() {
        for (int i = 0; i < table1.getRowCount(); i++) {
            table1.setValueAt(false, i, 1); // Clear Present
            table1.setValueAt(false, i, 2); // Clear Absent
            table1.setValueAt(false, i, 3); // Clear Medical
        }
    }


    private void mockAddAttendance() {
        String course = (String) comboBox1.getSelectedItem();
        String session = (String) comboBox2.getSelectedItem();
        String toUsername = (String) comboBox3.getSelectedItem();
        String date = datePicker1.getDate() != null ? datePicker1.getDate().toString() : null;

        if (date == null) {
            JOptionPane.showMessageDialog(this, "Please select a date.");
            return;
        }

        System.out.println("Simulating attendance insert:");
        for (int i = 0; i < table1.getRowCount(); i++) {
            String student = (String) table1.getValueAt(i, 0);
            String status = null;
            if ((Boolean) table1.getValueAt(i, 1)) status = "Present";
            else if ((Boolean) table1.getValueAt(i, 2)) status = "Absent";
            else if ((Boolean) table1.getValueAt(i, 3)) status = "Medical";

            if (status != null) {
                System.out.printf("Student: %s | Course: %s | Session: %s | Date: %s | Status: %s | TO: %s%n",
                        student, course, session, date, status, toUsername);
            }
        }

        JOptionPane.showMessageDialog(this, "Mock attendance inserted (see console).");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new attendancePage());
    }
}

 */


/*
add colums names and add scoller panel too
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import com.github.lgooddatepicker.components.*;
import java.awt.*;
import java.awt.event.*;
import java.time.format.DateTimeFormatter;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

public class attendancePage extends JFrame {
    private JPanel panel1;
    private JPanel insertAttendanse;
    private JComboBox<String> comboBox1;
    private JComboBox<String> comboBox2;
    private JComboBox<String> comboBox3;
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
    //private JScrollPane scrollPane1;

    public attendancePage() {
        // Set up date picker
        DatePickerSettings settings = new DatePickerSettings();
        settings.setFormatForDatesCommonEra("yyyy-MM-dd");
        settings.setFormatForTodayButton(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        datePicker1 = new DatePicker(settings);
        datePickerPanel.setLayout(new BorderLayout());
        datePickerPanel.add(datePicker1, BorderLayout.CENTER);

        // Add dummy values to combo boxes
        comboBox1.addItem("CS101");
        comboBox1.addItem("CS102");

        comboBox2.addItem("Lecture");
        comboBox2.addItem("Lab");

        comboBox3.addItem("TO001");
        comboBox3.addItem("TO002");

        // Set up table with column names
        DefaultTableModel model = new DefaultTableModel(
                new Object[][]{},
                new Object[]{"Student ID", "Present", "Absent", "Medical"}
        ) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 0 ? String.class : Boolean.class;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column > 0; // Only checkboxes editable
            }
        };
        table1.setModel(model);

        // Set up frame
        setTitle("Insert Attendance");
        setSize(1000, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setContentPane(panel1);
        setLocationRelativeTo(null);
        setVisible(true);

        // Button listeners
        showStudentsButton.addActionListener(e -> loadMockStudents());
        SELECTALLButton.addActionListener(e -> selectAllPresent());
        CLEARALLButton.addActionListener(e -> clearCheckboxSelections());
        ADDButton.addActionListener(e -> mockAddAttendance());
        button1.addActionListener(e -> new ToofficerPage());
    }

    private void loadMockStudents() {
        DefaultTableModel model = (DefaultTableModel) table1.getModel();
        model.setRowCount(0); // Clear existing rows

        model.addRow(new Object[]{"STU001", false, false, false});
        model.addRow(new Object[]{"STU002", false, false, false});
        model.addRow(new Object[]{"STU003", false, false, false});

        table1.getModel().addTableModelListener(new TableModelListener() {
            public void tableChanged(TableModelEvent e) {
                int row = e.getFirstRow();
                int col = e.getColumn();
                if (col >= 1 && col <= 3 && Boolean.TRUE.equals(table1.getValueAt(row, col))) {
                    for (int i = 1; i <= 3; i++) {
                        if (i != col) {
                            table1.setValueAt(false, row, i);
                        }
                    }
                }
            }
        });
    }

    private void selectAllPresent() {
        for (int i = 0; i < table1.getRowCount(); i++) {
            table1.setValueAt(true, i, 1);
            table1.setValueAt(false, i, 2);
            table1.setValueAt(false, i, 3);
        }
    }

    private void clearCheckboxSelections() {
        for (int i = 0; i < table1.getRowCount(); i++) {
            table1.setValueAt(false, i, 1);
            table1.setValueAt(false, i, 2);
            table1.setValueAt(false, i, 3);
        }
    }

    private void mockAddAttendance() {
        String course = (String) comboBox1.getSelectedItem();
        String session = (String) comboBox2.getSelectedItem();
        String toUsername = (String) comboBox3.getSelectedItem();
        String date = datePicker1.getDate() != null ? datePicker1.getDate().toString() : null;

        if (date == null) {
            JOptionPane.showMessageDialog(this, "Please select a date.");
            return;
        }

        System.out.println("Simulating attendance insert:");
        for (int i = 0; i < table1.getRowCount(); i++) {
            String student = (String) table1.getValueAt(i, 0);
            String status = null;
            if ((Boolean) table1.getValueAt(i, 1)) status = "Present";
            else if ((Boolean) table1.getValueAt(i, 2)) status = "Absent";
            else if ((Boolean) table1.getValueAt(i, 3)) status = "Medical";

            if (status != null) {
                System.out.printf("Student: %s | Course: %s | Session: %s | Date: %s | Status: %s | TO: %s%n",
                        student, course, session, date, status, toUsername);
            }
        }

        JOptionPane.showMessageDialog(this, "Mock attendance inserted (see console).");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new attendancePage());
    }
}


 */

/*
not works combo box

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import com.github.lgooddatepicker.components.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.time.format.DateTimeFormatter;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

public class attendancePage extends JFrame {
    private JPanel panel1;
    private JPanel insertAttendanse;
    private JComboBox<String> comboBox1;
    private JComboBox<String> comboBox2;
    private JComboBox<String> comboBox3;
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

    public attendancePage() {
        // Set up date picker
        DatePickerSettings settings = new DatePickerSettings();
        settings.setFormatForDatesCommonEra("yyyy-MM-dd");
        settings.setFormatForTodayButton(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        datePicker1 = new DatePicker(settings);
        datePickerPanel.setLayout(new BorderLayout());
        datePickerPanel.add(datePicker1, BorderLayout.CENTER);

        setTitle("Insert Attendance");
        setSize(1000, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setContentPane(panel1);
        setLocationRelativeTo(null);
        setVisible(true);

        // Button listeners
        showStudentsButton.addActionListener(e -> loadStudentsFromDatabase());
        SELECTALLButton.addActionListener(e -> selectAllPresent());
        CLEARALLButton.addActionListener(e -> clearCheckboxesOnly());
        CLEARButton.addActionListener(e -> clearEntireForm());
        ADDButton.addActionListener(e -> addAttendanceToDatabase());
    }

    private void loadStudentsFromDatabase() {
        DefaultTableModel model = new DefaultTableModel(
                new Object[]{"Student ID", "Present", "Absent", "Medical"}, 0) {
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
                    if (i != col) {
                        table1.setValueAt(false, row, i);
                    }
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
                String status = null;
                if ((Boolean) table1.getValueAt(i, 1)) status = "Present";
                else if ((Boolean) table1.getValueAt(i, 2)) status = "Absent";
                else if ((Boolean) table1.getValueAt(i, 3)) status = "Medical";

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
            JOptionPane.showMessageDialog(this, "Attendance successfully added.");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new attendancePage());
    }
}


 */

































/*full code with database connection without eligibility

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
    private JComboBox<String> comboBox1; // Course code
    private JComboBox<String> comboBox2; // Session type
    private JComboBox<String> comboBox3; // TO Username
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

    public attendancePage() {
        // Set up date picker
        DatePickerSettings settings = new DatePickerSettings();
        settings.setFormatForDatesCommonEra("yyyy-MM-dd");
        settings.setFormatForTodayButton(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        datePicker1 = new DatePicker(settings);
        datePickerPanel.setLayout(new BorderLayout());
        datePickerPanel.add(datePicker1, BorderLayout.CENTER);

        setTitle("Insert Attendance");
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
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ToofficerPage();
                dispose();
            }
        });
    }

    private void loadComboBoxData() {
        try (Connection con = getConnection()) {
            // Load course codes
            comboBox1.removeAllItems();
            PreparedStatement ps1 = con.prepareStatement("SELECT DISTINCT Course_code FROM Enrollment");
            ResultSet rs1 = ps1.executeQuery();
            while (rs1.next()) comboBox1.addItem(rs1.getString("Course_code"));

            // Load TO usernames
            comboBox3.removeAllItems();
            PreparedStatement ps2 = con.prepareStatement("SELECT Username FROM users WHERE Role = 'Technical Officer'");
            ResultSet rs2 = ps2.executeQuery();
            while (rs2.next()) comboBox3.addItem(rs2.getString("Username"));

            // Add fixed session types
            comboBox2.removeAllItems();
            comboBox2.addItem("Lecture");
            comboBox2.addItem("Lab");
            comboBox2.addItem("Tutorial");

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
        addAttendanceToDatabase(); // Same logic as add with ON DUPLICATE KEY
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new attendancePage());
    }
}

 */
/*
attendance table with eligibility but combo box issue

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
    private JComboBox<String> comboBox1; // Course code
    private JComboBox<String> comboBox2; // Session type
    private JComboBox<String> comboBox3; // TO Username
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
    private JButton CALCULATEELIGIBILITYButton; // <<<<<< New Button

    public attendancePage() {
        // Set up date picker
        DatePickerSettings settings = new DatePickerSettings();
        settings.setFormatForDatesCommonEra("yyyy-MM-dd");
        settings.setFormatForTodayButton(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        datePicker1 = new DatePicker(settings);
        datePickerPanel.setLayout(new BorderLayout());
        datePickerPanel.add(datePicker1, BorderLayout.CENTER);

        setTitle("Insert Attendance");
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

        // New Button listener
        CALCULATEELIGIBILITYButton.addActionListener(e -> calculateAttendanceEligibility());
    }

    private void loadComboBoxData() {
        try (Connection con = getConnection()) {
            // Load course codes
            comboBox1.removeAllItems();
            PreparedStatement ps1 = con.prepareStatement("SELECT DISTINCT Course_code FROM Enrollment");
            ResultSet rs1 = ps1.executeQuery();
            while (rs1.next()) comboBox1.addItem(rs1.getString("Course_code"));

            // Load TO usernames
            comboBox3.removeAllItems();
            PreparedStatement ps2 = con.prepareStatement("SELECT Username FROM users WHERE Role = 'Technical Officer'");
            ResultSet rs2 = ps2.executeQuery();
            while (rs2.next()) comboBox3.addItem(rs2.getString("Username"));

            // Add fixed session types
            comboBox2.removeAllItems();
            comboBox2.addItem("Lecture");
            comboBox2.addItem("Lab");
            comboBox2.addItem("Tutorial");

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
        addAttendanceToDatabase(); // Same logic as add with ON DUPLICATE KEY
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

    private void calculateAttendanceEligibility() {
        try (Connection con = getConnection()) {
            // Clear existing eligibility table
            PreparedStatement clearPs = con.prepareStatement("DELETE FROM attendance_eligibility");
            clearPs.executeUpdate();

            // Total sessions
            PreparedStatement totalSessionsPs = con.prepareStatement(
                    "SELECT Course_code, COUNT(DISTINCT Session_date) AS total_sessions FROM Attendance GROUP BY Course_code");
            ResultSet totalSessionsRs = totalSessionsPs.executeQuery();

            while (totalSessionsRs.next()) {
                String courseCode = totalSessionsRs.getString("Course_code");
                int totalSessions = totalSessionsRs.getInt("total_sessions");

                PreparedStatement studentsPs = con.prepareStatement(
                        "SELECT Student_Username, " +
                                "SUM(CASE WHEN Status = 'Present' THEN 1 ELSE 0 END) AS present_count, " +
                                "SUM(CASE WHEN Status = 'Medical' THEN 1 ELSE 0 END) AS medical_count " +
                                "FROM Attendance " +
                                "WHERE Course_code = ? " +
                                "GROUP BY Student_Username");
                studentsPs.setString(1, courseCode);
                ResultSet studentsRs = studentsPs.executeQuery();

                while (studentsRs.next()) {
                    String studentUsername = studentsRs.getString("Student_Username");
                    int presentCount = studentsRs.getInt("present_count");
                    int medicalCount = studentsRs.getInt("medical_count");

                    double attendancePercentage = (totalSessions > 0) ? ((double) presentCount / totalSessions) * 100 : 0;

                    String eligibilityStatus;
                    if (attendancePercentage > 80.0 && medicalCount == 0) {
                        eligibilityStatus = "Eligible";
                    } else if (attendancePercentage == 80.0 && medicalCount == 0) {
                        eligibilityStatus = "Eligible";
                    } else if (attendancePercentage > 80.0 && medicalCount > 0) {
                        eligibilityStatus = "Eligible with Medicals";
                    } else if (attendancePercentage < 80.0 && medicalCount > 0) {
                        eligibilityStatus = "Eligible with Medicals";
                    } else {
                        eligibilityStatus = "Not Eligible";
                    }

                    PreparedStatement insertPs = con.prepareStatement(
                            "INSERT INTO attendance_eligibility (Student_Username, Course_code, Attendance_Percentage, Medical_Count, Eligibility_Status) " +
                                    "VALUES (?, ?, ?, ?, ?)");
                    insertPs.setString(1, studentUsername);
                    insertPs.setString(2, courseCode);
                    insertPs.setDouble(3, attendancePercentage);
                    insertPs.setInt(4, medicalCount);
                    insertPs.setString(5, eligibilityStatus);
                    insertPs.executeUpdate();
                }
            }
            JOptionPane.showMessageDialog(this, "Attendance eligibility calculation completed!");
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new attendancePage());
    }
}


 */
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
    private JComboBox<String> comboBox1; // Course code
    private JComboBox<String> comboBox2; // Session type
    private JComboBox<String> comboBox3; // TO Username
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

    public attendancePage() {
        // Set up date picker
        DatePickerSettings settings = new DatePickerSettings();
        settings.setFormatForDatesCommonEra("yyyy-MM-dd");
        settings.setFormatForTodayButton(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        datePicker1 = new DatePicker(settings);
        datePickerPanel.setLayout(new BorderLayout());
        datePickerPanel.add(datePicker1, BorderLayout.CENTER);

        setTitle("Insert Attendance");
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
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ToofficerPage();
                dispose();
            }
        });
    }

    private void loadComboBoxData() {
        try (Connection con = getConnection()) {
            // Load course codes from Medical table
            comboBox1.removeAllItems();
            PreparedStatement ps1 = con.prepareStatement("SELECT DISTINCT Course_code FROM Medical");
            ResultSet rs1 = ps1.executeQuery();
            while (rs1.next()) {
                comboBox1.addItem(rs1.getString("Course_code"));
            }

            // Load session types from Attendance table
            comboBox2.removeAllItems();
            PreparedStatement ps2 = con.prepareStatement("SELECT DISTINCT Session_Type FROM Attendance");
            ResultSet rs2 = ps2.executeQuery();
            while (rs2.next()) {
                comboBox2.addItem(rs2.getString("Session_Type"));
            }

            // Load TO usernames from Medical table
            comboBox3.removeAllItems();
            PreparedStatement ps3 = con.prepareStatement("SELECT DISTINCT TO_Username FROM Medical");
            ResultSet rs3 = ps3.executeQuery();
            while (rs3.next()) {
                comboBox3.addItem(rs3.getString("TO_Username"));
            }

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

            PreparedStatement ps = con.prepareStatement("SELECT DISTINCT Student_Username FROM Attendance WHERE Course_code = ?");
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
                    if (i != col) table1.setValueAt(false, i);
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
        addAttendanceToDatabase();
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new attendancePage());
    }
}
