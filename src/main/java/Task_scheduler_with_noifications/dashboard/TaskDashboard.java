package Task_scheduler_with_noifications.dashboard;

import javafx.application.Application;
import javafx.collections.FXCollections; 
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos; 
import javafx.scene.Scene; 
import javafx.scene.control.Button; 
import javafx.scene.control.Label; 
import javafx.scene.control.Separator; 
import javafx.scene.control.TableCell; 
import javafx.scene.control.TableColumn; 
import javafx.scene.control.TableView; 
import javafx.scene.control.TextField; 
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority; 
import javafx.scene.layout.Region; 
import javafx.scene.layout.StackPane; 
import javafx.scene.layout.VBox; 
import javafx.scene.paint.Color; 
import javafx.scene.shape.Circle; 
import javafx.scene.text.Font; 
import javafx.scene.text.FontWeight; 
import javafx.stage.Stage;


public class TaskDashboard extends Application {

    @Override public void start(Stage stage) {
        BorderPane root = new BorderPane();
        root.setPrefSize(1200, 800);

// Left sidebar
        VBox sidebar = createSidebar();
        root.setLeft(sidebar);

        // Top bar
        HBox topbar = createTopBar();
        root.setTop(topbar);

        // Center content (dashboard)
        VBox center = new VBox();
        center.setSpacing(18);
        center.setPadding(new Insets(24));

        // Header area (title + cards)
        Label title = new Label("Work Update");
        title.setFont(Font.font("System", FontWeight.BOLD, 28));

        Label subtitle = new Label("Manage and track all operations tasks in one place");
        subtitle.setTextFill(Color.GRAY);

        VBox header = new VBox(title, subtitle);
        header.setSpacing(6);

        HBox cards = createSummaryCards();

        // Task list table
        TableView<Task> table = createTaskTable();
        VBox.setVgrow(table, Priority.ALWAYS);

        HBox headerAndAdd = new HBox();
        headerAndAdd.setAlignment(Pos.CENTER_LEFT);
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Button addTask = new Button("+ Add Task");
        addTask.getStyleClass().add("primary-button");
        headerAndAdd.getChildren().addAll(header, spacer, addTask);

        center.getChildren().addAll(headerAndAdd, cards, table);
        root.setCenter(center);

        Scene scene = new Scene(root);
        scene.getStylesheets().add("data:, /*inline css */\n " +
                        ".root { -fx-font-family: 'Segoe UI', 'Roboto', system; } " +
                "sidebar { -fx-background-color: white; } " +
                ".nav-label { -fx-text-fill: #6b7280; -fx-font-weight: 600; } " +
                ".card { -fx-background-color: white; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.06), 8,0,0,2); -fx-padding: 18; -fx-background-radius: 8; } " +
                ".summary-num { -fx-font-size: 20px; -fx-font-weight: bold; } .summary-label { -fx-text-fill: #6b7280; } " +
                ".table-view { -fx-background-color: transparent; } " +
                ".primary-button { -fx-background-color: #0b71eb; -fx-text-fill: white; -fx-padding: 8 12; -fx-background-radius: 6; } " +
                ".action-button { -fx-background-color: #e6f0ff; -fx-text-fill: #0b71eb; -fx-padding: 6 10; -fx-background-radius: 6; } ");

        stage.setScene(scene);
        stage.setTitle("Task_scheduler_with_noifications.dashboard.TaskDashboard");
        stage.show();

    }

    private VBox createSidebar() {
        VBox box = new VBox();
        box.getStyleClass().add("sidebar");
        box.setPrefWidth(220);
        box.setPadding(new Insets(18));
        box.setSpacing(12);

        Label logo = new Label("ProjectConnect");
        logo.setFont(Font.font("System", FontWeight.BOLD, 16));

        Label dash = new Label("Dashboard");
        dash.getStyleClass().add("nav-label");

        Label tasks = new Label("Tasks");
        tasks.getStyleClass().add("nav-label");

        Label teams = new Label("Teams");
        teams.getStyleClass().add("nav-label");

        Label reports = new Label("Reports");
        reports.getStyleClass().add("nav-label");

        Label settings = new Label("Settings");
        settings.getStyleClass().add("nav-label");

        VBox nav = new VBox(10, dash, tasks, teams, reports, settings);
        nav.setPadding(new Insets(12, 0, 0, 0));

        box.getChildren().addAll(logo, new Separator(), nav);
        return box;

    }

    private HBox createTopBar() {
        HBox top = new HBox();
        top.setPadding(new Insets(12, 18, 12, 18));
        top.setSpacing(12);
        top.setStyle("-fx-background-color: white; -fx-border-color: transparent transparent #efefef transparent; -fx-border-width: 0 0 1 0;");

        Label appTitle = new Label("Dashboard");
        appTitle.setFont(Font.font("System", FontWeight.SEMI_BOLD, 16));

        TextField search = new TextField();
        search.setPromptText("Search tasks");
        search.setPrefWidth(420);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox right = new HBox(12);
        right.setAlignment(Pos.CENTER_RIGHT);
        Circle avatar = new Circle(16, Color.web("#0b71eb"));
        Label initials = new Label("AK");
        initials.setTextFill(Color.WHITE);
        initials.setFont(Font.font(12));
        StackPane avatarStack = new StackPane(avatar, initials);

        right.getChildren().addAll(new Label("🔔"), avatarStack);

        top.getChildren().addAll(appTitle, search, spacer, right);
        return top;

    }

    private HBox createSummaryCards() { HBox cards = new HBox(16);

        cards.getChildren().addAll(
                createCard("Total Tasks", "42"),
                createCard("Pending", "12"),
                createCard("In Progress", "18"),
                createCard("Completed", "12")
        );

        return cards;

    }

    private VBox createCard(String label, String number) { VBox card = new VBox(8); card.getStyleClass().add("card"); card.setPrefWidth(180);

        Label lbl = new Label(label);
        lbl.setFont(Font.font(13));
        lbl.setTextFill(Color.web("#6b7280"));

        Label num = new Label(number);
        num.getStyleClass().add("summary-num");

        card.getChildren().addAll(lbl, num);
        return card;

    }

    private TableView<Task> createTaskTable() { TableView<Task> table = new TableView<>(); table.setItems(getSampleData());

        TableColumn<Task, String> idCol = new TableColumn<>("Task ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        idCol.setPrefWidth(120);

        TableColumn<Task, String> titleCol = new TableColumn<>("Title");
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        titleCol.setPrefWidth(380);

        TableColumn<Task, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusCol.setPrefWidth(120);

        TableColumn<Task, String> assignedCol = new TableColumn<>("Assigned To");
        assignedCol.setCellValueFactory(new PropertyValueFactory<>("assignedName"));
        assignedCol.setPrefWidth(160);
        assignedCol.setCellFactory(col -> new TableCell<Task, String>() {
            @Override
            protected void updateItem(String name, boolean empty) {
                super.updateItem(name, empty);
                if (empty || name == null) {
                    setGraphic(null);
                    setText(null);
                    return;
                }
                Circle c = new Circle(14, Color.web("#dbeafe"));
                Label initials = new Label(getInitials(name));
                initials.setFont(Font.font(12));
                StackPane stack = new StackPane(c, initials);
                setGraphic(new HBox(8, stack, new Label(name)));
            }
        });

        TableColumn<Task, String> dueCol = new TableColumn<>("Due Date");
        dueCol.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        dueCol.setPrefWidth(140);

        TableColumn<Task, Void> actionsCol = new TableColumn<>("Actions");
        actionsCol.setPrefWidth(120);
        actionsCol.setCellFactory(col -> new TableCell<Task, Void>() {
            private final Button btn = new Button("View");
            {
                btn.getStyleClass().add("action-button");
                btn.setOnAction(e -> {
                    Task t = getTableView().getItems().get(getIndex());
                    System.out.println("Open task: " + t.getTitle());
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(btn);
                }
            }
        });

        table.getColumns().addAll(idCol, titleCol, statusCol, assignedCol, dueCol, actionsCol);
        return table;

    }

    private ObservableList<Task> getSampleData() { ObservableList<Task> data = FXCollections.observableArrayList(); data.add(new Task("#T-001", "Update customer onboarding flow", "Pending", "Sarah Johnson", "02 Jul 2025")); data.add(new Task("#T-002", "Finalize Q3 operations budget", "In Progress", "Alex Morgan", "14 Jul 2025")); data.add(new Task("#T-003", "Conduct supplier performance review", "Completed", "Emily Chen", "25 Jun 2025")); data.add(new Task("#T-004", "Resolve webhook synchronization", "In Progress", "Michael Brown", "09 Jul 2025")); data.add(new Task("#T-005", "Implement new script for billing", "Pending", "Carol Watson", "18 Jul 2025")); data.add(new Task("#T-006", "Quarterly compliance audit", "Completed", "Leo Serapio", "01 Jul 2025")); data.add(new Task("#T-007", "Start planning new recruitment drive", "In Progress", "Jessica K.", "30 Jul 2025")); return data; }

    private String getInitials(String name) { String[] parts = name.split(" "); if (parts.length == 1) return parts[0].substring(0, Math.min(2, parts[0].length())).toUpperCase(); return ("" + parts[0].charAt(0) + parts[parts.length - 1].charAt(0)).toUpperCase(); }

    public static void main(String[] args) { launch(args); }

// Simple Task model for the table
    public static class Task {
        private final String id;
        private final String title;
        private final String status;
        private final String assignedName;
        private final String dueDate;

    public Task(String id, String title, String status, String assignedName, String dueDate) {
        this.id = id;
        this.title = title;
        this.status = status;
        this.assignedName = assignedName;
        this.dueDate = dueDate;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getStatus() { return status; }
    public String getAssignedName() { return assignedName; }
    public String getDueDate() { return dueDate; }

} }