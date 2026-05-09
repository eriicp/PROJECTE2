package com.reventa.api.dto;

public class RegisterRequest {
    private String nombreCompleto;
    private String email;
    private String password;
    private String dniNie;

    // Getters y Setters
    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getDniNie() { return dniNie; }
    public void setDniNie(String dniNie) { this.dniNie = dniNie; }
}