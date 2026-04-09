package com.reventa.api.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

import com.reventa.api.model.enums.EstadoVerificacion;

@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long idUsuario;

    @Column(name = "nombre_completo", nullable = false)
    private String nombreCompleto;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(name = "dni_nie", unique = true, length = 20)
    private String dniNie;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_verificacion", columnDefinition = "varchar(32) default 'pendiente'")
    private EstadoVerificacion estadoVerificacion;

    @Column(name = "stripe_account_id", length = 100)
    private String stripeAccountId;

    @Column(name = "reputacion_media", precision = 3, scale = 2)
    private BigDecimal reputacionMedia = new BigDecimal("5.00");

    @Column(name = "total_resenas")
    private Integer totalResenas = 0;

    @Column(name = "fecha_registro", insertable = false, updatable = false)
    private Timestamp fechaRegistro;

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getDniNie() {
        return dniNie;
    }

    public void setDniNie(String dniNie) {
        this.dniNie = dniNie;
    }

    public EstadoVerificacion getEstadoVerificacion() {
        return estadoVerificacion;
    }

    public void setEstadoVerificacion(EstadoVerificacion estadoVerificacion) {
        this.estadoVerificacion = estadoVerificacion;
    }

    public String getStripeAccountId() {
        return stripeAccountId;
    }

    public void setStripeAccountId(String stripeAccountId) {
        this.stripeAccountId = stripeAccountId;
    }

    public BigDecimal getReputacionMedia() {
        return reputacionMedia;
    }

    public void setReputacionMedia(BigDecimal reputacionMedia) {
        this.reputacionMedia = reputacionMedia;
    }

    public Integer getTotalResenas() {
        return totalResenas;
    }

    public void setTotalResenas(Integer totalResenas) {
        this.totalResenas = totalResenas;
    }

    public Timestamp getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Timestamp fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }


}