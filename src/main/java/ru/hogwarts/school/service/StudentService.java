package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.exception.StudentNotFoundException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student createStudent(Student student) {
        return studentRepository.save(student);
    }
    public Student getStudent(Long id) {
        return studentRepository.findById(id).orElse(null);
    }
    public List<Student> getAllStudents() {
        return new ArrayList<>(studentRepository.findAll());
    }
    public Student updateStudent(Student student) {
        Long studentId = student.getId();
        if (!studentRepository.existsById(studentId)) {
            throw new StudentNotFoundException(studentId);
        }
        return studentRepository.save(student);
    }
    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }
    public List<Student> findStudentByAge(int age) {
        if (age > 0) {
            return studentRepository.findAllByAge(age);
        }
        return new ArrayList<>();
    }

    public List<Student> findByAgeBetween(int minAge, int maxAge) {
        if (minAge > 0 && maxAge > minAge) {
            return studentRepository.findByAgeBetween(minAge, maxAge);
        }
        return new ArrayList<>();
    }

    public Faculty findFacultyByStudentName(String name) {
        return studentRepository.findFacultyByName(name)
                .orElseThrow(() -> new StudentNotFoundException("Студент не найден"));
    }
}
