import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class ViewGradeDetails {
    private JButton viewGradeDetailsButton;
    private JTextField CGPAfield;
    private JTextField SGPAfield;
    private JTable viewGradeTable;
    private JButton gradeBackBtnButton;
    private JTextField gradeCourseIDField;
    private JPanel mainPanel;
    private JFrame frame;

    public ViewGradeDetails() {
        // Initialize the frame and main panel
        frame = new JFrame("View Grade Details");
        frame.setContentPane(mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // Action listener for the "Back" button
        gradeBackBtnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();  // Close the current window
                new testStudent();  // Navigate back to the testStudent screen
            }
        });
    }
}