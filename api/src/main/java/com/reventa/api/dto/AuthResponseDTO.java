package com.reventa.api.dto;

public class AuthResponseDTO {
    private String token;
    private Long idUsuario;

    public AuthResponseDTO(String token, Long idUsuario) {
        this.token = token;
        this.idUsuario = idUsuario;
    }

    // Getters y Setters
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    
    public Long getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Long idUsuario) { this.idUsuario = idUsuario; }
}