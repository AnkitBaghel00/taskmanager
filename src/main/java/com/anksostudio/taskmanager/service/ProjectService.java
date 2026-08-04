package com.anksostudio.taskmanager.service;

import com.anksostudio.taskmanager.dto.CreateProjectRequestDto;
import com.anksostudio.taskmanager.dto.ProjectResponseDto;

import java.util.List;

public interface ProjectService {

    ProjectResponseDto createProject(CreateProjectRequestDto projectRequestDto);

    ProjectResponseDto getProject(Long id);
    List<ProjectResponseDto> getAllProject();
}
