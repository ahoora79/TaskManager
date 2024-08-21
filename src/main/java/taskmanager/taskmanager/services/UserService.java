package taskmanager.taskmanager.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import taskmanager.taskmanager.repositories.*;
import taskmanager.taskmanager.entities.*;
import taskmanager.taskmanager.exceptions.*;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    @Autowired
    public UserService(UserRepository userRepository,TaskRepository taskRepository) {
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
    }

    public User getUserById(String id) throws NotExistentUser {
        return userRepository.findById(id)
                .orElseThrow(NotExistentUser::new);
    }
    public Task getTaskById(Long id) throws NoClassDefFoundError {
        return taskRepository.findById(id)
                .orElseThrow(NoClassDefFoundError::new);
    }
    public void addUser(User user) throws UsernameAlreadyTaken {
        String username = user.getUsername();

        if (userRepository.existsById(username)) {
            throw new UsernameAlreadyTaken();
        }
        user.setPassword(String.valueOf(user.getPassword().hashCode()));
        userRepository.save(user);
    }
    public User login(String username, String password) throws NotExistentUser, IncorrectPassword {
        if (!userRepository.existsById(username)) {
            throw new NotExistentUser();
        }

        User user = getUserById(username);
        String hasPassword = Integer.toString(password.hashCode());
        if (!hasPassword.equals(user.getPassword())) {
            throw new IncorrectPassword();
        }
        return  user;
    }

    @Cacheable(value = "tasks", key = "#userId")
    public List<Task> getAllTasks(String userId) throws NotExistentUser{
            User user = this.getUserById(userId);
            return user.getTasks();
    }


    @CacheEvict(value = "tasks", key = "#username")
    @Transactional
    public void addTask(String username , Task task){
        try {
            User user = this.getUserById(username);
            user.getTasks().add(task);
            task.setUser(user);
            userRepository.save(user);// This will cascade and save the task not needed taskRepo.save
            ///taskRepository.save(task);
        }catch (Exception e){
            System.out.println("The Exception is " + e.getMessage());
        }
    }



    @CacheEvict(value = "tasks", key = "#username")
    @Transactional
    public void updateTaskStatus(Long taskId, String status,String username) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new RuntimeException("Task not found"));
        if (task != null) {
            task.setStatus(status);
            taskRepository.save(task);
        }
    }
    @CacheEvict(value = "tasks", key = "#username")
    @Transactional
    public void removeTask(Long taskId,String username){
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        User user = task.getUser();
        if (user != null) {
            user.getTasks().remove(task);
            userRepository.save(user); // Save the user to update their task list
        }

        taskRepository.delete(task); //delete the task
    }


}