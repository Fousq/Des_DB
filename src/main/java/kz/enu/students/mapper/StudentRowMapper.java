package kz.enu.students.mapper;

import kz.enu.students.Student;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class StudentRowMapper implements RowMapper<Student> {
    @Override
    public Student mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Student student = new Student();
        student.setId(resultSet.getInt("Код"));
        student.setFirstName(resultSet.getString("Имя"));
        student.setLastName(resultSet.getString("Фамилия"));
        student.setMiddleName(resultSet.getString("Очество"));
        student.setStudyGroup(resultSet.getString("Учебная группа"));
        student.setPhone(resultSet.getString("Телефон"));
        student.setCountry(resultSet.getString("Страна"));
        student.setCity(resultSet.getString("Город"));
        student.setAddress(resultSet.getString("Адрес"));
        student.setBirthday(resultSet.getDate("Дата рождения").toLocalDate());
        student.setGender(resultSet.getBoolean("Пол") ? "Мужчина" : "Женщина");
        student.setCNAverageScore(resultSet.getDouble("Компьютерные сети"));
        student.setNMAverageScore(resultSet.getDouble("Числовые методы"));
        student.setMMAverageScore(resultSet.getDouble("Математические методы"));
        return student;
    }
}
