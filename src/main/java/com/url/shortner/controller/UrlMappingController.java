package com.url.shortner.controller;

import com.url.shortner.dto.request.ClickEvenResponseDto;
import com.url.shortner.dto.request.UrlMappingResponseDto;
import com.url.shortner.model.User;
import com.url.shortner.service.AnalyticalService;
import com.url.shortner.service.UrlMappingService;
import com.url.shortner.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/urls")
@RequiredArgsConstructor
public class UrlMappingController {

    private final UrlMappingService urlMappingService;
    private final UserService userService;


    @PostMapping("/shorten")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<UrlMappingResponseDto> createShortUrl(@RequestBody Map<String,String> request,
                                                                Principal principal){
        // request{K : V} :- {"originalUrl" : "https://example.com"}
        String originalUrl = request.get("originalUrl");

        User user = userService.findByUsername(principal.getName());
        UrlMappingResponseDto responseDto = urlMappingService.createShortUrl(originalUrl,user);

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @GetMapping("/my-urls")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<UrlMappingResponseDto>> getAllUrls(Principal principal){
        User user = userService.findByUsername(principal.getName());
        List<UrlMappingResponseDto> responseDtos = urlMappingService.getAllUrls(user);
        return new ResponseEntity<>(responseDtos,HttpStatus.OK);
    }

}
