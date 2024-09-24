package com.example.todoapp.controller.service;

import org.springframework.stereotype.Service;

import com.example.todoapp.controller.model.Task;
import com.example.todoapp.controller.model.User;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TaskService {

    private Map<Long, Task> taskStore = new HashMap<>();

    public Task createTask(Task task) {
        task.setId(generateTaskId());
        taskStore.put(task.getId(), task);
        return task;
    }

    public List<Task> getTasksForUser(User user) {
        if (user.getRole().equals("Super")) {
            return new ArrayList<>(taskStore.values());
        } else if (user.getRole().equals("Company-Admin")) {
            return taskStore.values().stream()
                    .filter(task -> task.getCompanyId().equals(user.getCompanyId()))
                    .collect(Collectors.toList());
        } else {
            return taskStore.values().stream()
                    .filter(task -> task.getUserId().equals(user.getId()))
                    .collect(Collectors.toList());
        }
    }

    public Task updateTask(Long taskId, Task updatedTask, User user) {
        Task task = taskStore.get(taskId);

        if (task != null && canUserModifyTask(task, user)) {
            task.setDescription(updatedTask.getDescription());
            task.setCompleted(updatedTask.isCompleted());
            return task;
        } else {
            throw new RuntimeException("Task not found or permission denied.");
        }
    }

    public void deleteTask(Long taskId, User user) {
        Task task = taskStore.get(taskId);
        if (task != null && canUserModifyTask(task, user)) {
            taskStore.remove(taskId);
        } else {
            throw new RuntimeException("Task not found or permission denied.");
        }
    }

    private Long generateTaskId() {
        return (long) (taskStore.size() + 1);
    }
    
    private boolean canUserModifyTask(Task task, User user) {
        if (user.getRole().equals("Super")) {
            return true;
        }

        if (user.getRole().equals("Company-Admin") && task.getCompanyId().equals(user.getCompanyId())) {
            return true;
        }
        
        return user.getRole().equals("Standard") && task.getUserId().equals(user.getId());
    }
}
