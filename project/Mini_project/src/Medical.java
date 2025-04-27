import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Medical {
    private JButton medicalBackButton;
    private JButton viewMedicalsButton;
    private JTextField courseID;
    private JTable medicalTable;
    private JScrollPane medicalscroll;
    private JPanel mainPanel;
    private JFrame frame;

    public Medical() {
        frame = new JFrame("View Medical Information");
        frame.setContentPane(mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // Button action listener for medicalBackButton
        medicalBackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose(); // Close current window
                new testStudent(); // Open Student Dashboard
            }
        });

        // Button action listener for viewMedicalsButton
        viewMedicalsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String courseId = courseID.getText().trim();

                // Check if course ID is provided
                if (courseId.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Please enter a Course ID.");
                    return;
                }

                try {
                    // Establish connection to the database
                    Connection conn = DBConnection.getConnection(); // Assuming you have a DBConnection class
                    String sql = "SELECT * FROM medical WHERE Course_code = ?";
                    PreparedStatement pst = conn.prepareStatement(sql);
                    pst.setString(1, courseId); // Set the course ID parameter

                    ResultSet rs = pst.executeQuery();

                    // Create a table model to hold the medical data
                    DefaultTableModel model = new DefaultTableModel();
                    model.addColumn("Medical ID");
                    model.addColumn("Submission Date");
                    model.addColumn("Status");
                    model.addColumn("Start Date");
                    model.addColumn("End Date");
                    model.addColumn("Reason");
                    model.addColumn("Course Code");
                    model.addColumn("Student Username");
                    model.addColumn("TO Username");
                    model.addColumn("Medical Image");

                    // Fetch the results and populate the table
                    while (rs.next()) {
                        model.addRow(new Object[]{
                                rs.getString("Medical_id"),
                                rs.getString("Submission_date"),
                                rs.getString("Status"),
                                rs.getString("Start_date"),
                                rs.getString("End_date"),
                                rs.getString("Reason"),
                                rs.getString("Course_code"),
                                rs.getString("Student_Username"),
                                rs.getString("TO_Username"),
                                rs.getString("Medical_image")
                        });
                    }

                    // Set the table model
                    medicalTable.setModel(model);

                    // Close resources
                    rs.close();
                    pst.close();
                    conn.close();

                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(frame, "Error retrieving medical data.");
                }
            }
        });
    }
}
