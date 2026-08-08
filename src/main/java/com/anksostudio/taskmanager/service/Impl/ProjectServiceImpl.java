package com.anksostudio.taskmanager.service.Impl;

import com.anksostudio.taskmanager.dto.CreateProjectRequestDto;
import com.anksostudio.taskmanager.dto.ProjectResponseDto;
import com.anksostudio.taskmanager.exception.ResourceNotFoundException;
import com.anksostudio.taskmanager.model.Project;
import com.anksostudio.taskmanager.model.User;
import com.anksostudio.taskmanager.repository.ProjectRepository;
import com.anksostudio.taskmanager.repository.UserRepository;
import com.anksostudio.taskmanager.service.ProjectService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectServiceImpl implements ProjectService {
    private ProjectRepository projectRepository;
    private UserRepository userRepository;

    public ProjectServiceImpl(ProjectRepository projectRepository, UserRepository userRepository){
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }


    @Override
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ProjectResponseDto createProject(CreateProjectRequestDto projectRequestDto) {

       String email = SecurityContextHolder.getContext().getAuthentication().getName();
       User user = userRepository.findByEmail(email)
               .orElseThrow(() -> new ResourceNotFoundException("User not found"));

       Project mapProjected =  mapRequestToProject(projectRequestDto, user);
        Project save = projectRepository.save(mapProjected);

        return mapProjectResponse(save);
    }


    @Override
    public ProjectResponseDto getProject(Long id) {

        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        return mapProjectResponse(project);
    }

    @Override
    public List<ProjectResponseDto> getAllProject() {

        List<Project> projects = projectRepository.findAll();

        return projects.stream()
                .map(this::mapProjectResponse)
                .toList();
    }

    public Project mapRequestToProject(CreateProjectRequestDto requestDto, User user){
        Project project = new Project();

        project.setTitle(requestDto.getTitle());
        project.setDescription(requestDto.getDescription());
        project.setCreatedBy(user);

        return project;
    }

    public ProjectResponseDto mapProjectResponse(Project project){
        ProjectResponseDto responseDto = new ProjectResponseDto();

        responseDto.setId(project.getId());
        responseDto.setTitle(project.getTitle());
        responseDto.setDescription(project.getDescription());
        responseDto.setCreatedBy(project.getCreatedBy().getEmail());

        return responseDto;

    }
}
