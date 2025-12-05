package Task_scheduler_with_noifications.dashboard;
import Task_scheduler_with_noifications.dashboard.forms.EditUserProfileModal;
import Task_scheduler_with_noifications.models.User;
import Task_scheduler_with_noifications.service.impl.TaskServiceImpl;
import Task_scheduler_with_noifications.service.impl.UserServiceImpl;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

//public class SettingsApp {
//    private final VBox view;
//
//    public SettingsApp() {
//        view = new VBox(20);
//        view.setPadding(new Insets(40));
//        view.setAlignment(Pos.TOP_CENTER);
//        view.getStyleClass().add("settings-page");
//
//        Label userProfile = new Label("User Profile");
//        userProfile.getStyleClass().add("settings-title");
//
//        Button updateBtn = new Button("Update Profile");
//        updateBtn.getStyleClass().add("update-btn");
//
//        Button modeBtn = new Button("Toggle Light/Dark Mode");
//        modeBtn.getStyleClass().add("mode-btn");
//
//        view.getChildren().addAll(userProfile, updateBtn, modeBtn);
//    }
//
//    public VBox getView() {
//        return view;
//    }
//}


import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class SettingsApp {

    private VBox view;
    UserServiceImpl us = new UserServiceImpl(); // reuse DB access

    public SettingsApp() {
        // your user model
        User user = us.getUserProfile();

        view = new VBox(15);
        view.setPadding(new Insets(20));

        Label header = new Label("User Profile");
        header.getStyleClass().add("modal-header");

        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);

        Label nameLabel = new Label("Name:");
        TextField nameField = new TextField(user.getUserName());
        nameField.setEditable(false);

        Label emailLabel = new Label("Email:");
        TextField emailField = new TextField(user.getEmail());
        emailField.setEditable(false);

        Label roleLabel = new Label("Phone number:");
        TextField roleField = new TextField(user.getPhone());
        roleField.setEditable(false);

        grid.addRow(0, nameLabel, nameField);
        grid.addRow(1, emailLabel, emailField);
        grid.addRow(2, roleLabel, roleField);

        Button editBtn = new Button("Edit Profile");
        editBtn.getStyleClass().add("modal-button");
        editBtn.setOnAction(e -> new EditUserProfileModal().show(user, () -> {
            // refresh fields after editing
            nameField.setText(user.getUserName());
            emailField.setText(user.getEmail());
            roleField.setText(user.getPhone());
        }));

        view.getChildren().addAll(header, grid, editBtn);
    }

    public Parent getView() {
        return view;
    }
}


