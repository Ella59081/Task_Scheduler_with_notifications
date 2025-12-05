package Task_scheduler_with_noifications.dashboard.forms;

import Task_scheduler_with_noifications.models.User;
import Task_scheduler_with_noifications.service.impl.UserServiceImpl;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class EditUserProfileModal {

    private UserServiceImpl impl = new UserServiceImpl();

    public void show(User user, Runnable onUpdate) {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Edit User Profile");

        Label header = new Label("Edit Profile");
        header.getStyleClass().add("modal-header");

        TextField nameField = new TextField(user.getUserName());
        TextField emailField = new TextField(user.getEmail());
        TextField roleField = new TextField(user.getPhone());

        Button saveBtn = new Button("Save");
        saveBtn.getStyleClass().add("modal-button");
        saveBtn.setOnAction(e -> {
            try {
                user.setUserName(nameField.getText());
                user.setEmail(emailField.getText());
                user.setPhone((roleField.getText()));

                impl.updateUserProfile(user); // Save to DB

                if (onUpdate != null) onUpdate.run();
                stage.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        VBox layout = new VBox(10, header, nameField, emailField, roleField, saveBtn);
        layout.setPadding(new Insets(20));
        layout.getStyleClass().add("modal-container");

        Scene scene = new Scene(layout, 360, 300);
        scene.getStylesheets().add(getClass().getResource("/styles/viewTask.css").toExternalForm());
        stage.setScene(scene);
        stage.showAndWait();
    }
}

