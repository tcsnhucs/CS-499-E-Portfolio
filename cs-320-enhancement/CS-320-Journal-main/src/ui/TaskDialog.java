package ui;

import javafx.geometry.Insets;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import model.Task;

public class TaskDialog extends Dialog<Task> {

    private TextField nameField = new TextField();
    private TextArea descArea = new TextArea();

    public TaskDialog() {
        this(null);
    }

    public TaskDialog(Task task) {
        setTitle(task == null ? "Add Task" : "Edit Task");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        grid.add(new Label("Task Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Description:"), 0, 1);
        descArea.setPrefRowCount(4);
        grid.add(descArea, 1, 1);

        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red;");
        grid.add(errorLabel, 1, 2);

        if (task != null) {
            nameField.setText(task.getTaskName());
            descArea.setText(task.getTaskDescription());
        }

        getDialogPane().setContent(grid);
        getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        Node okButton = getDialogPane().lookupButton(ButtonType.OK);
        okButton.addEventFilter(ActionEvent.ACTION, event -> {
            try {
                new Task(nameField.getText(), descArea.getText());
                errorLabel.setText("");
            } catch (IllegalArgumentException ex) {
                event.consume();
                errorLabel.setText(ex.getMessage());
            }
        });

        setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                return new Task(nameField.getText(), descArea.getText());
            }
            return null;
        });
    }
}
