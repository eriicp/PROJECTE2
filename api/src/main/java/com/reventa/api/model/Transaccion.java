package com.reventa.api.model;

import java.math.BigDecimal;

import jakarta.persistence.*;

import java.time.LocalDateTime;

import com.reventa.api.model.enums.EstadoPago;

@Entity
@Table(name = "transacciones")
public class Transaccion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTransaccion;

    @ManyToOne
    @JoinColumn(name = "id_entrada")
    private Entrada entrada; // Debes tener la entidad Entrada creada

    @ManyToOne
    @JoinColumn(name = "id_comprador")
    private Usuario comprador;

    private String stripePaymentIntentId;
    private BigDecimal montoBase;
    private BigDecimal comisionPlataforma;
    private BigDecimal montoTotal;

    @Enumerated(EnumType.STRING)
    private EstadoPago estadoPago; // Enum: PENDIENTE, COMPLETADO, FALLIDO

    private LocalDateTime fechaTransaccion = LocalDateTime.now();
    
    // Getters y Setters...

    public Long getIdTransaccion() {
        return idTransaccion;
    }

    public void setIdTransaccion(Long idTransaccion) {
        this.idTransaccion = idTransaccion;
    }

    public Entrada getEntrada() {
        return entrada;
    }

    public void setEntrada(Entrada entrada) {
        this.entrada = entrada;
    }

    public Usuario getComprador() {
        return comprador;
    }

    public void setComprador(Usuario comprador) {
        this.comprador = comprador;
    }

    public String getStripePaymentIntentId() {
        return stripePaymentIntentId;
    }

    public void setStripePaymentIntentId(String stripePaymentIntentId) {
        this.stripePaymentIntentId = stripePaymentIntentId;
    }

    public BigDecimal getMontoBase() {
        return montoBase;
    }

    public void setMontoBase(BigDecimal montoBase) {
        this.montoBase = montoBase;
    }

    public BigDecimal getComisionPlataforma() {
        return comisionPlataforma;
    }

    public void setComisionPlataforma(BigDecimal comisionPlataforma) {
        this.comisionPlataforma = comisionPlataforma;
    }

    public BigDecimal getMontoTotal() {
        return montoTotal;
    }

    public void setMontoTotal(BigDecimal montoTotal) {
        this.montoTotal = montoTotal;
    }

    public EstadoPago getEstadoPago() {
        return estadoPago;
    }

    public void setEstadoPago(EstadoPago estadoPago) {
        this.estadoPago = estadoPago;
    }

    public LocalDateTime getFechaTransaccion() {
        return fechaTransaccion;
    }

    public void setFechaTransaccion(LocalDateTime fechaTransaccion) {
        this.fechaTransaccion = fechaTransaccion;
    }    
}