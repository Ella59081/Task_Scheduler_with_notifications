package Task_scheduler_with_noifications.dashboard;

import Task_scheduler_with_noifications.models.Project;
import Task_scheduler_with_noifications.models.Task;
import Task_scheduler_with_noifications.service.impl.ProjectServiceImpl;
import Task_scheduler_with_noifications.service.impl.TaskServiceImpl;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ViewProjectModel {
    private final ProjectServiceImpl impl = new ProjectServiceImpl();

    public void show(String ProjectId) {
        Project project;
        try {
            project = impl.getSingleProject(ProjectId);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        if (project == null) {
            System.out.println("Project not found");
            return;
        }

        // Modal Stage
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Project Details");

        // Header
        Label header = new Label("Project Details");
        header.getStyleClass().add("modal-header");

        // Task info
        Label id = new Label("ID: " + project.getId());
        Label name = new Label("Name: " + project.getName());
        Label status = new Label("Status: " + project.getStatus());
        Label due = new Label("Due Date: " + project.getDueDate());
        Label desc = new Label("Description: " + project.getDescription());

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
