SELECT student.name, student.age, faculty.name
FROM student INNER JOIN faculty ON faculty_id = faculty.id;



SELECT student.name, student.age, faculty.name
FROM student INNER JOIN faculty ON student.faculty_id = faculty.id
    LEFT JOIN avatar ON student.id = avatar.student_id
WHERE avatar.id IS NOT NULL;