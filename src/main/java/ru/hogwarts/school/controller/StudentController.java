package ru.hogwarts.school.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.List;

@RestController
@RequestMapping("student")
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public ResponseEntity<Student> createStudent(@RequestBody Student student) {
        Student createdStudent = studentService.createStudent(student);
        return ResponseEntity.ok(createdStudent);
    }

    @GetMapping("/faculty_by_student")
    public ResponseEntity<Faculty> getFaculty(@RequestParam String name) {
        Faculty faculty = studentService.findFacultyByStudentName(name);
        if (faculty == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(faculty);
    }

    @GetMapping
    public ResponseEntity<List<Student>> getStudents(@RequestParam(required = false) Integer age,
                                                     @RequestParam(required = false) Integer minAge,
                                                     @RequestParam(required = false) Integer maxAge) {
        List<Student> students;
        if (age != null) {
            students = studentService.findStudentByAge(age);
        } else if (minAge != null && maxAge != null) {
            students = studentService.findByAgeBetween(minAge, maxAge);
        } else {
            students = studentService.getAllStudents();
        }

        if (students.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(students);
    }

    @PutMapping
    public ResponseEntity<Student> updateStudent(@RequestBody Student student) {
        Student updatedStudent = studentService.updateStudent(student);
        if (updatedStudent == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedStudent);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable Long id) {
        Student student = studentService.getStudent(id);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(student);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudentById(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/count")
    public long countStudents() {
        return studentService.countAllStudents();
    }

    @GetMapping("/average_age")
    public float getAverageAge() {
        return studentService.getAverageAge();
    }

    @GetMapping("/five_latest_students")
    public List<Student> getFiveLatestStudents() {
        return studentService.getFiveLatestStudents();
    }
}