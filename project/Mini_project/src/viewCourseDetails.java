import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class viewCourseDetails {
    private JComboBox<String> comboBox1;
    private JButton viewCourseDetailsButton;
    private JTextField courseName;
    private JTextField courseType;
    private JTextField theoryHours;
    private JTextField practicalHours;
    private JTextField credits;
    private JTable table1;
    private JButton backBtn;
    private JPanel mainPanel;
    private JFrame frame;

    public viewCourseDetails() {
        // Initialize the frame and main panel
        frame = new JFrame("View Course Details");
        frame.setContentPane(mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // Add action listener for the "Back" button
        backBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();  // Close the current window
                new testStudent();  // Navigate back to the testStudent screen
            }
        });


    }
}
