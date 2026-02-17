package ui;

import java.time.LocalDate;

import javafx.geometry.Insets;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import model.Appointment;

public class AppointmentDialog extends Dialog<Appointment> {

    private DatePicker datePicker = new DatePicker(LocalDate.now());
    private TextField descField = new TextField();

    public AppointmentDialog() {
        this(null);
    }

    public AppointmentDialog(Appointment appt) {
        setTitle(appt == null ? "Add Appointment" : "Edit Appointment");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        grid.add(new Label("Date:"), 0, 0);
        grid.add(datePicker, 1, 0);
        grid.add(new Label("Description:"), 0, 1);
        grid.add(descField, 1, 1);

        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red;");
        grid.add(errorLabel, 1, 2);

        if (appt != null) {
            datePicker.setValue(LocalDate.parse(appt.getAppointmentDate()));
            descField.setText(appt.getAppointmentDesc());
        }

        getDialogPane().setContent(grid);
        getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        Node okButton = getDialogPane().lookupButton(ButtonType.OK);
        okButton.addEventFilter(ActionEvent.ACTION, event -> {
            try {
                LocalDate d = datePicker.getValue();
                String dateStr = d.toString();
                new Appointment(dateStr, descField.getText());
                errorLabel.setText("");
            } catch (IllegalArgumentException ex) {
                event.consume();
                errorLabel.setText(ex.getMessage());
            }
        });

        setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                LocalDate d = datePicker.getValue();
                String dateStr = d.toString();
                return new Appointment(dateStr, descField.getText());
            }
            return null;
        });
    }
}
