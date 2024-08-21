package taskmanager.taskmanager;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import taskmanager.taskmanager.entities.Task;
import taskmanager.taskmanager.entities.User;
import taskmanager.taskmanager.repositories.TaskRepository;
import taskmanager.taskmanager.repositories.UserRepository;
import taskmanager.taskmanager.services.UserService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class TaskManagerApplicationTests {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        taskRepository.deleteAll();
    }
    @AfterEach
    void teardown()
    {
        userRepository.deleteAll();
        taskRepository.deleteAll();
    }
    @Test
    void testAddAndRetrieveUser() throws Exception {
        // Create and add a user
        User user = new User();
        user.setUsername("testUser");
        user.setPassword(String.valueOf("password".hashCode())); // Simulate hashed password
        userService.addUser(user);

        // Retrieve the user
        User retrievedUser = userService.getUserById("testUser");
        assertEquals("testUser", retrievedUser.getUsername());
    }

    @Test
    void testAddAndRetrieveTask() throws Exception {
        // Create and add a user
        User user = new User("testUser",String.valueOf("password".hashCode()));
        userRepository.save(user);

        // Create and add a task
        Task task = new Task("title","discr","12/12","pending",user);

        taskRepository.save(task);
        // Retrieve and verify the task
        User retrievedUser = userService.getUserById("testUser");

        assertEquals(true,retrievedUser.getTasks().stream()
                .anyMatch(eachTask -> eachTask.getId().equals(task.getId())));
    }

    @Test
    void testUpdateTaskStatus() throws Exception {
        // Create and add a user and a task
        User user = new User();
        user.setUsername("testUser");
        user.setPassword(String.valueOf("password".hashCode()));
        userService.addUser(user);

        Task task = new Task("title","discr","12/12","pending",user);
        taskRepository.save(task);

        // Update the task status
        userService.updateTaskStatus(task.getId(), "Completed", task.getUser().getUsername());

        // Verify the task status
        Task updatedTask = taskRepository.findById(task.getId()).orElseThrow();
        assertEquals("Completed", updatedTask.getStatus());
    }

    @Test
    void testRemoveTask() throws Exception {
        // Create and add a user and a task
        User user = new User("testUser",String.valueOf("password".hashCode()));

        userRepository.save(user);

        Task task = new Task("title","discr","12/12","pending",user);
        taskRepository.save(task);

        // Remove the task
        userService.removeTask(task.getId(), "testUser");

        // Verify the task is removed
        assertThrows(RuntimeException.class, () -> taskRepository.findById(task.getId()).orElseThrow());
    }
}
