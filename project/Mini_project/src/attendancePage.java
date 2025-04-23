import javax.swing.*;
import com.github.lgooddatepicker.components.DatePicker;
import java.awt.*;

public class attendancePage extends JFrame {
    private JPanel panel1;
    private JPanel insertAttendanse;
    private JComboBox<String> comboBox1;
    private JComboBox<String> comboBox2;
    private JComboBox<String> comboBox3;
    private JPanel datePickerPanel;
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
    }

    public static void main(String[] args) {
        // Launch on the Event‑Dispatch Thread
        SwingUtilities.invokeLater(() -> new attendancePage());
    }
}
