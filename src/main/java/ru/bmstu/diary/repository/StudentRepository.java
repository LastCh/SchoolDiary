package ru.bmstu.diary.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.bmstu.diary.model.Student;
import ru.bmstu.diary.util.CsvReaderWriter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class StudentRepository {
    private final CsvReaderWriter csvReaderWriter;
    private final AtomicInteger idGen = new AtomicInteger();

    @Autowired
    public StudentRepository(CsvReaderWriter csvReaderWriter) {
        this.csvReaderWriter = csvReaderWriter;
        initializeIdGen();
    }

    private void initializeIdGen() {
        List<Student> students = listAll();
        if (students.isEmpty()) {
            idGen.set(0);
        } else {
            int maxId = students.stream().mapToInt(Student::getId).max().getAsInt();
            idGen.set(maxId);
        }
    }

    public List<Student> listAll() {
        List<String[]> data = csvReaderWriter.readAllLines();
        List<Student> students = new ArrayList<>();
        for (String[] row : data) {
            try {
                int id = Integer.parseInt(row[0]);
                String firstName = row[1];
                String lastName = row[2];
                int tokens = Integer.parseInt(row[3]);
                students.add(new Student(id, firstName, lastName, tokens));
            } catch (NumberFormatException e) {
                System.err.println("Incorrect string in CSV: " + String.join(";", row));
                continue;
            }
        }
        return students;
    }

    public Student add(String firstName, String lastName, int tokens) {
        List<Student> students = listAll();
        int newId = findNextAvailableId(students);
        Student student = new Student(newId, firstName, lastName, tokens);
        students.add(student);
        saveAll(students);

        // Обновите idGen
        int currentMax = students.stream().mapToInt(Student::getId).max().orElse(0);
        idGen.set(currentMax);

        return student;
    }

    private int findNextAvailableId(List<Student> students) {
        if (students.isEmpty()) return 1;

        // Найдите реальный максимальный ID из списка
        int maxId = students.stream()
                .mapToInt(Student::getId)
                .max()
                .orElse(0);

        boolean[] usedIds = new boolean[maxId + 2];

        for (Student s : students) {
            int id = s.getId();
            if (id > 0 && id <= maxId + 1) {
                usedIds[id] = true;
            }
        }

        for (int i = 1; i < usedIds.length; i++) {
            if (!usedIds[i]) {
                return i;
            }
        }

        return maxId + 1;
    }


    public Student update(Student student) {
        List<Student> students = listAll();
        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).getId() == student.getId()) {
                students.set(i, student);
                saveAll(students);
                return student;
            }
        }
        throw new RuntimeException("Student not found");
    }

    public Student findById(int id) {
        return listAll().stream()
                .filter(s -> s.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public boolean delete(int id) {
        List<Student> students = listAll();
        boolean removed = students.removeIf(s -> s.getId() == id);
        if (removed) {
            saveAll(students);
        }
        return removed;
    }

    private void saveAll(List<Student> students) {
        List<String[]> data = new ArrayList<>();
        for (Student s : students) {
            String[] row = {String.valueOf(s.getId()), s.getFirstName(), s.getLastName(), String.valueOf(s.getTokens())};
            data.add(row);
        }
        csvReaderWriter.writeAllLines(data);
    }
}