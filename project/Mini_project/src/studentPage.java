import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class studentPage {
    private JButton LOGOUTButton;
    private JButton EDITUSER;
    private JButton viewAttendance;
    private JButton viewMedical;
    private JButton viewCourseDetails;
    private JButton viewGrade;
    private JButton viewNotice;
    private JButton viewTimeTableButton;
    private JPanel mainPanel;
    private JTextField welcomemsgTextField;
    private JTextField phoneNumberTextField;
    private JTextField studentPNumberField;
    private JButton studenteditProfilePictureButton;
    private JButton studentsaveButton;

    public JPanel getMainPanel() {
        return mainPanel;
    }


    public studentPage() {

        //  NEW LOGIC START (initially hide fields)
        welcomemsgTextField.setEditable(false);
        phoneNumberTextField.setEditable(false);
        studentPNumberField.setEditable(false);

        studenteditProfilePictureButton.setVisible(false);
        studentsaveButton.setVisible(false);
        phoneNumberTextField.setVisible(false);
        studentPNumberField.setVisible(false);

        // Set welcome message and TG number (simulate from DB)
        welcomemsgTextField.setText("Hi, John"); // ➔ Replace with DB value
        studentPNumberField.setText("TG20240012"); // ➔ Replace with DB value
        // NEW LOGIC END


        // Action listener for the View Attendance button
        viewAttendance.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(mainPanel);
                // Set the ViewAttendance panel as the current frame's content
                currentFrame.setContentPane(new ViewAttendance(currentFrame).getMainPanel());
                currentFrame.revalidate();
                currentFrame.repaint();
            }
        });

        // Action listener for the View Course Details button
        viewCourseDetails.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(mainPanel);
                // Set the viewCourseDetails panel as the current frame's content
                currentFrame.setContentPane(new viewCourseDetails(currentFrame).getMainPanel());
                currentFrame.revalidate();
                currentFrame.repaint();
            }
        });
        viewGrade.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(mainPanel);
                // Set the viewCourseDetails panel as the current frame's content
                currentFrame.setContentPane(new ViewGradeDetails(currentFrame).getMainPanel());
                currentFrame.revalidate();
                currentFrame.repaint();
            }
        });


        viewMedical.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(mainPanel);
                // Set the viewCourseDetails panel as the current frame's content
                currentFrame.setContentPane(new Medical(currentFrame).getMainPanel());
                currentFrame.revalidate();
                currentFrame.repaint();
            }
        });
        viewTimeTableButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(mainPanel);
                // Set the viewCourseDetails panel as the current frame's content
                currentFrame.setContentPane(new ViewTimeTable(currentFrame).getMainPanel());
                currentFrame.revalidate();
                currentFrame.repaint();
            }
        });
        viewNotice.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(mainPanel);
                // Set the viewCourseDetails panel as the current frame's content
                currentFrame.setContentPane(new viewNotice(currentFrame).getMainPanel());
                currentFrame.revalidate();
                currentFrame.repaint();
            }
        });

        //edit user action lisner
        EDITUSER.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                phoneNumberTextField.setEditable(true);
                phoneNumberTextField.setVisible(true);

                studenteditProfilePictureButton.setVisible(true);
                studentsaveButton.setVisible(true);

                studentPNumberField.setEditable(false);
                studentPNumberField.setVisible(true);
            }
        });
        //end of edituser


        //start of edit profile pic
        studenteditProfilePictureButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showOpenDialog(mainPanel);
                if (result == JFileChooser.APPROVE_OPTION) {
                    // Get selected file
                    java.io.File selectedFile = fileChooser.getSelectedFile();
                    // Upload / Save the file path or file data into your database here

                    JOptionPane.showMessageDialog(mainPanel, "Profile picture updated!");
                }
            }
        });
  //end of profile picture


        //start of save btn
        studentsaveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String updatedPhoneNumber = phoneNumberTextField.getText();
                // You can add DB update logic here

                JOptionPane.showMessageDialog(mainPanel, "Profile updated successfully!");

                phoneNumberTextField.setEditable(false);
                studenteditProfilePictureButton.setVisible(false);
                studentsaveButton.setVisible(false);
            }
        });

        //end of save bttn
       /* LOGOUTButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(mainPanel);
                currentFrame.dispose(); // close the student dashboard frame

                // Now manually open the login page frame exactly like loginPage main method
                JFrame frame = new JFrame("Login Page");
                frame.setContentPane(new loginPage().LoginPage);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setResizable(false);
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });*/
    }

    public void openDashboard(JFrame currentFrame) {
        currentFrame.setTitle("Student Dashboard");
        currentFrame.setContentPane(this.getMainPanel());
        currentFrame.revalidate();
        currentFrame.repaint();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Student Dashboard");
        studentPage page = new studentPage();
        page.openDashboard(frame);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 500);
        frame.setVisible(true);
    }
}


