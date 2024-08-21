package taskmanager.taskmanager.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import taskmanager.taskmanager.entities.Task;
import taskmanager.taskmanager.entities.User;
import taskmanager.taskmanager.services.UserService;

import java.util.Date;

@Controller
public class HomeController {

    private final UserService userService;
    @Autowired
    public HomeController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/home")
    public String home(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            model.addAttribute("username", user.getUsername());
            return "home"; // This resolves to home.html in the templates directory
        }
        return "redirect:/login";
    }
}
