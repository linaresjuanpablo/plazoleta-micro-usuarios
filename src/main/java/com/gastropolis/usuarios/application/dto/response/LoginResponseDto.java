package com.gastropolis.usuarios.application.dto.response;

public class LoginResponseDto {

    private String token;
    private String tokenType;

    public LoginResponseDto() {
    }

    public LoginResponseDto(String token) {
        this.token = token;
        this.tokenType = "Bearer";
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }
}
