package kz.enu.students;

import kz.enu.students.mapper.StudentMapper;
import kz.enu.students.mapper.StudentRowMapper;
import kz.enu.students.model.StudentUpdateDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Repository
public class StudentRepository {
    private static final String GROUP_BY_STUDENT_ID = "GROUP BY student.id";
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
            GROUP_BY_STUDENT_ID;
    private static final String SELECT_STUDENTS_BY_GENDER_QUERY =
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
                    "WHERE studentinfo.gender = :studentGender " +
                    GROUP_BY_STUDENT_ID;
    private static final String GET_STUDENT_BY_ID_PROCEDURE_NAME = "get_student";
    private static final String UPDATE_STUDENT_PROCEDURE_NAME = "update_student";
    private static final String DELETE_STUDENT_SCORES = "DELETE FROM studentscore WHERE student_id = :studentId";
    private static final String DELETE_STUDENT_INFO = "DELETE FROM studentinfo WHERE student_id = :studentId";
    private static final String DELETE_STUDENT = "DELETE FROM student WHERE id = :studentId";
    private StudentRowMapper studentRowMapper;
    private JdbcTemplate jdbcTemplate;
    private NamedParameterJdbcTemplate parameterJdbcTemplate;
    private SimpleJdbcCall jdbcCall;

    @Autowired
    public StudentRepository(DataSource dataSource, StudentRowMapper studentRowMapper) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        this.studentRowMapper = studentRowMapper;
        parameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
    }

    public List<Student> getStudents(Integer filterSetId, Boolean isMaleSelected, Boolean asc) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        switch (FilterSetType.getFilterTypeById(filterSetId)) {
            case NO_FILTER:
                return jdbcTemplate.query(SELECT_STUDENTS_QUERY, studentRowMapper);
            case FILTER_BY_GENDER:
                Objects.requireNonNull(isMaleSelected);
                parameters.addValue("studentGender", isMaleSelected);
                return parameterJdbcTemplate.query(SELECT_STUDENTS_BY_GENDER_QUERY, parameters, studentRowMapper);
            case FILTER_SORTED:
                Objects.requireNonNull(asc);
                String sortStudentQuery = SELECT_STUDENTS_QUERY.concat(asc ?
                        " ORDER BY student.last_name ASC, student.first_name ASC" :
                        " ORDER BY student.last_name DESC, student.first_name DESC");
                return jdbcTemplate.query(sortStudentQuery, studentRowMapper);
            case FILTER_BY_GENDER_SORTED:
                Objects.requireNonNull(isMaleSelected);
                Objects.requireNonNull(asc);
                parameters.addValue("studentGender", isMaleSelected);
                String sortStudentByGenderQuery = SELECT_STUDENTS_BY_GENDER_QUERY.concat(asc ?
                        " ORDER BY student.last_name ASC, student.first_name ASC" :
                        " ORDER BY student.last_name DESC, student.first_name DESC");
                return parameterJdbcTemplate.query(sortStudentByGenderQuery, parameters, studentRowMapper);
            default:
                return Collections.emptyList();
        }
    }

    public SqlRowSet getCursorOnStudents() {
        return jdbcTemplate.queryForRowSet(SELECT_STUDENTS_QUERY);
    }

    public Student getStudent(long studentId) {
        jdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName(GET_STUDENT_BY_ID_PROCEDURE_NAME);
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("student_id", studentId);
        Map<String, Object> objectMap = (Map<String, Object>) ((List) jdbcCall.execute(parameters).get("#result-set-1")).get(0);
        return StudentMapper.map(objectMap);
    }

    public void update(StudentUpdateDto student) {
        jdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName(UPDATE_STUDENT_PROCEDURE_NAME);
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("studentId", student.getId());
        parameters.addValue("student_first_name", student.getFirstName());
        parameters.addValue("student_last_name", student.getLastName());
        parameters.addValue("student_middle_name", student.getMiddleName());
        parameters.addValue("student_study_group", student.getStudyGroup());
        parameters.addValue("student_phone", student.getPhone());
        parameters.addValue("student_country", student.getCountry());
        parameters.addValue("student_city", student.getCity());
        parameters.addValue("student_address", student.getAddress());
        parameters.addValue("student_gender", Objects.equals(student.getGender(), "M"));
        jdbcCall.execute(parameters);
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
