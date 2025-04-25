import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class courseManagmentPage {
    private JButton button1;
    private JComboBox courseid;
    private JComboBox status;
    private JComboBox lecturerid;
    private JTextField material;
    private JButton INSERTButton;
    private JButton UPDATEButton;
    private JButton REMOVEButton;
    private JButton RESETButton;
    private JTextField search;
    private JPanel coureManagementPage;
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;

    public courseManagmentPage() {
        JFrame frame = new JFrame("Course Management Page");
        frame.setContentPane(coureManagementPage);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 500);
        frame.setResizable(false);// or use frame.pack()
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        RESETButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                courseid.setSelectedIndex(0);
                status.setSelectedIndex(0);
                lecturerid.setSelectedIndex(0);

            }
        });
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                new adminPage();
            }
        });
    }
}
