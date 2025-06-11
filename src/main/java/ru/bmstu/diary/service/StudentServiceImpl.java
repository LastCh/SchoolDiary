package ru.bmstu.diary.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.bmstu.diary.model.Student;
import ru.bmstu.diary.repository.StudentRepository;

import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {
    private final StudentRepository repository;

    @Autowired
    public StudentServiceImpl(StudentRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Student> listAll() {
        return repository.listAll();
    }

    @Override
    public Student add(String firstName, String lastName, int tokens) {
        if (tokens < 0 || tokens > 100) {
            throw new IllegalArgumentException("Tokens must be between 0 and 100");
        }
        return repository.add(firstName, lastName, tokens);
    }

    @Override
    public Student update(Student s) {
        if (s.getTokens() < 0 || s.getTokens() > 100) {
            throw new IllegalArgumentException("Tokens must be between 0 and 100");
        }
        return repository.update(s);
    }

    @Override
    public Student findById(int id) {
        return repository.findById(id);
    }

    @Override
    public boolean delete(int id) {
        return repository.delete(id);
    }
}