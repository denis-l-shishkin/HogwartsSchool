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
public class FacultyControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private FacultyController facultyController;

    @Autowired
    private FacultyRepository facultyRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void contextLoads() throws Exception {
        Assertions.assertThat(facultyController).isNotNull();
    }

    @Test
    void whenCreateFaculty_thenShouldCreateAndReturnFaculty() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setName("Полеты на метле");
        faculty.setColor("Зеленый в белый горошек");

        Assertions
                .assertThat(this.restTemplate.postForObject("http://localhost:" + port + "/faculty", faculty, String.class))
                .isNotNull();
    }

    @Test
    void whenGetFacultyById_thenShouldReturnFaculty() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setName("Посиделки у Хагрида");
        faculty.setColor("Серебристый Клювокрыл");
        Faculty savedFaculty = facultyRepository.save(faculty);

        Faculty result = restTemplate.getForObject("http://localhost:" + port + "/faculty/" + savedFaculty.getId(), Faculty.class);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getId()).isEqualTo(savedFaculty.getId());
        Assertions.assertThat(result.getName()).isEqualTo(savedFaculty.getName());
        Assertions.assertThat(result.getColor()).isEqualTo(savedFaculty.getColor());
    }

    @Test
    void whenUpdateFaculty_thenShouldUpdateAndReturnFaculty() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setName("Косой переулок");
        faculty.setColor("Мрачный");
        Faculty savedFaculty = facultyRepository.save(faculty);

        Faculty updatedFaculty = new Faculty();
        updatedFaculty.setId(savedFaculty.getId());
        updatedFaculty.setName("Косой переулок Новый");
        updatedFaculty.setColor("Мрачный Новый");

        restTemplate.put("http://localhost:" + port + "/faculty", updatedFaculty);

        Faculty result = restTemplate.getForObject("http://localhost:" + port + "/faculty/" + savedFaculty.getId(), Faculty.class);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getName()).isEqualTo("Косой переулок Новый");
        Assertions.assertThat(result.getColor()).isEqualTo("Мрачный Новый");
    }

    @Test
    void whenDeleteFacultyById_thenShouldDeleteFaculty() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setName("Посиделки у Хагрида - на удаление");
        faculty.setColor("Серебристый Клювокрыл - на удаление");
        Faculty savedFaculty = facultyRepository.save(faculty);
        Long deletedId = savedFaculty.getId();

        restTemplate.delete("http://localhost:" + port + "/faculty/" + savedFaculty.getId());

        Assertions
                .assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/faculty/" + deletedId, String.class))
                .isNull();
    }


    @Test
    void whenGetFacultiesByColor_thenShouldReturnFaculties() throws Exception {
        Faculty faculty1 = new Faculty();
        faculty1.setName("Название1");
        faculty1.setColor("Цвет1");
        facultyRepository.save(faculty1);

        Faculty faculty2 = new Faculty();
        faculty2.setName("Название2");
        faculty2.setColor("Цвет2");
        facultyRepository.save(faculty2);

        Assertions.assertThat(restTemplate.getForObject("http://localhost:" + port + "/faculty?color=" + faculty2.getColor(), String.class))
                .isNotNull();
    }

    @Test
    void whenGetFacultiesByName_thenShouldReturnFaculties() throws Exception {
        Faculty faculty1 = new Faculty();
        faculty1.setName("Название3");
        faculty1.setColor("Цвет3");
        facultyRepository.save(faculty1);

        Faculty faculty2 = new Faculty();
        faculty2.setName("Название4");
        faculty2.setColor("Цвет4");
        facultyRepository.save(faculty2);

        Assertions
                .assertThat(restTemplate.getForObject("http://localhost:" + port + "/faculty?color=" + faculty2.getName(), String.class))
                .isNotNull();
    }

    @Test
    void whenGetAllFaculties_thenShouldReturnAllFaculties() throws Exception {
        Faculty faculty1 = new Faculty();
        faculty1.setName("Название5");
        faculty1.setColor("Цвет5");
        facultyRepository.save(faculty1);

        Faculty faculty2 = new Faculty();
        faculty2.setName("Название6");
        faculty2.setColor("Цвет6");
        facultyRepository.save(faculty2);

        Assertions
                .assertThat(restTemplate.getForObject("http://localhost:" + port + "/faculty", String.class))
                .isNotNull();
    }

    @Test
    void whenGetStudentsByFacultyName_thenShouldReturnStudentsList() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setName("Факультет6");
        faculty.setColor("Цвет6");
        Faculty savedFaculty = facultyRepository.save(faculty);

        Student student = new Student();
        student.setName("Harry Potter");
        student.setAge(12);
        student.setFaculty(savedFaculty);
        studentRepository.save(student);

        Assertions
                .assertThat(restTemplate.getForObject("http://localhost:" + port + "/faculty/students_by_faculty?name=" + faculty.getName(), String.class))
                .isNotNull();
    }
}
