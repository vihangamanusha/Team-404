import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class courseManagmentPage {
    private JButton button1;
    private JComboBox courseid;
    private JComboBox status;
    private JComboBox lecturerid;
    private JTextField material;
    private JButton MATERIALSButton;
    private JButton INSERTButton;
    private JButton UPDATEButton;
    private JButton REMOVEButton;
    private JButton RESETButton;
    private JTextField search;

    public courseManagmentPage() {
        RESETButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                courseid.setSelectedIndex(0);
                status.setSelectedIndex(0);
                lecturerid.setSelectedIndex(0);
                
            }
        });
    }
}
