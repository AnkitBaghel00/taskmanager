package com.anksostudio.taskmanager.controller;


import com.anksostudio.taskmanager.dto.LoginRequestDto;
import com.anksostudio.taskmanager.dto.LoginResponseDto;
import com.anksostudio.taskmanager.dto.RegisterRequestDto;
import com.anksostudio.taskmanager.dto.RegisterResponseDto;
import com.anksostudio.taskmanager.service.Impl.UserServiceImpl;
import com.anksostudio.taskmanager.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    private UserService userService;
    public UserController(UserService userService){
        this.userService = userService;
    }


    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDto> register(@Valid @RequestBody RegisterRequestDto dto){

        RegisterResponseDto savedUser = userService.register(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto loginRequestDto){

        LoginResponseDto loggedin = userService.login(loginRequestDto);

        return ResponseEntity.status(HttpStatus.OK).body(loggedin);
    }



}
