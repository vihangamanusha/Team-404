
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.*;
import java.io.File;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class MedicalPage extends JFrame {

    private JTextField textField1; // Student Username
    private JComboBox<String> comboBox1; // Course Code
    private JTextField textField2; // Medical ID
    private JComboBox<String> comboBox2; // TO Username
    private JComboBox<String> comboBox3; // Status
    private JTextArea textArea1; // Reason
    private JButton ADDButton;
    private JButton UPDATEButton;
    private JButton DELETEButton;
    private JButton CLEARButton;
    private JButton SEARCHButton;
    private JButton UPLOADMEDICALIMAGEButton;
    private JButton CLEARTHEIMAGEButton;
    private JButton button1;
    private JPanel mainpanel;
    private JPanel SubmissionDatePanel;
    private JPanel StartDatePanel;
    private JPanel EndDatePanel;
    private JPanel formpanel;
    private JPanel ButtonPanel;
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

        // Add action listeners to buttons
        ADDButton.addActionListener(e -> addDataToDatabase());
        UPDATEButton.addActionListener(e -> updateDataInDatabase());
        DELETEButton.addActionListener(e -> deleteRecordInDatabase());
        SEARCHButton.addActionListener(e -> searchDataInDatabase());
        CLEARTHEIMAGEButton.addActionListener(e -> clearImage());

        setContentPane(mainpanel);

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

    private void clearImage() {
        imagePicker.setIcon(null);
        imagePicker.setText("Drag and drop image here or click to choose");
        selectedImageFile = null;
    }

    private void loadComboBoxData() {
        try (Connection conn = DBConnection.getConnection()) {
            if (conn == null) {
                JOptionPane.showMessageDialog(this, "Database connection failed.");
                return;
            }

            comboBox1.removeAllItems();
            comboBox2.removeAllItems();
            comboBox3.removeAllItems();

            // Load Course Codes (ComboBox 1 - Course_code)
            String sql1 = "SELECT DISTINCT Course_code FROM Course_unit";
            try (PreparedStatement ps = conn.prepareStatement(sql1);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    comboBox1.addItem(rs.getString("Course_code"));
                }
            }

            // Load TO Usernames (ComboBox 2 - TO_Username)
            String sql2 = "SELECT DISTINCT Username FROM Technical_officer";
            try (PreparedStatement ps = conn.prepareStatement(sql2);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    comboBox2.addItem(rs.getString("Username"));
                }
            }

            // Load Status options (ComboBox 3 - Pending, Approved, Rejected)
            comboBox3.addItem("Pending");
            comboBox3.addItem("Approved");
            comboBox3.addItem("Rejected");

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading combo box data: " + ex.getMessage());
        }
    }

    private void addDataToDatabase() {
        try (Connection conn = DBConnection.getConnection()) {
            if (conn == null) {
                JOptionPane.showMessageDialog(this, "Database connection failed.");
                return;
            }

            String sql = "INSERT INTO Medical (Medical_id, Submission_date, Status, Start_date, End_date, Reason, Course_code, Student_Username, TO_Username, Medical_image) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, textField2.getText());
                ps.setString(2, submissionDatePicker.getText());
                ps.setString(3, (String) comboBox3.getSelectedItem());
                ps.setString(4, startDatePicker.getText());
                ps.setString(5, endDatePicker.getText());
                ps.setString(6, textArea1.getText());
                ps.setString(7, (String) comboBox1.getSelectedItem());
                ps.setString(8, textField1.getText());
                ps.setString(9, (String) comboBox2.getSelectedItem());
                ps.setString(10, selectedImageFile != null ? selectedImageFile.getAbsolutePath() : null);

                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Record added successfully!");
                clearForm();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error adding record: " + e.getMessage());
        }
    }

    private void updateDataInDatabase() {
        try (Connection conn = DBConnection.getConnection()) {
            if (conn == null) {
                JOptionPane.showMessageDialog(this, "Database connection failed.");
                return;
            }

            String sql = "UPDATE Medical SET Submission_date = ?, Status = ?, Start_date = ?, End_date = ?, Reason = ?, Course_code = ?, Student_Username = ?, TO_Username = ?, Medical_image = ? "
                    + "WHERE Medical_id = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, submissionDatePicker.getText());
                ps.setString(2, (String) comboBox3.getSelectedItem());
                ps.setString(3, startDatePicker.getText());
                ps.setString(4, endDatePicker.getText());
                ps.setString(5, textArea1.getText());
                ps.setString(6, (String) comboBox1.getSelectedItem());
                ps.setString(7, textField1.getText());
                ps.setString(8, (String) comboBox2.getSelectedItem());
                ps.setString(9, selectedImageFile != null ? selectedImageFile.getAbsolutePath() : null);
                ps.setString(10, textField2.getText());

                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Record updated successfully!");
                clearForm();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating record: " + e.getMessage());
        }
    }

    private void deleteRecordInDatabase() {
        try (Connection conn = DBConnection.getConnection()) {
            if (conn == null) {
                JOptionPane.showMessageDialog(this, "Database connection failed.");
                return;
            }

            String sql = "DELETE FROM Medical WHERE Medical_id = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, textField2.getText());
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Record deleted successfully!");
                clearForm();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error deleting record: " + e.getMessage());
        }
    }

    private void searchDataInDatabase() {
        try (Connection conn = DBConnection.getConnection()) {
            if (conn == null) {
                JOptionPane.showMessageDialog(this, "Database connection failed.");
                return;
            }

            String sql = "SELECT * FROM Medical WHERE Medical_id = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, textField2.getText());
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        textField1.setText(rs.getString("Student_Username"));
                        comboBox1.setSelectedItem(rs.getString("Course_code"));
                        comboBox2.setSelectedItem(rs.getString("TO_Username"));
                        comboBox3.setSelectedItem(rs.getString("Status"));
                        textArea1.setText(rs.getString("Reason"));
                        submissionDatePicker.setText(rs.getString("Submission_date"));
                        startDatePicker.setText(rs.getString("Start_date"));
                        endDatePicker.setText(rs.getString("End_date"));
                        String imagePath = rs.getString("Medical_image");
                        if (imagePath != null && !imagePath.isEmpty()) {
                            setImage(new File(imagePath));
                        } else {
                            clearImage();
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "No record found.");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error searching record: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MedicalPage::new);
    }
}
