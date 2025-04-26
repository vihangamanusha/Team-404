/*import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class viewNotice {
    private JTable viewNoticeTable;
    private JButton noticeBackBtn;
    private JButton clickToViewYourButton;

    public viewNotice() {
        noticeBackBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        clickToViewYourButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }
}
*/


import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class viewNotice {
    private JButton noticeBackBtn;
    private JButton viewNoticesButton;
    private JTable noticeTable;
    private JPanel mainPanel;

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public viewNotice(JFrame currentFrame) {
        // Set up table columns
        String[] columnNames = {"Date", "Notice Title", "Download Link"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        noticeTable.setModel(model);

        // Sample data (you can replace this with real data from a file or DB)
        String[][] data = {
                {"2025-04-25", "Midterm Notice", "https://example.com/notice1.pdf"},
                {"2025-04-26", "Exam Schedule", "https://example.com/notice2.pdf"}
        };

        // View Notices Button
        viewNoticesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.setRowCount(0); // Clear old data
                for (String[] row : data) {
                    model.addRow(row);
                }
            }
        });

        // Back Button
        noticeBackBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentFrame.setContentPane(new studentPage().getMainPanel());
                currentFrame.revalidate();
                currentFrame.repaint();
            }
        });
    }
}
