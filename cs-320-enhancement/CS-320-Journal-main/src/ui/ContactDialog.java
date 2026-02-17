package ui;

import javafx.geometry.Insets;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import model.Contact;

public class ContactDialog extends Dialog<Contact> {

    private TextField firstNameField = new TextField();
    private TextField lastNameField = new TextField();
    private TextField phoneField = new TextField();
    private TextField addressField = new TextField();

    public ContactDialog() {
        this(null);
    }

    public ContactDialog(Contact contact) {
        setTitle(contact == null ? "Add Contact" : "Edit Contact");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        grid.add(new Label("First Name:"), 0, 0);
        grid.add(firstNameField, 1, 0);
        grid.add(new Label("Last Name:"), 0, 1);
        grid.add(lastNameField, 1, 1);
        grid.add(new Label("Phone (10 digits):"), 0, 2);
        grid.add(phoneField, 1, 2);
        grid.add(new Label("Address:"), 0, 3);
        grid.add(addressField, 1, 3);

        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red;");
        grid.add(errorLabel, 1, 4);

        if (contact != null) {
            firstNameField.setText(contact.getFirstName());
            lastNameField.setText(contact.getLastName());
            phoneField.setText(contact.getPhone());
            addressField.setText(contact.getAddress());
        }

        getDialogPane().setContent(grid);
        getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        Node okButton = getDialogPane().lookupButton(ButtonType.OK);
        okButton.addEventFilter(ActionEvent.ACTION, event -> {
            try {
                new Contact(firstNameField.getText(), lastNameField.getText(), phoneField.getText(), addressField.getText());
                errorLabel.setText("");
            } catch (IllegalArgumentException ex) {
                event.consume();
                errorLabel.setText(ex.getMessage());
            }
        });

        setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                return new Contact(firstNameField.getText(), lastNameField.getText(), phoneField.getText(),
                        addressField.getText());
            }
            return null;
        });
    }
}
