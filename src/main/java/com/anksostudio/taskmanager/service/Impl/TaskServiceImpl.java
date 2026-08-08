package com.anksostudio.taskmanager.service.Impl;

import com.anksostudio.taskmanager.dto.TaskCreateReqDto;
import com.anksostudio.taskmanager.dto.TaskRespDto;
import com.anksostudio.taskmanager.dto.TaskStatusUpdateReqDto;
import com.anksostudio.taskmanager.exception.ResourceNotFoundException;
import com.anksostudio.taskmanager.model.*;
import com.anksostudio.taskmanager.repository.ProjectRepository;
import com.anksostudio.taskmanager.repository.TaskRepository;
import com.anksostudio.taskmanager.repository.UserRepository;
import com.anksostudio.taskmanager.service.TaskService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


@Service
public class TaskServiceImpl implements TaskService {

    private TaskRepository taskRepository;
    private UserRepository userRepository;
    private ProjectRepository projectRepository;

    public TaskServiceImpl(TaskRepository taskRepository, UserRepository userRepository, ProjectRepository projectRepository){
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
    }


    @Override
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public TaskRespDto createTask(TaskCreateReqDto createReqDto) {

        User assignedTo = userRepository.findById(createReqDto.getAssignedToId())
                .orElseThrow(() -> new ResourceNotFoundException("Assigned user not found"));

        Project project = projectRepository.findById(createReqDto.getProjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Project does not exists, please create an project first"));

        Task task = mapCreateReqToTask(createReqDto,assignedTo,project);
        Task savedTask = taskRepository.save(task);

        return mapTaskToResp(savedTask);

    }

    @Override
    public Page<TaskRespDto> getTask(Long projectId, Pageable pageable) {

        Page<Task> tasks;

        if(projectId != null){
            tasks = taskRepository.findByProject_Id(projectId, pageable);
        } else {
            tasks = taskRepository.findAll(pageable);
        }


        return tasks.map(this::mapTaskToResp);
    }

    @Override
    public TaskRespDto updateTaskStatus(Long taskId, TaskStatusUpdateReqDto statusUpdateReqDto) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        boolean isAssignee = task.getAssignedTo().getId().equals(currentUser.getId());
        boolean isMangerOrAdmin = currentUser.getRole() == Role.MANAGER || currentUser.getRole() == Role.ADMIN;

        if(!isAssignee && !isMangerOrAdmin){
            throw new AccessDeniedException("You are not allowed to update this task's status");
        }

        task.setStatus(statusUpdateReqDto.getStatus());

        Task updatedTask = taskRepository.save(task);


        return mapTaskToResp(updatedTask);
    }


    public Task mapCreateReqToTask(TaskCreateReqDto reqDto, User user, Project project){
        Task task = new Task();

        task.setTitle(reqDto.getTitle());
        task.setDescription(reqDto.getDescription());
        task.setStatus(reqDto.getStatus() !=null ? reqDto.getStatus() : Status.TODO);
        task.setPriority(reqDto.getPriority() !=null ? reqDto.getPriority() : Priority.MEDIUM);
        task.setDueDate(reqDto.getDueDate());
        task.setAssignedTo(user);
        task.setProject(project);

        return task;
    }

    public TaskRespDto mapTaskToResp(Task task){
        TaskRespDto respDto = new TaskRespDto();

        respDto.setId(task.getId());
        respDto.setTitle(task.getTitle());
        respDto.setDescription(task.getDescription());
        respDto.setStatus(task.getStatus());
        respDto.setPriority(task.getPriority());
        respDto.setDueDate(task.getDueDate());
        respDto.setProjectId(task.getProject().getId());
        respDto.setProjectTitle(task.getProject().getTitle());
        respDto.setAssignedToId(task.getAssignedTo().getId());
        respDto.setAssignedToEmail(task.getAssignedTo().getEmail());
        respDto.setCreatedAt(task.getCreatedAt());
        respDto.setUpdatedAt(task.getUpdatedAt());

        return respDto;
    }
}
