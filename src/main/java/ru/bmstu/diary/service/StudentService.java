package ru.bmstu.diary.service;

import ru.bmstu.diary.model.Student;

import java.util.List;

public interface StudentService {
    List<Student> listAll();
    Student add(String first, String last, int tokens);
    Student update(Student s);
    Student findById(int id);
    boolean delete(int id);
}