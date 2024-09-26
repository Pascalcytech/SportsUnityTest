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
    void testCreateTask() {
        Task task = new Task(null, "Task 1", false, 1L, 101L);
        Task createdTask = taskService.createTask(task);
        
        assertNotNull(createdTask.getId(), "Task ID should be generated");
        assertEquals("Task 1", createdTask.getDescription());
    }

    @Test
    void testGetTasksForUser_Standard() {
        Task task1 = new Task(null, "Task 1", false, 1L, 101L);
        Task task2 = new Task(null, "Task 2", false, 2L, 101L);
        
        taskService.createTask(task1);
        taskService.createTask(task2);

        User standardUser = new User(1L, "Standard User", "Standard", 101L);
        List<Task> tasksForStandardUser = taskService.getTasksForUser(standardUser);

        assertEquals(1, tasksForStandardUser.size(), "Standard user should only see their tasks");
        assertEquals("Task 1", tasksForStandardUser.get(0).getDescription());
    }

    @Test
    void testGetTasksForUser_CompanyAdmin() {
        Task task1 = new Task(null, "Task 1", false, 1L, 101L);
        Task task2 = new Task(null, "Task 2", false, 2L, 101L);
        Task task3 = new Task(null, "Task 3", false, 3L, 102L);
        
        taskService.createTask(task1);
        taskService.createTask(task2);
        taskService.createTask(task3);

        User companyAdmin = new User(2L, "Company Admin", "Company-Admin", 101L);
        List<Task> tasksForCompanyAdmin = taskService.getTasksForUser(companyAdmin);

        assertEquals(2, tasksForCompanyAdmin.size(), "Company Admin should see all tasks from their company");
    }
}
