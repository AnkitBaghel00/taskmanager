package com.anksostudio.taskmanager.service.Impl;

import com.anksostudio.taskmanager.dto.LoginRequestDto;
import com.anksostudio.taskmanager.dto.LoginResponseDto;
import com.anksostudio.taskmanager.dto.RegisterRequestDto;
import com.anksostudio.taskmanager.dto.RegisterResponseDto;
import com.anksostudio.taskmanager.exception.DuplicateResourceException;
import com.anksostudio.taskmanager.exception.InvalidCredentialsException;
import com.anksostudio.taskmanager.exception.ResourceNotFoundException;
import com.anksostudio.taskmanager.model.Role;
import com.anksostudio.taskmanager.model.User;
import com.anksostudio.taskmanager.repository.UserRepository;
import com.anksostudio.taskmanager.security.JwtUtil;
import com.anksostudio.taskmanager.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;



@Service
public class UserServiceImpl implements UserService {

     private UserRepository userRepository;
     private PasswordEncoder passwordEncoder;
     private JwtUtil jwtUtil;

     public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil){
         this.userRepository = userRepository;
         this.passwordEncoder = passwordEncoder;
         this.jwtUtil = jwtUtil;
     }

    @Override
    public RegisterResponseDto register(RegisterRequestDto registerRequestDto) {

         if (userRepository.findByEmail(registerRequestDto.getEmail()).isPresent()){
//             throw new RuntimeException("Email already registered");
             throw new DuplicateResourceException("Email already registered");
         }

          User user = mapRegisterRequestDtotoUser(registerRequestDto);
          User saveUser = userRepository.save(user);

          RegisterResponseDto registerResponseDto = mapSaveUsertoDTO(saveUser);


          return registerResponseDto;
    }


    @Override
    public LoginResponseDto login(LoginRequestDto requestDto) {
         User user = userRepository.findByEmail(requestDto.getEmail())
                 .orElseThrow(() -> new ResourceNotFoundException("Email is not registered"));


       boolean passwordMatches = passwordEncoder.matches(
                requestDto.getPassword(),
                user.getPassword()
        );

       if (!passwordMatches){
//           throw new RuntimeException("Invalid password");
           throw new InvalidCredentialsException("Invalid password");
       }



        String token =  jwtUtil.generateToken(user.getEmail(),user.getRole().name());


        return mapUserToLoginResponse(user,token);

    }



    public LoginResponseDto mapUserToLoginResponse(User user, String token){
         LoginResponseDto responseDto = new LoginResponseDto();

         responseDto.setId(user.getId());
         responseDto.setEmail(user.getEmail());
         responseDto.setRole(user.getRole().name());
         responseDto.setToken(token);

         return responseDto;
    }

    public User mapRegisterRequestDtotoUser(RegisterRequestDto reqDTO){
         User user = new User();
         user.setName(reqDTO.getName());
         user.setEmail(reqDTO.getEmail());

         user.setPassword(passwordEncoder.encode(reqDTO.getPassword()));
         user.setRole(Role.MEMBER);

         return user;

    }

    public RegisterResponseDto mapSaveUsertoDTO(User user){
         RegisterResponseDto respDto = new RegisterResponseDto();

         respDto.setId(user.getId());
         respDto.setName(user.getName());
         respDto.setEmail(user.getEmail());
         respDto.setRole(user.getRole());

         return respDto;
    }
}
