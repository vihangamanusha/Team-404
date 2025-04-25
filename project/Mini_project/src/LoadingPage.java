import javax.swing.*;
import java.awt.*;

public class LoadingPage {
    private JPanel loadingpagepanel;

    public LoadingPage() {



        // Frame setup
        JFrame frame = new JFrame("Loading...");
        frame.setContentPane(loadingpagepanel); // Set the existing panel as the content pane
        frame.setUndecorated(true); // No window border
        frame.setSize(600, 500); // Set the frame size
        frame.setResizable(false);
        frame.setLocationRelativeTo(null); // Center the frame on screen
        frame.setVisible(true);

        // Simulate loading process
        new Thread(() -> {
            try {
                Thread.sleep(3000); // Simulate loading process (wait for 3 seconds)
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            frame.dispose(); // Close the loading screen after completion
            SwingUtilities.invokeLater(() -> new loginPage()); // Load the login page after loading
        }).start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoadingPage::new); // Start the loading screen
    }
}
