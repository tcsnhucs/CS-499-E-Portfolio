package ui;

import java.io.File;
import java.io.IOException;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.application.Platform;
import javafx.stage.WindowEvent;

import model.Contact;
import service.ContactService;
import persistence.PersistenceManager;

public class MainApp extends Application {

    private ContactService contactService = new ContactService();
    private service.TaskService taskService = new service.TaskService();
    private service.AppointmentService appointmentService = new service.AppointmentService();

    private TableView<Contact> contactTable = new TableView<>();
    private ObservableList<Contact> contactData = FXCollections.observableArrayList();

    private TableView<model.Task> taskTable = new TableView<>();
    private ObservableList<model.Task> taskData = FXCollections.observableArrayList();

    private TableView<model.Appointment> appointmentTable = new TableView<>();
    private ObservableList<model.Appointment> appointmentData = FXCollections.observableArrayList();

    private File currentFile = null;
    private Stage primaryStage;
    private boolean dirty = false;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("CS-320 Journal Manager");

        Menu fileMenu = new Menu("File");
        MenuItem openItem = new MenuItem("Open");
        MenuItem saveItem = new MenuItem("Save");
        MenuItem saveAsItem = new MenuItem("Save As");
        MenuItem exitItem = new MenuItem("Exit");
        fileMenu.getItems().addAll(openItem, saveItem, saveAsItem, exitItem);

        MenuBar menuBar = new MenuBar(fileMenu);

        this.primaryStage = primaryStage;
        primaryStage.setOnCloseRequest(e -> handleCloseRequest(e));

        openItem.setOnAction(e -> openFile(primaryStage));
        saveItem.setOnAction(e -> saveFile(primaryStage, false));
        saveAsItem.setOnAction(e -> saveFile(primaryStage, true));
        exitItem.setOnAction(e -> primaryStage.close());

        TabPane tabPane = new TabPane();
        Tab contactsTab = new Tab("Contacts");
        contactsTab.setClosable(false);
        contactsTab.setContent(buildContactsPane());
        tabPane.getTabs().add(contactsTab);

        // adding tasks and appointments tabs
        Tab tasksTab = new Tab("Tasks");
        tasksTab.setClosable(false);
        tasksTab.setContent(buildTasksPane());
        tabPane.getTabs().add(tasksTab);

        Tab apptTab = new Tab("Appointments");
        apptTab.setClosable(false);
        apptTab.setContent(buildAppointmentsPane());
        tabPane.getTabs().add(apptTab);

        BorderPane root = new BorderPane();
        root.setTop(menuBar);
        root.setCenter(tabPane);

        Scene scene = new Scene(root, 1000, 700);
        primaryStage.setScene(scene);
        primaryStage.show();

        refreshContactTable();
        refreshTaskTable();
        refreshAppointmentTable();
    }

    private BorderPane buildTasksPane() {
        BorderPane pane = new BorderPane();

        TableColumn<model.Task, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("taskId"));
        TableColumn<model.Task, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("taskName"));
        TableColumn<model.Task, String> descCol = new TableColumn<>("Description");
        descCol.setCellValueFactory(new PropertyValueFactory<>("taskDescription"));

        taskTable.getColumns().setAll(idCol, nameCol, descCol);
        taskTable.setItems(taskData);

        Button addBtn = new Button("Add");
        Button editBtn = new Button("Edit");
        Button delBtn = new Button("Delete");

        addBtn.setOnAction(e -> {
            TaskDialog dialog = new TaskDialog();
            dialog.showAndWait().ifPresent(t -> {
                taskService.createTask(t.getTaskName(), t.getTaskDescription());
                refreshTaskTable();
                markDirty();
            });
        });

        editBtn.setOnAction(e -> {
            model.Task selected = taskTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                TaskDialog dialog = new TaskDialog(selected);
                dialog.showAndWait().ifPresent(t -> {
                    taskService.updateTaskName(t.getTaskName(), selected.getTaskId());
                    taskService.updateTaskDescription(t.getTaskDescription(), selected.getTaskId());
                    refreshTaskTable();
                    markDirty();
                });
            }
        });

        delBtn.setOnAction(e -> {
            model.Task selected = taskTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                taskService.deleteTask(selected.getTaskId());
                refreshTaskTable();
                markDirty();
            }
        });

        HBox controls = new HBox(10, addBtn, editBtn, delBtn);
        controls.setStyle("-fx-padding:10;");

        pane.setCenter(taskTable);
        pane.setBottom(controls);
        return pane;
    }

    private BorderPane buildAppointmentsPane() {
        BorderPane pane = new BorderPane();

        TableColumn<model.Appointment, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        TableColumn<model.Appointment, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("appointmentDate"));
        TableColumn<model.Appointment, String> descCol = new TableColumn<>("Description");
        descCol.setCellValueFactory(new PropertyValueFactory<>("appointmentDesc"));

        appointmentTable.getColumns().setAll(idCol, dateCol, descCol);
        appointmentTable.setItems(appointmentData);

        Button addBtn = new Button("Add");
        Button editBtn = new Button("Edit");
        Button delBtn = new Button("Delete");

        addBtn.setOnAction(e -> {
            AppointmentDialog dialog = new AppointmentDialog();
            dialog.showAndWait().ifPresent(a -> {
                appointmentService.createAppointment(a.getAppointmentDate(), a.getAppointmentDesc());
                refreshAppointmentTable();
                markDirty();
            });
        });

        editBtn.setOnAction(e -> {
            model.Appointment selected = appointmentTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                AppointmentDialog dialog = new AppointmentDialog(selected);
                dialog.showAndWait().ifPresent(a -> {
                    appointmentService.updateAppointmentDate(a.getAppointmentDate(), selected.getAppointmentId());
                    appointmentService.updateAppointmentDesc(a.getAppointmentDesc(), selected.getAppointmentId());
                    refreshAppointmentTable();
                    markDirty();
                });
            }
        });

        delBtn.setOnAction(e -> {
            model.Appointment selected = appointmentTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                appointmentService.deleteAppointment(selected.getAppointmentId());
                refreshAppointmentTable();
                markDirty();
            }
        });

        HBox controls = new HBox(10, addBtn, editBtn, delBtn);
        controls.setStyle("-fx-padding:10;");

        pane.setCenter(appointmentTable);
        pane.setBottom(controls);
        return pane;
    }
    private BorderPane buildContactsPane() {
        BorderPane pane = new BorderPane();

        TableColumn<Contact, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("contactId"));
        TableColumn<Contact, String> firstCol = new TableColumn<>("First");
        firstCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        TableColumn<Contact, String> lastCol = new TableColumn<>("Last");
        lastCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        TableColumn<Contact, String> phoneCol = new TableColumn<>("Phone");
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        TableColumn<Contact, String> addrCol = new TableColumn<>("Address");
        addrCol.setCellValueFactory(new PropertyValueFactory<>("address"));

        contactTable.getColumns().setAll(idCol, firstCol, lastCol, phoneCol, addrCol);
        contactTable.setItems(contactData);

        Button addBtn = new Button("Add");
        Button editBtn = new Button("Edit");
        Button delBtn = new Button("Delete");

        addBtn.setOnAction(e -> {
            ContactDialog dialog = new ContactDialog();
            dialog.showAndWait().ifPresent(c -> {
                contactService.createContact(c.getFirstName(), c.getLastName(), c.getPhone(), c.getAddress());
                refreshContactTable();
                markDirty();
            });
        });

        editBtn.setOnAction(e -> {
            Contact selected = contactTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                ContactDialog dialog = new ContactDialog(selected);
                dialog.showAndWait().ifPresent(c -> {
                    contactService.updateFirstName(c.getFirstName(), selected.getContactId());
                    contactService.updateLastName(c.getLastName(), selected.getContactId());
                    contactService.updatePhone(c.getPhone(), selected.getContactId());
                    contactService.updateAddress(c.getAddress(), selected.getContactId());
                    refreshContactTable();
                    markDirty();
                });
            }
        });

        delBtn.setOnAction(e -> {
            Contact selected = contactTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                contactService.deleteContact(selected.getContactId());
                refreshContactTable();
                markDirty();
            }
        });

        HBox controls = new HBox(10, addBtn, editBtn, delBtn);
        controls.setStyle("-fx-padding:10;");

        pane.setCenter(contactTable);
        pane.setBottom(controls);
        return pane;
    }

    private void refreshContactTable() {
        contactData.setAll(contactService.getAllContacts());
    }

    private void refreshTaskTable() {
        taskData.setAll(taskService.getAllTasks());
    }

    private void refreshAppointmentTable() {
        appointmentData.setAll(appointmentService.getAllAppointments());
    }

    private void openFile(Stage owner) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Open Data File");
        File f = chooser.showOpenDialog(owner);
        if (f != null) {
            try {
                PersistenceManager.load(f, contactService, taskService, appointmentService);
                currentFile = f;
                refreshContactTable();
                refreshTaskTable();
                refreshAppointmentTable();
                dirty = false;
                showInfo("Load complete", "Data loaded successfully from: " + f.getName());
            } catch (IOException ex) {
                showError("Load failed", ex.getMessage());
            }
        }
    }

    private void saveFile(Stage owner, boolean saveAs) {
        try {
            if (currentFile == null || saveAs) {
                FileChooser chooser = new FileChooser();
                chooser.setTitle("Save Data File");
                File f = chooser.showSaveDialog(owner);
                if (f != null) {
                    currentFile = f;
                } else {
                    return;
                }
            }
            PersistenceManager.save(currentFile, contactService, taskService, appointmentService);
            dirty = false;
            showInfo("Save complete", "Data saved to: " + currentFile.getName());
        } catch (IOException ex) {
            showError("Save failed", ex.getMessage());
        }
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR, message, ButtonType.OK);
        alert.setTitle(title);
        alert.showAndWait();
    }

    private void showInfo(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION, message, ButtonType.OK);
        alert.setTitle(title);
        alert.showAndWait();
    }



    private void handleCloseRequest(WindowEvent e) {
        if (!dirty) return;
        Alert alert = new Alert(AlertType.CONFIRMATION, "You have unsaved changes. Save before exit?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
        alert.setTitle("Unsaved changes");
        alert.showAndWait().ifPresent(resp -> {
            if (resp == ButtonType.YES) {
                saveFile(primaryStage, false);
            } else if (resp == ButtonType.CANCEL) {
                e.consume();
            }
        });
    }

    private void markDirty() {
        dirty = true;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
