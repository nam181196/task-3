package com.example.student.repository;

import com.example.student.model.Student;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class StudentRepository {
    private static final String FILE_PATH = "/Users/nam/Downloads/student/src/main/resources/static/student.txt";
    private final AtomicInteger idCounter = new AtomicInteger(0);

    public StudentRepository() {
        File file = new File(FILE_PATH);
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    Student student = Student.fromString(line);
                    idCounter.updateAndGet(x -> Math.max(x, student.getId()));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Student save(Student student) throws IOException {
        if (student.getId() == 0) {
            student.setId(idCounter.incrementAndGet());
        }
        List<Student> students = findAll();
        students.removeIf(existingStudent -> existingStudent.getId() == student.getId());
        students.add(student);
        writeToFile(students);
        return student;
    }

    public List<Student> findAll() throws IOException {
        List<Student> students = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                students.add(Student.fromString(line));
            }
        }
        return students;
    }

    public Optional<Student> findById(int id) throws IOException {
        return findAll().stream().filter(student -> student.getId() == id).findFirst();
    }

    public Optional<Student> findByName(String name) throws IOException {
        return findAll().stream().filter(student -> student.getName().toUpperCase().equals(name.toUpperCase()) ).findFirst();
    }
    public List<Student> findBySort(String fieldName) throws IOException {
        List<Student> students = findAll();
        return students.stream()
                .sorted((s1, s2) -> {
                    if ("name".equals(fieldName)) {
                        return s1.getName().compareToIgnoreCase(s2.getName());
                    }
                    switch (fieldName) {
                        case "gender":
                            return s1.getGender().compareToIgnoreCase(s2.getGender());
                        case "mathScore":
                            return Double.compare(s1.getMathScore(), s2.getMathScore());
                        case "physicsScore":
                            return Double.compare(s1.getPhysicsScore(), s2.getPhysicsScore());
                        case "chemistryScore":
                            return Double.compare(s1.getChemistryScore(), s2.getChemistryScore());
                        case "averageScore":
                            return Double.compare(s1.getAvgScore(), s2.getAvgScore());
                        case "evaluation":
                            return s1.getEvaluation().compareTo(s2.getEvaluation());
                        default:
                            throw new IllegalArgumentException("Unknown field: " + fieldName);
                    }
                })
                .collect(Collectors.toList());
    }
    public void deleteById(int id) throws IOException {
        List<Student> students = findAll();
        students.removeIf(student -> student.getId() == id);
        writeToFile(students);
    }




    private void writeToFile(List<Student> students) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Student student : students) {
                writer.write(student.toString());
                writer.newLine();
            }
        }
    }
}
