package com.totm.totm.dto;

import lombok.Data;

@Data
public class TokenResponseDto {
    private String accessToken;
    private String refreshToken;

    public TokenResponseDto(String accessToken, String refreshToken) {
        this.accessToken = "Bearer " + accessToken;
        this.refreshToken = "Bearer "+ refreshToken;
    }
}
