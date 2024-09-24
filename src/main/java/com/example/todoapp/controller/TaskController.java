package com.example.todoapp.controller;

import com.example.todoapp.controller.model.Task;
import com.example.todoapp.controller.model.User;
import com.example.todoapp.controller.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    // La méthode getTasks modifiée pour accepter le rôle dans la requête
    @GetMapping
    public List<Task> getTasks(@RequestParam String role) {
        User currentUser;

        // Simulation des différents types d'utilisateurs selon le rôle
        switch (role) {
            case "Super":
                currentUser = new User(3L, "Super User", "Super", 0L);
                break;
            case "Company-Admin":
                currentUser = new User(2L, "Company Admin", "Company-Admin", 101L); // même companyId pour les tests
                break;
            default:
                currentUser = new User(1L, "Standard User", "Standard", 101L);
        }

        return taskService.getTasksForUser(currentUser);
    }

    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task, @RequestParam String role) {
        User currentUser;

        // Simulation des rôles dans createTask aussi
        switch (role) {
            case "Super":
                currentUser = new User(3L, "Super User", "Super", 0L);
                break;
            case "Company-Admin":
                currentUser = new User(2L, "Company Admin", "Company-Admin", 101L);
                break;
            default:
                currentUser = new User(1L, "Standard User", "Standard", 101L);
        }

        task.setUserId(currentUser.getId());
        task.setCompanyId(currentUser.getCompanyId());
        return ResponseEntity.ok(taskService.createTask(task));
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<Task> updateTask(@PathVariable Long taskId, @RequestBody Task task, @RequestParam String role) {
        User currentUser;

        switch (role) {
            case "Super":
                currentUser = new User(3L, "Super User", "Super", 0L);
                break;
            case "Company-Admin":
                currentUser = new User(2L, "Company Admin", "Company-Admin", 101L);
                break;
            default:
                currentUser = new User(1L, "Standard User", "Standard", 101L);
        }

        return ResponseEntity.ok(taskService.updateTask(taskId, task, currentUser));
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long taskId, @RequestParam String role) {
        User currentUser;

        switch (role) {
            case "Super":
                currentUser = new User(3L, "Super User", "Super", 0L);
                break;
            case "Company-Admin":
                currentUser = new User(2L, "Company Admin", "Company-Admin", 101L);
                break;
            default:
                currentUser = new User(1L, "Standard User", "Standard", 101L);
        }

        taskService.deleteTask(taskId, currentUser);
        return ResponseEntity.noContent().build();
    }
}
