package com.example.student.service;

import com.example.student.model.Student;
import com.example.student.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@CacheConfig(cacheNames = "student")
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @CacheEvict(allEntries = true) //Xoá toàn bộ cache khi có thay đổi
    public Student saveStudent(Student student) throws IOException {
        return studentRepository.save(student);
    }

    @Cacheable(key = "#root.methodName") // Cache kết quả của phương thức này
    public List<Student> getAllStudents() throws IOException {
        return studentRepository.findAll();
    }

    @Cacheable(key = "#id") // Cache kết quả của phương thức này
    public Optional<Student> getStudentById(int id) throws IOException {
        return studentRepository.findById(id);
    }

    @Cacheable(key="#name")
    public Optional<Student> getStudentByName(String name) throws IOException {
        System.out.println(name);
        return studentRepository.findByName(name);
    }

    @Cacheable(key="#sortBy")
    public List<Student> findBySort(String sortBy) throws IOException {
        return studentRepository.findBySort(sortBy);
    }

    public void deleteStudent(int id) throws IOException {
        studentRepository.deleteById(id);
    }
}

