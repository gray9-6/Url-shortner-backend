package com.url.shortner.mapper;

import com.url.shortner.dto.request.ClickEvenResponseDto;
import com.url.shortner.model.ClickEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface ClickEventMapper {

    ClickEventMapper INSTANCE = Mappers.getMapper(ClickEventMapper.class);

    @Mapping(source = "clickDate", target = "clickDate", qualifiedByName = "mapLocalDateTimeToLocalDate")
    List<ClickEvenResponseDto> toClickEvenResponseDto(List<ClickEvent> clickEvents);

    @Named("mapLocalDateTimeToLocalDate")
    static LocalDate mapLocalDateTimeToLocalDate(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.toLocalDate() : null;
    }
}
