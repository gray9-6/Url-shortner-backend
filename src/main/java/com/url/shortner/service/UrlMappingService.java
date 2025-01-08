package com.url.shortner.service;

import com.url.shortner.dto.request.UrlMappingResponseDto;
import com.url.shortner.mapper.UrlMapper;
import com.url.shortner.model.ClickEvent;
import com.url.shortner.model.UrlMapping;
import com.url.shortner.model.User;
import com.url.shortner.repository.ClickEventRepository;
import com.url.shortner.repository.UrlMappingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UrlMappingService {

    private final UrlMappingRepository urlMappingRepository;
    private final ClickEventRepository clickEventRepository;

    public UrlMappingResponseDto createShortUrl(String originalUrl, User user) {
        String shortUrl = generateShortUrl();
        UrlMapping urlMapping = new UrlMapping()
                .setShortUrl(shortUrl)
                .setOriginalUrl(originalUrl)
                .setUser(user)
                .setCreatedDate(LocalDateTime.now());

        UrlMapping savedUrlMapping = urlMappingRepository.save(urlMapping);


        return UrlMapper.INSTANCE.toUrlMappingResponseDto(savedUrlMapping)
                .setUserName(savedUrlMapping.getUser().getUsername());
    }

    private String generateShortUrl() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYVabcdefghijklmnopqrstuvwxyz1234567890";

        SecureRandom random = new SecureRandom();
        StringBuilder shortUrl = new StringBuilder(8);

        for (int i = 0; i < 8; i++) {
            shortUrl.append(characters.charAt(random.nextInt(characters.length())));
        }

        return shortUrl.toString();
    }

    public List<UrlMappingResponseDto> getAllUrls(User user) {
        List<UrlMapping> urlMappings = urlMappingRepository.findByUser(user);
        return UrlMapper.INSTANCE.toUrlMappingResponseDtos(urlMappings)
                .stream()
                .map(url->url.setUserName(user.getUsername()))
                .toList();
    }

    public UrlMapping getOriginalUrl(String shortUrl) {
        UrlMapping urlMapping = urlMappingRepository.findByShortUrl(shortUrl)
                .orElseThrow(()->new RuntimeException("No original url for this short url: " + shortUrl));

        if(urlMapping != null){
            urlMapping.setClickCount(urlMapping.getClickCount() + 1);
            urlMappingRepository.save(urlMapping);

            // Record Click Event
            ClickEvent clickEvent = new ClickEvent()
                    .setClickDate(LocalDateTime.now())
                    .setUrlMapping(urlMapping);

            clickEventRepository.save(clickEvent);

        }

        return urlMapping;
    }
}
