package repository;

import org.junit.jupiter.api.*;
import org.springframework.core.io.ClassPathResource;
import ru.bmstu.diary.model.Student;
import ru.bmstu.diary.repository.StudentRepository;
import ru.bmstu.diary.util.CsvReaderWriter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StudentRepositoryTest {
    private static final String TEST_CSV = "test-students.csv";
    private Path tempPath;
    private CsvReaderWriter csvReaderWriter;
    private StudentRepository repository;

    @BeforeEach
    void setUp() throws IOException {
        // Создаём временный файл и копируем туда содержимое ресурса
        Path original = new ClassPathResource(TEST_CSV).getFile().toPath();
        tempPath = Files.createTempFile("students-", ".csv");
        Files.copy(original, tempPath, java.nio.file.StandardCopyOption.REPLACE_EXISTING);

        csvReaderWriter = new CsvReaderWriter(tempPath.toString(), new ClassPathResource(TEST_CSV));
        repository = new StudentRepository(csvReaderWriter);
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(tempPath);
    }

    @Test
    void testListAll() {
        List<Student> students = repository.listAll();
        assertEquals(2, students.size());
        assertEquals("John", students.get(0).getFirstName());
        assertEquals("Smith", students.get(1).getLastName());
    }

    @Test
    void testAdd() {
        Student added = repository.add("Alice", "Brown", 40);

        assertNotNull(added);
        assertEquals("Alice", added.getFirstName());
        assertEquals("Brown", added.getLastName());
        assertEquals(40, added.getTokens());

        List<Student> all = repository.listAll();
        assertEquals(3, all.size());
    }

    @Test
    void testFindByIdExists() {
        Student student = repository.findById(1);
        assertNotNull(student);
        assertEquals("John", student.getFirstName());
    }

    @Test
    void testFindByIdNotExists() {
        Student student = repository.findById(999);
        assertNull(student);
    }

    @Test
    void testUpdate() {
        Student original = repository.findById(2);
        assertNotNull(original);

        Student updated = new Student(original.getId(), "Jane", "Updated", 88);
        Student result = repository.update(updated);

        assertEquals("Updated", result.getLastName());
        assertEquals(88, result.getTokens());

        Student reloaded = repository.findById(2);
        assertEquals("Updated", reloaded.getLastName());
    }

    @Test
    void testDelete() {
        boolean deleted = repository.delete(1);
        assertTrue(deleted);

        Student shouldBeGone = repository.findById(1);
        assertNull(shouldBeGone);

        List<Student> all = repository.listAll();
        assertEquals(1, all.size());
    }
}
