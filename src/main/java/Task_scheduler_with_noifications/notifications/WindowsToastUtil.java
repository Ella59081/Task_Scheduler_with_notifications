package Task_scheduler_with_noifications.notifications;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class WindowsToastUtil {

    public static void showToast(String title, String message) {
        // only run on Windows
        if (!System.getProperty("os.name").toLowerCase().contains("win")) return;

        try {
            Path tmp = Files.createTempFile("toast_", ".ps1");
            String script = buildPowerShellToastScript(escapeForPowershell(title), escapeForPowershell(message));
            Files.write(tmp, script.getBytes(), StandardOpenOption.TRUNCATE_EXISTING);
            // Run powershell script (fire-and-forget)
            new ProcessBuilder("powershell", "-ExecutionPolicy", "Bypass", "-File", tmp.toAbsolutePath().toString())
                    .inheritIO()
                    .start();
            // schedule delete on exit
            tmp.toFile().deleteOnExit();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String escapeForPowershell(String s) {
        // Use single-quoted strings in PowerShell: replace ' with '' (PowerShell escaping)
        return s.replace("'", "''");
    }

    private static String buildPowerShellToastScript(String title, String message) {
        // Uses Windows Runtime Toast API via PowerShell
        return "[Windows.UI.Notifications.ToastNotificationManager, Windows.UI.Notifications, ContentType = WindowsRuntime] > $null\n"
                + "$template = [Windows.UI.Notifications.ToastTemplateType]::ToastText02\n"
                + "$xml = [Windows.UI.Notifications.ToastNotificationManager]::GetTemplateContent($template)\n"
                + "$textNodes = $xml.GetElementsByTagName('text')\n"
                + "$textNodes.Item(0).AppendChild($xml.CreateTextNode('" + title + "')) > $null\n"
                + "$textNodes.Item(1).AppendChild($xml.CreateTextNode('" + message + "')) > $null\n"
                + "$toast = [Windows.UI.Notifications.ToastNotification]::new($xml)\n"
                + "$notifier = [Windows.UI.Notifications.ToastNotificationManager]::CreateToastNotifier('TaskManagerApp')\n"
                + "$notifier.Show($toast)\n";
    }
}
