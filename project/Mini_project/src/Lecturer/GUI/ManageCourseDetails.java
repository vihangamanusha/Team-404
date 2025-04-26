package Lecturer.GUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class ManageCourseDetails {
    private JPanel panel1;
    private JButton backButton;
    private JTable table1;
    private JTextField filePath;
    private JTextField couresCode;
    private JButton ADDButton;
    private JButton UPDATEButton;
    private JButton REMOVEButton;
    private JTextField lectureNum;

    private JFrame frame;

    public ManageCourseDetails() {
        frame = new JFrame("Manage Course Details");
        frame.setContentPane(panel1);
        frame.setSize(800, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // Table Model Setup
        String[] columnNames = { "Course Code", "Lecturer No", "File Path" };
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        table1.setModel(tableModel);

        // Add Button Logic
        ADDButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String course = couresCode.getText().trim();
                String lecturer = lectureNum.getText().trim();
                String path = filePath.getText().trim();

                if (course.isEmpty() || lecturer.isEmpty() || path.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Please fill all fields.");
                    return;
                }

                File file = new File(path);
                if (!file.exists() || !file.isFile()) {
                    JOptionPane.showMessageDialog(frame, "File path is invalid or does not exist.");
                    return;
                }

                tableModel.addRow(new Object[]{ course, lecturer, path });

                // Optional: Clear inputs
                couresCode.setText("");
                lectureNum.setText("");
                filePath.setText("");
            }
        });

        // Remove Button Logic
        REMOVEButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table1.getSelectedRow();

                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(frame, "Please select a row to remove.");
                    return;
                }

                String fileToDelete = (String) tableModel.getValueAt(selectedRow, 2);
                File file = new File(fileToDelete);

                int confirm = JOptionPane.showConfirmDialog(frame, "Do you want to delete the file as well?", "Delete File", JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    if (file.exists() && file.delete()) {
                        JOptionPane.showMessageDialog(frame, "File deleted from system.");
                    } else {
                        JOptionPane.showMessageDialog(frame, "Failed to delete the file or it doesn't exist.");
                    }
                }

                tableModel.removeRow(selectedRow);
            }
        });

        // Optional: Update button logic can be added here
        UPDATEButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(frame, "Update logic not implemented yet.");
            }
        });

        // Back Button Logic
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                new LectureDashboard(); // Assuming you have this class
            }
        });
    }

}
