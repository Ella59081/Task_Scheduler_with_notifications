package Task_scheduler_with_noifications.dashboard.forms;

import Task_scheduler_with_noifications.models.Project;
import Task_scheduler_with_noifications.models.Task;
import Task_scheduler_with_noifications.service.impl.ProjectServiceImpl;
import Task_scheduler_with_noifications.service.impl.TaskServiceImpl;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class EditProjectModel {
    private final ProjectServiceImpl impl = new ProjectServiceImpl();

    public void show(Project project, Runnable onUpdate) {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Edit Project");

        // Header
        Label header = new Label("Edit Project");
        header.getStyleClass().add("modal-header");

        // Editable fields
        TextField nameField = new TextField(project.getName());
        nameField.setPromptText("Task Name");

        TextField statusField = new TextField(project.getStatus());
        statusField.setPromptText("Status");

        TextField dueField = new TextField(project.getDueDate());
        dueField.setPromptText("Due Date");

        TextField descField = new TextField(project.getDescription());
        descField.setPromptText("Description");

        // Save button
        Button saveBtn = new Button("Save");
        saveBtn.getStyleClass().add("modal-button");
        saveBtn.setOnAction(e -> {
            try {
                project.setName(nameField.getText());
                project.setStatus(statusField.getText());
                project.setDueDate(dueField.getText());
                project.setDescription(descField.getText());

                impl.updateProject(project);  // Save to database

                if (onUpdate != null) onUpdate.run();  // Refresh table

                stage.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        VBox layout = new VBox(10, header, nameField, statusField, dueField, descField, saveBtn);
        layout.setPadding(new Insets(20));
        layout.getStyleClass().add("modal-container");

        Scene scene = new Scene(layout, 360, 360);
        scene.getStylesheets().add(getClass().getResource("/styles/viewTask.css").toExternalForm());
        stage.setScene(scene);
        stage.showAndWait();
    }
}
