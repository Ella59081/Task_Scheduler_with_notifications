package Task_scheduler_with_noifications;

import Task_scheduler_with_noifications.AppDesign.WelcomeApp;
import Task_scheduler_with_noifications.config.DButil;
import Task_scheduler_with_noifications.config.DatabaseConnection;
import Task_scheduler_with_noifications.dashboard.DashboardApp;
import Task_scheduler_with_noifications.models.Project;
import Task_scheduler_with_noifications.models.Task;
import Task_scheduler_with_noifications.models.User;
import Task_scheduler_with_noifications.notifications.NotificationsManager;
import Task_scheduler_with_noifications.service.impl.ProjectServiceImpl;
import Task_scheduler_with_noifications.service.impl.TaskServiceImpl;
import Task_scheduler_with_noifications.service.impl.UserServiceImpl;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

import static Task_scheduler_with_noifications.config.generateTableId.generateId;

public class Main {

    public static void main(String[] args) {
        UserServiceImpl us = new UserServiceImpl();

        DButil.initDatabase();
        Application.launch(WelcomeApp.class, args);
        if(us.getUserProfile() == null){
            User u = new User("User", null, null, null,null);
            us.addUser(u);
        }

    }

}
