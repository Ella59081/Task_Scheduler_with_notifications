package Task_scheduler_with_noifications.dashboard;
import Task_scheduler_with_noifications.dashboard.forms.AddTaskForm;
import Task_scheduler_with_noifications.dashboard.forms.EditTaskModal;
import Task_scheduler_with_noifications.enums.ActivityType;
import Task_scheduler_with_noifications.enums.Method;
import Task_scheduler_with_noifications.models.Task;
import Task_scheduler_with_noifications.notifications.NotificationsManager;
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

public class TasksApp {
    private final BorderPane view;
    private TableView<Task> table;
    TaskServiceImpl impl = new TaskServiceImpl();
    NotificationsManager ns = new NotificationsManager();

    public TasksApp() {
        buildUI();
        loadTasks();

        List<Task> taskList = impl.getAllTasks();
        view = new BorderPane();
        view.getStyleClass().add("tasks-page");

        // 🔹 Top Bar
        Button addBtn = new Button("+ Add New Task");
        addBtn.setOnAction(e->{
            Stage owner = (Stage)view.getScene().getWindow();
            try {
                new AddTaskForm().show(owner, taskList::add);
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
        table = new TableView<>();

        TableColumn<Task, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Task, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Task, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        TableColumn<Task, String> dueDateCol = new TableColumn<>("Due Date");
        dueDateCol.setCellValueFactory(new PropertyValueFactory<>("dueDate"));

        TableColumn<Task, Void> actionsCol = new TableColumn<>("Actions");
        actionsCol.setCellFactory(col -> new TableCell<Task, Void>(){
            private final Button viewBtn = new Button("View");
            private final Button editBtn = new Button("Edit");
            private final Button deleteBtn = new Button("Delete");
            private final HBox container = new HBox(8, viewBtn, editBtn, deleteBtn);
            {
                container.setAlignment(Pos.CENTER_LEFT);
                actionsCol.setPrefWidth(200);
                deleteBtn.setOnAction(e->{
                    Task task = getTableView().getItems().get(getIndex());
                    boolean confirmed = confirm("Delete Task", "Delete task " + task.getName() + " ?");
                    if(!confirmed) return;
                    new Thread(()->{
                        try {
                            impl.deleteTask(task.getId());
                            getTableView().getItems().remove(task);
                            ns.sendNotifications(task, Method.in_app, ActivityType.Task_deleted);
                        } catch (Exception ex){
                            ex.printStackTrace();
                            ns.sendErrorNotification("Failed to delete " + task.getName());
                        }
                    }).start();
                });

                viewBtn.setOnAction(e->{
                    Task task = getTableView().getItems().get(getIndex());
                    new ViewTaskModal().show(task.getId());
                });

                editBtn.setOnAction(e -> {
                    Task task = getTableView().getItems().get(getIndex());

                    new EditTaskModal().show(task, () -> {
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
        List<Task> tasks = impl.getAllTasks();
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
