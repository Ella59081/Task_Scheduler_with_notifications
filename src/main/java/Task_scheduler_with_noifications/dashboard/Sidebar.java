package Task_scheduler_with_noifications.dashboard;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.util.Objects;

public class Sidebar {
    private final VBox view;

    public Sidebar(DashboardApp dashboard) {
        view = new VBox(15);
        view.setPadding(new Insets(20));
        view.setAlignment(Pos.TOP_CENTER);
        view.getStyleClass().add("sidebar");

        view.getChildren().addAll(
                createNavButton("Overview", "/icons/home.png", () -> dashboard.showPage("overview")),
                createNavButton("Tasks", "/icons/tasks.png", () -> dashboard.showPage("tasks")),
                createNavButton("Projects", "/icons/projects.png", () -> dashboard.showPage("projects")),
                createNavButton("Notifications", "/icons/notifications.png", () -> dashboard.showPage("notifications")),
                createNavButton("Activity Log", "/icons/activity.png", () -> dashboard.showPage("activity")),
                createNavButton("Settings", "/icons/settings.png", () -> dashboard.showPage("settings"))
        );
    }

    private Button createNavButton(String text, String iconPath, Runnable action) {
        ImageView icon = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream(iconPath))));
        icon.setFitWidth(20);
        icon.setFitHeight(20);

        Button btn = new Button(text, icon);
        btn.getStyleClass().add("nav-btn");
        btn.setOnAction(e -> action.run());
        return btn;
    }

    public VBox getView() {
        return view;
    }
}

