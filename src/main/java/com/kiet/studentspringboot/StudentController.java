
package com.kiet.studentspringboot;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/students")
public class StudentController {

    private List<Student> students = new ArrayList<>();

    // 1. Add Student
    @PostMapping
    public ResponseEntity<?> addStudent(@RequestBody Student student) {

        for (Student s : students) {
            if (s.getStudentId() == student.getStudentId()) {
                return ResponseEntity.badRequest()
                        .body("Student ID already exists");
            }
        }

        if (student.getProgrammingScore() < 0 ||
                student.getProgrammingScore() > 100 ||
                student.getMathematicsScore() < 0 ||
                student.getMathematicsScore() > 100) {

            return ResponseEntity.badRequest()
                    .body("Scores must be between 0 and 100");
        }

        students.add(student);

        return ResponseEntity.ok("Student added successfully");
    }

    // 2. Retrieve All Students
    @GetMapping
    public List<Student> getAllStudents() {
        return students;
    }

    // 3. Search Student by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getStudentById(@PathVariable int id) {

        for (Student s : students) {
            if (s.getStudentId() == id) {
                return ResponseEntity.ok(s);
            }
        }

        return ResponseEntity.status(404)
                .body("Student not found");
    }

    // 4. Update Scores and Technical Skills
    @PutMapping("/{id}")
    public ResponseEntity<?> updateStudent(
            @PathVariable int id,
            @RequestBody Student updatedStudent) {

        if (updatedStudent.getProgrammingScore() < 0 ||
                updatedStudent.getProgrammingScore() > 100 ||
                updatedStudent.getMathematicsScore() < 0 ||
                updatedStudent.getMathematicsScore() > 100) {

            return ResponseEntity.badRequest()
                    .body("Scores must be between 0 and 100");
        }

        for (Student s : students) {
            if (s.getStudentId() == id) {

                s.setProgrammingScore(
                        updatedStudent.getProgrammingScore());

                s.setMathematicsScore(
                        updatedStudent.getMathematicsScore());

                s.setTechnicalSkills(
                        updatedStudent.getTechnicalSkills());

                return ResponseEntity.ok(
                        "Student updated successfully");
            }
        }

        return ResponseEntity.status(404)
                .body("Student not found");
    }

    // 5. Delete Student
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStudent(@PathVariable int id) {

        for (Student s : students) {
            if (s.getStudentId() == id) {
                students.remove(s);
                return ResponseEntity.ok(
                        "Student deleted successfully");
            }
        }

        return ResponseEntity.status(404)
                .body("Student not found");
    }

    // 6. Display Merit List
    @GetMapping("/merit-list")
    public List<Student> getMeritList() {

        List<Student> meritList = new ArrayList<>(students);

        meritList.sort(
                Comparator.comparingDouble(Student::getAverageScore)
                        .reversed()
        );

        return meritList;
    }
}
