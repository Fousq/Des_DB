package kz.enu.students.mapper;

import kz.enu.students.Student;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class StudentSqlRowSetMapper {

    public Student mapRow(SqlRowSet sqlRowSet) {
        Student student = new Student();
        student.setId(sqlRowSet.getInt("Код"));
        student.setFirstName(sqlRowSet.getString("Имя"));
        student.setLastName(sqlRowSet.getString("Фамилия"));
        student.setMiddleName(sqlRowSet.getString("Очество"));
        student.setStudyGroup(sqlRowSet.getString("Учебная группа"));
        student.setPhone(sqlRowSet.getString("Телефон"));
        student.setCountry(sqlRowSet.getString("Страна"));
        student.setCity(sqlRowSet.getString("Город"));
        student.setAddress(sqlRowSet.getString("Адрес"));
        student.setBirthday(sqlRowSet.getDate("Дата рождения").toLocalDate());
        student.setGender(sqlRowSet.getBoolean("Пол") ? "Мужчина" : "Женщина");
        student.setCNAverageScore(sqlRowSet.getDouble("Компьютерные сети"));
        student.setNMAverageScore(sqlRowSet.getDouble("Числовые методы"));
        student.setMMAverageScore(sqlRowSet.getDouble("Математические методы"));
        return student;
    }
}
