import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ToofficerPage extends JFrame{
    private JButton LOGOUTButton;
    private JButton EDITUSERButton;
    private JButton ATTENDANCEButton;
    private JButton MEDICALButton;
    private JButton NOTICEButton;
    private JButton TIMETABLEButton;
    private JPanel mainpanel;


    public ToofficerPage() {
        setTitle("Technical Officer Page");
        setContentPane(mainpanel);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1000, 500);
        setLocationRelativeTo(null);
        setVisible(true);
        ATTENDANCEButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new attendancePage();

            }
        });
        MEDICALButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                 new MedicalPage();

            }
        });
        NOTICEButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               // new TOViewNoticePage();

            }
        });
        TIMETABLEButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new TOViewTimeTablePage();

            }
        });
        LOGOUTButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new loginPage();
            }
        });
        EDITUSERButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new newTOEdit();
                dispose();
            }
        });
        LOGOUTButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new loginPage();
                dispose();
            }
        });
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(ToofficerPage::new);

    }


}
