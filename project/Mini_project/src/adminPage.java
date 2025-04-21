import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class adminPage {
    private JButton button1;
    private JButton LOGOUTButton;
    private JButton EDITUSERButton;
    private JPanel adminpage;

    public adminPage() {
        JFrame frame = new JFrame("Admin Page");
        frame.setContentPane(adminpage);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 500);
        frame.setResizable(false);// or use frame.pack()
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        LOGOUTButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose(); //  close current window
                new loginPage().showLoginFrame(); // open login page againj
            }
        });
    }
}
