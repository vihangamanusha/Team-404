import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TOViewNoticePage extends JFrame{
    private JPanel noticePanel;
    private JButton button1;

    public TOViewNoticePage() {
        JFrame frame = new JFrame("Notice Page");
        setContentPane(noticePanel);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 500);
        setLocationRelativeTo(null);
        setVisible(true);
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ToofficerPage();
                dispose();

            }
        });
    }



    public static void main(String[] args) {
        SwingUtilities.invokeLater(TOViewNoticePage::new);
    }


}
