import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UsereditPage {
    private JButton button1;
    private JPanel edituserpage;
    private JTextField firstname;
    private JTextField lastname;
    private JTextField email;
    private JTextField phonenumber;
    private JPasswordField passwordField1;
    private JPasswordField passwordField2;
    private JTextField image;
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
        RESETButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                firstname.setText("");
                lastname.setText("");
                email.setText("");
                phonenumber.setText("");
                passwordField1.setText("");
                passwordField2.setText("");
                image.setText("");

            }
        });
    }
}
