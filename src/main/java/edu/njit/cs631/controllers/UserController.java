package edu.njit.cs631.controllers;

import edu.njit.cs631.models.Course;
import edu.njit.cs631.models.CustomUser;
import edu.njit.cs631.models.Grade;
import edu.njit.cs631.repositories.CourseRepository;
import edu.njit.cs631.repositories.CustomUserRepository;
import edu.njit.cs631.repositories.GradeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@Controller
public class UserController {
    @Autowired
    private CustomUserRepository repository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private GradeRepository gradeRepository;

    @GetMapping("/dashboard")
    public String facultyDashboard(Authentication authentication, Model model) {
        var user = getUser(authentication);
        if (user == null) {
            return "/sign-in";
        }

        List<Course> courses = null;
        if (user.getRole().equals("FACULTY")) {
            courses = courseRepository.findByFaculty(user);
            model.addAttribute("courses", courses);
        }

        if (user.getRole().equals("STUDENT")) {
            courses = getCoursesForStudent(user);
            model.addAttribute("courses", courses);
        }

        model.addAttribute("courses", courses);
        model.addAttribute("user", user);
        return "/dashboard";
    }



    @GetMapping("/course/{id}")
    public String userCourse(@PathVariable Long id, Authentication authentication, Model model) {
        var user = getUser(authentication);
        if (user == null) {
            return "redirect:/sign-in";
        }

        Optional<Course> courseOptional = courseRepository.findById(id);
        if (courseOptional.isEmpty()) {
            return "redirect:/dashboard";
        }
        Course course = courseOptional.get();

        if (user.getRole().equals("FACULTY")) {
            if (!course.getFaculty().equals(user)) {
                return "redirect:/dashboard";
            }

            List<Grade> grades = gradeRepository.findByCourse(course);
            model.addAttribute("grades", grades);
        }

        if (user.getRole().equals("STUDENT")) {
            if (!course.getStudents().contains(user)) {
                return "redirect:/dashboard";
            }

            Optional<Grade> gradeOptional = gradeRepository.findByCourseAndStudent(course, user);
            gradeOptional.ifPresent(grade -> model.addAttribute("grade", grade));
        }

        model.addAttribute("course", course);
        model.addAttribute("user", user);
        return "/course";
    }


    private CustomUser getUser(Authentication authentication) {
        String email = authentication.getName();
        Optional<CustomUser> user = repository.findByEmail(email);
        return user.orElse(null);
    }

    private List<Course> getCoursesForStudent(CustomUser student) {
        return courseRepository.findCoursesByStudent(student);
    }
}
