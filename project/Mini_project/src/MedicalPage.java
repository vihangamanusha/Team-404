/*
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.*;
import java.io.File;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class MedicalPage extends JFrame {

    private JTextField textField1; // Student ID
    private JComboBox<String> comboBox1; // Course Code
    private JTextField textField2; // Medical ID
    private JComboBox<String> comboBox2; // TO ID
    private JComboBox<String> comboBox3; // Status
    private JTextArea textArea1; // Reason
    private JButton ADDButton, UPDATEButton, DELETEButton, CLEARButton, SEARCHButton;
    private JButton UPLOADMEDICALIMAGEButton, CLEARTHEIMAGEButton, button1;
    private JPanel mainpanel, SubmissionDatePanel, StartDatePanel, EndDatePanel;
    private JPanel formpanel, ButtonPanel;
    private JLabel imagePicker;

    private DatePicker submissionDatePicker, startDatePicker, endDatePicker;
    private File selectedImageFile = null;

    public MedicalPage() {
        setTitle("Medical Page");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1000, 500);
        setLocationRelativeTo(null);

        //  Set DatePickers with format and placeholder
        DatePickerSettings submissionSettings = new DatePickerSettings();
        submissionSettings.setFormatForDatesCommonEra("yyyy-MM-dd");
        submissionSettings.setFormatForTodayButton(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        submissionDatePicker = new DatePicker(submissionSettings);

        DatePickerSettings startSettings = new DatePickerSettings();
        startSettings.setFormatForDatesCommonEra("yyyy-MM-dd");
        startSettings.setFormatForTodayButton(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        startDatePicker = new DatePicker(startSettings);

        DatePickerSettings endSettings = new DatePickerSettings();
        endSettings.setFormatForDatesCommonEra("yyyy-MM-dd");
        endSettings.setFormatForTodayButton(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        endDatePicker = new DatePicker(endSettings);

        SubmissionDatePanel.setLayout(new BorderLayout());
        SubmissionDatePanel.add(submissionDatePicker, BorderLayout.CENTER);
        StartDatePanel.setLayout(new BorderLayout());
        StartDatePanel.add(startDatePicker, BorderLayout.CENTER);
        EndDatePanel.setLayout(new BorderLayout());
        EndDatePanel.add(endDatePicker, BorderLayout.CENTER);

        comboBox1.setModel(new DefaultComboBoxModel<>(new String[]{"ICT2152", "ICT2220", "ICT2243"}));
        comboBox2.setModel(new DefaultComboBoxModel<>(new String[]{"T001", "T002", "T003"}));
        comboBox3.setModel(new DefaultComboBoxModel<>(new String[]{"Pending", "Approved", "Rejected"}));

        imagePicker.setTransferHandler(new TransferHandler() {
            public boolean canImport(TransferSupport support) {
                return support.isDataFlavorSupported(DataFlavor.javaFileListFlavor);
            }

            public boolean importData(TransferSupport support) {
                try {
                    Transferable t = support.getTransferable();
                    List<File> files = (List<File>) t.getTransferData(DataFlavor.javaFileListFlavor);
                    if (!files.isEmpty()) {
                        setImage(files.get(0));
                        return true;
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                return false;
            }
        });

        imagePicker.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        imagePicker.setHorizontalAlignment(JLabel.CENTER);
        imagePicker.setText("Drag and drop image here or click to choose");
        imagePicker.setPreferredSize(new Dimension(200, 200));
        imagePicker.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                chooseImageFile();
            }
        });

        setContentPane(mainpanel);

        ADDButton.addActionListener(e -> {
            try (Connection conn = getConnection()) {
                String sql = "INSERT INTO medical (Medical_id, Course_code, Student_Username, TO_Username, Submission_date, Status, Start_date, End_date, Reason, Medical_image) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, textField2.getText());
                ps.setString(2, (String) comboBox1.getSelectedItem());
                ps.setString(3, textField1.getText());
                ps.setString(4, (String) comboBox2.getSelectedItem());
                ps.setString(5, submissionDatePicker.getDate() != null ? submissionDatePicker.getDate().toString() : null);
                ps.setString(6, (String) comboBox3.getSelectedItem());
                ps.setString(7, startDatePicker.getDate() != null ? startDatePicker.getDate().toString() : null);
                ps.setString(8, endDatePicker.getDate() != null ? endDatePicker.getDate().toString() : null);
                ps.setString(9, textArea1.getText());
                ps.setString(10, selectedImageFile != null ? selectedImageFile.getAbsolutePath() : null);
                ps.executeUpdate();
                JOptionPane.showMessageDialog(null, "Medical record added.");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
            }
        });

        SEARCHButton.addActionListener(e -> {
            try (Connection conn = getConnection()) {
                String sql = "SELECT * FROM medical WHERE Medical_id = ? AND Student_Username = ?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, textField2.getText());
                ps.setString(2, textField1.getText());
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    comboBox1.setSelectedItem(rs.getString("Course_code"));
                    comboBox2.setSelectedItem(rs.getString("TO_Username"));
                    comboBox3.setSelectedItem(rs.getString("Status"));
                    textArea1.setText(rs.getString("Reason"));
                    submissionDatePicker.setDate(LocalDate.parse(rs.getString("Submission_date")));
                    startDatePicker.setDate(LocalDate.parse(rs.getString("Start_date")));
                    endDatePicker.setDate(LocalDate.parse(rs.getString("End_date")));
                    String imagePath = rs.getString("Medical_image");
                    if (imagePath != null && !imagePath.isEmpty()) {
                        setImage(new File(imagePath));
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "No record found.");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
            }
        });

        UPDATEButton.addActionListener(e -> {
            try (Connection conn = getConnection()) {
                String sql = "UPDATE medical SET Course_code=?, TO_Username=?, Submission_date=?, Status=?, Start_date=?, End_date=?, Reason=?, Medical_image=? WHERE Medical_id=? AND Student_Username=?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, (String) comboBox1.getSelectedItem());
                ps.setString(2, (String) comboBox2.getSelectedItem());
                ps.setString(3, submissionDatePicker.getDate() != null ? submissionDatePicker.getDate().toString() : null);
                ps.setString(4, (String) comboBox3.getSelectedItem());
                ps.setString(5, startDatePicker.getDate() != null ? startDatePicker.getDate().toString() : null);
                ps.setString(6, endDatePicker.getDate() != null ? endDatePicker.getDate().toString() : null);
                ps.setString(7, textArea1.getText());
                ps.setString(8, selectedImageFile != null ? selectedImageFile.getAbsolutePath() : null);
                ps.setString(9, textField2.getText());
                ps.setString(10, textField1.getText());
                ps.executeUpdate();
                JOptionPane.showMessageDialog(null, "Record updated.");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Update failed: " + ex.getMessage());
            }
        });

        DELETEButton.addActionListener(e -> {
            try (Connection conn = getConnection()) {
                String sql = "DELETE FROM medical WHERE Medical_id = ? AND Student_Username = ?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, textField2.getText());
                ps.setString(2, textField1.getText());
                int rows = ps.executeUpdate();
                if (rows > 0) {
                    JOptionPane.showMessageDialog(null, "Record deleted.");
                    clearForm();
                } else {
                    JOptionPane.showMessageDialog(null, "No record found to delete.");
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Delete failed: " + ex.getMessage());
            }
        });

        CLEARButton.addActionListener(e -> clearForm());

        UPLOADMEDICALIMAGEButton.addActionListener(e -> chooseImageFile());

        CLEARTHEIMAGEButton.addActionListener(e -> {
            selectedImageFile = null;
            imagePicker.setIcon(null);
            imagePicker.setText("Drag and drop image here or click to choose");
        });

        setVisible(true);

        button1.addActionListener(e -> {
            new ToofficerPage();
            dispose();
        });
    }

    private void chooseImageFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select Image");
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Images", "jpg", "jpeg", "png", "gif");
        fileChooser.setFileFilter(filter);
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            setImage(fileChooser.getSelectedFile());
        }
    }

    private void setImage(File imageFile) {
        try {
            ImageIcon icon = new ImageIcon(imageFile.getAbsolutePath());
            Image img = icon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
            imagePicker.setIcon(new ImageIcon(img));
            imagePicker.setText("");
            selectedImageFile = imageFile;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to load image: " + e.getMessage());
        }
    }

    private void clearForm() {
        textField1.setText("");
        textField2.setText("");
        comboBox1.setSelectedIndex(0);
        comboBox2.setSelectedIndex(0);
        comboBox3.setSelectedIndex(0);
        textArea1.setText("");
        submissionDatePicker.clear();
        startDatePicker.clear();
        endDatePicker.clear();
        imagePicker.setIcon(null);
        imagePicker.setText("Drag and drop image here or click to choose");
        selectedImageFile = null;
    }

    private Connection getConnection() {
        try {
            return DriverManager.getConnection("jdbc:mysql://localhost:3306/tecmis", "your_username", "1234");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database connection failed: " + e.getMessage());
            return null;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MedicalPage::new);
    }
}
*/

/*connect db and see
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.*;
import java.io.File;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ArrayList;

public class MedicalPage extends JFrame {

    private JTextField textField1; // Student ID
    private JComboBox<String> comboBox1; // Course Code
    private JTextField textField2; // Medical ID
    private JComboBox<String> comboBox2; // TO ID
    private JComboBox<String> comboBox3; // Status
    private JTextArea textArea1; // Reason
    private JButton ADDButton, UPDATEButton, DELETEButton, CLEARButton, SEARCHButton;
    private JButton UPLOADMEDICALIMAGEButton, CLEARTHEIMAGEButton, button1;
    private JPanel mainpanel, SubmissionDatePanel, StartDatePanel, EndDatePanel;
    private JPanel formpanel, ButtonPanel;
    private JLabel imagePicker;

    private DatePicker submissionDatePicker, startDatePicker, endDatePicker;
    private File selectedImageFile = null;

    public MedicalPage() {
        setTitle("Medical Page");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1000, 500);
        setLocationRelativeTo(null);

        //  Set DatePickers with format and placeholder
        DatePickerSettings submissionSettings = new DatePickerSettings();
        submissionSettings.setFormatForDatesCommonEra("yyyy-MM-dd");
        submissionSettings.setFormatForTodayButton(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        submissionDatePicker = new DatePicker(submissionSettings);

        DatePickerSettings startSettings = new DatePickerSettings();
        startSettings.setFormatForDatesCommonEra("yyyy-MM-dd");
        startSettings.setFormatForTodayButton(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        startDatePicker = new DatePicker(startSettings);

        DatePickerSettings endSettings = new DatePickerSettings();
        endSettings.setFormatForDatesCommonEra("yyyy-MM-dd");
        endSettings.setFormatForTodayButton(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        endDatePicker = new DatePicker(endSettings);

        SubmissionDatePanel.setLayout(new BorderLayout());
        SubmissionDatePanel.add(submissionDatePicker, BorderLayout.CENTER);
        StartDatePanel.setLayout(new BorderLayout());
        StartDatePanel.add(startDatePicker, BorderLayout.CENTER);
        EndDatePanel.setLayout(new BorderLayout());
        EndDatePanel.add(endDatePicker, BorderLayout.CENTER);

        loadComboBoxData(); // <<< Load combo box values from database here

        imagePicker.setTransferHandler(new TransferHandler() {
            public boolean canImport(TransferSupport support) {
                return support.isDataFlavorSupported(DataFlavor.javaFileListFlavor);
            }

            public boolean importData(TransferSupport support) {
                try {
                    Transferable t = support.getTransferable();
                    List<File> files = (List<File>) t.getTransferData(DataFlavor.javaFileListFlavor);
                    if (!files.isEmpty()) {
                        setImage(files.get(0));
                        return true;
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                return false;
            }
        });

        imagePicker.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        imagePicker.setHorizontalAlignment(JLabel.CENTER);
        imagePicker.setText("Drag and drop image here or click to choose");
        imagePicker.setPreferredSize(new Dimension(200, 200));
        imagePicker.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                chooseImageFile();
            }
        });

        setContentPane(mainpanel);

        ADDButton.addActionListener(e -> {
            try (Connection conn = getConnection()) {
                String sql = "INSERT INTO medical (Medical_id, Course_code, Student_Username, TO_Username, Submission_date, Status, Start_date, End_date, Reason, Medical_image) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, textField2.getText());
                ps.setString(2, (String) comboBox1.getSelectedItem());
                ps.setString(3, textField1.getText());
                ps.setString(4, (String) comboBox2.getSelectedItem());
                ps.setString(5, submissionDatePicker.getDate() != null ? submissionDatePicker.getDate().toString() : null);
                ps.setString(6, (String) comboBox3.getSelectedItem());
                ps.setString(7, startDatePicker.getDate() != null ? startDatePicker.getDate().toString() : null);
                ps.setString(8, endDatePicker.getDate() != null ? endDatePicker.getDate().toString() : null);
                ps.setString(9, textArea1.getText());
                ps.setString(10, selectedImageFile != null ? selectedImageFile.getAbsolutePath() : null);
                ps.executeUpdate();
                JOptionPane.showMessageDialog(null, "Medical record added.");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
            }
        });

        SEARCHButton.addActionListener(e -> {
            try (Connection conn = getConnection()) {
                String sql = "SELECT * FROM medical WHERE Medical_id = ? AND Student_Username = ?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, textField2.getText());
                ps.setString(2, textField1.getText());
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    comboBox1.setSelectedItem(rs.getString("Course_code"));
                    comboBox2.setSelectedItem(rs.getString("TO_Username"));
                    comboBox3.setSelectedItem(rs.getString("Status"));
                    textArea1.setText(rs.getString("Reason"));
                    submissionDatePicker.setDate(LocalDate.parse(rs.getString("Submission_date")));
                    startDatePicker.setDate(LocalDate.parse(rs.getString("Start_date")));
                    endDatePicker.setDate(LocalDate.parse(rs.getString("End_date")));
                    String imagePath = rs.getString("Medical_image");
                    if (imagePath != null && !imagePath.isEmpty()) {
                        setImage(new File(imagePath));
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "No record found.");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
            }
        });

        UPDATEButton.addActionListener(e -> {
            try (Connection conn = getConnection()) {
                String sql = "UPDATE medical SET Course_code=?, TO_Username=?, Submission_date=?, Status=?, Start_date=?, End_date=?, Reason=?, Medical_image=? WHERE Medical_id=? AND Student_Username=?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, (String) comboBox1.getSelectedItem());
                ps.setString(2, (String) comboBox2.getSelectedItem());
                ps.setString(3, submissionDatePicker.getDate() != null ? submissionDatePicker.getDate().toString() : null);
                ps.setString(4, (String) comboBox3.getSelectedItem());
                ps.setString(5, startDatePicker.getDate() != null ? startDatePicker.getDate().toString() : null);
                ps.setString(6, endDatePicker.getDate() != null ? endDatePicker.getDate().toString() : null);
                ps.setString(7, textArea1.getText());
                ps.setString(8, selectedImageFile != null ? selectedImageFile.getAbsolutePath() : null);
                ps.setString(9, textField2.getText());
                ps.setString(10, textField1.getText());
                ps.executeUpdate();
                JOptionPane.showMessageDialog(null, "Record updated.");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Update failed: " + ex.getMessage());
            }
        });

        DELETEButton.addActionListener(e -> {
            try (Connection conn = getConnection()) {
                String sql = "DELETE FROM medical WHERE Medical_id = ? AND Student_Username = ?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, textField2.getText());
                ps.setString(2, textField1.getText());
                int rows = ps.executeUpdate();
                if (rows > 0) {
                    JOptionPane.showMessageDialog(null, "Record deleted.");
                    clearForm();
                } else {
                    JOptionPane.showMessageDialog(null, "No record found to delete.");
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Delete failed: " + ex.getMessage());
            }
        });

        CLEARButton.addActionListener(e -> clearForm());

        UPLOADMEDICALIMAGEButton.addActionListener(e -> chooseImageFile());

        CLEARTHEIMAGEButton.addActionListener(e -> {
            selectedImageFile = null;
            imagePicker.setIcon(null);
            imagePicker.setText("Drag and drop image here or click to choose");
        });

        setVisible(true);

        button1.addActionListener(e -> {
            new ToofficerPage();
            dispose();
        });
    }

    private void loadComboBoxData() {
        try (Connection conn = getConnection()) {
            // Load Course Codes
            PreparedStatement ps1 = conn.prepareStatement("SELECT DISTINCT Course_code FROM medical");
            ResultSet rs1 = ps1.executeQuery();
            List<String> courseCodes = new ArrayList<>();
            while (rs1.next()) {
                courseCodes.add(rs1.getString("Course_code"));
            }
            comboBox1.setModel(new DefaultComboBoxModel<>(courseCodes.toArray(new String[0])));

            // Load TO IDs
            PreparedStatement ps2 = conn.prepareStatement("SELECT DISTINCT TO_Username FROM medical");
            ResultSet rs2 = ps2.executeQuery();
            List<String> toIDs = new ArrayList<>();
            while (rs2.next()) {
                toIDs.add(rs2.getString("TO_Username"));
            }
            comboBox2.setModel(new DefaultComboBoxModel<>(toIDs.toArray(new String[0])));

            // Load Status
            PreparedStatement ps3 = conn.prepareStatement("SELECT DISTINCT Status FROM medical");
            ResultSet rs3 = ps3.executeQuery();
            List<String> statuses = new ArrayList<>();
            while (rs3.next()) {
                statuses.add(rs3.getString("Status"));
            }
            comboBox3.setModel(new DefaultComboBoxModel<>(statuses.toArray(new String[0])));

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error loading combo box data: " + ex.getMessage());
        }
    }

    private void chooseImageFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select Image");
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Images", "jpg", "jpeg", "png", "gif");
        fileChooser.setFileFilter(filter);
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            setImage(fileChooser.getSelectedFile());
        }
    }

    private void setImage(File imageFile) {
        try {
            ImageIcon icon = new ImageIcon(imageFile.getAbsolutePath());
            Image img = icon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
            imagePicker.setIcon(new ImageIcon(img));
            imagePicker.setText("");
            selectedImageFile = imageFile;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to load image: " + e.getMessage());
        }
    }

    private void clearForm() {
        textField1.setText("");
        textField2.setText("");
        comboBox1.setSelectedIndex(0);
        comboBox2.setSelectedIndex(0);
        comboBox3.setSelectedIndex(0);
        textArea1.setText("");
        submissionDatePicker.clear();
        startDatePicker.clear();
        endDatePicker.clear();
        imagePicker.setIcon(null);
        imagePicker.setText("Drag and drop image here or click to choose");
        selectedImageFile = null;
    }

    private Connection getConnection() {
        try {
            return DriverManager.getConnection("jdbc:mysql://localhost:3306/tecmis", "your_username", "1234");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database connection failed: " + e.getMessage());
            return null;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MedicalPage::new);
    }
}


 */
/*

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.*;
import java.io.File;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class MedicalPage extends JFrame
*/







import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.*;
import java.io.File;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class MedicalPage extends JFrame {

    private JTextField textField1; // Student ID
    private JComboBox<String> comboBox1; // Course Code
    private JTextField textField2; // Medical ID
    private JComboBox<String> comboBox2; // TO ID
    private JComboBox<String> comboBox3; // Status
    private JTextArea textArea1; // Reason
    private JButton ADDButton, UPDATEButton, DELETEButton, CLEARButton, SEARCHButton;
    private JButton UPLOADMEDICALIMAGEButton, CLEARTHEIMAGEButton, button1;
    private JPanel mainpanel, SubmissionDatePanel, StartDatePanel, EndDatePanel;
    private JPanel formpanel, ButtonPanel;
    private JLabel imagePicker;

    private DatePicker submissionDatePicker, startDatePicker, endDatePicker;
    private File selectedImageFile = null;

    public MedicalPage() {
        setTitle("Medical Page");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1000, 500);
        setLocationRelativeTo(null);

        // Set DatePickers
        DatePickerSettings submissionSettings = new DatePickerSettings();
        submissionSettings.setFormatForDatesCommonEra("yyyy-MM-dd");
        submissionSettings.setFormatForTodayButton(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        submissionDatePicker = new DatePicker(submissionSettings);

        DatePickerSettings startSettings = new DatePickerSettings();
        startSettings.setFormatForDatesCommonEra("yyyy-MM-dd");
        startSettings.setFormatForTodayButton(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        startDatePicker = new DatePicker(startSettings);

        DatePickerSettings endSettings = new DatePickerSettings();
        endSettings.setFormatForDatesCommonEra("yyyy-MM-dd");
        endSettings.setFormatForTodayButton(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        endDatePicker = new DatePicker(endSettings);

        SubmissionDatePanel.setLayout(new BorderLayout());
        SubmissionDatePanel.add(submissionDatePicker, BorderLayout.CENTER);
        StartDatePanel.setLayout(new BorderLayout());
        StartDatePanel.add(startDatePicker, BorderLayout.CENTER);
        EndDatePanel.setLayout(new BorderLayout());
        EndDatePanel.add(endDatePicker, BorderLayout.CENTER);

        // Load combo box data from database
        loadComboBoxData();

        imagePicker.setTransferHandler(new TransferHandler() {
            public boolean canImport(TransferSupport support) {
                return support.isDataFlavorSupported(DataFlavor.javaFileListFlavor);
            }

            public boolean importData(TransferSupport support) {
                try {
                    Transferable t = support.getTransferable();
                    List<File> files = (List<File>) t.getTransferData(DataFlavor.javaFileListFlavor);
                    if (!files.isEmpty()) {
                        setImage(files.get(0));
                        return true;
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                return false;
            }
        });

        imagePicker.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        imagePicker.setHorizontalAlignment(JLabel.CENTER);
        imagePicker.setText("Drag and drop image here or click to choose");
        imagePicker.setPreferredSize(new Dimension(200, 200));
        imagePicker.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                chooseImageFile();
            }
        });

        setContentPane(mainpanel);

        ADDButton.addActionListener(e -> {
            try (Connection conn = getConnection()) {
                String sql = "INSERT INTO medical (Medical_id, Course_code, Student_Username, TO_Username, Submission_date, Status, Start_date, End_date, Reason, Medical_image) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, textField2.getText());
                ps.setString(2, (String) comboBox1.getSelectedItem());
                ps.setString(3, textField1.getText());
                ps.setString(4, (String) comboBox2.getSelectedItem());
                ps.setString(5, submissionDatePicker.getDate() != null ? submissionDatePicker.getDate().toString() : null);
                ps.setString(6, (String) comboBox3.getSelectedItem());
                ps.setString(7, startDatePicker.getDate() != null ? startDatePicker.getDate().toString() : null);
                ps.setString(8, endDatePicker.getDate() != null ? endDatePicker.getDate().toString() : null);
                ps.setString(9, textArea1.getText());
                ps.setString(10, selectedImageFile != null ? selectedImageFile.getAbsolutePath() : null);
                ps.executeUpdate();
                JOptionPane.showMessageDialog(null, "Medical record added.");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
            }
        });

        SEARCHButton.addActionListener(e -> {
            try (Connection conn = getConnection()) {
                String sql = "SELECT * FROM medical WHERE Medical_id = ? AND Student_Username = ?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, textField2.getText());
                ps.setString(2, textField1.getText());
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    comboBox1.setSelectedItem(rs.getString("Course_code"));
                    comboBox2.setSelectedItem(rs.getString("TO_Username"));
                    comboBox3.setSelectedItem(rs.getString("Status"));
                    textArea1.setText(rs.getString("Reason"));
                    submissionDatePicker.setDate(LocalDate.parse(rs.getString("Submission_date")));
                    startDatePicker.setDate(LocalDate.parse(rs.getString("Start_date")));
                    endDatePicker.setDate(LocalDate.parse(rs.getString("End_date")));
                    String imagePath = rs.getString("Medical_image");
                    if (imagePath != null && !imagePath.isEmpty()) {
                        setImage(new File(imagePath));
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "No record found.");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
            }
        });

        UPDATEButton.addActionListener(e -> {
            try (Connection conn = getConnection()) {
                String sql = "UPDATE medical SET Course_code=?, TO_Username=?, Submission_date=?, Status=?, Start_date=?, End_date=?, Reason=?, Medical_image=? WHERE Medical_id=? AND Student_Username=?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, (String) comboBox1.getSelectedItem());
                ps.setString(2, (String) comboBox2.getSelectedItem());
                ps.setString(3, submissionDatePicker.getDate() != null ? submissionDatePicker.getDate().toString() : null);
                ps.setString(4, (String) comboBox3.getSelectedItem());
                ps.setString(5, startDatePicker.getDate() != null ? startDatePicker.getDate().toString() : null);
                ps.setString(6, endDatePicker.getDate() != null ? endDatePicker.getDate().toString() : null);
                ps.setString(7, textArea1.getText());
                ps.setString(8, selectedImageFile != null ? selectedImageFile.getAbsolutePath() : null);
                ps.setString(9, textField2.getText());
                ps.setString(10, textField1.getText());
                ps.executeUpdate();
                JOptionPane.showMessageDialog(null, "Record updated.");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Update failed: " + ex.getMessage());
            }
        });

        DELETEButton.addActionListener(e -> {
            try (Connection conn = getConnection()) {
                String sql = "DELETE FROM medical WHERE Medical_id = ? AND Student_Username = ?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, textField2.getText());
                ps.setString(2, textField1.getText());
                int rows = ps.executeUpdate();
                if (rows > 0) {
                    JOptionPane.showMessageDialog(null, "Record deleted.");
                    clearForm();
                } else {
                    JOptionPane.showMessageDialog(null, "No record found to delete.");
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Delete failed: " + ex.getMessage());
            }
        });

        CLEARButton.addActionListener(e -> clearForm());

        UPLOADMEDICALIMAGEButton.addActionListener(e -> chooseImageFile());

        CLEARTHEIMAGEButton.addActionListener(e -> {
            selectedImageFile = null;
            imagePicker.setIcon(null);
            imagePicker.setText("Drag and drop image here or click to choose");
        });

        button1.addActionListener(e -> {
            new ToofficerPage();
            dispose();
        });

        setVisible(true);
    }

    private void chooseImageFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select Image");
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Images", "jpg", "jpeg", "png", "gif");
        fileChooser.setFileFilter(filter);
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            setImage(fileChooser.getSelectedFile());
        }
    }

    private void setImage(File imageFile) {
        try {
            ImageIcon icon = new ImageIcon(imageFile.getAbsolutePath());
            Image img = icon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
            imagePicker.setIcon(new ImageIcon(img));
            imagePicker.setText("");
            selectedImageFile = imageFile;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to load image: " + e.getMessage());
        }
    }

    private void clearForm() {
        textField1.setText("");
        textField2.setText("");
        comboBox1.setSelectedIndex(0);
        comboBox2.setSelectedIndex(0);
        comboBox3.setSelectedIndex(0);
        textArea1.setText("");
        submissionDatePicker.setDate(null);
        startDatePicker.setDate(null);
        endDatePicker.setDate(null);
        imagePicker.setIcon(null);
        imagePicker.setText("Drag and drop image here or click to choose");
        selectedImageFile = null;
    }

    private Connection getConnection() {
        try {
            return DriverManager.getConnection("jdbc:mysql://localhost:3306/tecmis", "your_username", "1234");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database connection failed: " + e.getMessage());
            return null;
        }
    }

    private void loadComboBoxData() {
        try (Connection conn = getConnection()) {
            if (conn == null) {
                JOptionPane.showMessageDialog(this, "Database connection failed.");
                return;
            }

            comboBox1.removeAllItems();
            comboBox2.removeAllItems();
            comboBox3.removeAllItems();

            String sql1 = "SELECT DISTINCT Course_code FROM course";
            try (PreparedStatement ps = conn.prepareStatement(sql1);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    comboBox1.addItem(rs.getString("Course_code"));
                }
            }

            String sql2 = "SELECT DISTINCT TO_Username FROM technical_officer";
            try (PreparedStatement ps = conn.prepareStatement(sql2);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    comboBox2.addItem(rs.getString("TO_Username"));
                }
            }

            comboBox3.addItem("Pending");
            comboBox3.addItem("Approved");
            comboBox3.addItem("Rejected");

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading combo box data: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MedicalPage::new);
    }
}
