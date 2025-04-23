import javax.swing.*;

public class ToofficerPage extends JFrame{
    private JButton LOGOUTButton;
    private JButton EDITUSERButton;
    private JButton ATTENDANCEButton;
    private JButton MEDICALButton;
    private JButton NOTICEButton;
    private JButton TIMETABLEButton;
    private JPanel mainpanel;
    private JButton button5;
    private JButton button6;

    public ToofficerPage() {
        setTitle("ToofficerPage");
        setContentPane(mainpanel);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1000, 500);
        setLocationRelativeTo(null);
        setVisible(true);
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(ToofficerPage::new);

    }


}
