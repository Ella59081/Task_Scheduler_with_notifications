package Task_scheduler_with_noifications.dashboard;
import Task_scheduler_with_noifications.enums.Status;
import Task_scheduler_with_noifications.models.Project;
import Task_scheduler_with_noifications.models.Task;
import Task_scheduler_with_noifications.service.impl.ProjectServiceImpl;
import Task_scheduler_with_noifications.service.impl.TaskServiceImpl;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.PieChart;
import javafx.scene.layout.*;
import javafx.scene.control.Label;

import java.util.ArrayList;
import java.util.List;

public class OverviewApp {
    private final VBox view;

    public OverviewApp() {
        TaskServiceImpl ts = new TaskServiceImpl();
        ProjectServiceImpl ps = new ProjectServiceImpl();
        List<Task> allTasks = ts.getAllTasks();
        List<Project> allProjects = ps.getAllProjects();
        int projectsSize = allProjects.size();
        int taskSize = allTasks.size();
        List<Task> completed = new ArrayList<>();
        int completedSize = completed.size();
        List<Task> pending = new ArrayList<>();
        int pendingSize = pending.size();
        for(Task t: allTasks){
            if(t.getStatus().equals("completed")){
                completed.add(t);
            }
        }
        for(Task t: allTasks){
            if(t.getStatus().equals("pending")){
                pending.add(t);
            }
        }
        view = new VBox(20);
        view.setPadding(new Insets(20));
        view.setAlignment(Pos.TOP_CENTER);
        view.getStyleClass().add("overview-page");
        Label message = new Label("Manage and track your projects and tasks in one place");
        message.setAlignment(Pos.TOP_LEFT);
        message.setStyle("-fx-font-size: 20px; ");

        HBox cards = new HBox(20,
                createCard("Total Tasks", ""+taskSize),
                createCard("Pending Tasks", ""+projectsSize),
                createCard("Completed Tasks", ""+completedSize),
                createCard("Pending Tasks", ""+pendingSize)
        );
        cards.setAlignment(Pos.CENTER);

        PieChart chart = new PieChart();
        chart.getData().addAll(
                new PieChart.Data("Pending", 35),
                new PieChart.Data("Completed", 75),
                new PieChart.Data("Overdue", 10)
        );

        view.getChildren().addAll(message, cards, chart);
    }

    private VBox createCard(String title, String value) {
        Label t = new Label(title);
        t.getStyleClass().add("card-title");

        Label v = new Label(value);
        v.getStyleClass().add("card-value");

        VBox box = new VBox(5, t, v);
        box.getStyleClass().add("card");
        box.setAlignment(Pos.CENTER);
        box.setPrefWidth(200);
        return box;
    }

    public VBox getView() {
        return view;
    }
}

