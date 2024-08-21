package services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import taskmanager.taskmanager.entities.Task;
import taskmanager.taskmanager.entities.User;
import taskmanager.taskmanager.exceptions.*;
import taskmanager.taskmanager.repositories.TaskRepository;
import taskmanager.taskmanager.repositories.UserRepository;
import taskmanager.taskmanager.services.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private UserService userService;

    //Prepare Test Fixture
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    //Test for get users
    @Test
    void testGetUserById_UserExists() throws NotExistentUser {

        String userId = "testUser";
        User user = new User();
        user.setUsername(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));


        User result = userService.getUserById(userId);


        assertEquals(user, result);
    }

    @Test
    void testGetUserById_UserDoesNotExist() {

        String userId = "nonexistentUser";
        when(userRepository.findById(userId)).thenReturn(Optional.empty());


        assertThrows(NotExistentUser.class, () -> userService.getUserById(userId));
    }

    // Test for addUser
    @Test
    void testAddUser_Success() throws UsernameAlreadyTaken {

        User user = new User();
        user.setUsername("testUser");
        user.setPassword("password");
        when(userRepository.existsById(user.getUsername())).thenReturn(false);


        userService.addUser(user);


        verify(userRepository, times(1)).save(user);
        assertEquals(String.valueOf("password".hashCode()), user.getPassword()); // Ensure password is hashed
    }

    @Test
    void testAddUser_UsernameAlreadyTaken() {

        User user = new User();
        user.setUsername("testUser");
        when(userRepository.existsById(user.getUsername())).thenReturn(true);


        assertThrows(UsernameAlreadyTaken.class, () -> userService.addUser(user));
    }

    // Test for login
    @Test
    void testLogin_Success() throws NotExistentUser, IncorrectPassword {

        String username = "testUser";
        String password = "password";
        User user = new User();
        user.setUsername(username);
        user.setPassword(String.valueOf(password.hashCode()));
        when(userRepository.existsById(username)).thenReturn(true);
        when(userRepository.findById(username)).thenReturn(Optional.of(user));


        assertDoesNotThrow(() -> userService.login(username, password));

    }

    @Test
    void testLogin_UserDoesNotExist() {

        String username = "nonexistentUser";
        String password = "password";
        when(userRepository.existsById(username)).thenReturn(false);


        assertThrows(NotExistentUser.class, () -> userService.login(username, password));
    }

    @Test
    void testLogin_IncorrectPassword() {

        String username = "testUser";
        String password = "password";
        User user = new User();
        user.setUsername(username);
        user.setPassword(String.valueOf("wrongPassword".hashCode()));
        when(userRepository.existsById(username)).thenReturn(true);
        when(userRepository.findById(username)).thenReturn(Optional.of(user));


        assertThrows(IncorrectPassword.class, () -> userService.login(username, password));
    }

    // Test for getAllTasks
    @Test
    void testGetAllTasks_Success() throws NotExistentUser {

        String userId = "testUser";
        User user = new User();
        user.setUsername(userId);
        List<Task> tasks = new ArrayList<>();
        Task task = new Task();
        task.setId(1L);
        tasks.add(task);
        user.setTasks(tasks);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));


        List<Task> result = userService.getAllTasks(userId);


        assertEquals(tasks, result);
    }

    @Test
    void testGetAllTasks_UserDoesNotExist() {

        String userId = "nonexistentUser";
        when(userRepository.findById(userId)).thenReturn(Optional.empty());


        assertThrows(NotExistentUser.class, () -> userService.getAllTasks(userId));
    }

    // Test for addTask
    @Test
    void testAddTask_Success() throws NotExistentUser {
        String username = "testUser";
        User user = new User();
        user.setUsername(username);
        when(userRepository.findById(username)).thenReturn(Optional.of(user));
        Task task = new Task();
        task.setId(1L);


        userService.addTask(username, task);


        assertTrue(user.getTasks().contains(task));
        assertEquals(user, task.getUser());
    }

    // Test for updateTaskStatus
    @Test
    void testUpdateTaskStatus_Success() {
        String taskId = "task1";
        String newStatus = "Completed";
        String username = "testUser";
        Task task = new Task();
        task.setId(1L);
        task.setStatus("Pending");
        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));


        userService.updateTaskStatus(task.getId(), newStatus, username);


        assertEquals(newStatus, task.getStatus());
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    void testUpdateTaskStatus_TaskNotFound() {

        Long taskId = -1L;
        String newStatus = "Completed";
        String username = "testUser";
        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());


        assertThrows(RuntimeException.class, () -> userService.updateTaskStatus(taskId, newStatus, username));
    }

    // Test for removeTask
    @Test
    void testRemoveTask_Success() {

        Long taskId = 1L;
        String username = "testUser";
        Task task = new Task();
        task.setId(1L);
        User user = new User();
        user.setUsername(username);
        user.getTasks().add(task);
        task.setUser(user);
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));


        userService.removeTask(taskId, username);


        assertFalse(user.getTasks().contains(task));
        verify(taskRepository, times(1)).delete(task);
    }

    @Test
    void testRemoveTask_TaskNotFound() {

        Long taskId = -1L;
        String username = "testUser";
        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());


        assertThrows(RuntimeException.class, () -> userService.removeTask(taskId, username));
    }
}
