package Admin;

import javax.swing.*;

public class LoadingPage {
    private JPanel loadingpagepanel;

    public LoadingPage() {




        JFrame frame = new JFrame("Loading...");
        frame.setContentPane(loadingpagepanel);
        frame.setUndecorated(true); // No window border
        frame.setSize(600, 500);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);


        new Thread(() -> {
            try {
                Thread.sleep(3000); // Simulate loading process (wait for 3 seconds)
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            frame.dispose();
            SwingUtilities.invokeLater(() -> new loginPage()); // Load the login page after loading
        }).start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoadingPage::new); // Start the loading screen
    }
}
