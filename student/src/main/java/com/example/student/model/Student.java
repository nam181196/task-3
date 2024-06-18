package com.example.student.model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Student {
    private int id;
    private String gender;
    private String name;
    private double mathScore;
    private double physicsScore;
    private double chemistryScore;
    private double avgScore;
    private String evaluation;

    public Student(int id, String gender, String name, double mathScore, double physicsScore, double chemistryScore) {
        this.id = id;
        this.gender = gender;
        this.name = name;
        this.mathScore = mathScore;
        this.physicsScore = physicsScore;
        this.chemistryScore = chemistryScore;
        this.avgScore = calculateAvgScore();
        this.evaluation = evaluate();
    }

    public static Student fromString(String line) {
        String[] parts = line.split(",");
        return new Student(
                Integer.parseInt(parts[0]),
                parts[1],
                parts[2],
                Double.parseDouble(parts[3]),
                Double.parseDouble(parts[4]),
                Double.parseDouble(parts[5])
        );
    }

    private double calculateAvgScore() {
        return (mathScore + physicsScore + chemistryScore) / 3.0;
    }

    private String evaluate() {
        double avg = calculateAvgScore();
        if (avg >= 8) {
            return "Excellent";
        } else if (avg >= 6.5) {
            return "Good";
        } else if (avg >= 5) {
            return "Average";
        } else {
            return "Poor";
        }
    }

    @Override
    public String toString() {
        return id + "," + gender + "," + name + "," + mathScore + "," + physicsScore + "," + chemistryScore + "," + avgScore + "," + evaluation;
    }
}
