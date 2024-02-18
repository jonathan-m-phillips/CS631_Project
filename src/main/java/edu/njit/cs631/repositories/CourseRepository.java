package edu.njit.cs631.repositories;

import edu.njit.cs631.models.Course;
import edu.njit.cs631.models.CustomUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {
    Course findById(long id);
    List<Course> findByFaculty(CustomUser faculty);

    @Query("SELECT c FROM Course c JOIN c.students s WHERE s = :student")
    List<Course> findCoursesByStudent(@Param("student") CustomUser student);
}
