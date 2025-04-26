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
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class viewNotice {
    private JButton noticeBackBtn;
    private JButton clickToViewYourButton;
    private JTable viewNoticeTable;
    private JPanel mainPanel;

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public viewNotice(JFrame currentFrame) {
        mainPanel = new JPanel(new BorderLayout());

        // Create Table
        String[] columnNames = {"Date", "Notice Title", "Download Link"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        viewNoticeTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(viewNoticeTable);

        // Create Buttons
        JPanel buttonPanel = new JPanel();
        clickToViewYourButton = new JButton("Click to View Your Notices");
        noticeBackBtn = new JButton("Back");
        buttonPanel.add(clickToViewYourButton);
        buttonPanel.add(noticeBackBtn);

        // Add components to main panel
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Sample data
        String[][] data = {
                {"2025-04-25", "Midterm Notice", "https://example.com/notice1.pdf"},
                {"2025-04-26", "Exam Schedule", "https://example.com/notice2.pdf"}
        };

        // Button listeners
        clickToViewYourButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.setRowCount(0); // Clear old data
                for (String[] row : data) {
                    model.addRow(row);
                }
            }
        });

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
