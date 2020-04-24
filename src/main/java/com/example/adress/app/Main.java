package com.example.adress.app;

import com.example.adress.app.controller.BirthdayStatisticController;
import com.example.adress.app.controller.PersonEditDialogController;
import com.example.adress.app.controller.PersonOverviewController;
import com.example.adress.app.controller.RootLayoutController;
import com.example.adress.app.domain.Person;
import com.example.adress.app.domain.PersonListWrapper;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;
import java.util.prefs.Preferences;

import static com.example.adress.app.utils.FxmlConstants.*;

public class Main extends Application {

    private Stage primaryStage;

    private BorderPane rootLayout;
    /**
     * Данные, в виде наблюдаемого списка адресатов.
     */
    private ObservableList<Person> personData = FXCollections.observableArrayList();

    /**
     * Конструктор
     */
    public Main() {
	// В качестве образца добавляем некоторые данные
	personData.add(new Person("Hans", "Muster"));
	personData.add(new Person("Ruth", "Mueller"));
	personData.add(new Person("Heinz", "Kurz"));
	personData.add(new Person("Cornelia", "Meier"));
	personData.add(new Person("Werner", "Meyer"));
	personData.add(new Person("Lydia", "Kunz"));
	personData.add(new Person("Anna", "Best"));
	personData.add(new Person("Stefan", "Meier"));
	personData.add(new Person("Martin", "Mueller"));
    }

    @Override
    public void start(Stage primaryStage) {
	this.primaryStage = primaryStage;
	this.primaryStage.setTitle("AddressApp");
	this.primaryStage.getIcons().add(new Image(ICON));
	initRootLayout();

	showPersonOverview();
    }

    /**
     * Инициализирует корневой макет и пытается загрузить последний открытый
     * файл с адресатами.
     */
    private void initRootLayout() {
	try {
	    // Загружаем корневой макет из fxml файла.
	    FXMLLoader loader = new FXMLLoader();
	    loader.setLocation(Main.class.getResource(ROOT_LAYOUT));
	    rootLayout = (BorderPane) loader.load();

	    // Отображаем сцену, содержащую корневой макет.
	    Scene scene = new Scene(rootLayout);
	    primaryStage.setScene(scene);

	    // Даём контроллеру доступ к главному прилодению.
	    RootLayoutController controller = loader.getController();
	    controller.setMainApp(this);

	    primaryStage.show();
	} catch (IOException e) {
	    e.printStackTrace();
	}

	// Пытается загрузить последний открытый файл с адресатами.
	File file = getPersonFilePath();
	if (file != null) {
	    loadPersonDataFromFile(file);
	}
    }

    /**
     * Показывает в корневом макете сведения об адресатах
     */
    private void showPersonOverview() {
	try {
	    FXMLLoader loader = new FXMLLoader();
	    loader.setLocation(Main.class.getResource(PERSON_OVERVIEW));
	    AnchorPane personOverview = (AnchorPane) loader.load();
	    // Помещаем сведения об адресатах в центр корневого макета.
	    rootLayout.setCenter(personOverview);
	    // Даём контроллеру доступ к главному приложению.
	    PersonOverviewController controller = loader.getController();
	    controller.setMainApp(this);
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    /**
     * Открывает диалоговое окно для изменения деталей указанного адресата.
     * Если пользователь кликнул OK, то изменения сохраняются в предоставленном
     * объекте адресата и возвращается значение true.
     *
     * @param person - объект адресата, который надо изменить
     * @return true, если пользователь кликнул OK, в противном случае false.
     */
    public boolean showPersonEditDialog(Person person) {
	try {
	    // Загружаем fxml-файл и создаём новую сцену
	    // для всплывающего диалогового окна.
	    FXMLLoader loader = new FXMLLoader();
	    loader.setLocation(Main.class.getResource(PERSON_EDIT));
	    AnchorPane page = (AnchorPane) loader.load();

	    // Создаём диалоговое окно Stage.
	    Stage dialogStage = new Stage();
	    dialogStage.setTitle("Edit Person");
	    dialogStage.initModality(Modality.WINDOW_MODAL);
	    dialogStage.initOwner(primaryStage);
	    Scene scene = new Scene(page);
	    dialogStage.setScene(scene);

	    // Передаём адресата в контроллер.
	    PersonEditDialogController controller = loader.getController();
	    controller.setDialogStage(dialogStage);
	    controller.setPerson(person);
	    dialogStage.getIcons().add(new Image(ICON));
	    // Отображаем диалоговое окно и ждём, пока пользователь его не закроет
	    dialogStage.showAndWait();

	    return controller.isOkClicked();
	} catch (IOException e) {
	    e.printStackTrace();
	    return false;
	}
    }

    /**
     * Открывает диалоговое окно для вывода статистики дней рождений.
     */
    public void showBirthdayStatistics() {
	try {
	    // Загружает fxml-файл и создаёт новую сцену для всплывающего окна.
	    FXMLLoader loader = new FXMLLoader();
	    loader.setLocation(Main.class.getResource(BIRTHDAY_STATISTIC));
	    AnchorPane page = (AnchorPane) loader.load();
	    Stage dialogStage = new Stage();
	    dialogStage.setTitle("Birthday Statistics");
	    dialogStage.initModality(Modality.WINDOW_MODAL);
	    dialogStage.initOwner(primaryStage);
	    Scene scene = new Scene(page);
	    dialogStage.setScene(scene);

	    // Передаёт адресатов в контроллер.
	    BirthdayStatisticController controller = loader.getController();
	    controller.setPersonData(personData);
	    dialogStage.getIcons().add(new Image(ICON));
	    dialogStage.show();

	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    /**
     * Возвращает preference файла адресатов, то есть, последний открытый файл.
     * Этот preference считывается из реестра, специфичного для конкретной
     * операционной системы. Если preference не был найден, то возвращается null.
     */
    public File getPersonFilePath() {
	Preferences prefs = Preferences.userNodeForPackage(Main.class);
	String filePath = prefs.get("filePath", null);
	if (filePath != null) {
	    return new File(filePath);
	} else {
	    return null;
	}
    }

    /**
     * Задаёт путь текущему загруженному файлу. Этот путь сохраняется
     * в реестре, специфичном для конкретной операционной системы.
     *
     * @param file - файл или null, чтобы удалить путь
     */
    public void setPersonFilePath(File file) {
	Preferences prefs = Preferences.userNodeForPackage(Main.class);
	if (file != null) {
	    prefs.put("filePath", file.getPath());

	    // Обновление заглавия сцены.
	    primaryStage.setTitle("AddressApp - " + file.getName());
	} else {
	    prefs.remove("filePath");

	    // Обновление заглавия сцены.
	    primaryStage.setTitle("AddressApp");
	}
    }

    /**
     * Загружает информацию об адресатах из указанного файла.
     * Текущая информация об адресатах будет заменена.
     */
    public void loadPersonDataFromFile(File file) {
	try {
	    JAXBContext context = JAXBContext.newInstance(PersonListWrapper.class);
	    Unmarshaller um = context.createUnmarshaller();

	    // Чтение XML из файла и демаршализация.
	    PersonListWrapper wrapper = (PersonListWrapper) um.unmarshal(file);

	    personData.clear();
	    personData.addAll(wrapper.getPersons());

	    // Сохраняем путь к файлу в реестре.
	    setPersonFilePath(file);

	} catch (Exception e) { // catches ANY exception
	    Alert alert = new Alert(Alert.AlertType.ERROR);
	    alert.setTitle("Error");
	    alert.setHeaderText("Could not load data");
	    alert.setContentText("Could not load data from file:\n" + file.getPath());

	    alert.showAndWait();
	}
    }

    /**
     * Сохраняет текущую информацию об адресатах в указанном файле.
     */
    public void savePersonDataToFile(File file) {
	try {
	    JAXBContext context = JAXBContext.newInstance(PersonListWrapper.class);
	    Marshaller m = context.createMarshaller();
	    m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

	    // Обёртываем наши данные об адресатах.
	    PersonListWrapper wrapper = new PersonListWrapper();
	    wrapper.setPersons(personData);

	    // Маршаллируем и сохраняем XML в файл.
	    m.marshal(wrapper, file);

	    // Сохраняем путь к файлу в реестре.
	    setPersonFilePath(file);
	} catch (Exception e) { // catches ANY exception
	    Alert alert = new Alert(Alert.AlertType.ERROR);
	    alert.setTitle("Error");
	    alert.setHeaderText("Could not save data");
	    alert.setContentText("Could not save data to file:\n" + file.getPath());

	    alert.showAndWait();
	}
    }

    /**
     * Возвращает главную сцену.
     *
     * @return Сцена
     */
    public Stage getPrimaryStage() {
	return primaryStage;
    }

    /**
     * Возвращает данные в виде наблюдаемого списка адресатов.
     *
     * @return список адресатов
     */
    public ObservableList<Person> getPersonData() {
	return personData;
    }

    public static void main(String[] args) {
	launch(args);
    }
}
