package com.anksostudio.taskmanager.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import javax.xml.transform.sax.SAXResult;

@Getter
@Setter
public class LoginRequestDto {

    @NotBlank(message = "Email is required to login")
    @Email
    private String email;

    @NotBlank(message = "Password is required")
    private String password;
}
