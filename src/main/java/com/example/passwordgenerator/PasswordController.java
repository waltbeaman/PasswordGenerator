package com.example.passwordgenerator;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

import java.security.SecureRandom;
import java.util.List;
import java.util.Random;

public class PasswordController {
    @FXML
    private Button generatePasswordButton;
    @FXML
    private Button copyPasswordButton;
    @FXML
    private Label generatedPasswordText;
    @FXML
    private Label generatedPasswordLabel;
    @FXML
    private ComboBox<Integer> lengthComboBox;
    @FXML
    private CheckBox includeUppercaseCheckBox;
    @FXML
    private CheckBox includeLowercaseCheckBox;
    @FXML
    private CheckBox includeNumbersCheckBox;
    @FXML
    private CheckBox includeSpecialsCheckBox;
    @FXML
    private Label errorLabel;

    @FXML
    public void initialize() {
        setInitialValues();
        setUpEventListeners();
    }

    private void setInitialValues() {
        // Set up password length options
        ObservableList<Integer> passwordLengthOptions = FXCollections.observableArrayList(6, 8, 10, 12, 14, 16, 20, 24);
        lengthComboBox.setItems(passwordLengthOptions);
        lengthComboBox.getSelectionModel().selectFirst(); // Set default password length
        generatedPasswordLabel.setVisible(false);
        // Disable Copy to Clipboard button until password has been generated
        copyPasswordButton.setDisable(true);
    }

    private void setUpEventListeners() {
        // Enable the Generate Password button only if at least one option is selected
        generatePasswordButton.disableProperty().bind(
                includeUppercaseCheckBox.selectedProperty().not()
                        .and(includeLowercaseCheckBox.selectedProperty().not())
                        .and(includeNumbersCheckBox.selectedProperty().not())
                        .and(includeSpecialsCheckBox.selectedProperty().not())
        );
    }

    @FXML
    public void handleGenerateButtonClicked() {
        // Generate the password based on user's preferences
        int passwordLength = lengthComboBox.getValue();
        String password = generatePassword(passwordLength, includeUppercaseCheckBox.isSelected(),
                includeLowercaseCheckBox.isSelected(), includeNumbersCheckBox.isSelected(),
                includeSpecialsCheckBox.isSelected());
        // Display the generated password in the password label
        generatedPasswordText.setText(password);
        generatedPasswordLabel.setVisible(true);
        copyPasswordButton.setDisable(false);
    }

    private String generatePassword(int length, boolean useUppercase, boolean useLowercase,
                                    boolean useNumbers, boolean useSpecialCharacters) {
        String uppercaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowercaseLetters = "abcdefghijklmnopqrstuvwxyz";
        String numbers = "0123456789";
        String specialCharacters = "!@#$%^&*-_=+|;:'\",./?`~\\";

        StringBuilder allowedCharacters = new StringBuilder();
        if (useUppercase) {
            allowedCharacters.append(uppercaseLetters);
        }
        if (useLowercase) {
            allowedCharacters.append(lowercaseLetters);
        }
        if (useNumbers) {
            allowedCharacters.append(numbers);
        }
        if (useSpecialCharacters) {
            allowedCharacters.append(specialCharacters);
        }

        StringBuilder password = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(allowedCharacters.length());
            password.append(allowedCharacters.charAt(randomIndex));
        }

        return password.toString();
    }

    @FXML
    public void handleCopyButtonClicked() {
        String password = generatedPasswordText.getText();
        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();
        content.putString(password);
        clipboard.setContent(content);
    }

}