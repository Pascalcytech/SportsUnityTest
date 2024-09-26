package com.example.todoapp.controller.model;

public class User {
	private Long id;
    private String name;
    private String role; // Standard, CompanyAdmin, SuperUser
    private Long companyId;
    
    public User(Long id, String name, String role, Long companyId) {
    	this.id = id;
    	this.name = name;
    	this.role = role;
    	this.companyId = companyId;
    }
    
    // Getters and Setters
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public Long getCompanyId() {
		return companyId;
	}
	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}
}
