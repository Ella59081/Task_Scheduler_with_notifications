package Task_scheduler_with_noifications.dashboard;
import Task_scheduler_with_noifications.dashboard.forms.EditTaskModal;
import Task_scheduler_with_noifications.enums.ActivityType;
import Task_scheduler_with_noifications.enums.Method;
import Task_scheduler_with_noifications.models.Notification;
import Task_scheduler_with_noifications.models.Task;
import Task_scheduler_with_noifications.notifications.NotificationsManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;

public class NotificationsApp {
    private final VBox view;
    private TableView<Notification> table;


    NotificationsManager impl = new NotificationsManager();

    public NotificationsApp() {
        view = new VBox(10);
        view.setPadding(new Insets(20));
        view.getStyleClass().add("notifications-page");
        buildUI();
        loadNotifications();

//        TableView<String> table = new TableView<>();
//        table.getStyleClass().add("notifications-table");
//
//        TableColumn<String, String> idCol = new TableColumn<>("ID");
//        TableColumn<String, String> activityCol = new TableColumn<>("Activity");
//        TableColumn<String, String> messageCol = new TableColumn<>("Message");
//        TableColumn<String, String> dateCol = new TableColumn<>("Date Sent");
//        TableColumn<String, String> timeCol = new TableColumn<>("Time Sent");
//
//        table.getColumns().addAll(idCol, activityCol, messageCol, dateCol, timeCol);
        view.getChildren().add(table);
    }

    private void buildUI() {
        table = new TableView<>();

        TableColumn<Notification, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Notification, String> nameCol = new TableColumn<>("Activity");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("activity"));

        TableColumn<Notification, String> statusCol = new TableColumn<>("Method");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("method"));

        TableColumn<Notification, String> message = new TableColumn<>("Message");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("message"));

        TableColumn<Notification, String> dueDateCol = new TableColumn<>("TimeStamp");
        dueDateCol.setCellValueFactory(new PropertyValueFactory<>("timeStamp"));


        table.getColumns().addAll(idCol, nameCol, statusCol, message, dueDateCol);

        assert view != null;
    }

    private void loadNotifications() {
        List<Notification> not = impl.getNotifications();
        assert table != null;
        table.getItems().setAll(not);
    }

    // 🎯 Call this after add/update/delete
    public void refreshTable() {
        loadNotifications();
    }

    public VBox getView() {
        return view;
    }
}

