import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class notificationPage {
    private JButton button1;
    private JPanel notificationPanel;
    private JTextField title;
    private JTextArea content;
    private JComboBox categary;
    private JTextField attachment;
    private JButton RESETButton;
    private JButton UPLOADButton;

    public notificationPage() {
        JFrame frame = new JFrame("Notification Page");
        frame.setContentPane(notificationPanel);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(1000, 500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                  frame.dispose();
                  new adminPage();
            }
        });
        RESETButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                title.setText("");
                content.setText("");
                categary.setSelectedIndex(0);
                attachment.setText("");
            }
        });
    }
}
