package kz.enu.students;

import kz.enu.students.mapper.StudentSqlRowSetMapper;
import kz.enu.students.model.StudentUpdateDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/")
@Controller
public class StudentController {
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private StudentSqlRowSetMapper studentSqlRowSetMapper;

    // modify to get filtered or sorted list
    @GetMapping
    public String students(Model model, @RequestParam(name = "filterSetId", defaultValue = "0") Integer filterSetId,
                           @RequestParam(name = "maleSelected", required = false) Boolean isMaleSelected,
                           @RequestParam(name = "asc", required = false) Boolean asc) {
        model.addAttribute("students", studentRepository.getStudents(filterSetId, isMaleSelected, asc));
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

    @GetMapping("/update")
    public String updateStudents(Model model) {
        model.addAttribute("students", studentRepository.getStudents(FilterSetType.NO_FILTER.getId(), true, true));
        return "updateStudentsTable";
    }

    @GetMapping("/update/student/{studentId}")
    public String updateStudent(Model model, @PathVariable("studentId") Long studentId) {
        model.addAttribute("student", studentRepository.getStudent(studentId));
        model.addAttribute("studentUpdate", new StudentUpdateDto());
        return "studentUpdate";
    }

    @PostMapping("/update/student")
    public String updateStudent(@ModelAttribute("student") StudentUpdateDto studentUpdate) {
        studentRepository.update(studentUpdate);
        return "redirect:/update";
    }

    @GetMapping("/delete")
    public String deleteStudents(Model model) {
        model.addAttribute("students", studentRepository.getStudents(FilterSetType.NO_FILTER.getId(), true, true));
        return "deleteStudentsTable";
    }

    @GetMapping("/delete/student/{studentId}")
    public String deleteStudent(@PathVariable("studentId") Long studentId) {
        studentRepository.delete(studentId);
        return "redirect:/delete";
    }
}
