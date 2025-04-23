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
    private JButton button1;
    private JButton button2;
    private JButton button3;
    private JButton BACKButton;
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
    }

    public static void main(String[] args) {
        // Launch on the Event‑Dispatch Thread
        SwingUtilities.invokeLater(() -> new attendancePage());
    }
}
