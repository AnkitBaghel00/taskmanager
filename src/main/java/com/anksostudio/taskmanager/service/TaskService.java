package com.anksostudio.taskmanager.service;

import com.anksostudio.taskmanager.dto.TaskCreateReqDto;
import com.anksostudio.taskmanager.dto.TaskRespDto;
import com.anksostudio.taskmanager.dto.TaskStatusUpdateReqDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TaskService {

    TaskRespDto createTask(TaskCreateReqDto createReqDto);

    Page<TaskRespDto> getTask(Long projectId, Pageable pageable);

//    Page<TaskRespDto> getTask(Long projectId, Status status, Long assignedToId, Pageable pageable);

    TaskRespDto updateTaskStatus(Long taskId, TaskStatusUpdateReqDto statusUpdateReqDto);
}

