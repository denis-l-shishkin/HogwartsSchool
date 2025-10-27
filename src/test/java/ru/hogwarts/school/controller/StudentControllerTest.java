package ru.hogwarts.school.controller;

import net.minidev.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StudentController.class)
public class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private StudentService studentService;

    @InjectMocks
    private StudentController studentController;

    @Test
    void whenCreateStudent_thenShouldReturnCreatedStudent() throws Exception {
        Student student = new Student(null, "Гарри Поттер", 11);

        String name = "Гарри Поттер";
        Integer age = 11;

        JSONObject studentObject = new JSONObject();
        studentObject.put("name", name);
        studentObject.put("age", age);

        when(studentService.createStudent(any(Student.class))).thenReturn(student);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/student")
                        .content(studentObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(student.getId()))
                .andExpect(jsonPath("$.name").value(student.getName()))
                .andExpect(jsonPath("$.age").value(student.getAge()));
    }

    @Test
    void whenGetFacultyByStudentName_thenShouldReturnFaculty() throws Exception {
        Faculty faculty = new Faculty(246L, "Гриффиндор", "Красный");

        when(studentService.findFacultyByStudentName("Гарри Поттер")).thenReturn(faculty);

        mockMvc.perform(get("/student/faculty_by_student")
                        .param("name", "Гарри Поттер"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(faculty.getId()))
                .andExpect(jsonPath("$.name").value(faculty.getName()))
                .andExpect(jsonPath("$.color").value(faculty.getColor()));
    }

    @Test
    void whenGetStudentsByAge_thenShouldReturnFoundStudents() throws Exception {
        Student student1 = new Student(247L, "Гарри Поттер", 11);
        Student student2 = new Student(248L, "Рон Уизли", 11);
        List<Student> students = Arrays.asList(student1, student2);

        when(studentService.findStudentByAge(11)).thenReturn(students);

        mockMvc.perform(get("/student")
                        .param("age", "11"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].age").value(11))
                .andExpect(jsonPath("$[1].age").value(11));
    }

    @Test
    void whenGetStudentsByAgeRange_thenShouldReturnFoundStudents() throws Exception {
        Student student1 = new Student(249L, "Гарри Поттер", 11);
        Student student2 = new Student(250L, "Гермиона Грейнджер", 12);
        List<Student> students = Arrays.asList(student1, student2);

        when(studentService.findByAgeBetween(11, 12)).thenReturn(students);

        mockMvc.perform(get("/student")
                        .param("minAge", "11")
                        .param("maxAge", "12"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].age").value(11))
                .andExpect(jsonPath("$[1].age").value(12));
    }

    @Test
    void whenGetAllStudents_thenShouldReturnAllStudents() throws Exception {
        Student student1 = new Student(251L, "Гарри Поттер", 11);
        Student student2 = new Student(252L, "Гермиона Грейнджер", 12);
        Student student3 = new Student(253L, "Рон Уизли", 11);

        List<Student> students = Arrays.asList(student1, student2, student3);

        when(studentService.getAllStudents()).thenReturn(students);

        mockMvc.perform(get("/student"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].name").value(student1.getName()))
                .andExpect(jsonPath("$[1].name").value(student2.getName()))
                .andExpect(jsonPath("$[2].name").value(student3.getName()));
    }


    @Test
    void whenUpdateStudent_thenShouldReturnUpdatedStudent() throws Exception {
        Student updatedStudent = new Student(261L, "Гарри Поттер Обновленный", 12);

        Long id = 1L;
        String name = "Гарри Поттер Обновленный";
        Integer age = 12;

        JSONObject studentObject = new JSONObject();
        studentObject.put("id", id);
        studentObject.put("name", name);
        studentObject.put("age", age);

        when(studentService.updateStudent(any(Student.class))).thenReturn(updatedStudent);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/student")
                        .content(studentObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(updatedStudent.getId()))
                .andExpect(jsonPath("$.name").value(updatedStudent.getName()))
                .andExpect(jsonPath("$.age").value(updatedStudent.getAge()));
    }

    @Test
    void whenGetStudentById_thenShouldReturnStudent() throws Exception {
        Student student = new Student(1L, "Гарри Поттер", 11);

        when(studentService.getStudent(1L)).thenReturn(student);

        mockMvc.perform(get("/student/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(student.getId()))
                .andExpect(jsonPath("$.name").value(student.getName()))
                .andExpect(jsonPath("$.age").value(student.getAge()));
    }

    @Test
    void whenDeleteStudentById_thenShouldReturnOk() throws Exception {
        mockMvc.perform(delete("/student/1"))
                .andExpect(status().isOk());

        verify(studentService).deleteStudent(1L);
    }
}
