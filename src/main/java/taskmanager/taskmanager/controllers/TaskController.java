package taskmanager.taskmanager.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import taskmanager.taskmanager.entities.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import taskmanager.taskmanager.services.*;
import taskmanager.taskmanager.exceptions.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class TaskController {
    private final UserService userService;

    @Autowired
    public TaskController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/tasks/{userId}")
    public List<Task> getAllTasks(@PathVariable String userId) {
        try {
            List<Task> tasks = userService.getAllTasks(userId);
            return tasks;
        } catch (NotExistentUser e) {
            return null;
        }
    }
    @PostMapping("/tasks")
    public ResponseEntity<String> addingTask(@RequestBody Map<String, Object> taskData) throws NotExistentUser {
        String title = (String) taskData.get("title");
        String description = (String) taskData.get("description");
        String status = (String) taskData.get("status");
        String localDate = (String) taskData.get("localDate");
        String username = (String) taskData.get("username");

        User user = userService.getUserById(username);
        Task task = new Task(title,description,localDate,status,user);
        userService.addTask(user.getUsername(),task);

        return ResponseEntity.ok("Task created successfully");
    }

    @PutMapping(value = "/tasks/{taskId}")
    public ResponseEntity<String> updateTask(@RequestBody Map<String, Object> taskData,
                                        @PathVariable String taskId) {

        String newStatus = (String) taskData.get("status");
        userService.updateTaskStatus(Long.valueOf(taskId),newStatus,userService.getTaskById(Long.valueOf(taskId)).getUser().getUsername());

        return ResponseEntity.ok("Task updated successfully");

    }

    @DeleteMapping(value = "/tasks/{taskId}")
    public ResponseEntity<String> deleteTask(@PathVariable String taskId){
        userService.removeTask(Long.valueOf(taskId),userService.getTaskById(Long.valueOf(taskId)).getUser().getUsername());
        return ResponseEntity.ok("Task removed successfully");

    }

}