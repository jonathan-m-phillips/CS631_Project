package edu.njit.cs631.repositories;

import edu.njit.cs631.models.Course;
import edu.njit.cs631.models.CustomUser;
import edu.njit.cs631.models.Grade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GradeRepository extends JpaRepository<Grade, Long> {
    List<Grade> findByCourse(Course course);
    Optional<Grade> findByCourseAndStudent(Course course, CustomUser student);
    void deleteAllByCourse(Course course);
}
