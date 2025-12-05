package Task_scheduler_with_noifications.dashboard.forms;

import Task_scheduler_with_noifications.config.generateTableId;
import Task_scheduler_with_noifications.enums.ActivityType;
import Task_scheduler_with_noifications.enums.Method;
import Task_scheduler_with_noifications.models.Project;
import Task_scheduler_with_noifications.models.Task;
import Task_scheduler_with_noifications.notifications.NotificationsManager;
import Task_scheduler_with_noifications.service.impl.ProjectServiceImpl;
import Task_scheduler_with_noifications.service.impl.TaskServiceImpl;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.function.Consumer;

public class AddProjectForm {
    public void show(Stage owner, Consumer<Project> onSaved) {
        Stage stage = new Stage();
        stage.initOwner(owner);
        stage.initModality(Modality.WINDOW_MODAL);

        VBox layout = new VBox(15);
        layout.getStyleClass().add("form-container");

        Label title = new Label("Add Project Task");
        title.getStyleClass().add("form-title");

        TextField nameField = new TextField();
        nameField.setPromptText("Project Name");
        nameField.getStyleClass().add("input-field");

        TextField description = new TextField();
        description.setPromptText("Description");
        nameField.getStyleClass().add("input-field");

        DatePicker dueDate = new DatePicker();
        dueDate.setPromptText("Due date");
        dueDate.getStyleClass().add("input-field");

//        String[] items = {""};
//        Combo<String> statusField = new ComboBox<>("");

        Button save = new Button("Add Project");
        save.getStyleClass().add("primary-button");

        layout.getChildren().addAll(title, nameField, description,
                dueDate, save);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));
        NotificationsManager nm = new NotificationsManager();

        save.setOnAction(e -> {
            try {
                ProjectServiceImpl impl = new ProjectServiceImpl();
                LocalDate date = LocalDate.now();
                String taskId = generateTableId.generateId("Projects");
                Project newTask = new Project(
                        taskId,
                        nameField.getText(),
                        description.getText(),
                        dueDate.getValue().toString(),
                        "New",
                        date.toString(),
                        "Unassigned",
                        "Unassigned"
                );

                // 👉 CALL YOUR METHOD HEREb
                impl.addProject(newTask);
                nm.sendProjectNot(newTask, Method.push , ActivityType.New_task_created);

                // return the saved task to TasksPage
                onSaved.accept(newTask);

                stage.close();

            } catch (Exception ex) {
                ex.printStackTrace();
                nm.sendErrorNotification("An error occurred" + ex.getMessage());

            }
        });

        Scene scene = new Scene(layout, 400, 350);
        scene.getStylesheets().add("/styles/forms.css");

        stage.setScene(scene);
        stage.show();
    }
}
