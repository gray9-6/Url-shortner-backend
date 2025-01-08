package com.url.shortner.controller;

import com.url.shortner.dto.request.ClickEvenResponseDto;
import com.url.shortner.model.User;
import com.url.shortner.service.AnalyticalService;
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
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
public class AnalyticalController {

    private final AnalyticalService analyticalService;
    private final UserService userService;


    @GetMapping("/{shortUrl}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<ClickEvenResponseDto>> getUrlAnalytics(@PathVariable String shortUrl,
                                                @RequestParam(value = "startDate") String startDate,  // 2024-12-01T00:00:00
                                                @RequestParam(value = "startDate") String endDate){
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        LocalDateTime start = LocalDateTime.parse(startDate,formatter);
        LocalDateTime end = LocalDateTime.parse(endDate,formatter);

        List<ClickEvenResponseDto> response = analyticalService.getClickEventsByDate(shortUrl,start,end);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/total-clicks")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Map<LocalDate,Long>> getTotalClicksByDate(Principal principal,
                                                                    @RequestParam(value = "startDate") String startDate,  // 2024-12-01
                                                                    @RequestParam(value = "startDate") String endDate){
        User user = userService.findByUsername(principal.getName());

        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
        LocalDate start = LocalDate.parse(startDate,formatter);
        LocalDate end = LocalDate.parse(endDate,formatter);

        Map<LocalDate,Long> totalClicks = analyticalService.getTotalClicksByUserAndDate(user,start,end);
        return new ResponseEntity<>(totalClicks, HttpStatus.OK);
    }
}
