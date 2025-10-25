package ru.hogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findAllByAge(int age);

    List<Student> findByAgeBetween(int minAge, int maxAge);

    List<Student> findAllByFaculty_Id(Long id);

    @Query("SELECT s.faculty FROM Student as s WHERE s.name = :name")
    Optional<Faculty> findFacultyByName(@Param("name") String name);
}
