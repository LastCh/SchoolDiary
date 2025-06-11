package ru.bmstu.diary.controller;

import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.bmstu.diary.annotation.TeacherOnly;
import ru.bmstu.diary.model.Student;
import ru.bmstu.diary.model.TokenUpdateRequest;
import ru.bmstu.diary.service.StudentService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/students")
@Api(tags = "Student API")
public class StudentController {

    @Autowired
    private StudentService service;

    @GetMapping
    @ApiOperation("Get all students")
    public List<Student> getAll() {
        return service.listAll();
    }

    @PostMapping
    @TeacherOnly
    @ApiOperation("Add a new student")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-Role", value = "Role required for teacher-only operations", required = true, dataType = "string", paramType = "header", defaultValue = "TEACHER")
    })
    public Student add(@RequestBody Student s) {
        if (s.getId() != 0) {
            throw new IllegalArgumentException("ID must be blank when adding a new student");
        }
        return service.add(s.getFirstName(), s.getLastName(), s.getTokens());
    }

    @GetMapping("/{id}")
    @ApiOperation("Get student by ID")
    public ResponseEntity<Student> find(@PathVariable int id) {
        Student s = service.findById(id);
        return s != null ? ResponseEntity.ok(s) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    @TeacherOnly
    @ApiOperation("Update student tokens (Teacher only)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-Role", value = "Role required for teacher-only operations", required = true, dataType = "string", paramType = "header", defaultValue = "TEACHER")
    })
    public ResponseEntity<Student> update(@PathVariable int id, @RequestBody TokenUpdateRequest tokenUpdate) {
        Student existing = service.findById(id);
        if (existing == null) {
            return ResponseEntity.notFound().build();
        }
        existing.setTokens(tokenUpdate.getTokens());
        return ResponseEntity.ok(service.update(existing));
    }

    @DeleteMapping("/{id}")
    @TeacherOnly
    @ApiOperation("Delete a student (Teacher only)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-Role", value = "Role required for teacher-only operations", required = true, dataType = "string", paramType = "header", defaultValue = "TEACHER")
    })
    public ResponseEntity<Void> delete(@PathVariable int id) {
        return service.delete(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}