package ru.hogwarts.school.controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StudentControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private StudentController studentController;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private FacultyRepository facultyRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void contextLoads() throws Exception {
        Assertions.assertThat(studentController).isNotNull();
    }

    @Test
    void whenCreateStudent_thenShouldCreateAndReturnStudent() throws Exception {
        Student student = new Student();
        student.setName("Гарри Поттер");
        student.setAge(12);

        Assertions
                .assertThat(this.restTemplate.postForObject("http://localhost:" + port + "/student", student, String.class))
                .isNotNull();
    }

    @Test
    void whenGetStudentById_thenShouldReturnStudent() throws Exception {
        Student student = new Student();
        student.setName("Гарри Поттер");
        student.setAge(10);
        Student savedStudent = studentRepository.save(student);

        Student result = restTemplate.getForObject("http://localhost:" + port + "/student/" + savedStudent.getId(), Student.class);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getId()).isEqualTo(savedStudent.getId());
        Assertions.assertThat(result.getName()).isEqualTo(savedStudent.getName());
        Assertions.assertThat(result.getAge()).isEqualTo(savedStudent.getAge());
    }

    @Test
    void whenUpdateStudent_thenShouldUpdateAndReturnStudent() throws Exception {
        Student student = new Student();
        student.setName("Рон Уизли");
        student.setAge(12);
        Student savedStudent = studentRepository.save(student);

        Student updatedStudent = new Student();
        updatedStudent.setId(savedStudent.getId());
        updatedStudent.setName("Рон Уизли новый");
        updatedStudent.setAge(12);

        restTemplate.put("http://localhost:" + port + "/student", updatedStudent);

        Student result = restTemplate.getForObject("http://localhost:" + port + "/student/" + savedStudent.getId(), Student.class);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getName()).isEqualTo("Рон Уизли новый");
        Assertions.assertThat(result.getAge()).isEqualTo(12);
    }

    @Test
    void whenDeleteStudentById_thenShouldDeleteStudent() throws Exception {
        Student student = new Student();
        student.setName("Драко Малфой - на удаление");
        student.setAge(12);
        Student savedStudent = studentRepository.save(student);
        Long deletedId = savedStudent.getId();

        restTemplate.delete("http://localhost:" + port + "/student/" + savedStudent.getId());

        Assertions
                .assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/student/" + deletedId, String.class))
                .isNull();
    }

    @Test
    void whenGetStudentsByAge_thenShouldReturnStudents() throws Exception {
        Student student1 = new Student();
        student1.setName("Гарри Поттер");
        student1.setAge(12);
        studentRepository.save(student1);

        Student student2 = new Student();
        student2.setName("Гермиона Грейнджер");
        student2.setAge(12);
        studentRepository.save(student2);

        Assertions
                .assertThat(restTemplate.getForObject("http://localhost:" + port + "/student?age=" + 12, String.class))
                .isNotNull();
    }

    @Test
    void whenGetStudentsByAgeRange_thenShouldReturnStudents() throws Exception {
        Student student1 = new Student();
        student1.setName("Гарри Поттер");
        student1.setAge(11);
        studentRepository.save(student1);

        Student student2 = new Student();
        student2.setName("Гермиона Грейнджер");
        student2.setAge(12);
        studentRepository.save(student2);

        Student student3 = new Student();
        student3.setName("Фред Уизли");
        student3.setAge(15);
        studentRepository.save(student3);

        Assertions
                .assertThat(restTemplate.getForObject("http://localhost:" + port + "/student?minAge=11&maxAge=12", String.class))
                .isNotNull();
    }

    @Test
    void whenGetAllStudents_thenShouldReturnAllStudents() throws Exception {
        Student student1 = new Student();
        student1.setName("Гарри Поттер");
        student1.setAge(11);
        studentRepository.save(student1);

        Student student2 = new Student();
        student2.setName("Гермиона Грейнджер");
        student2.setAge(12);
        studentRepository.save(student2);

        Assertions
                .assertThat(restTemplate.getForObject("http://localhost:" + port + "/student", String.class))
                .isNotNull();
    }

    @Test
    void whenGetFacultyByStudentName_thenShouldReturnFaculty() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setName("Гриффиндор");
        faculty.setColor("Красный");
        Faculty savedFaculty = facultyRepository.save(faculty);

        Student student = new Student();
        student.setName("Гарри Поттер");
        student.setAge(12);
        student.setFaculty(savedFaculty);
        studentRepository.save(student);

        Assertions
                .assertThat(restTemplate.getForObject("http://localhost:" + port + "/student/faculty_by_student?name=" + student.getName(), Faculty.class))
                .isNotNull();
    }
}
