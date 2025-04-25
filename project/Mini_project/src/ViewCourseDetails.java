/*import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.table.DefaultTableModel;

public class ViewCourseDetails {
    private JPanel mainPanel;
    private JComboBox<String> courseComBox;
    private JTextField courseName;
    private JTextField courseType;
    private JTextField practicalHours;
    private JTextField credits;
    private JTable table1;
    private JButton coursebackBtn;
    private JLabel CourseType;
    private JLabel TheoryHours;

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public ViewCourseDetails(JFrame currentFrame) {
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        courseComBox = new JComboBox<>();
        courseName = new JTextField(20);
        courseType = new JTextField(20); // For Course Type
        practicalHours = new JTextField(20);
        credits = new JTextField(20);
        coursebackBtn = new JButton("Back");

        // Set all text fields as non-editable
        courseName.setEditable(false);
        courseType.setEditable(false); // Course Type
        practicalHours.setEditable(false);
        credits.setEditable(false);

        // Define columns for materials table
        String[] columnNames = {"Material Name", "Upload Date", "Download Link"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        table1 = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(table1);

        // Add components to the panel
        mainPanel.add(new JLabel("Select Course Code:"));
        mainPanel.add(courseComBox);
        mainPanel.add(new JLabel("Course Name:"));
        mainPanel.add(courseName);
        mainPanel.add(new JLabel("Course Type:"));
        mainPanel.add(courseType);
        mainPanel.add(new JLabel("Theory Hours:"));
        mainPanel.add(new JTextField(20)); // Theory Hours (Add text field if needed)
        mainPanel.add(new JLabel("Practical Hours:"));
        mainPanel.add(practicalHours);
        mainPanel.add(new JLabel("Credits:"));
        mainPanel.add(credits);
        mainPanel.add(new JLabel("Lecture Materials:"));
        mainPanel.add(tableScrollPane);
        mainPanel.add(coursebackBtn);

        // Add mock course codes into JComboBox
        courseComBox.addItem("ICT2133");
        courseComBox.addItem("ICT2142");
        courseComBox.addItem("ICT2152");

        // On selection, fetch course details and lecture materials (mock data)
        courseComBox.addActionListener(e -> {
            String selectedCode = (String) courseComBox.getSelectedItem();

            // Mock data for course details
            if (selectedCode != null) {
                if (selectedCode.equals("ICT2133")) {
                    courseName.setText("Data Structures");
                    courseType.setText("Theory + Practical");
                    practicalHours.setText("2 hours");
                    credits.setText("3");
                } else if (selectedCode.equals("ICT2142")) {
                    courseName.setText("Computer Networks");
                    courseType.setText("Practical");
                    practicalHours.setText("3 hours");
                    credits.setText("4");
                } else if (selectedCode.equals("ICT2152")) {
                    courseName.setText("Database Systems");
                    courseType.setText("Theory");
                    practicalHours.setText("2 hours");
                    credits.setText("3");
                }

                // Mock lecture materials
                DefaultTableModel model = (DefaultTableModel) table1.getModel();
                model.setRowCount(0); // Clear previous rows

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
            }
        });

        // Back button action
        coursebackBtn.addActionListener(e -> {
            studentPage studentDashboard = new studentPage();
            studentDashboard.openDashboard(currentFrame);
        });

        currentFrame.setTitle("Course Details");
        currentFrame.setContentPane(mainPanel);
        currentFrame.revalidate();
        currentFrame.repaint();
    }
}*/





/*import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ViewCourseDetails {
    private JPanel mainPanel;
    private JComboBox<String> courseComBox;
    private JTextField courseName;
    private JTextField courseType;
    private JTextField practicalHours;
    private JTextField credits;
    private JTable table1;
    private JButton coursebackBtn;
    private JLabel CourseType;
    private JLabel TheoryHours;

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public ViewCourseDetails(JFrame currentFrame) {
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        courseComBox = new JComboBox<>();
        courseName = new JTextField(20);
        courseType = new JTextField(20); // For Course Type
        practicalHours = new JTextField(20);
        credits = new JTextField(20);
        coursebackBtn = new JButton("Back");

        // Set all text fields as non-editable
        courseName.setEditable(false);
        courseType.setEditable(false); // Course Type
        practicalHours.setEditable(false);
        credits.setEditable(false);

        // Define columns for materials table
        String[] columnNames = {"Material Name", "Upload Date", "Download Link"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        table1 = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(table1);

        // Add components to the panel
        mainPanel.add(new JLabel("Select Course Code:"));
        mainPanel.add(courseComBox);
        mainPanel.add(new JLabel("Course Name:"));
        mainPanel.add(courseName);
        mainPanel.add(new JLabel("Course Type:"));
        mainPanel.add(courseType);
        mainPanel.add(new JLabel("Theory Hours:"));
        mainPanel.add(new JTextField(20)); // Theory Hours (Add text field if needed)
        mainPanel.add(new JLabel("Practical Hours:"));
        mainPanel.add(practicalHours);
        mainPanel.add(new JLabel("Credits:"));
        mainPanel.add(credits);
        mainPanel.add(new JLabel("Lecture Materials:"));
        mainPanel.add(tableScrollPane);
        mainPanel.add(coursebackBtn);

        // Add mock course codes into JComboBox
        courseComBox.addItem("ICT2133");
        courseComBox.addItem("ICT2142");
        courseComBox.addItem("ICT2152");

        // On selection, fetch course details and lecture materials (mock data)
        courseComBox.addActionListener(e -> {
            String selectedCode = (String) courseComBox.getSelectedItem();

            // Mock data for course details
            if (selectedCode != null) {
                if (selectedCode.equals("ICT2133")) {
                    courseName.setText("Data Structures");
                    courseType.setText("Theory + Practical");
                    practicalHours.setText("2 hours");
                    credits.setText("3");
                } else if (selectedCode.equals("ICT2142")) {
                    courseName.setText("Computer Networks");
                    courseType.setText("Practical");
                    practicalHours.setText("3 hours");
                    credits.setText("4");
                } else if (selectedCode.equals("ICT2152")) {
                    courseName.setText("Database Systems");
                    courseType.setText("Theory");
                    practicalHours.setText("2 hours");
                    credits.setText("3");
                }

                // Mock lecture materials
                DefaultTableModel model = (DefaultTableModel) table1.getModel();
                model.setRowCount(0); // Clear previous rows

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
            }
        });

        // Back button action
        coursebackBtn.addActionListener(e -> {
            studentPage studentDashboard = new studentPage(currentFrame); // Pass current frame to studentPage constructor
            studentDashboard.openDashboard(currentFrame); // Switch back to dashboard
        });

        currentFrame.setTitle("Course Details");
        currentFrame.setContentPane(mainPanel);
        currentFrame.revalidate();
        currentFrame.repaint();
    }
}*/



import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ViewCourseDetails {
    private JPanel mainPanel;
    private JComboBox<String> courseComBox;
    private JTextField courseName;
    private JTextField theoryHours;
    private JTextField practicalHours;
    private JTextField credits;
    private JTable table1;
    private JButton coursebackBtn;
    private JButton viewCourseDetailsButton;
    private JTextField textField1;

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public ViewCourseDetails(JFrame currentFrame) {
        // Initialize components
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        courseComBox = new JComboBox<>();
        courseName = new JTextField(20);
        textField1= new JTextField(20);  // Ensure this field name matches the one in the form
        theoryHours = new JTextField(20);
        practicalHours = new JTextField(20);
        credits = new JTextField(20);
        viewCourseDetailsButton = new JButton("View Course Details");
        coursebackBtn = new JButton("Back");

        // Make fields non-editable
        courseName.setEditable(false);
        textField1.setEditable(false);
        theoryHours.setEditable(false);
        practicalHours.setEditable(false);
        credits.setEditable(false);

        // Set up the table
        String[] columnNames = {"Material Name", "Upload Date", "Download Link"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        table1 = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(table1);

        // Add components to panel
        mainPanel.add(new JLabel("Select Course Code:"));
        mainPanel.add(courseComBox);
        mainPanel.add(new JLabel("Course Name:"));
        mainPanel.add(courseName);
        mainPanel.add(new JLabel("Course Type:"));
        mainPanel.add(textField1);
        mainPanel.add(new JLabel("Theory Hours:"));
        mainPanel.add(theoryHours);
        mainPanel.add(new JLabel("Practical Hours:"));
        mainPanel.add(practicalHours);
        mainPanel.add(new JLabel("Credits:"));
        mainPanel.add(credits);
        mainPanel.add(viewCourseDetailsButton);
        mainPanel.add(new JLabel("Lecture Materials:"));
        mainPanel.add(tableScrollPane);
        mainPanel.add(coursebackBtn);

        // Add mock course codes
        courseComBox.addItem("ICT2133");
        courseComBox.addItem("ICT2142");
        courseComBox.addItem("ICT2152");

        // Action for View Course Details Button
        viewCourseDetailsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedCode = (String) courseComBox.getSelectedItem();

                if (selectedCode != null) {
                    if (selectedCode.equals("ICT2133")) {
                        courseName.setText("Data Structures");
                        textField1.setText("Theory + Practical");
                        theoryHours.setText("2 hours");
                        practicalHours.setText("2 hours");
                        credits.setText("3");
                    } else if (selectedCode.equals("ICT2142")) {
                        courseName.setText("Computer Networks");
                        textField1.setText("Practical");
                        theoryHours.setText("1 hour");
                        practicalHours.setText("3 hours");
                        credits.setText("4");
                    } else if (selectedCode.equals("ICT2152")) {
                        courseName.setText("Database Systems");
                        textField1.setText("Theory");
                        theoryHours.setText("3 hours");
                        practicalHours.setText("2 hours");
                        credits.setText("3");
                    }

                    // Update table
                    DefaultTableModel model = (DefaultTableModel) table1.getModel();
                    model.setRowCount(0);

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

        // Back button
        coursebackBtn.addActionListener(e -> {
            studentPage studentDashboard = new studentPage();
            studentDashboard.openDashboard(currentFrame);
        });

        // Load this panel into current frame
        currentFrame.setTitle("Course Details");
        currentFrame.setContentPane(mainPanel);
        currentFrame.revalidate();
        currentFrame.repaint();
    }
}

