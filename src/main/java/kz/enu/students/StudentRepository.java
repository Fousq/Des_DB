package kz.enu.students;

import kz.enu.students.mapper.StudentRowMapper;
import kz.enu.students.model.StudentUpdateDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.Date;
import java.util.List;
import java.util.Objects;

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
    private static final String SELECT_STUDENT_BY_ID = "SELECT student.id as \"Код\", student.first_name as \"Имя\", student.last_name as \"Фамилия\", " +
            "student.middle_name as \"Очество\", student.study_group as \"Учебная группа\", " +
            "studentinfo.phone as \"Телефон\", studentinfo.country as \"Страна\", " +
            "studentinfo.city as \"Город\", studentinfo.address as \"Адрес\", " +
            "studentinfo.birthday as \"Дата рождения\", studentinfo.gender as \"Пол\", " +
            "AVG(studentscore.computer_network) as \"Компьютерные сети\", " +
            "AVG(studentscore.numeric_methods) as \"Числовые методы\", " +
            "AVG(studentscore.mathematics_methods) as \"Математические методы\" FROM student " +
            "INNER JOIN studentInfo ON student.id = studentInfo.student_id " +
            "INNER JOIN studentScore ON student.id = studentScore.student_id " +
            "WHERE student.id = :studentId " +
            "GROUP BY student.id";
    private static final String UPDATE_STUDENT_QUERY = "UPDATE student SET first_name = :studentFirstName, " +
            "last_name = :studentLastName, middle_name = :studentMiddleName, " +
            "study_group = :studentStudyGroup WHERE id = :studentId";
    private static final String UPDATE_STUDENT_INFO_QUERY = "UPDATE studentinfo SET phone = :studentPhone, " +
            "country = :studentCountry, city = :studentCity, address = :studentAddress, " +
            "gender = :studentGender " +
            "WHERE student_id = :studentId";
    private static final String DELETE_STUDENT_SCORES = "DELETE FROM studentscore WHERE student_id = :studentId";
    private static final String DELETE_STUDENT_INFO = "DELETE FROM studentinfo WHERE student_id = :studentId";
    private static final String DELETE_STUDENT = "DELETE FROM student WHERE id = :studentId";
    private StudentRowMapper studentRowMapper;
    private JdbcTemplate jdbcTemplate;
    private NamedParameterJdbcTemplate parameterJdbcTemplate;

    @Autowired
    public StudentRepository(DataSource dataSource, StudentRowMapper studentRowMapper) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        this.studentRowMapper = studentRowMapper;
        parameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
    }

    public List<Student> getStudents() {
        return jdbcTemplate.query(SELECT_STUDENTS_QUERY, studentRowMapper);
    }

    public SqlRowSet getCursorOnStudents() {
        return jdbcTemplate.queryForRowSet(SELECT_STUDENTS_QUERY);
    }

    public Student getStudent(long studentId) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("studentId", studentId);
        return parameterJdbcTemplate.queryForObject(SELECT_STUDENT_BY_ID, parameters, studentRowMapper);
    }

    @Transactional
    public void update(StudentUpdateDto student) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("studentId", student.getId());
        parameters.addValue("studentFirstName", student.getFirstName());
        parameters.addValue("studentLastName", student.getLastName());
        parameters.addValue("studentMiddleName", student.getMiddleName());
        parameters.addValue("studentStudyGroup", student.getStudyGroup());
        parameters.addValue("studentPhone", student.getPhone());
        parameters.addValue("studentCountry", student.getCountry());
        parameters.addValue("studentCity", student.getCity());
        parameters.addValue("studentAddress", student.getAddress());
        parameters.addValue("studentGender", Objects.equals(student.getGender(), "M"));
        parameterJdbcTemplate.update(UPDATE_STUDENT_QUERY, parameters);
        parameterJdbcTemplate.update(UPDATE_STUDENT_INFO_QUERY, parameters);
    }

    @Transactional
    public void delete(Long studentId) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("studentId", studentId);
        parameterJdbcTemplate.update(DELETE_STUDENT_SCORES, parameters);
        parameterJdbcTemplate.update(DELETE_STUDENT_INFO, parameters);
        parameterJdbcTemplate.update(DELETE_STUDENT, parameters);
    }
}
