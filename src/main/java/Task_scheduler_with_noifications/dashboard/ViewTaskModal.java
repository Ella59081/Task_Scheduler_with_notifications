package Task_scheduler_with_noifications.dashboard;

import Task_scheduler_with_noifications.enums.Method;
import Task_scheduler_with_noifications.models.Task;
import Task_scheduler_with_noifications.notifications.NotificationsManager;
import Task_scheduler_with_noifications.service.impl.TaskServiceImpl;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ViewTaskModal {

    private final TaskServiceImpl impl = new TaskServiceImpl();

    public void show(String taskId) {
        Task task;
        try {
            task = impl.getSingleTask(taskId);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        if (task == null) {
            System.out.println("Task not found");
            return;
        }

        // Modal Stage
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Task Details");

        // Header
        Label header = new Label("Task Details");
        header.getStyleClass().add("modal-header");

        // Task info
        Label id = new Label("ID: " + task.getId());
        Label name = new Label("Name: " + task.getName());
        Label status = new Label("Status: " + task.getStatus());
        Label due = new Label("Due Date: " + task.getDueDate());
        Label desc = new Label("Description: " + task.getDescription());

        id.getStyleClass().add("modal-label");
        name.getStyleClass().add("modal-label");
        status.getStyleClass().add("modal-label");
        due.getStyleClass().add("modal-label");
        desc.getStyleClass().add("modal-label");

        VBox layout = new VBox(10, header, id, name, status, due, desc);
        layout.setPadding(new Insets(20));
        layout.getStyleClass().add("modal-container");

        Scene scene = new Scene(layout, 360, 300);
        scene.getStylesheets().add(getClass().getResource("/styles/viewTask.css").toExternalForm());
        stage.setScene(scene);
        stage.showAndWait();
    }
}


