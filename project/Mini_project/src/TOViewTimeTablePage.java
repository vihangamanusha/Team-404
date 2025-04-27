import javax.swing.*;

public class TOViewTimeTablePage extends JFrame {
    private JPanel timetablePanel;
    private JButton button1;
    private JButton CLICKHERETOVIEWButton;

    public TOViewTimeTablePage() {
        setContentPane(timetablePanel);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 500);
        setLocationRelativeTo(null);
        setVisible(true);
        button1.addActionListener(e -> {
            dispose();
            new ToofficerPage();
        });

    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(TOViewTimeTablePage::new);


    }
}
