package com.anksostudio.taskmanager.dto;

import com.anksostudio.taskmanager.model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class RegisterResponseDto {
    private Long id;
    private String name;
    private String email;
    private Role role;
}
