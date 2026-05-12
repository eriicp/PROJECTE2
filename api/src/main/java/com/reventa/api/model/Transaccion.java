package com.reventa.api.model;

import java.math.BigDecimal;

import jakarta.persistence.*;

import java.time.LocalDateTime;

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
}