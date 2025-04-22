import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UsereditPage {
    private JButton button1;
    private JPanel edituserpage;
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JTextField textField4;
    private JPasswordField passwordField1;
    private JPasswordField passwordField2;
    private JTextField textField5;
    private JButton UPLOADButton;
    private JButton UPDATEButton;
    private JButton RESETButton;

    public UsereditPage() {
        JFrame frame = new JFrame("Edit User");
        frame.setContentPane(edituserpage);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 500);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);

        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                  frame.dispose();
                  new adminPage();
            }
        });
    }
}
