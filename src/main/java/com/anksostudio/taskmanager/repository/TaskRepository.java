package com.anksostudio.taskmanager.repository;

import com.anksostudio.taskmanager.model.Status;
import com.anksostudio.taskmanager.model.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {

    Page<Task> findByProject_Id(Long projectId, Pageable pageable);
    Page<Task> findByStatus(Status status, Pageable pageable);
    Page<Task> findByAssignedTo_Id(Long assignedToId, Pageable pageable);
    Page<Task> findByProject_IdAndStatus(Long projectId, Status status, Pageable pageable);
}
