package edu.njit.cs631.controllers;

import edu.njit.cs631.models.Course;
import edu.njit.cs631.models.CustomUser;
import edu.njit.cs631.repositories.CourseRepository;
import edu.njit.cs631.repositories.CustomUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@Controller
public class FacultyController {
    @Autowired
    private CustomUserRepository repository;

    @Autowired
    private CourseRepository courseRepository;

    @GetMapping("/create-course")
    public String createCourse(Authentication authentication, Model model) {
        var user = getUser(authentication);
        if (user == null) {
            return "redirect:/sign-in";
        }

        model.addAttribute("course", new Course());
        model.addAttribute("user", user);
        return "/faculty/create-course";
    }

    @PostMapping("/create-course")
    public String createCourse(Authentication authentication, @ModelAttribute Course course) {
        var user = getUser(authentication);
        if (user == null) {
            return "redirect:/sign-in";
        }

        course.setFaculty(user);
        courseRepository.save(course);
        return "redirect:/course/" + course.getId();
    }

    private CustomUser getUser(Authentication authentication) {
        String email = authentication.getName();
        Optional<CustomUser> user = repository.findByEmail(email);
        return user.orElse(null);
    }
}
