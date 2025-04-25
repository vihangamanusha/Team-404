/*import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class viewCourseDetails {
    private JComboBox comboBox1;
    private JButton viewCourseDetailsButton;
    private JTextField courseName;
    private JTextField courseType;
    private JTextField theoryHours;
    private JTextField practicalHours;
    private JTextField credits;
    private JTable table1;
    private JButton backBtn;
    private JPanel mainPanel;

    public JPanel getMainPanel() {
        return mainPanel;
    }



    public viewCourseDetails() {
        backBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        viewCourseDetailsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }
}*/

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

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public viewCourseDetails(JFrame currentFrame) {
        // Initialize the components
        comboBox1 = new JComboBox<>();
        courseName = new JTextField(20);
        courseType = new JTextField(20);
        theoryHours = new JTextField(20);
        practicalHours = new JTextField(20);
        credits = new JTextField(20);
        viewCourseDetailsButton = new JButton("View Course Details");
        backBtn = new JButton("Back");
        table1 = new JTable();
        mainPanel = new JPanel();

        // Make fields non-editable
        courseName.setEditable(false);
        courseType.setEditable(false);
        theoryHours.setEditable(false);
        practicalHours.setEditable(false);
        credits.setEditable(false);

        // Set up the table
        String[] columnNames = {"Material Name", "Upload Date", "Download Link"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        table1.setModel(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(table1);

        // Add components to the main panel
        mainPanel.add(new JLabel("Select Course Code:"));
        mainPanel.add(comboBox1);
        mainPanel.add(new JLabel("Course Name:"));
        mainPanel.add(courseName);
        mainPanel.add(new JLabel("Course Type:"));
        mainPanel.add(courseType);
        mainPanel.add(new JLabel("Theory Hours:"));
        mainPanel.add(theoryHours);
        mainPanel.add(new JLabel("Practical Hours:"));
        mainPanel.add(practicalHours);
        mainPanel.add(new JLabel("Credits:"));
        mainPanel.add(credits);
        mainPanel.add(viewCourseDetailsButton);
        mainPanel.add(new JLabel("Lecture Materials:"));
        mainPanel.add(tableScrollPane);
        mainPanel.add(backBtn);
        mainPanel.setSize(1000,500);

        // Add course codes to the combo box
        comboBox1.addItem("ICT2133");
        comboBox1.addItem("ICT2142");
        comboBox1.addItem("ICT2152");







        // Action listener for the "View Course Details" button
       /* viewCourseDetailsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {*/

        viewCourseDetailsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                String selectedCode = (String) comboBox1.getSelectedItem();

                if (selectedCode != null) {
                    // Fill in the course details based on the selected course code
                    if (selectedCode.equals("ICT2133")) {
                        courseName.setText("Data Structures");
                        courseType.setText("Theory + Practical");
                        theoryHours.setText("2 hours");
                        practicalHours.setText("2 hours");
                        credits.setText("3");
                    } else if (selectedCode.equals("ICT2142")) {
                        courseName.setText("Computer Networks");
                        courseType.setText("Practical");
                        theoryHours.setText("1 hour");
                        practicalHours.setText("3 hours");
                        credits.setText("4");
                    } else if (selectedCode.equals("ICT2152")) {
                        courseName.setText("Database Systems");
                        courseType.setText("Theory");
                        theoryHours.setText("3 hours");
                        practicalHours.setText("2 hours");
                        credits.setText("3");
                    }

                    // Update the lecture materials table based on the selected course
                    DefaultTableModel model = (DefaultTableModel) table1.getModel();
                    model.setRowCount(0); // Clear the previous rows

                    if (selectedCode.equals("ICT2133")) {
                        model.addRow(new Object[]{"Lecture 1 - Introduction", "2025-04-01", "link1"});
                        model.addRow(new Object[]{"Lecture 2 - Arrays", "2025-04-03", "link2"});
                    } else if (selectedCode.equals("ICT2142")) {
                        model.addRow(new Object[]{"Lecture 1 - OSI Model", "2025-04-01", "link3"});
                        model.addRow(new Object[]{"Lecture 2 - IP Addressing", "2025-04-02", "link4"});
                    } else if (selectedCode.equals("ICT2152")) {
                        model.addRow(new Object[]{"Lecture 1 - Relational Databases", "2025-04-01", "link5"});
                        model.addRow(new Object[]{"Lecture 2 - SQL Basics", "2025-04-03", "link6"});
                    }
                } else {
                    JOptionPane.showMessageDialog(mainPanel, "Please select a course code.");
                }
            }
        });

        // Action listener for the "Back" button
        /*backBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Add functionality to go back to the previous screen (e.g., Student Dashboard)
                // For now, just showing a simple message box
                JOptionPane.showMessageDialog(mainPanel, "Going back to the previous screen...");
            }
        });*/


        backBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Return to the previous screen (e.g., Student Dashboard)
                currentFrame.setContentPane(new studentPage().getMainPanel());
                currentFrame.revalidate();
                currentFrame.repaint();
            }
        });
    }
}

