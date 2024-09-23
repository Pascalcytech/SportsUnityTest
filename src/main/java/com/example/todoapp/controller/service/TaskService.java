package com.example.todoapp.controller.service;

import org.springframework.stereotype.Service;

import com.example.todoapp.controller.model.Task;

import java.util.*;

@Service
public class TaskService {

    private Map<Long, Task> taskStore = new HashMap<>();

    public Task createTask(Task task) {
        task.setId(generateTaskId());
        taskStore.put(task.getId(), task);
        return task;
    }

    public List<Task> getTasks() {
        return new ArrayList<>(taskStore.values());
    }

    public Task updateTask(Long taskId, Task updatedTask) {
        Task task = taskStore.get(taskId);
        task.setDescription(updatedTask.getDescription());
        task.setCompleted(updatedTask.isCompleted());
        return task;
    }

    public void deleteTask(Long taskId) {
        taskStore.remove(taskId);
    }

    private Long generateTaskId() {
        return (long) (taskStore.size() + 1);
    }
}
