import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.io.*;

public class ToofficerPage {
    private JButton LOGOUTButton;
    private JButton EDITUSERButton;
    private JButton ATTENDANCEButton;
    private JButton MEDICALButton;
    private JButton NOTICEButton;
    private JButton TIMETABLEButton;
    private JPanel mainpanel;

    private JFrame frame;  // New JFrame

    public ToofficerPage() {
        frame = new JFrame("Technical Officer Page");
        frame.setContentPane(mainpanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

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
                new ToOfficerNoticePage();
            }
        });

        TIMETABLEButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openTimetable();
            }
        });

        LOGOUTButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new loginPage();
                frame.dispose();
            }
        });

        EDITUSERButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new newTOEdit();
                frame.dispose();
            }
        });
    }

    private void openTimetable() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();

            String sql = "SELECT Content FROM timetable LIMIT 1";
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            if (rs.next()) {
                String filepath = rs.getString("Content");

                filepath = filepath.replace("\\", "\\\\").replace("/", "\\");

                File file = new File(filepath);

                if (file.exists()) {
                    BufferedReader reader = new BufferedReader(new FileReader(file));
                    StringBuilder content = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        content.append(line).append("\n");
                    }
                    reader.close();

                    JTextArea textArea = new JTextArea(content.toString());
                    textArea.setEditable(false);
                    JScrollPane scrollPane = new JScrollPane(textArea);
                    scrollPane.setPreferredSize(new java.awt.Dimension(500, 400));

                    JOptionPane.showMessageDialog(null, scrollPane, "Timetable", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Timetable file not found!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "No timetable found in database!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException | IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error opening timetable: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    // Main method (psvm)
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ToofficerPage());
    }
}
