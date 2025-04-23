import javax.swing.*;

public class MedicalPage extends JFrame{

    private JTextField textField1;
    private JComboBox comboBox1;
    private JTextField textField2;
    private JComboBox comboBox2;
    private JComboBox comboBox3;
    private JTextArea textArea1;
    private JButton ADDButton;
    private JButton UPDATEButton;
    private JButton DELETEButton;
    private JButton CLEARButton;
    private JButton BACKButton;
    private JPanel mainpanel;

    public MedicalPage() {
        setTitle("MedicalPage");
        setContentPane(mainpanel);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1000, 500);
        setLocationRelativeTo(null);
        setVisible(true);
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(MedicalPage::new);
    }

}
