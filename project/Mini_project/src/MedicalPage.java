import javax.swing.*;
import com.github.lgooddatepicker.components.DatePicker;

import java.awt.*;

public class MedicalPage extends JFrame {

    private JTextField textField1;
    private JComboBox comboBox1;
    private JTextField textField2;
    private JComboBox comboBox2;
    private JComboBox comboBox3;
    private JTextArea textArea1;
    private JButton ADDButton;
    private JButton UPDATEButton;
    private JButton DELETEButton;
    private JButton CLEARButton;
    private JButton BACKButton;

    private JPanel mainpanel;
    private JPanel SubmissionDatePanel;
    private JPanel StartDatePanel;
    private JPanel EndDatePanel;
    private JPanel formpanel;
    private JPanel ButtonPanel;

    private DatePicker submissionDatePicker;
    private DatePicker startDatePicker;
    private DatePicker endDatePicker;

    public MedicalPage() {
        setTitle("MedicalPage");

        // Initialize DatePickers
        submissionDatePicker = new DatePicker();
        startDatePicker = new DatePicker();
        endDatePicker = new DatePicker();

        // Add DatePickers to their respective panels
        SubmissionDatePanel.setLayout(new BorderLayout());
        SubmissionDatePanel.add(submissionDatePicker, BorderLayout.CENTER);

        StartDatePanel.setLayout(new BorderLayout());
        StartDatePanel.add(startDatePicker, BorderLayout.CENTER);

        EndDatePanel.setLayout(new BorderLayout());
        EndDatePanel.add(endDatePicker, BorderLayout.CENTER);



        // Only set content pane once
        setContentPane(mainpanel);
        //setContentPane( formpanel);
       // setContentPane( ButtonPanel);

        // Frame settings
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1000, 500);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MedicalPage::new);
    }
}
