package com.url.shortner.mapper;

import com.url.shortner.dto.request.UrlMappingResponseDto;
import com.url.shortner.model.UrlMapping;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface UrlMapper {

    UrlMapper INSTANCE = Mappers.getMapper(UrlMapper.class);

    UrlMappingResponseDto toUrlMappingResponseDto(UrlMapping urlMapping);

    List<UrlMappingResponseDto> toUrlMappingResponseDtos(List<UrlMapping> urlMappings);
}
