package com.anksostudio.taskmanager.service;

import com.anksostudio.taskmanager.dto.LoginRequestDto;
import com.anksostudio.taskmanager.dto.LoginResponseDto;
import com.anksostudio.taskmanager.dto.RegisterRequestDto;
import com.anksostudio.taskmanager.dto.RegisterResponseDto;
import com.anksostudio.taskmanager.exception.DuplicateResourceException;
import com.anksostudio.taskmanager.model.Role;
import com.anksostudio.taskmanager.model.User;
import com.anksostudio.taskmanager.repository.UserRepository;
import com.anksostudio.taskmanager.security.JwtUtil;
import com.anksostudio.taskmanager.service.Impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.swing.text.html.Option;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void register_shouldSaveNewUser_whenEmailNotTaken() {
        // Arrange
        RegisterRequestDto requestDto = new RegisterRequestDto();
        requestDto.setName("Test User");
        requestDto.setEmail("test@example.com");
        requestDto.setPassword("password123");

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password123")).thenReturn("hashedPassword");

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setName("Test User");
        savedUser.setEmail("test@example.com");
        savedUser.setPassword("hashedPassword");
        savedUser.setRole(Role.MEMBER);

        when(userRepository.save(org.mockito.ArgumentMatchers.any(User.class))).thenReturn(savedUser);

        // Act
        RegisterResponseDto result = userService.register(requestDto);

        // Assert
        assertEquals("Test User", result.getName());
        assertEquals("test@example.com", result.getEmail());
        assertEquals(Role.MEMBER, result.getRole());
    }

    @Test
    void register_shouldThrowException_whenEmailAlreadyExists(){

        RegisterRequestDto requestDto = new RegisterRequestDto();
        requestDto.setEmail("test@example.com");
        requestDto.setName("Test user");
        requestDto.setPassword("password123");

        User existingUser = new User();
        existingUser.setEmail("test@example.com");

       when(userRepository.findByEmail("test@example.com"))
               .thenReturn(Optional.of(existingUser));

       assertThrows(DuplicateResourceException.class, () -> {
           userService.register(requestDto);
       });

    }


    @Test
    void login_successfully_andReturnToken(){
        LoginRequestDto requestDto = new LoginRequestDto();
        requestDto.setEmail("test@example.com");
        requestDto.setPassword("password123");

        User existingUser = new User();
        existingUser.setEmail("test@example.com");
        existingUser.setPassword("hashedPassword");
        existingUser.setRole(Role.MEMBER);

        when(userRepository.findByEmail("test@example.com"))
                .thenReturn(Optional.of(existingUser));

        when(passwordEncoder.matches("password123", "hashedPassword"))
                .thenReturn(true);

        when(jwtUtil.generateToken("test@example.com", "MEMBER"))
                .thenReturn("fake-jwt-token");

        // Act
        LoginResponseDto result = userService.login(requestDto);

        // Assert
        assertEquals("test@example.com", result.getEmail());
        assertEquals("fake-jwt-token", result.getToken());
    }
}