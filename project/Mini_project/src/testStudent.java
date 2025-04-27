import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;

public class testStudent extends Container {
    private JPanel mainPanel;
    private JButton LOGOUTButton;
    private JButton EDITUSER;
    private JButton viewMedical;
    private JButton viewCourseDetails;
    private JButton viewAttendance;
    private JButton viewGrade;
    private JButton viewTimeTableButton;
    private JButton viewNotice;
    private JFrame frame;

    public testStudent() {
        frame = new JFrame("Student Dashboard");
        frame.setContentPane(mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // Action listener for the "View TimeTable" button
        viewTimeTableButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String loggedInUsername = "AD0001"; // Replace with the actual logged-in username

                try {
                    // Step 1: Query the database for the timetable content
                    Connection conn = DBConnection.getConnection();
                    String query = "SELECT Content FROM timetable WHERE Username = ?";
                    PreparedStatement stmt = conn.prepareStatement(query);
                    stmt.setString(1, loggedInUsername); // Replace with the actual username

                    ResultSet rs = stmt.executeQuery();
                    if (rs.next()) {
                        // Step 2: Retrieve the file path from the database
                        String filePath = rs.getString("Content");

                        // Step 3: Display the content of the timetable file
                        displayTimetable(filePath);
                    } else {
                        JOptionPane.showMessageDialog(frame, "No timetable found for the user.");
                    }

                    conn.close();

                } catch (SQLException | IOException ex) {
                    JOptionPane.showMessageDialog(frame, "Error loading timetable: " + ex.getMessage());
                }
            }
        });

        // Other button action listeners...
        viewAttendance.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new testviewAttendance();
                frame.dispose();
            }
        });

        viewMedical.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                new Medical();
            }
        });

        viewCourseDetails.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                new viewCourseDetails();
            }
        });

        viewGrade.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                new ViewGradeDetails();
            }
        });
        viewNotice.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                new studentNoticePage();
            }
        });
        LOGOUTButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                new loginPage();
            }
        });
        EDITUSER.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                new Studenteditpage();
            }
        });
    }

    // Method to display the timetable content from a file
    private void displayTimetable(String filePath) throws IOException {
        // Read the file and display the content in a text area
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        StringBuilder timetableContent = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            timetableContent.append(line).append("\n");
        }

        reader.close();

        // Display the timetable content in a JTextArea (without JScrollPane)
        JTextArea textArea = new JTextArea(timetableContent.toString());
        textArea.setEditable(false); // Make the text area non-editable

        // Display the text area in a dialog box
        JOptionPane.showMessageDialog(frame, textArea, "Timetable", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        new testStudent();
    }
}
