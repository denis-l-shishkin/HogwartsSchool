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
import ru.hogwarts.school.service.FacultyService;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FacultyController.class)
public class FacultyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FacultyService facultyService;

    @InjectMocks
    private FacultyController facultyController;

    @Test
    void whenCreateFaculty_thenShouldReturnCreatedFaculty() throws Exception {
        Faculty faculty = new Faculty(null, "Гриффиндор", "Красный");

        String name = "Гриффиндор";
        String color = "Красный";

        JSONObject facultyObject = new JSONObject();
        facultyObject.put("name", name);
        facultyObject.put("color", color);

        when(facultyService.createFaculty(any(Faculty.class))).thenReturn(faculty);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/faculty")
                        .content(facultyObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(faculty.getId()))
                .andExpect(jsonPath("$.name").value((faculty.getName())))
                .andExpect(jsonPath("$.color").value(faculty.getColor()));
    }

    @Test
    void whenGetStudentsByFacultyName_thenShouldReturnStudentsList() throws Exception {
        Student student1 = new Student(31L, "Гарри Поттер", 11);
        Student student2 = new Student(32L, "Гермиона Грейнджер", 12);
        Student student3 = new Student(33L, "Рон Уизли", 11);
        List<Student> students = Arrays.asList(student1, student2, student3);

        when(facultyService.getStudentsByFacultyName("Гриффиндор")).thenReturn(students);

        mockMvc.perform(get("/faculty/students_by_faculty")
                        .param("name", "Гриффиндор"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].name").value(student1.getName()))
                .andExpect(jsonPath("$[1].name").value(student2.getName()))
                .andExpect(jsonPath("$[2].name").value(student3.getName()));
    }

    @Test
    void whenGetFacultiesByName_thenShouldReturnFoundFaculties() throws Exception {
        Faculty faculty = new Faculty(345L, "Гриффиндор", "Красный");
        List<Faculty> faculties = Arrays.asList(faculty);

        when(facultyService.getFacultiesByName("Гриффиндор")).thenReturn(faculties);

        mockMvc.perform(get("/faculty")
                        .param("name", "Гриффиндор"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Гриффиндор"));
    }

    @Test
    void whenGetFacultiesByColor_thenShouldReturnFoundFaculties() throws Exception {
        Faculty faculty = new Faculty(345L, "Гриффиндор", "Красный");
        List<Faculty> faculties = Arrays.asList(faculty);

        when(facultyService.getFacultiesByColor("Красный")).thenReturn(faculties);

        mockMvc.perform(get("/faculty")
                        .param("color", "Красный"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].color").value("Красный"));
    }

    @Test
    void whenGetAllFaculties_thenShouldReturnAllFaculties() throws Exception {
        Faculty faculty1 = new Faculty(345L, "Гриффиндор", "Красный");
        Faculty faculty2 = new Faculty(346L, "Слизерин", "Зеленый");

        List<Faculty> faculties = Arrays.asList(faculty1, faculty2);

        when(facultyService.getAllFaculties()).thenReturn(faculties);

        mockMvc.perform(get("/faculty"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void whenUpdateFaculty_thenShouldReturnUpdatedFaculty() throws Exception {
        Faculty updatedFaculty = new Faculty(360L, "Гриффиндор новый", "Красный новый");

        Long id = 360L;
        String name = "Гриффиндор новый";
        String color = "Красный новый";

        JSONObject facultyObject = new JSONObject();
        facultyObject.put("id", id);
        facultyObject.put("name", name);
        facultyObject.put("color", color);

        when(facultyService.updateFaculty(any(Faculty.class))).thenReturn(updatedFaculty);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/faculty")
                        .content(facultyObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(updatedFaculty.getId()))
                .andExpect(jsonPath("$.name").value(updatedFaculty.getName()))
                .andExpect(jsonPath("$.color").value(updatedFaculty.getColor()));
    }

    @Test
    void whenGetFacultyById_thenShouldReturnFaculty() throws Exception {
        Faculty faculty = new Faculty(362L, "Гриффиндор", "Красный");

        when(facultyService.getFaculty(362L)).thenReturn(faculty);

        mockMvc.perform(get("/faculty/362"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(faculty.getId()))
                .andExpect(jsonPath("$.name").value(faculty.getName()))
                .andExpect(jsonPath("$.color").value(faculty.getColor()));
    }

    @Test
    void whenDeleteFacultyById_thenShouldReturnOk() throws Exception {
        mockMvc.perform(delete("/faculty/362"))
                .andExpect(status().isOk());
    }
}