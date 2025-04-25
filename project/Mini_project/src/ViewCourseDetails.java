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
    private JTextField courseType;
    private JTextField theoryHours;      // ✅ Added field
    private JTextField practicalHours;
    private JTextField credits;
    private JTable table1;
    private JButton coursebackBtn;
    private JLabel CourseType;
    private JLabel TheoryHours;
    private JButton viewCourseDetailsButton;

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public ViewCourseDetails(JFrame currentFrame) {
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        courseComBox = new JComboBox<>();
        courseName = new JTextField(20);
        courseType = new JTextField(20);
        theoryHours = new JTextField(20);      // ✅ Initialize theoryHours
        practicalHours = new JTextField(20);
        credits = new JTextField(20);
        coursebackBtn = new JButton("Back");

        // Set all text fields as non-editable
        courseName.setEditable(false);
        courseType.setEditable(false);
        theoryHours.setEditable(false);       // ✅ Make it non-editable
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
        mainPanel.add(theoryHours);           // ✅ Add to panel
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

            if (selectedCode != null) {
                if (selectedCode.equals("ICT2133")) {
                    courseName.setText("Data Structures");
                    courseType.setText("Theory + Practical");
                    theoryHours.setText("2 hours");       // ✅ Set theory hours
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
        viewCourseDetailsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }
}

