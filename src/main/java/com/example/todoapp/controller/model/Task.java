package com.example.todoapp.controller.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity // Indicates that this class is a JPA entity
public class Task {

    @Id // Specifies the primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Automatically generate the ID
    private Long id;

    @Column(nullable = false) // Indicates that description cannot be null
    private String description;

    private Long userId;

    private Long companyId;

    private boolean completed;

    // Default constructor for JPA
    public Task() {
    }

    // Parameterized constructor
    public Task(Long id, String description, boolean completed, Long userId, Long companyId) {
        this.id = id;
        this.description = description;
        this.completed = completed;
        this.userId = userId;
        this.companyId = companyId;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public Long getCompanyId() {
        return companyId;
    }
    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }
    public boolean isCompleted() {
        return completed;
    }
    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
