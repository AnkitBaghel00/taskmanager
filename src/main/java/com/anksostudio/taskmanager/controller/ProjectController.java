package com.anksostudio.taskmanager.controller;


import com.anksostudio.taskmanager.dto.CreateProjectRequestDto;
import com.anksostudio.taskmanager.dto.ProjectResponseDto;
import com.anksostudio.taskmanager.service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.jpa.persistenceunit.SpringPersistenceUnitInfo;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/project")
public class ProjectController {

    private ProjectService projectService;

    public ProjectController(ProjectService projectService){
        this.projectService = projectService;
    }

    @PostMapping("/create")
    public ResponseEntity<ProjectResponseDto> createProject(@Valid @RequestBody CreateProjectRequestDto projectRequestDto){

        ProjectResponseDto createdProject = projectService.createProject(projectRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdProject);
    }

    @GetMapping
    public ResponseEntity<List<ProjectResponseDto>> getAllProjects(){

       List<ProjectResponseDto> allProjects =  projectService.getAllProject();

       return ResponseEntity.status(HttpStatus.OK).body(allProjects);
    }

    @GetMapping("/{id}" )
    public ResponseEntity<ProjectResponseDto> getProject(@PathVariable Long id){

        ProjectResponseDto projectDetails = projectService.getProject(id);

        return ResponseEntity.status(HttpStatus.OK).body(projectDetails);

    }


}
