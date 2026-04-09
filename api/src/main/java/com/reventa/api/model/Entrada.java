package com.reventa.api.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

import com.reventa.api.model.enums.EstadoEntrada;

@Entity
@Table(name = "entradas")
public class Entrada {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_entrada")
    private Long idEntrada;

    // Relación Foreign Key con Eventos
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_evento")
    private Evento evento;

    // Relación Foreign Key con Usuarios (Vendedor)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_vendedor")
    private Usuario vendedor;

    @Column(name = "precio_reventa", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioReventa;

    @Column(name = "cod_digital_unico", unique = true, length = 100)
    private String codDigitalUnico;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(32) default 'disponible'")
    private EstadoEntrada estado;

    @Column(name = "es_destacada")
    private Boolean esDestacada = false;

    @Column(name = "tipo_asiento", nullable = false, length = 100)
    private String tipoAsiento;

    @Column(length = 50)
    private String fila;

    @Column(length = 50)
    private String asiento;

    public Long getIdEntrada() {
        return idEntrada;
    }

    public void setIdEntrada(Long idEntrada) {
        this.idEntrada = idEntrada;
    }

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }

    public Usuario getVendedor() {
        return vendedor;
    }

    public void setVendedor(Usuario vendedor) {
        this.vendedor = vendedor;
    }

    public BigDecimal getPrecioReventa() {
        return precioReventa;
    }

    public void setPrecioReventa(BigDecimal precioReventa) {
        this.precioReventa = precioReventa;
    }

    public String getCodDigitalUnico() {
        return codDigitalUnico;
    }

    public void setCodDigitalUnico(String codDigitalUnico) {
        this.codDigitalUnico = codDigitalUnico;
    }

    public EstadoEntrada getEstado() {
        return estado;
    }

    public void setEstado(EstadoEntrada estado) {
        this.estado = estado;
    }

    public Boolean getEsDestacada() {
        return esDestacada;
    }

    public void setEsDestacada(Boolean esDestacada) {
        this.esDestacada = esDestacada;
    }

    public String getTipoAsiento() {
        return tipoAsiento;
    }

    public void setTipoAsiento(String tipoAsiento) {
        this.tipoAsiento = tipoAsiento;
    }

    public String getFila() {
        return fila;
    }

    public void setFila(String fila) {
        this.fila = fila;
    }

    public String getAsiento() {
        return asiento;
    }

    public void setAsiento(String asiento) {
        this.asiento = asiento;
    }

  
}