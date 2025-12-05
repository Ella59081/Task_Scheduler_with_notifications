package Task_scheduler_with_noifications.AppDesign;
import Task_scheduler_with_noifications.Main;
import Task_scheduler_with_noifications.dashboard.DashboardApp;
import Task_scheduler_with_noifications.dashboard.TaskDashboard;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Objects;

public class WelcomeApp extends Application {

    public void startDashboard(Stage stage) throws Exception {
        DashboardApp dashboard = new DashboardApp();
        Scene scene = new Scene(dashboard.getRoot(), 1200, 700);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/styles/dashboard.css")).toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Task Scheduler Dashboard");
        stage.show();
    }

    @Override
    public void start(Stage stage) {
        // 🔹 App logo or icon
        ImageView logo = new ImageView(
                new Image(getClass().getResourceAsStream(""))
        );
        logo.setFitWidth(100);
        logo.setFitHeight(100);

        // 🔹 App title
        Label title = new Label("Welcome to ProjectConnect");
        title.getStyleClass().add("title");

        // 🔹 Subtitle
        Label subtitle = new Label("Organize your day, boost your productivity!");
        subtitle.getStyleClass().add("subtitle");

        // 🔹 Continue button
        Button startBtn = new Button("Get Started");
        startBtn.getStyleClass().add("primary-btn");

        // 🔹 Main layout
        VBox layout = new VBox(20, logo, title, subtitle, startBtn);
        layout.setAlignment(Pos.CENTER);
        layout.getStyleClass().add("welcome-pane");

        StackPane root = new StackPane(layout);
        Scene scene = new Scene(root, 600, 400);

        // 🔹 Load CSS
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource(
                "/styles/welcomeApp.css")).toExternalForm());

        stage.setScene(scene);
        stage.setTitle("Welcome to ProjectConnect");
        stage.show();

        // 🔹 Animation - fade in the layout
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(1.5), layout);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();

        // 🔹 When button clicked, fade out
        startBtn.setOnAction(e -> {
            FadeTransition fadeOut = new FadeTransition(Duration.seconds(1), layout);
            fadeOut.setFromValue(1);
            fadeOut.setToValue(0);
            fadeOut.setOnFinished(ev -> {
//                Stage newStage = new Stage();
                stage.close();
                try {
                    startDashboard(stage);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            });
            fadeOut.play();
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
