package com.reventa.api.dto;

public class UsuarioPerfilDTO {
    private Long idUsuario;
    private String nombreCompleto;
    private String email;
    private String estadoVerificacion;
    private Double reputacionMedia;
    private Integer totalResenas;

    // Constructores, Getters y Setters
    public UsuarioPerfilDTO(Long idUsuario, String nombreCompleto, String email, String estadoVerificacion, Double reputacionMedia, Integer totalResenas) {
        this.idUsuario = idUsuario;
        this.nombreCompleto = nombreCompleto;
        this.email = email;
        this.estadoVerificacion = estadoVerificacion != null ? estadoVerificacion : "pendiente";
        this.reputacionMedia = reputacionMedia != null ? reputacionMedia : 0.0;
        this.totalResenas = totalResenas != null ? totalResenas : 0;
    }

    // (Genera los getters y setters aquí)
    public Long getIdUsuario() { return idUsuario; }
    public String getNombreCompleto() { return nombreCompleto; }
    public String getEmail() { return email; }
    public String getEstadoVerificacion() { return estadoVerificacion; }
    public Double getReputacionMedia() { return reputacionMedia; }
    public Integer getTotalResenas() { return totalResenas; }
}