package com.anksostudio.taskmanager.dto;

import com.anksostudio.taskmanager.model.Status;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskStatusUpdateReqDto {
    @NotNull(message = "Status is required")
    private Status status;
}
