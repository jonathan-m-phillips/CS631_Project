package edu.njit.cs631.controllers;

import edu.njit.cs631.models.CustomUser;
import edu.njit.cs631.repositories.CustomUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AuthController {
    @Autowired
    private CustomUserRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @PostMapping("/register")
    public ModelAndView register(@ModelAttribute CustomUser user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        repository.save(user);

        return new ModelAndView("redirect:/sign-in");
    }

    @GetMapping("/sign-in")
    public String login() {
        return "sign-in";
    }
}
