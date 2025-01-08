package com.url.shortner.dto.request;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;

@Data
@Accessors(chain = true)
public class ClickEvenResponseDto {
    private LocalDate clickDate;
    private Long count;
}
