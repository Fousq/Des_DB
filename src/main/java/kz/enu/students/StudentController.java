package kz.enu.students;

import kz.enu.students.mapper.StudentRowMapper;
import kz.enu.students.mapper.StudentSqlRowSetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/")
@Controller
public class StudentController {
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private StudentSqlRowSetMapper studentSqlRowSetMapper;

    @GetMapping
    public String students(Model model) {
        model.addAttribute("students", studentRepository.getStudents());
        return "index";
    }

    @GetMapping("/select")
    public String selectStudent(Model model, @RequestParam(defaultValue = "1") int position) {
        SqlRowSet cursorOnStudents = studentRepository.getCursorOnStudents();
        boolean isOnRow = cursorOnStudents.absolute(position);
        boolean isNotFound = false;
        if (isOnRow) {
            model.addAttribute("student", studentSqlRowSetMapper.mapRow(cursorOnStudents));
        } else {
            model.addAttribute("errorMsg", "Cannot found student beyond the list");
            isNotFound = true;
        }
        model.addAttribute("isNotFound", isNotFound);
        model.addAttribute("isFirst", cursorOnStudents.isFirst());
        model.addAttribute("isLast", cursorOnStudents.isLast());
        model.addAttribute("previous", position - 1);
        model.addAttribute("next", position + 1);
        return "selectStudents";
    }
}
