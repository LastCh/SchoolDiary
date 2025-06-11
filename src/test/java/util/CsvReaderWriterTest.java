package util;

import org.junit.jupiter.api.*;
import org.springframework.core.io.ClassPathResource;
import ru.bmstu.diary.util.CsvReaderWriter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CsvReaderWriterTest {
    private Path tempFilePath;
    private CsvReaderWriter csvReaderWriter;

    @BeforeEach
    void setUp() throws IOException {
        // Create a temporary file
        tempFilePath = Files.createTempFile("test-students", ".csv");
        // Copy the content of the resource file to the temporary file
        ClassPathResource resource = new ClassPathResource("test-students.csv");
        Files.copy(resource.getInputStream(), tempFilePath, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
        csvReaderWriter = new CsvReaderWriter(tempFilePath.toString(), resource);
    }

    @AfterEach
    void tearDown() throws IOException {
        // Delete the temporary file after each test
        Files.deleteIfExists(tempFilePath);
    }

    @Test
    void testReadAllLines() {
        List<String[]> data = csvReaderWriter.readAllLines();
        assertNotNull(data, "Data should not be null");
        assertEquals(2, data.size(), "There should be 2 entries in the test CSV");
        assertArrayEquals(new String[]{"1", "John", "Doe", "50"}, data.get(0), "First line data mismatch");
        assertArrayEquals(new String[]{"2", "Jane", "Smith", "75"}, data.get(1), "Second line data mismatch");
    }

    @Test
    void testWriteAllLines() {
        List<String[]> newData = Arrays.asList(
                new String[]{"3", "Alice", "Brown", "60"},
                new String[]{"4", "Bob", "Taylor", "80"}
        );
        csvReaderWriter.writeAllLines(newData);

        List<String[]> updatedData = csvReaderWriter.readAllLines();
        assertEquals(2, updatedData.size(), "After write, there should be 2 entries in the test CSV");
        assertArrayEquals(new String[]{"3", "Alice", "Brown", "60"}, updatedData.get(0), "Written data mismatch for entry 1");
        assertArrayEquals(new String[]{"4", "Bob", "Taylor", "80"}, updatedData.get(1), "Written data mismatch for entry 2");
    }
}