package ru.hogwarts.school.controller;

import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.FacultyService;

import java.util.List;

@RestController
@RequestMapping("faculty")
public class FacultyController {
    private final FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @PostMapping
    public Faculty createFaculty(@RequestBody Faculty faculty) {
        return facultyService.createFaculty(faculty);
    }

    @GetMapping("/students_by_faculty")
    public List<Student> getStudents(@RequestParam String name) {
        return facultyService.getStudentsByFacultyName(name);
    }

    @GetMapping
    public List<Faculty> getFaculties(@RequestParam(required = false) String color,
                                      @RequestParam(required = false) String name) {
        if (name != null) {
            return facultyService.getFacultiesByName(name);
        }
        if (color != null) {
            return facultyService.getFacultiesByColor(color);
        }
        return facultyService.getAllFaculties();
    }

    @PutMapping
    public Faculty updateFaculty(@RequestBody Faculty faculty) {
        return facultyService.updateFaculty(faculty);
    }

    @GetMapping("/{id}")
    public Faculty getFacultyById(@PathVariable Long id) {
        return facultyService.getFaculty(id);
    }

    @DeleteMapping("/{id}")
    public void deleteFacultyById(@PathVariable Long id) {
        facultyService.deleteFaculty(id);
    }
}