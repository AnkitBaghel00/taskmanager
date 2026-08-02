package com.anksostudio.taskmanager.dto;

import com.anksostudio.taskmanager.model.Role;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class RegisterRequestDto {

    @NotBlank(message = "Name should not be empty")
   private String name;

    @Email(message = "Please enter a valid email")
    @NotBlank(message = "Email should not be empty")
   private String email;

    @Size(min = 8,message = "Password must be at least 8 characters")
    @NotBlank(message = "Password should not be empty")
   private String password;


}
