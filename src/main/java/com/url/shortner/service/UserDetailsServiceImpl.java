package com.url.shortner.service;

import com.url.shortner.model.User;
import com.url.shortner.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;


    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException("user not found with username: " + username));

        return UserDetailsImpl.build(user);
    }
}

/*
    Note :-
           With this method custom implementation of loadUserByUsername ,
           we are telling spring security that,this is how you should load the user,

           because we are storing user in mysql and we have custom user and userRepository

           So with this method we are telling spring security this is how you fetch the user information after
           validating the token

 */