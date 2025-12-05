//package Task_scheduler_with_noifications.dashboard;
//import javafx.geometry.Insets;
//import javafx.geometry.Pos;
//import javafx.scene.control.*;
//import javafx.scene.layout.BorderPane;
//import javafx.scene.layout.HBox;
//import javafx.scene.layout.VBox;
//
//public class ProjectsApp {
//    private final BorderPane view;
//
//    public ProjectsApp() {
//        view = new BorderPane();
//        view.getStyleClass().add("projects-page");
//
//        Button addProjectBtn = new Button("+ Add New Project");
//        addProjectBtn.getStyleClass().add("add-btn");
//        HBox topBar = new HBox(addProjectBtn);
//        topBar.setAlignment(Pos.CENTER_RIGHT);
//        topBar.setPadding(new Insets(15));
//
//        TableView<String> table = new TableView<>();
//        table.getStyleClass().add("projects-table");
//
//        TableColumn<String, String> idCol = new TableColumn<>("ID");
//        TableColumn<String, String> nameCol = new TableColumn<>("Project Name");
//        TableColumn<String, String> statusCol = new TableColumn<>("Status");
//        TableColumn<String, String> dueDateCol = new TableColumn<>("Deadline");
//        TableColumn<String, String> actionsCol = new TableColumn<>("Actions");
//
//        table.getColumns().addAll(idCol, nameCol, statusCol, dueDateCol, actionsCol);
//
//        VBox center = new VBox(table);
//        center.setPadding(new Insets(20));
//
//        view.setTop(topBar);
//        view.setCenter(center);
//    }
//
//    public BorderPane getView() {
//        return view;
//    }
//}

package Task_scheduler_with_noifications.dashboard;
import Task_scheduler_with_noifications.dashboard.forms.AddProjectForm;
import Task_scheduler_with_noifications.dashboard.forms.AddTaskForm;
import Task_scheduler_with_noifications.dashboard.forms.EditProjectModel;
import Task_scheduler_with_noifications.dashboard.forms.EditTaskModal;
import Task_scheduler_with_noifications.enums.ActivityType;
import Task_scheduler_with_noifications.enums.Method;
import Task_scheduler_with_noifications.models.Project;
import Task_scheduler_with_noifications.models.Task;
import Task_scheduler_with_noifications.notifications.NotificationsManager;
import Task_scheduler_with_noifications.service.ProjectService;
import Task_scheduler_with_noifications.service.impl.ProjectServiceImpl;
import Task_scheduler_with_noifications.service.impl.TaskServiceImpl;
import com.almasb.fxgl.core.util.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.util.List;
import java.util.Optional;

public class ProjectsApp {
    private final BorderPane view;
    private TableView<Project> table;
    ProjectServiceImpl impl = new ProjectServiceImpl();
    NotificationsManager ns = new NotificationsManager();

    public ProjectsApp() {
        buildUI();
        loadTasks();

        List<Project> projectList = impl.getAllProjects();
        view = new BorderPane();
        view.getStyleClass().add("projects-page");

        // 🔹 Top Bar
        Button addBtn = new Button("+ Add New Project");
        addBtn.setOnAction(e->{
            Stage owner = (Stage)view.getScene().getWindow();
            try {
                new AddProjectForm().show(owner, projectList::add);
                refreshTable();
                table.refresh();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });

        addBtn.getStyleClass().add("add-btn");
        HBox topBar = new HBox(addBtn);
        topBar.setPadding(new Insets(15));
        topBar.setAlignment(Pos.CENTER_RIGHT);
        table.setPadding(new Insets(15));


        // 🔹 Table
//        table.getStyleClass().add("tasks-table");
//
//        TableColumn<String, String> idCol = new TableColumn<>("ID");
//        TableColumn<String, String> nameCol = new TableColumn<>("Name");
//        TableColumn<String, String> statusCol = new TableColumn<>("Status");
//        TableColumn<String, String> dueDateCol = new TableColumn<>("Due Date");
//        TableColumn<String, String> actionsCol = new TableColumn<>("Actions");
//
//        table.getColumns().addAll(idCol, nameCol, statusCol, dueDateCol, actionsCol);
//
//        VBox center = new VBox(table);
//        center.setPadding(new Insets(20));
//
        view.setTop(topBar);
        view.setCenter(table);

    }

    private void buildUI() {
        table = new TableView<Project>();

        TableColumn<Project, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Project, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Project, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        TableColumn<Project, String> dueDateCol = new TableColumn<>("Due Date");
        dueDateCol.setCellValueFactory(new PropertyValueFactory<>("dueDate"));

        TableColumn<Project, Void> actionsCol = new TableColumn<>("Actions");
        actionsCol.setCellFactory(col -> new TableCell<Project, Void>(){
            private final Button viewBtn = new Button("View");
            private final Button editBtn = new Button("Edit");
            private final Button deleteBtn = new Button("Delete");
            private final HBox container = new HBox(8, viewBtn, editBtn, deleteBtn);
            {
                container.setAlignment(Pos.CENTER_LEFT);
                actionsCol.setPrefWidth(200);
                deleteBtn.setOnAction(e->{
                    Project p = getTableView().getItems().get(getIndex());
                    boolean confirmed = confirm("Delete Project", "Delete Project " + p.getName() + " ?");
                    if(!confirmed) return;
                    new Thread(()->{
                        try {
                            impl.deleteProject(p.getId());
                            getTableView().getItems().remove(p);
                            ns.sendProjectNot(p, Method.in_app, ActivityType.Project_deleted);
                        } catch (Exception ex){
                            ex.printStackTrace();
                            ns.sendErrorNotification("Failed to delete " + p.getName());
                        }
                    }).start();
                });

                viewBtn.setOnAction(e->{
                    Project project = getTableView().getItems().get(getIndex());
                    new ViewProjectModel().show(project.getId());
                });

                editBtn.setOnAction(e -> {
                    Project project = getTableView().getItems().get(getIndex());

                    new EditProjectModel().show(project, () -> {
                        // Refresh TableView row after edit
                        table.refresh();
                        refreshTable();
                    });
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(container);
                }
            }
        });

        table.getColumns().addAll(idCol, nameCol, statusCol, dueDateCol, actionsCol);

        assert view != null;
    }



    private boolean confirm(String title, String text){
        Alert a = new Alert(Alert.AlertType.CONFIRMATION, text, ButtonType.YES, ButtonType.NO);
        a.setTitle(title);
        Optional<ButtonType> res = a.showAndWait();
        return res.isPresent() && res.get() == ButtonType.YES;
    }


    private void loadTasks() {
        List<Project> tasks = impl.getAllProjects();
        assert table != null;
        table.getItems().setAll(tasks);
    }

    // 🎯 Call this after add/update/delete
    public void refreshTable() {
        loadTasks();
    }

    public Node getView() {
        return view;
    }
}

