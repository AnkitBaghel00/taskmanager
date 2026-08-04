package com.anksostudio.taskmanager.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectResponseDto {

    private Long id;
    private String title;
    private String description;
    private String createdBy;

}
