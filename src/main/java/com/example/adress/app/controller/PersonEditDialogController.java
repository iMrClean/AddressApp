package com.example.adress.app.controller;

import com.example.adress.app.domain.Person;
import com.example.adress.app.utils.DateUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Окно для изменения информации об адресате.
 */
public class PersonEditDialogController {

    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField streetField;
    @FXML
    private TextField postalCodeField;
    @FXML
    private TextField cityField;
    @FXML
    private TextField birthdayField;

    private Stage dialogStage;
    private Person person;
    private boolean okClicked = false;

    /**
     * Устанавливает сцену для этого окна.
     *
     * @param dialogStage
     */
    public void setDialogStage(Stage dialogStage) {
	this.dialogStage = dialogStage;
    }

    /**
     * Задаёт адресата, информацию о котором будем менять.
     *
     * @param person
     */
    public void setPerson(Person person) {
	this.person = person;

	firstNameField.setText(person.getFirstName());
	lastNameField.setText(person.getLastName());
	streetField.setText(person.getStreet());
	postalCodeField.setText(Integer.toString(person.getPostalCode()));
	cityField.setText(person.getCity());
	birthdayField.setText(DateUtil.format(person.getBirthday()));
	birthdayField.setPromptText("dd.mm.yyyy");
    }

    /**
     * Returns true, если пользователь кликнул OK, в другом случае false.
     *
     * @return
     */
    public boolean isOkClicked() {
	return okClicked;
    }

    /**
     * Вызывается, когда пользователь кликнул по кнопке OK.
     */
    @FXML
    private void handleOk() {
	if (isInputValid()) {
	    person.setFirstName(firstNameField.getText());
	    person.setLastName(lastNameField.getText());
	    person.setStreet(streetField.getText());
	    person.setPostalCode(Integer.parseInt(postalCodeField.getText()));
	    person.setCity(cityField.getText());
	    person.setBirthday(DateUtil.parse(birthdayField.getText()));

	    okClicked = true;
	    dialogStage.close();
	}
    }

    /**
     * Вызывается, когда пользователь кликнул по кнопке Cancel.
     */
    @FXML
    private void handleCancel() {
	dialogStage.close();
    }

    /**
     * Проверяет пользовательский ввод в текстовых полях.
     *
     * @return true, если пользовательский ввод корректен
     */
    private boolean isInputValid() {
	String errorMessage = "";

	if (firstNameField.getText() == null || firstNameField.getText().length() == 0) {
	    errorMessage += "Пустое поле имя!\n";
	}
	if (lastNameField.getText() == null || lastNameField.getText().length() == 0) {
	    errorMessage += "Пустое поле фамилия!!\n";
	}
	if (streetField.getText() == null || streetField.getText().length() == 0) {
	    errorMessage += "Пустое поле улица\n";
	}

	if (postalCodeField.getText() == null || postalCodeField.getText().length() == 0) {
	    errorMessage += "Пустое поле почтовый индекс\n";
	} else {
	    // пытаемся преобразовать почтовый код в int.
	    try {
		Integer.parseInt(postalCodeField.getText());
	    } catch (NumberFormatException e) {
		errorMessage += "Неверно заданно поле почтовый индекс!\n";
	    }
	}

	if (cityField.getText() == null || cityField.getText().length() == 0) {
	    errorMessage += "Пустое поле город!\n";
	}

	if (birthdayField.getText() == null || birthdayField.getText().length() == 0) {
	    errorMessage += "Пустое поле дата рождения!";
	} else {
	    if (!DateUtil.validDate(birthdayField.getText())) {
		errorMessage += "Неверно задано поле дата рождения! формат dd.mm.yyyy!\n";
	    }
	}

	if (errorMessage.length() == 0) {
	    return true;
	} else {
	    // Показываем сообщение об ошибке.
	    Alert alert = new Alert(AlertType.ERROR);
	    alert.initOwner(dialogStage);
	    alert.setTitle("Ошибка");
	    alert.setHeaderText("Неверно заданы поля");
	    alert.setContentText(errorMessage);

	    alert.showAndWait();

	    return false;
	}
    }
}