package com.example.todoapp.controller.model;

public class Task {
	private Long id;
    private String description;
    private Long userId;
    private Long companyId;
    private boolean completed;
    
    public Task(Long id, String description, boolean completed, Long userId, Long companyId) {
        this.id = id;
        this.description = description;
        this.completed = completed;
        this.userId = userId;
        this.companyId = companyId;
    }
    
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
