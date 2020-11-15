package kz.enu.students.mapper;

import kz.enu.students.Student;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Map;

public class StudentMapper {

    public static Student map(Map<String, Object> objectMap) {
        Student student = new Student();
        student.setId((Integer) objectMap.get("Код"));
        student.setFirstName((String) objectMap.get("Имя"));
        student.setLastName((String) objectMap.get("Фамилия"));
        student.setMiddleName((String) objectMap.get("Очество"));
        student.setStudyGroup((String) objectMap.get("Учебная группа"));
        student.setPhone((String) objectMap.get("Телефон"));
        student.setCountry((String) objectMap.get("Страна"));
        student.setCity((String) objectMap.get("Город"));
        student.setAddress((String) objectMap.get("Адрес"));
        student.setBirthday(((Date) objectMap.get("Дата рождения")).toLocalDate());
        student.setGender(((Boolean) objectMap.get("Пол")) ? "Мужчина" : "Женщина");
        student.setCNAverageScore(((BigDecimal) objectMap.get("Компьютерные сети")).doubleValue());
        student.setNMAverageScore(((BigDecimal) objectMap.get("Числовые методы")).doubleValue());
        student.setMMAverageScore(((BigDecimal) objectMap.get("Математические методы")).doubleValue());
        return student;
    }
}
