package com.anksostudio.taskmanager.dto;

import com.anksostudio.taskmanager.model.Priority;
import com.anksostudio.taskmanager.model.Project;
import com.anksostudio.taskmanager.model.Status;
import com.anksostudio.taskmanager.model.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class TaskRespDto {

    private Long id;

    private String title;

    private String description;

    private Status status;

    private Priority priority;

    private LocalDate dueDate;

    private Long projectId;
    private String projectTitle;
    private Long assignedToId;
    private String assignedToEmail;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
