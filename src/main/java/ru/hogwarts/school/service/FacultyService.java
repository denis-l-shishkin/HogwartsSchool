package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.exception.FacultyNotFoundException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

import javax.lang.model.element.Name;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class FacultyService {

    private static final Logger logger = LoggerFactory.getLogger(FacultyService.class);

    private final FacultyRepository facultyRepository;
    private final StudentRepository studentRepository;

    public FacultyService(FacultyRepository facultyRepository, StudentRepository studentRepository) {
        this.facultyRepository = facultyRepository;
        this.studentRepository = studentRepository;
    }
    public Faculty createFaculty(Faculty faculty) {
        faculty.setId(null);
        logger.info("Creating faculty");
        return facultyRepository.save(faculty);
    }
    public Faculty getFaculty(Long id) {
        Faculty faculty = facultyRepository.findById(id).orElse(null);
        if (faculty != null) {
            logger.info("Found faculty");
            return faculty;
        }
        logger.warn("Faculty {} not found", id);
        return null;
    }
    public List<Faculty> getAllFaculties() {
        logger.info("Getting all faculties");
        return new ArrayList<>(facultyRepository.findAll());
    }
    public Faculty updateFaculty(Faculty faculty) {
        Long facultyId = faculty.getId();
        if (!facultyRepository.existsById(facultyId)) {
            logger.error("Faculty with id {} not found", facultyId);
            throw new FacultyNotFoundException(facultyId);
        }
        logger.info("Updating faculty");
        return facultyRepository.save(faculty);
    }
    public void deleteFaculty(Long id) {
        logger.info("Deleting faculty with id {}", id);
        facultyRepository.deleteById(id);
    }
    public List<Faculty> getFacultiesByColor(String color) {
        logger.info("Getting all faculties by color {}", color);
        return facultyRepository.findAllByColorIgnoreCase(color);
    }

    public List<Faculty> getFacultiesByName(String name) {
        logger.info("Getting all faculties by name {}", name);
        return facultyRepository.findAllByNameIgnoreCase(name);
    }

    public List<Student> getStudentsByFacultyName(String name) {
        Faculty faculty = facultyRepository.findByNameIgnoreCase(name)
                .orElseThrow(() -> {
                    logger.error("Faculty with name {} not found", name);
                    return new FacultyNotFoundException("Факультет не найден");
                });
        List<Student> students = studentRepository.findAllByFaculty_Id(faculty.getId());
        logger.info("Found {} students", students.size());
        return new ArrayList<>(students);
    }

    public String getMaxLengthNameFaculty() {
        return facultyRepository.findAll().stream()
                .map(Faculty::getName)
                .max(Comparator.comparingInt(String::length))
                .orElse("Список факультетов пуст");
    }
}
