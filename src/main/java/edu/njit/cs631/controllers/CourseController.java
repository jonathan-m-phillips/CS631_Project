package edu.njit.cs631.controllers;

import edu.njit.cs631.models.CustomUser;
import edu.njit.cs631.repositories.CourseRepository;
import edu.njit.cs631.repositories.CustomUserRepository;
import edu.njit.cs631.repositories.GradeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class CourseController {
    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CustomUserRepository userRepository;

    @Autowired
    private GradeRepository gradeRepository;

    @GetMapping("/courses")
    public String listAllCourses(Authentication authentication, Model model) {
        var user = getUser(authentication);
        var courses = courseRepository.findAll();
        model.addAttribute("courses", courses);
        model.addAttribute("user", user);
        return "/all-courses";
    }

    @PostMapping("/courses/enroll")
    public String enrollOrDropCourse(@RequestParam Long courseId, Authentication authentication) {
        var user = getUser(authentication);
        var course = courseRepository.findById(courseId).orElse(null);
        if (user != null && course != null) {
            if (course.getStudents().contains(user)) {
                course.getStudents().remove(user);
            } else {
                course.getStudents().add(user);
            }
            courseRepository.save(course);
        }
        return "redirect:/courses";
    }

    @PostMapping("/courses/delete")
    public String deleteCourse(@RequestParam Long courseId, Authentication authentication) {
        var user = userRepository.findByEmail(authentication.getName()).orElse(null);
        var course = courseRepository.findById(courseId).orElse(null);
        if (course != null && course.getFaculty().equals(user)) {
            gradeRepository.deleteAllByCourse(course);
            courseRepository.delete(course);
        }
        return "redirect:/courses";
    }

    private CustomUser getUser(Authentication authentication) {
        String email = authentication.getName();
        Optional<CustomUser> user = userRepository.findByEmail(email);
        return user.orElse(null);
    }
}

