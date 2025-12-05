package Task_scheduler_with_noifications.dashboard;
import Task_scheduler_with_noifications.models.Activity;
import Task_scheduler_with_noifications.models.Notification;
import Task_scheduler_with_noifications.notifications.NotificationsManager;
import javafx.geometry.Insets;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.util.List;

public class ActivityLogApp {
    private final VBox view;
    private TableView<Activity> table;

    NotificationsManager impl = new NotificationsManager();

    public ActivityLogApp() {
        view = new VBox(10);
        view.setPadding(new Insets(20));
        view.getStyleClass().add("activitylog-page");
        buildUI();
        loadNotifications();



//        TableView<String> table = new TableView<>();
//        table.getStyleClass().add("activitylog-table");
//
//        TableColumn<String, String> idCol = new TableColumn<>("ID");
//        TableColumn<String, String> activityCol = new TableColumn<>("Activity");
//        TableColumn<String, String> messageCol = new TableColumn<>("Details");
//        TableColumn<String, String> dateCol = new TableColumn<>("Date");
//        TableColumn<String, String> timeCol = new TableColumn<>("Time");
//
//        table.getColumns().addAll(idCol, activityCol, messageCol, dateCol, timeCol);
        view.getChildren().add(table);
    }

    private void buildUI() {
        table = new TableView<>();

        TableColumn<Activity, String> idCol = new TableColumn<>("Activity");
        idCol.setCellValueFactory(new PropertyValueFactory<>("activity"));

        TableColumn<Activity, String> nameCol = new TableColumn<>("TimeStamp");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("dateTime"));

        TableColumn<Activity, String> statusCol = new TableColumn<>("ProjectId");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("projectId"));

        TableColumn<Activity, String> dueDateCol = new TableColumn<>("TaskId");
        dueDateCol.setCellValueFactory(new PropertyValueFactory<>("taskId"));


        table.getColumns().addAll(idCol, nameCol, statusCol, dueDateCol);

        assert view != null;
    }

    private void loadNotifications() {
        List<Activity> act = impl.getActivityLog();
        assert table != null;
        table.getItems().setAll(act);
    }

    public VBox getView() {
        return view;
    }
}

