package Task_scheduler_with_noifications.dashboard;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.Objects;

public class DashboardApp {

    private final BorderPane root;
    private final Sidebar sidebar;
    private final StackPane content;

    public DashboardApp() {
        root = new BorderPane();
        sidebar = new Sidebar(this);
        content = new StackPane();
        HBox topbar = createTopBar();
        root.setTop(topbar);



        // Default page
        showPage("overview");

        root.setLeft(sidebar.getView());
        root.setCenter(content);
    }

    private HBox createTopBar() {
        HBox top = new HBox();
        top.setPadding(new Insets(10, 10, 10, 50));
        top.setSpacing(20);
        top.setStyle("-fx-background-color: white; -fx-border-color: transparent transparent #efefef transparent; -fx-border-width: 0 0 1 0;");

        Label appTitle = new Label("Project Connect");
        appTitle.setFont(Font.font("System", FontWeight.SEMI_BOLD, 16));
        appTitle.setStyle("");


        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox right = new HBox(12);
        right.setAlignment(Pos.CENTER_RIGHT);
        Circle avatar = new Circle(16, Color.web("#0b71eb"));
        Label initials = new Label("AK");
        initials.setTextFill(Color.WHITE);
        initials.setFont(Font.font(12));
        StackPane avatarStack = new StackPane(avatar, initials);

        right.getChildren().addAll(new Label("🔔"), avatarStack);

        top.getChildren().addAll(appTitle,spacer, right);
        return top;

    }

    public void showPage(String name) {
        content.getChildren().clear();
        switch (name.toLowerCase()) {
            case "overview" -> content.getChildren().add(new OverviewApp().getView());
            case "tasks" -> content.getChildren().add(new TasksApp().getView());
            case "projects" -> content.getChildren().add(new ProjectsApp().getView());
            case "notifications" -> content.getChildren().add(new NotificationsApp().getView());
            case "activity" -> content.getChildren().add(new ActivityLogApp().getView());
            case "settings" -> content.getChildren().add(new SettingsApp().getView());
        }
    }

    public BorderPane getRoot() {
        return root;
    }
}

