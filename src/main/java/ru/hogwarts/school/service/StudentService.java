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
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import java.util.stream.Stream;

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

    public List<String> getAllNamesBeginWithLetter(String letter) {
        return studentRepository.findAll().stream()
                .map(Student -> Student.getName().split(" ")[0])
                .filter(name -> name.startsWith(letter))
                .map(String::toUpperCase)
                .sorted()
                .collect(Collectors.toList());
    }

    public double getAverageAgeWithFindAll() {
        return studentRepository.findAll().stream()
                .mapToInt(Student::getAge)
                .average()
                .orElse(0.0);
    }

    public long calculateExample() {// ввиду переполнения меняю int на long
        long startTime = System.nanoTime();
        long sum = 1_000_000L * (1_000_000L + 1L) / 2L;
        long endTime = System.nanoTime();
        System.out.println(endTime - startTime);
        return sum;
        /*long sum = Stream.iterate(1L, a -> a +1L)
                .limit(1_000_000)
                .reduce(0L, (a, b) -> a + b);
        Среднее время выполнения 32895899 наносекунд
        -------
        long sum = Stream.iterate(1L, a -> a +1L)
                .limit(1_000_000)
                .reduce(0L, Long::sum);
        Среднее время выполнения 28409599 наносекунд
        -------
        long sum = LongStream.iterate(1, a -> a +1)
                .limit(1_000_000)
                .reduce(0L, Long::sum);
        Среднее время выполнения 9588659 наносекунд
        -------
        long sum = LongStream.rangeClosed(1, 1_000_000)
                .sum();
        Среднее время выполнения 1949600 наносекунд
        -------
        long sum = LongStream.rangeClosed(1, 1_000_000)
                .parallel()
                .sum();
        Среднее время выполнения 576500 наносекунд
        -------
        long sum = 1_000_000L * (1_000_000L + 1L) / 2L;
        Среднее время выполнения 280 наносекунд
        Вывод: Итоговое выражение выполняется в 117_000 раз быстрее первоначального!
        Сложность последнего выражения O(1) против потока О(n)
        */
    }

}
