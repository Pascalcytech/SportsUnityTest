package com.example.todoapp.controller.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.todoapp.controller.model.Task;
import com.example.todoapp.controller.model.User;
import com.example.todoapp.controller.repository.TaskRepository;

import java.util.List;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    // Get tasks based on the user's role
    public List<Task> getTasksForUser(User user) {
        if (user.getRole().equals("Super")) {
            // Super users can access all tasks
            return taskRepository.findAll();
        } else if (user.getRole().equals("Company-Admin")) {
            // Company-Admin users can access tasks of all users in the same company
            return taskRepository.findByCompanyId(user.getCompanyId());
        } else {
            // Standard users can only access their own tasks
            return taskRepository.findByUserId(user.getId());
        }
    }

    // Create a new task
    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    // Update task with role-based restriction
    public Task updateTask(Long taskId, Task updatedTask, User user) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        // Role-based update permission logic:
        if (user.getRole().equals("Super")) {
            // Super users can update any task
            return updateTaskFields(task, updatedTask);
        } else if (user.getRole().equals("Company-Admin")) {
            // Company-Admin users can update tasks within their company
            if (task.getCompanyId().equals(user.getCompanyId())) {
                return updateTaskFields(task, updatedTask);
            } else {
                throw new RuntimeException("Permission denied: Cannot update tasks outside your company.");
            }
        } else {
            // Standard users can only update their own tasks
            if (task.getUserId().equals(user.getId())) {
                return updateTaskFields(task, updatedTask);
            } else {
                throw new RuntimeException("Permission denied: Cannot update other users' tasks.");
            }
        }
    }

    // Delete task with role-based restriction
    public void deleteTask(Long taskId, User user) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        // Role-based delete permission logic:
        if (user.getRole().equals("Super")) {
            // Super users can delete any task
            taskRepository.delete(task);
        } else if (user.getRole().equals("Company-Admin")) {
            // Company-Admin users can delete tasks within their company
            if (task.getCompanyId().equals(user.getCompanyId())) {
                taskRepository.delete(task);
            } else {
                throw new RuntimeException("Permission denied: Cannot delete tasks outside your company.");
            }
        } else {
            // Standard users can only delete their own tasks
            if (task.getUserId().equals(user.getId())) {
                taskRepository.delete(task);
            } else {
                throw new RuntimeException("Permission denied: Cannot delete other users' tasks.");
            }
        }
    }

    // Helper method to update task fields
    private Task updateTaskFields(Task existingTask, Task updatedTask) {
        existingTask.setDescription(updatedTask.getDescription());
        existingTask.setCompleted(updatedTask.isCompleted());
        return taskRepository.save(existingTask);
    }
}