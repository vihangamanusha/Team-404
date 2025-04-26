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
    private JTable viewNoticeTable;
    private JButton noticeBackBtn;
    private JButton clickToViewYourButton;
    private JPanel mainPanel;

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public viewNotice(JFrame currentFrame) {
        // Set up table model with column headers
        String[] columnNames = {"Notice Published Date", "Download Notice"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        viewNoticeTable.setModel(model);

        // Button to load notices
        clickToViewYourButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Sample data
                String[][] notices = {
                        {"2025-04-26", "https://example.com/notice1.pdf"},
                        {"2025-04-25", "https://example.com/notice2.pdf"}
                };

                // Clear old rows and add new ones
                model.setRowCount(0);
                for (String[] row : notices) {
                    model.addRow(row);
                }
            }
        });

        // Back to Student Page
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
