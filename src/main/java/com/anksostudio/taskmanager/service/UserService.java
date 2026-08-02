package com.anksostudio.taskmanager.service;

import com.anksostudio.taskmanager.dto.LoginRequestDto;
import com.anksostudio.taskmanager.dto.RegisterRequestDto;
import com.anksostudio.taskmanager.dto.RegisterResponseDto;

public interface UserService {

    RegisterResponseDto register(RegisterRequestDto registerRequestDto);
    RegisterResponseDto login(LoginRequestDto requestDto);
}
