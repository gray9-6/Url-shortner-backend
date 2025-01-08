package com.url.shortner.controller;

import com.url.shortner.dto.request.LoginRequestDto;
import com.url.shortner.dto.request.RegisterRequestDto;
import com.url.shortner.dto.response.JwtAuthenticationResponse;
import com.url.shortner.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;



    @PostMapping("/public/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequestDto registerRequestDto){
        try {
            userService.registerUser(registerRequestDto);
            return new ResponseEntity<>("success", HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/public/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequestDto requestDto){
        try {
            JwtAuthenticationResponse authenticationResponse = userService.loginUser(requestDto);
            return new ResponseEntity<>(authenticationResponse, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
