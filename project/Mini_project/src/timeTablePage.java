import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class timeTablePage {
    private JButton button1;
    private JTextField PDF;
    private JButton INSERTButton;
    private JButton RESETButton;
    private JTextField search;
    private JButton SEARCHButton;
    private JButton REMOVEButton;
    private JComboBox level;
    private JPanel timetablepage;

    public timeTablePage() {
        JFrame frame = new JFrame("Time Table");
        frame.setContentPane(timetablepage);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 500);
        frame.setResizable(false);// or use frame.pack()
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        RESETButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                level.setSelectedIndex(0);
                search.setText("");
                PDF.setText("");

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
