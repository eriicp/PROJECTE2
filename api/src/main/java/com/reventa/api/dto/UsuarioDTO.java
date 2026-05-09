package com.reventa.api.dto;

import com.reventa.api.model.enums.EstadoVerificacion;
import java.math.BigDecimal;
import java.sql.Timestamp;

public class UsuarioDTO {

    private Long idUsuario;
    private String nombreCompleto;
    private String email;
    private String dniNie;
    private EstadoVerificacion estadoVerificacion;
    private BigDecimal reputacionMedia;
    private Integer totalResenas;
    private Timestamp fechaRegistro;

    public UsuarioDTO() {
    }

    public UsuarioDTO(Long idUsuario, String nombreCompleto, String email, String dniNie, 
                      EstadoVerificacion estadoVerificacion, BigDecimal reputacionMedia, 
                      Integer totalResenas, Timestamp fechaRegistro) {
        this.idUsuario = idUsuario;
        this.nombreCompleto = nombreCompleto;
        this.email = email;
        this.dniNie = dniNie;
        this.estadoVerificacion = estadoVerificacion;
        this.reputacionMedia = reputacionMedia;
        this.totalResenas = totalResenas;
        this.fechaRegistro = fechaRegistro;
    }

    // Getters y Setters
    public Long getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Long idUsuario) { this.idUsuario = idUsuario; }

    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getDniNie() { return dniNie; }
    public void setDniNie(String dniNie) { this.dniNie = dniNie; }

    public EstadoVerificacion getEstadoVerificacion() { return estadoVerificacion; }
    public void setEstadoVerificacion(EstadoVerificacion estadoVerificacion) { this.estadoVerificacion = estadoVerificacion; }

    public BigDecimal getReputacionMedia() { return reputacionMedia; }
    public void setReputacionMedia(BigDecimal reputacionMedia) { this.reputacionMedia = reputacionMedia; }

    public Integer getTotalResenas() { return totalResenas; }
    public void setTotalResenas(Integer totalResenas) { this.totalResenas = totalResenas; }

    public Timestamp getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(Timestamp fechaRegistro) { this.fechaRegistro = fechaRegistro; }
}