package com.example.todoapp.controller.service;

import com.example.todoapp.controller.TodoListAppApplication;
import com.example.todoapp.controller.model.Task;
import com.example.todoapp.controller.model.User;
import com.example.todoapp.controller.repository.TaskRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

@SpringBootTest // Loads the full application context
@ContextConfiguration(classes = TodoListAppApplication.class)
class TaskServiceTest {

    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskRepository taskRepository;

    @BeforeEach
    void setUp() {
        taskRepository.deleteAll(); // Clean the repository before each test
    }

    @Test
    void testGetTasksForStandardUser() {
        Task task1 = new Task(null, "Task 1", false, 1L, 101L); // belongs to user 1, company 101
        Task task2 = new Task(null, "Task 2", false, 2L, 101L); // belongs to user 2, company 101
        Task task3 = new Task(null, "Task 3", false, 1L, 102L); // belongs to user 1, company 102
        
        taskService.createTask(task1);
        taskService.createTask(task2);
        taskService.createTask(task3);

        // Standard user (only sees their own tasks)
        User standardUser = new User(1L, "Standard User", "Standard", 101L);
        List<Task> tasksForStandardUser = taskService.getTasksForUser(standardUser);

        assertEquals(1, tasksForStandardUser.size(), "Standard user should only see their own tasks");
        assertEquals("Task 1", tasksForStandardUser.get(0).getDescription());
    }

    @Test
    void testGetTasksForCompanyAdmin() {
        Task task1 = new Task(null, "Task 1", false, 1L, 101L); // belongs to user 1, company 101
        Task task2 = new Task(null, "Task 2", false, 2L, 101L); // belongs to user 2, company 101
        Task task3 = new Task(null, "Task 3", false, 3L, 102L); // belongs to user 3, company 102
        
        taskService.createTask(task1);
        taskService.createTask(task2);
        taskService.createTask(task3);

        // Company-Admin user (sees all tasks from their company)
        User companyAdmin = new User(2L, "Company Admin", "Company-Admin", 101L);
        List<Task> tasksForCompanyAdmin = taskService.getTasksForUser(companyAdmin);

        assertEquals(2, tasksForCompanyAdmin.size(), "Company-Admin should see all tasks from their company");
    }

    @Test
    void testGetTasksForSuperUser() {
        Task task1 = new Task(null, "Task 1", false, 1L, 101L); // belongs to user 1, company 101
        Task task2 = new Task(null, "Task 2", false, 2L, 101L); // belongs to user 2, company 101
        Task task3 = new Task(null, "Task 3", false, 3L, 102L); // belongs to user 3, company 102
        
        taskService.createTask(task1);
        taskService.createTask(task2);
        taskService.createTask(task3);

        // Super user (sees all tasks)
        User superUser = new User(3L, "Super User", "Super", null);
        List<Task> tasksForSuperUser = taskService.getTasksForUser(superUser);

        assertEquals(3, tasksForSuperUser.size(), "Super user should see all tasks across companies and users");
    }
}
