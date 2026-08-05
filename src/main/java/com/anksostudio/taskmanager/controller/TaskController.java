package com.anksostudio.taskmanager.controller;


import com.anksostudio.taskmanager.dto.TaskCreateReqDto;
import com.anksostudio.taskmanager.dto.TaskRespDto;
import com.anksostudio.taskmanager.dto.TaskStatusUpdateReqDto;
import com.anksostudio.taskmanager.model.Status;
import com.anksostudio.taskmanager.model.Task;
import com.anksostudio.taskmanager.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/task")
public class TaskController {

    private TaskService taskService;

    public TaskController(TaskService taskService){
        this.taskService = taskService;
    }

    @PostMapping("/create")
    public ResponseEntity<TaskRespDto> createTask(@Valid @RequestBody TaskCreateReqDto reqDto){
        TaskRespDto taskCreated = taskService.createTask(reqDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(taskCreated);
    }

    @GetMapping
    public ResponseEntity<Page<TaskRespDto>> getTasks(
            @RequestParam(required = false) Long projectId,
            Pageable pageable
            ){
        Page<TaskRespDto> tasks = taskService.getTask(projectId, pageable);

        return ResponseEntity.ok(tasks);
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<TaskRespDto> updateTask(@Valid @RequestBody TaskStatusUpdateReqDto updateReqDto, @PathVariable Long id){

        TaskRespDto updatedTask = taskService.updateTaskStatus(id, updateReqDto);

        return ResponseEntity.ok(updatedTask);
    }


}
