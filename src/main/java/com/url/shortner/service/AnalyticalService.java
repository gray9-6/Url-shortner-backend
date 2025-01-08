package com.url.shortner.service;


import com.url.shortner.dto.request.ClickEvenResponseDto;
import com.url.shortner.mapper.ClickEventMapper;
import com.url.shortner.model.ClickEvent;
import com.url.shortner.model.UrlMapping;
import com.url.shortner.model.User;
import com.url.shortner.repository.ClickEventRepository;
import com.url.shortner.repository.UrlMappingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnalyticalService {

    private final UrlMappingRepository urlMappingRepository;
    private final ClickEventRepository clickEventRepository;

    public List<ClickEvenResponseDto> getClickEventsByDate(String shortUrl, LocalDateTime start, LocalDateTime end) {

        Optional<UrlMapping> optionalUrlMapping = urlMappingRepository.findByShortUrl(shortUrl);

        if(optionalUrlMapping.isEmpty()){
            return new ArrayList<>();
        }

        List<ClickEvent> clickEvents = clickEventRepository.findByUrlMappingAndClickDateBetween(optionalUrlMapping.get(),start,end);

        return clickEvents.stream()
                .collect(Collectors.groupingBy(clickEvent -> clickEvent.getClickDate().toLocalDate(),Collectors.counting()))
                .entrySet().stream()
                .map(entry->{
                    return new ClickEvenResponseDto()
                            .setClickDate(entry.getKey())
                            .setCount(entry.getValue());
                })
                .collect(Collectors.toList());

//        return ClickEventMapper.INSTANCE.toClickEvenResponseDto(clickEvents);

    }

    public Map<LocalDate, Long> getTotalClicksByUserAndDate(User user, LocalDate startDate, LocalDate endDate) {
         List<UrlMapping> urlMappings = urlMappingRepository.findByUser(user);
         List<ClickEvent> clickEvents = clickEventRepository.findByUrlMappingInAndClickDateBetween(urlMappings,startDate.atStartOfDay(),endDate.plusDays(1).atStartOfDay());

         return clickEvents.stream()
                 .collect(Collectors.groupingBy(clickEvent -> clickEvent.getClickDate().toLocalDate(),Collectors.counting()));
    }
}
