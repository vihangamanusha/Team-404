import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Medical {
    private JButton medicalBackButton;
    private JTextField facultyOfTechnologyUniversityTextField;
    private JButton viewMedicalsButton;
    private JTable medicalTable;
    private JTextField courseID;
    private JPanel mainPanel;
    private JFrame frame;

    public Medical() {
        frame = new JFrame("View medical information");
        frame.setContentPane(mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // Button action listener should be inside the constructor
        medicalBackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose(); // Close current window
                new testStudent(); // Open Student Dashboard
            }
        });
    }
}
