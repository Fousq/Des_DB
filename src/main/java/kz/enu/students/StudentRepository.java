package kz.enu.students;

import kz.enu.students.mapper.StudentRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class StudentRepository {
    private static final String SELECT_STUDENTS_QUERY =
            "SELECT student.id as \"Код\", student.first_name as \"Имя\", student.last_name as \"Фамилия\", " +
            "student.middle_name as \"Очество\", student.study_group as \"Учебная группа\", " +
            "studentinfo.phone as \"Телефон\", studentinfo.country as \"Страна\", " +
            "studentinfo.city as \"Город\", studentinfo.address as \"Адрес\", " +
            "studentinfo.birthday as \"Дата рождения\", studentinfo.gender as \"Пол\", " +
            "AVG(studentscore.computer_network) as \"Компьютерные сети\", " +
            "AVG(studentscore.numeric_methods) as \"Числовые методы\", " +
            "AVG(studentscore.mathematics_methods) as \"Математические методы\" FROM student " +
            "INNER JOIN studentInfo ON student.id = studentInfo.student_id " +
            "INNER JOIN studentScore ON student.id = studentScore.student_id " +
            "GROUP BY student.id";
    private StudentRowMapper studentRowMapper;
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public StudentRepository(DataSource dataSource, StudentRowMapper studentRowMapper) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        this.studentRowMapper = studentRowMapper;
    }

    public List<Student> getStudents() {
        return jdbcTemplate.query(SELECT_STUDENTS_QUERY, studentRowMapper);
    }

    public SqlRowSet getCursorOnStudents() {
        return jdbcTemplate.queryForRowSet(SELECT_STUDENTS_QUERY);
    }
}
