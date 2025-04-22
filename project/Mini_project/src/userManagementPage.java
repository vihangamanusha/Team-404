import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class userManagementPage {
    private JPanel UsermanagementPage;
    private JButton button1;
    private JTextField id;
    private JTextField username;
    private JTextField fname;
    private JTextField lname;
    private JComboBox gender;
    private JTextField phoneno;
    private JTextField email;
    private JComboBox role;
    private JPasswordField passwordField1;
    private JComboBox level;
    private JButton RESETButton;
    private JButton UPDATEButton;
    private JButton INSERTButton;

    public userManagementPage() {
        JFrame frame = new JFrame("User Management Page");
        frame.setContentPane(UsermanagementPage);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(1000, 500);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);



        RESETButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                id.setText("");
                username.setText("");
                fname.setText("");
                lname.setText("");
                gender.setSelectedIndex(0);
                phoneno.setText("");
                email.setText("");
                role.setSelectedIndex(0);
                passwordField1.setText("");
                level.setSelectedIndex(0);

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
