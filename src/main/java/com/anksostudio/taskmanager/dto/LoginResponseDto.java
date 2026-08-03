package com.anksostudio.taskmanager.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponseDto {

   private Long id;
   private String email;
   private String role;
   private String token;
}
