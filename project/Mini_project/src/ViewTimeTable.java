import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ViewTimeTable {
    private JButton TimetableBacktn;
    private JButton viewYourTimeTableButton;
    private JTable table1;
    private JPanel mainPanel;

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public ViewTimeTable(JFrame currentFrame) {
        // Set up table columns
        String[] columnNames = {"Date", "Download Link"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        table1.setModel(model);

        // Handle View Time Table button
        viewYourTimeTableButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Sample timetable data
                String[][] data = {
                        {"2025-04-28", "https://example.com/timetable1.pdf"},
                        {"2025-04-29", "https://example.com/timetable2.pdf"}
                };

                // Clear old data and insert new data
                model.setRowCount(0);
                for (String[] row : data) {
                    model.addRow(row);
                }
            }
        });

        // Handle Back button
        TimetableBacktn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentFrame.setContentPane(new studentPage().getMainPanel());
                currentFrame.revalidate();
                currentFrame.repaint();
            }
        });
    }
}


