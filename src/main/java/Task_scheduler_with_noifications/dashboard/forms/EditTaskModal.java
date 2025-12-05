package Task_scheduler_with_noifications.dashboard.forms;

import Task_scheduler_with_noifications.models.Task;
import Task_scheduler_with_noifications.service.impl.TaskServiceImpl;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class EditTaskModal {

    private final TaskServiceImpl impl = new TaskServiceImpl();

    public void show(Task task, Runnable onUpdate) {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Edit Task");

        // Header
        Label header = new Label("Edit Task");
        header.getStyleClass().add("modal-header");

        // Editable fields
        TextField nameField = new TextField(task.getName());
        nameField.setPromptText("Task Name");

        TextField statusField = new TextField(task.getStatus());
        statusField.setPromptText("Status");

        TextField dueField = new TextField(task.getDueDate());
        dueField.setPromptText("Due Date");

        TextField descField = new TextField(task.getDescription());
        descField.setPromptText("Description");

        // Save button
        Button saveBtn = new Button("Save");
        saveBtn.getStyleClass().add("modal-button");
        saveBtn.setOnAction(e -> {
            try {
                task.setName(nameField.getText());
                task.setStatus(statusField.getText());
                task.setDueDate(dueField.getText());
                task.setDescription(descField.getText());

                impl.updateTask(task);  // Save to database

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

