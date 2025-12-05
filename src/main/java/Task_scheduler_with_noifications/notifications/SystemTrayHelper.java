package Task_scheduler_with_noifications.notifications;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.awt.*;
import java.awt.event.ActionListener;
import java.net.URL;
import javax.imageio.ImageIO;

public class SystemTrayHelper {

    private TrayIcon trayIcon;

    public void addAppToTray(Stage stage) throws Exception {
        if (!SystemTray.isSupported()) return;

        SystemTray tray = SystemTray.getSystemTray();
        URL imageUrl = getClass().getResource("/icons/tray.png"); // add a small icon to resources
        Image image = (imageUrl != null) ? ImageIO.read(imageUrl) : Toolkit.getDefaultToolkit().createImage(new byte[0]);

        PopupMenu popup = new PopupMenu();
        MenuItem openItem = new MenuItem("Open TaskManager");
        MenuItem exitItem = new MenuItem("Exit");

        openItem.addActionListener(e -> Platform.runLater(() -> {
            stage.show();
            stage.toFront();
        }));
        exitItem.addActionListener(e -> {
            tray.remove(trayIcon);
            Platform.exit();
            System.exit(0);
        });

        popup.add(openItem);
        popup.addSeparator();
        popup.add(exitItem);

        trayIcon = new TrayIcon(image, "TaskManager", popup);
        trayIcon.setImageAutoSize(true);
        tray.add(trayIcon);

        // when user closes window, hide instead of exit
        stage.setOnCloseRequest(e -> {
            e.consume(); // prevent app exit
            Platform.runLater(stage::hide);
            trayIcon.displayMessage("TaskManager", "App minimized to tray. Notifications will continue.", TrayIcon.MessageType.INFO);
        });
    }
}
