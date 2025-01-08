package com.url.shortner.dto.response;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class JwtAuthenticationResponse {
    private String token;
}
