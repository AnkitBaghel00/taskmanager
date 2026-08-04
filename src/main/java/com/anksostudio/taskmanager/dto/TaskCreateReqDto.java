package com.anksostudio.taskmanager.dto;

import com.anksostudio.taskmanager.model.Priority;
import com.anksostudio.taskmanager.model.Status;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class TaskCreateReqDto {

    @NotBlank(message = "Title is required")
    private String title;
    @NotBlank(message = "Description is required")
    private String description;

    private Status status;
    private Priority priority;

    private Long projectId;
    private Long assignedToId;

    private LocalDate dueDate;

}
