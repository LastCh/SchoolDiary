package controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.bmstu.diary.controller.StudentController;
import ru.bmstu.diary.model.Student;
import ru.bmstu.diary.service.StudentService;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StudentControllerTest {

    @Mock
    private StudentService studentService;

    @InjectMocks
    private StudentController studentController;

    @BeforeEach
    void setup() {
        // No MockMvc setup needed
    }

    @Test
    void testGetAllStudents() {
        List<Student> students = Arrays.asList(
                new Student(1, "Иван", "Иванов", 100),
                new Student(2, "Мария", "Петрова", 80)
        );
        when(studentService.listAll()).thenReturn(students);

        List<Student> result = studentController.getAll();
        assertEquals(2, result.size());
        assertEquals("Иван", result.get(0).getFirstName());
    }

    @Test
    void testAddStudent() {
        Student requestStudent = new Student(0, "Анна", "Сидорова", 60);
        Student createdStudent = new Student(3, "Анна", "Сидорова", 60);
        when(studentService.add("Анна", "Сидорова", 60)).thenReturn(createdStudent);

        // Assuming controller has a method like: Student add(Student student)
        Student result = studentController.add(requestStudent);
        assertEquals(3, result.getId());
        assertEquals("Анна", result.getFirstName());
    }
}