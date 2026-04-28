package ru.itmo.wp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.itmo.wp.domain.User;
import ru.itmo.wp.service.UserService;

@Controller
public class UsersPage extends Page {
    private final UserService userService;

    public UsersPage(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users/all")
    public String users(Model model) {
        model.addAttribute("users", userService.findAll());
        return "UsersPage";
    }

    @GetMapping("/user/{id}")
    public String userProfile(Model model, @PathVariable String id) {
        try {
            long userId = Long.parseLong(id);
            User user = userService.findById(userId);
            if (user == null) {
                model.addAttribute("errorMessage", "No such user");
                return "UserPage";
            }
            model.addAttribute("user", user);
            return "UserPage";
        } catch (NumberFormatException e) {
            model.addAttribute("errorMessage", "Invalid user ID format. Please provide a valid number.");
            return "UserPage";
        }
    }
}