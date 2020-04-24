package com.example.adress.app.utils.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalDate;

/**
 * Адаптер (для JAXB) для преобразования между типом LocalDate и строковым
 * представлением даты в стандарте ISO 8601, например как '2012-12-03'.
 */
public class LocalDateAdapter extends XmlAdapter<String, LocalDate> {

    @Override
    public LocalDate unmarshal(String v) {
	return LocalDate.parse(v);
    }

    @Override
    public String marshal(LocalDate v) {
	return v.toString();
    }
}