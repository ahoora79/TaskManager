package taskmanager.taskmanager.controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import taskmanager.taskmanager.entities.User;
import taskmanager.taskmanager.exceptions.*;
import taskmanager.taskmanager.services.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;


@Controller
@CrossOrigin(origins = "http://localhost:63342")
public class AuthController {

    private final UserService userService;
    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }



    @GetMapping(value = "/login")
    public String loginPage(Model model) throws UsernameAlreadyTaken {
        return "login";

    }

    @GetMapping(value = "/signup")
    public String signupPage(Model model) throws UsernameAlreadyTaken {
        return "signup";

    }



    @PostMapping(value = "/login")
    public String login(@RequestParam String username, @RequestParam String password, HttpServletRequest request) {
        try {
            User user = userService.login(username, password);
            HttpSession session = request.getSession(true);
            session.setAttribute("user", user);

            return "redirect:/home";
        } catch (NotExistentUser | IncorrectPassword e) {
            return "redirect:/error";
        }
    }


    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public String signup(@RequestParam String username, @RequestParam String password, HttpServletRequest request) {

        User newUser = new User(username, password);
        try {
            userService.addUser(newUser);

            // Create session and add assign user to it
            HttpSession session = request.getSession(true);
            session.setAttribute("user", newUser);
            System.out.println("Session created for user: " + session.getAttribute("user"));
            return "redirect:/login";
        } catch (UsernameAlreadyTaken e) {
            return "redirect:/error";
        }
    }

    @GetMapping(value = "/logout/{userId}")
    public String logout(@PathVariable String userId, HttpServletRequest request) {
        HttpSession session = request.getSession(false); // don't create a session if it doesn't exist

        // If a session exists, invalidate it
        if (session != null)
            session.invalidate();

        return "redirect:/login";
    }

}