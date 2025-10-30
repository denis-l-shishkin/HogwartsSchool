package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.exception.StudentNotFoundException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class StudentService {
    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student createStudent(Student student) {
        logger.info("Creating student");
        return studentRepository.save(student);
    }
    public Student getStudent(Long id) {
        Student student = studentRepository.findById(id).orElse(null);
        if (student != null) {
            logger.info("Found student");
            return studentRepository.findById(id).orElse(null);
        }
        logger.warn("Student with id {} not found", id);
        return null;
    }
    public List<Student> getAllStudents() {
        logger.debug("Getting all students");
        return new ArrayList<>(studentRepository.findAll());
    }
    public Student updateStudent(Student student) {
        Long studentId = student.getId();
        if (!studentRepository.existsById(studentId)) {
            logger.error("Student with id {} not found", studentId);
            throw new StudentNotFoundException(studentId);
        }
        logger.info("Updating student");
        return studentRepository.save(student);
    }
    public void deleteStudent(Long id) {
        logger.debug("Deleting student");
        studentRepository.deleteById(id);
    }
    public List<Student> findStudentByAge(int age) {
        if (age > 0) {
            logger.debug("Finding students by age");
            return studentRepository.findAllByAge(age);
        }
        logger.warn("Students by age {} not found", age);
        return new ArrayList<>();
    }

    public List<Student> findByAgeBetween(int minAge, int maxAge) {
        if (minAge > 0 && maxAge > minAge) {
            logger.info("Finding students by age between");
            return studentRepository.findByAgeBetween(minAge, maxAge);
        }
        logger.warn("Students by age between {} and {} not found", minAge, maxAge);
        return new ArrayList<>();
    }

    public Faculty findFacultyByStudentName(String name) {
        logger.info("Finding faculty by name");
        return studentRepository.findByName(name).getFaculty();
    }

    public long countAllStudents() {
        logger.info("Counting all students");
        return studentRepository.countAllStudents();
    }

    public float getAverageAge() {
        logger.debug("Getting average age");
        return studentRepository.getAverageAge();
    }

    public List<Student> getFiveLatestStudents() {
        logger.info("Getting five latest students");
        return studentRepository.getFiveLatestStudents();
    }
}
