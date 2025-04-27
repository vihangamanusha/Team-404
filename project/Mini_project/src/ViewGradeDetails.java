/*import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ViewGradeDetails {
    //private JTextField studentId;
    private JButton viewGradeDetailsButton;
    private JTextField CGPAfield;
    private JTextField SGPAfield;
    private JTable viewGradeTable;
    private JButton gradeBackBtnButton;
    private JTextField gradeCourseIDField;
    private JPanel mainPanel;

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public ViewGradeDetails(JFrame currentFrame) {
        //studentId = new JTextField(15);
        CGPAfield = new JTextField(10);
        SGPAfield = new JTextField(10);
        viewGradeDetailsButton = new JButton("View Grade Details");
        gradeBackBtnButton = new JButton("Back");
        viewGradeTable = new JTable();
        mainPanel = new JPanel();

        // Make fields non-editable
        CGPAfield.setEditable(false);
        SGPAfield.setEditable(false);

        // Set up the grade table
        String[] columnNames = {"Course ID", "CA", "Grade"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        viewGradeTable.setModel(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(viewGradeTable);

        // Add components to the main panel
        mainPanel.add(new JLabel("Student ID:"));
       // mainPanel.add(studentId);
        mainPanel.add(viewGradeDetailsButton);
        mainPanel.add(new JLabel("SGPA:"));
        mainPanel.add(SGPAfield);
        mainPanel.add(new JLabel("CGPA:"));
        mainPanel.add(CGPAfield);
        mainPanel.add(new JLabel("Grade Details:"));
        mainPanel.add(tableScrollPane);
        mainPanel.add(gradeBackBtnButton);
        mainPanel.setSize(1000, 500);


        viewGradeDetailsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
              //  String enteredId = studentId.getText().trim();
                DefaultTableModel model = (DefaultTableModel) viewGradeTable.getModel();
                model.setRowCount(0); // Clear previous data

              //  if (enteredId.equals("ST1234")) {
                    SGPAfield.setText("3.5");
                    CGPAfield.setText("3.6");

                    model.addRow(new Object[]{"ICT2133", "75", "A"});
                    model.addRow(new Object[]{"ICT2142", "68", "B+"});
                    model.addRow(new Object[]{"ICT2152", "80", "A"});
                } else if (enteredId.equals("ST5678")) {
                    SGPAfield.setText("3.2");
                    CGPAfield.setText("3.3");

                    model.addRow(new Object[]{"ICT2133", "65", "B"});
                    model.addRow(new Object[]{"ICT2142", "72", "B+"});
                    model.addRow(new Object[]{"ICT2152", "70", "B+"});
                } else {
                    JOptionPane.showMessageDialog(mainPanel, "No data found for Student ID: " + enteredId);
                }
            }
        });
        gradeBackBtnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentFrame.setContentPane(new studentPage().getMainPanel());
                currentFrame.revalidate();
                currentFrame.repaint();
            }
        });
    }
}*/



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

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public ViewGradeDetails(JFrame currentFrame) {
        CGPAfield = new JTextField(10);
        SGPAfield = new JTextField(10);
        gradeCourseIDField = new JTextField(10);
        viewGradeDetailsButton = new JButton("View Grade Details");
        gradeBackBtnButton = new JButton("Back");
        viewGradeTable = new JTable();
        mainPanel = new JPanel();

        // Make fields non-editable
        CGPAfield.setEditable(false);
        SGPAfield.setEditable(false);

        // Set up the grade table
        String[] columnNames = {"Course ID", "CA", "Grade"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        viewGradeTable.setModel(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(viewGradeTable);

        // Add components to the main panel
        mainPanel.add(new JLabel("Course ID:"));
        mainPanel.add(gradeCourseIDField);
        mainPanel.add(viewGradeDetailsButton);
        mainPanel.add(new JLabel("SGPA:"));
        mainPanel.add(SGPAfield);
        mainPanel.add(new JLabel("CGPA:"));
        mainPanel.add(CGPAfield);
        mainPanel.add(new JLabel("Grade Details:"));
        mainPanel.add(tableScrollPane);
        mainPanel.add(gradeBackBtnButton);
        mainPanel.setSize(1000, 500);

        // Action for the 'View Grade Details' button
        viewGradeDetailsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String enteredCourseID = gradeCourseIDField.getText().trim();
                DefaultTableModel model = (DefaultTableModel) viewGradeTable.getModel();
                model.setRowCount(0); // Clear previous data

                // Fetch data from the database
                try {
                    Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/your_database", "your_username", "your_password");
                    String query = "SELECT course_id, ca, grade FROM grades WHERE course_id = ?";
                    PreparedStatement stmt = conn.prepareStatement(query);
                    stmt.setString(1, enteredCourseID);
                    ResultSet rs = stmt.executeQuery();

                    if (rs.next()) {
                        // Set SGPA and CGPA values (replace this with actual logic to calculate from DB)
                        SGPAfield.setText("3.5"); // Example value
                        CGPAfield.setText("3.6"); // Example value

                        // Populate table with retrieved data
                        do {
                            model.addRow(new Object[]{rs.getString("course_id"), rs.getString("ca"), rs.getString("grade")});
                        } while (rs.next());
                    } else {
                        JOptionPane.showMessageDialog(mainPanel, "No data found for Course ID: " + enteredCourseID);
                    }

                    rs.close();
                    stmt.close();
                    conn.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(mainPanel, "Error retrieving data from the database.");
                }
            }
        });

        // Action for the 'Back' button
        gradeBackBtnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentFrame.setContentPane(new studentPage().getMainPanel());
                currentFrame.revalidate();
                currentFrame.repaint();
            }
        });
    }
}
