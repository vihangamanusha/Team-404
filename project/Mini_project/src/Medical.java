/*import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Medical {
    private JButton medicalBackButton;
    private JTextField facultyOfTechnologyUniversityTextField;
    private JButton viewMedicalsButton;
    private JTable table1;
    private JTextField courseID;
    private JPanel mainPanel;

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public Medical(JFrame currentFrame) {
        // Set up column names
        String[] columnNames = {"Course ID", "Date", "Medical"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        table1.setModel(tableModel);


    public Medical() {
        viewMedicalsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        medicalBackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }
}
*/

/*import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Medical {
    private JButton medicalBackButton;
    private JTextField facultyOfTechnologyUniversityTextField;
    private JButton viewMedicalsButton;
    private JTable table1;
    private JTextField courseID;
    private JPanel mainPanel;

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public Medical(JFrame currentFrame) {
        // Set up the table model
        String[] columnNames = {"Course ID", "Date", "Medical"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

        table1.setModel(tableModel);

        // View button logic
        viewMedicalsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String inputID = courseID.getText().trim();
                tableModel.setRowCount(0); // Clear previous data

                if (inputID.equalsIgnoreCase("ICT2133")) {
                    tableModel.addRow(new Object[]{"ICT2133", "2025-03-10", "Accepted"});
                    tableModel.addRow(new Object[]{"ICT2133", "2025-04-05", "Pending"});
                } else if (inputID.equalsIgnoreCase("ICT2142")) {
                    tableModel.addRow(new Object[]{"ICT2142", "2025-03-20", "Accepted"});
                } else if (inputID.equalsIgnoreCase("ICT2152")) {
                    tableModel.addRow(new Object[]{"ICT2152", "2025-02-14", "Rejected"});
                } else {
                    JOptionPane.showMessageDialog(mainPanel, "No medical details found for that Course ID.");
                }
            }
        });

        // Back button logic
        medicalBackButton.addActionListener(new ActionListener() {
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

public class Medical {
    private JButton medicalBackButton;
    private JTextField facultyOfTechnologyUniversityTextField;
    private JButton viewMedicalsButton;
    private JTable medicalTable;
    private JTextField courseID;
    private JPanel mainPanel;

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public Medical(JFrame currentFrame) {
        // Create the table model with column names
        String[] columnNames = {"Course ID", "Date", "Medical"};
        DefaultTableModel tableModel = new DefaultTableModel(null, columnNames);
        medicalTable.setModel(tableModel);  // Set the model to the table

        // View button action: Populate table based on input
        viewMedicalsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String inputID = courseID.getText().trim();
                tableModel.setRowCount(0); // Clear previous rows

                if (inputID.equalsIgnoreCase("ICT2133")) {
                    tableModel.addRow(new Object[]{"ICT2133", "2025-03-10", "Accepted"});
                    tableModel.addRow(new Object[]{"ICT2133", "2025-04-05", "Pending"});
                } else if (inputID.equalsIgnoreCase("ICT2142")) {
                    tableModel.addRow(new Object[]{"ICT2142", "2025-03-20", "Accepted"});
                } else if (inputID.equalsIgnoreCase("ICT2152")) {
                    tableModel.addRow(new Object[]{"ICT2152", "2025-02-14", "Rejected"});
                } else {
                    JOptionPane.showMessageDialog(mainPanel, "No medical details found for that Course ID.");
                }
            }
        });

        // Back button: go back to student page
        medicalBackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentFrame.setContentPane(new studentPage().getMainPanel());
                currentFrame.revalidate();
                currentFrame.repaint();
            }
        });
    }
}
