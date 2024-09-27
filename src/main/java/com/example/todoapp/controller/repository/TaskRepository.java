package com.example.todoapp.controller.repository;

import com.example.todoapp.controller.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByUserIdAndCompanyId(Long userId, Long companyId);
    List<Task> findByCompanyId(Long companyId);
}