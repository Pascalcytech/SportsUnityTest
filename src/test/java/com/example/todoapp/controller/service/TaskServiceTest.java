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
    

    @Test
    void testCreateTaskForStandardUser() {
        // Standard user creates a task
        User standardUser = new User(1L, "Standard User", "Standard", 101L);
        Task newTask = new Task(null, "New Task", false, standardUser.getId(), standardUser.getCompanyId());

        Task createdTask = taskService.createTask(newTask);

        assertNotNull(createdTask.getId(), "Created task should have an ID");
        assertEquals("New Task", createdTask.getDescription(), "Task description should match");
        assertEquals(standardUser.getId(), createdTask.getUserId(), "Task user ID should match the creator");
        assertEquals(standardUser.getCompanyId(), createdTask.getCompanyId(), "Task company ID should match the creator's company");
    }

    @Test
    void testCreateTaskForCompanyAdmin() {
        // Company-Admin creates a task
        User companyAdmin = new User(2L, "Company Admin", "Company-Admin", 101L);
        Task newTask = new Task(null, "Admin Task", false, companyAdmin.getId(), companyAdmin.getCompanyId());

        Task createdTask = taskService.createTask(newTask);

        assertNotNull(createdTask.getId(), "Created task should have an ID");
        assertEquals("Admin Task", createdTask.getDescription(), "Task description should match");
        assertEquals(companyAdmin.getId(), createdTask.getUserId(), "Task user ID should match the creator");
        assertEquals(companyAdmin.getCompanyId(), createdTask.getCompanyId(), "Task company ID should match the creator's company");
    }

    @Test
    void testCreateTaskForSuperUser() {
        // Super user creates a task
        User superUser = new User(3L, "Super User", "Super", null);
        Task newTask = new Task(null, "Super Task", false, superUser.getId(), 101L); // Assign to company 101

        Task createdTask = taskService.createTask(newTask);

        assertNotNull(createdTask.getId(), "Created task should have an ID");
        assertEquals("Super Task", createdTask.getDescription(), "Task description should match");
        assertEquals(superUser.getId(), createdTask.getUserId(), "Task user ID should match the creator");
        assertEquals(101L, createdTask.getCompanyId(), "Task company ID should match the assigned company");
    }
    @Test
    void testUpdateTaskByStandardUser() {
        // Set up user and task
        User standardUser = new User(1L, "Standard User", "Standard", 101L);
        Task task = new Task(null, "Task to be updated", false, standardUser.getId(), standardUser.getCompanyId());
        Task existingTask = taskService.createTask(task);

        // Update task details
        Task updatedTask = new Task(null, "Updated Task Description", true, standardUser.getId(), standardUser.getCompanyId());
        Task result = taskService.updateTask(existingTask.getId(), updatedTask, standardUser);

        // Assert updated task details
        assertEquals("Updated Task Description", result.getDescription());
        assertTrue(result.isCompleted());
    }

    @Test
    void testUpdateTaskByCompanyAdmin() {
        // Set up users and task
        User companyAdmin = new User(2L, "Company Admin", "Company-Admin", 101L);
        User standardUser = new User(1L, "Standard User", "Standard", 101L);
        Task task = new Task(null, "Task for Admin to update", false, standardUser.getId(), standardUser.getCompanyId());
        Task existingTask = taskService.createTask(task);

        // Update task details by company admin
        Task updatedTask = new Task(null, "Updated by Admin", true, standardUser.getId(), standardUser.getCompanyId());
        Task result = taskService.updateTask(existingTask.getId(), updatedTask, companyAdmin);

        // Assert updated task details
        assertEquals("Updated by Admin", result.getDescription());
        assertTrue(result.isCompleted());
    }

    @Test
    void testUpdateTaskBySuperUser() {
        // Set up user and task
        User superUser = new User(3L, "Super User", "Super", null);
        User standardUser = new User(1L, "Standard User", "Standard", 101L);
        Task task = new Task(null, "Task for Super User to update", false, standardUser.getId(), standardUser.getCompanyId());
        Task existingTask = taskService.createTask(task);

        // Update task details by super user
        Task updatedTask = new Task(null, "Updated by Super User", true, standardUser.getId(), standardUser.getCompanyId());
        Task result = taskService.updateTask(existingTask.getId(), updatedTask, superUser);

        // Assert updated task details
        assertEquals("Updated by Super User", result.getDescription());
        assertTrue(result.isCompleted());
    }

    @Test
    void testUpdateTaskByCompanyAdminOutsideCompany() {
        // Set up users and task
        User companyAdmin = new User(2L, "Company Admin", "Company-Admin", 101L);
        User standardUser = new User(1L, "Standard User", "Standard", 102L); // Different company
        Task task = new Task(null, "Task in another company", false, standardUser.getId(), standardUser.getCompanyId());
        Task existingTask = taskService.createTask(task);

        // Attempt to update task by company admin
        Task updatedTask = new Task(null, "Trying to update from another company", true, standardUser.getId(), standardUser.getCompanyId());
        Exception exception = assertThrows(RuntimeException.class, () -> {
            taskService.updateTask(existingTask.getId(), updatedTask, companyAdmin);
        });

        // Assert exception message
        assertEquals("Permission denied: Cannot update tasks outside your company.", exception.getMessage());
    }

    @Test
    void testUpdateTaskByStandardUserNotOwner() {
        // Set up users and task
        User standardUser = new User(1L, "Standard User", "Standard", 101L);
        User anotherStandardUser = new User(2L, "Another Standard User", "Standard", 101L);
        Task task = new Task(null, "Task to be updated", false, anotherStandardUser.getId(), anotherStandardUser.getCompanyId());
        Task existingTask = taskService.createTask(task);

        // Attempt to update task by a standard user who does not own the task
        Task updatedTask = new Task(null, "Trying to update another user's task", true, anotherStandardUser.getId(), anotherStandardUser.getCompanyId());
        Exception exception = assertThrows(RuntimeException.class, () -> {
            taskService.updateTask(existingTask.getId(), updatedTask, standardUser);
        });

        // Assert exception message
        assertEquals("Permission denied: Cannot update other users' tasks.", exception.getMessage());
    }
    

    @Test
    void testDeleteTaskByStandardUser() {
        // Set up user and task
        User standardUser = new User(1L, "Standard User", "Standard", 101L);
        Task task = new Task(null, "Task to be deleted", false, standardUser.getId(), standardUser.getCompanyId());
        Task existingTask = taskService.createTask(task);

        // Delete task by standard user
        taskService.deleteTask(existingTask.getId(), standardUser);

        // Assert task is deleted
        assertThrows(RuntimeException.class, () -> {
            taskService.updateTask(existingTask.getId(), existingTask, standardUser);
        });
    }

    @Test
    void testDeleteTaskByCompanyAdmin() {
        // Set up users and task
        User companyAdmin = new User(2L, "Company Admin", "Company-Admin", 101L);
        User standardUser = new User(1L, "Standard User", "Standard", 101L);
        Task task = new Task(null, "Task for Admin to delete", false, standardUser.getId(), standardUser.getCompanyId());
        Task existingTask = taskService.createTask(task);

        // Delete task by company admin
        taskService.deleteTask(existingTask.getId(), companyAdmin);

        // Assert task is deleted
        assertThrows(RuntimeException.class, () -> {
            taskService.updateTask(existingTask.getId(), existingTask, companyAdmin);
        });
    }

    @Test
    void testDeleteTaskBySuperUser() {
        // Set up user and task
        User superUser = new User(3L, "Super User", "Super", null);
        User standardUser = new User(1L, "Standard User", "Standard", 101L);
        Task task = new Task(null, "Task for Super User to delete", false, standardUser.getId(), standardUser.getCompanyId());
        Task existingTask = taskService.createTask(task);

        // Delete task by super user
        taskService.deleteTask(existingTask.getId(), superUser);

        // Assert task is deleted
        assertThrows(RuntimeException.class, () -> {
            taskService.updateTask(existingTask.getId(), existingTask, superUser);
        });
    }

    @Test
    void testDeleteTaskByCompanyAdminOutsideCompany() {
        // Set up users and task
        User companyAdmin = new User(2L, "Company Admin", "Company-Admin", 101L);
        User standardUser = new User(1L, "Standard User", "Standard", 102L); // Different company
        Task task = new Task(null, "Task in another company", false, standardUser.getId(), standardUser.getCompanyId());
        Task existingTask = taskService.createTask(task);

        // Attempt to delete task by company admin
        Exception exception = assertThrows(RuntimeException.class, () -> {
            taskService.deleteTask(existingTask.getId(), companyAdmin);
        });

        // Assert exception message
        assertEquals("Permission denied: Cannot delete tasks outside your company.", exception.getMessage());
    }

    @Test
    void testDeleteTaskByStandardUserNotOwner() {
        // Set up users and task
        User standardUser = new User(1L, "Standard User", "Standard", 101L);
        User anotherStandardUser = new User(2L, "Another Standard User", "Standard", 101L);
        Task task = new Task(null, "Task to be deleted by another user", false, anotherStandardUser.getId(), anotherStandardUser.getCompanyId());
        Task existingTask = taskService.createTask(task);

        // Attempt to delete task by a standard user who does not own the task
        Exception exception = assertThrows(RuntimeException.class, () -> {
            taskService.deleteTask(existingTask.getId(), standardUser);
        });

        // Assert exception message
        assertEquals("Permission denied: Cannot delete other users' tasks.", exception.getMessage());
    }
}
