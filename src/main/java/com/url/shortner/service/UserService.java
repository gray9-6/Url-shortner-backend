package com.url.shortner.service;

import com.url.shortner.dto.request.LoginRequestDto;
import com.url.shortner.dto.request.RegisterRequestDto;
import com.url.shortner.dto.response.JwtAuthenticationResponse;
import com.url.shortner.model.User;
import com.url.shortner.repository.UserRepository;
import com.url.shortner.security.jwt.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    public User registerUser(RegisterRequestDto registerRequestDto){
        User user = new User()
                .setUsername(registerRequestDto.getUsername())
                .setEmail(registerRequestDto.getEmail())
                .setPassword(passwordEncoder.encode(registerRequestDto.getPassword()))
                .setRole("ROLE_USER");

        return userRepository.save(user);
    }

    public JwtAuthenticationResponse loginUser(LoginRequestDto requestDto) {
        // we are creating a new instance for UsernamePasswordAuthenticationToken and using authenticationManager
        // to authenticate the username and password, Question is how does it now that username and password is correct
        // it uses DaoAuthenticationProvider , which uses loadUserByUsername
        // after successfully authentication, it returns the authenticated object
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(requestDto.getUserName(),requestDto.getPassword())
        );

        // this means SpringSecurityConstruct will hold the authentication data for current request/session
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // token generation part
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String jwt = jwtUtils.generateToken(userDetails);

        return new JwtAuthenticationResponse()
                .setToken(jwt);

    }

    public User findByUsername(String name) {
        return userRepository.findByUsername(name)
                .orElseThrow(()-> new UsernameNotFoundException("User not found with username: " + name));
    }
}
