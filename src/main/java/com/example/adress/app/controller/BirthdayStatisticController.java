package com.example.adress.app.controller;

import com.example.adress.app.domain.Person;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.XYChart;

import java.util.Arrays;
import java.util.List;

/**
 * Контроллер для представления статистики дней рождений.
 */
public class BirthdayStatisticController {

    @FXML
    private BarChart<String, Integer> barChart;

    @FXML
    private CategoryAxis xAxis;

    private ObservableList<String> monthNames = FXCollections.observableArrayList();

    /**
     * Инициализирует класс-контроллер. Этот метод вызывается автоматически
     * после того, как fxml-файл был загружен.
     */
    @FXML
    private void initialize() {
	// Получаем массив с английскими именами месяцев.
	//	String[] months = DateFormatSymbols.getInstance(Locale.ENGLISH).getMonths();
	String[] months = { "январь", "февраль", "март", "апрель", "май", "июнь", "июль", "август", "сентябрь", "октябрь", "ноябрь", "декабрь" };
	// Преобразуем его в список и добавляем в наш ObservableList месяцев.
	monthNames.addAll(Arrays.asList(months));

	// Назначаем имена месяцев категориями для горизонтальной оси.
	xAxis.setCategories(monthNames);
    }

    /**
     * Задаёт адресатов, о которых будет показана статистика.
     *
     * @param persons
     */
    public void setPersonData(List<Person> persons) {
	// Считаем адресатов, имеющих дни рождения в указанном месяце.
	int[] monthCounter = new int[12];
	for (Person p : persons) {
	    int month = p.getBirthday().getMonthValue() - 1;
	    monthCounter[month]++;
	}

	XYChart.Series<String, Integer> series = new XYChart.Series<>();

	// Создаём объект XYChart.Data для каждого месяца.
	// Добавляем его в серии.
	for (int i = 0; i < monthCounter.length; i++) {
	    series.getData().add(new XYChart.Data<>(monthNames.get(i), monthCounter[i]));
	}

	barChart.getData().add(series);
    }
}