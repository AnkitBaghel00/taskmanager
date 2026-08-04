package com.anksostudio.taskmanager.service.Impl;

import com.anksostudio.taskmanager.repository.TaskRepository;
import com.anksostudio.taskmanager.service.TaskService;
import org.springframework.stereotype.Service;


@Service
public class TaskServiceImpl implements TaskService {

    private TaskRepository taskRepository;

    public TaskServiceImpl(TaskRepository taskRepository){
        this.taskRepository = taskRepository;
    }



}
